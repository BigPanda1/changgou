package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Map search(Map<String, String> searchMap) {

        Map<String,Object> resultMap = new HashMap<>();

        if (searchMap != null) {
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery(); //定义bool组合查询对象

            //按照搜索关键字进行查询
            if (StringUtils.isNotEmpty(searchMap.get("keyword"))){
                MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("name", searchMap.get("keyword")).operator(Operator.AND);  //构建一个match查询方式放入bool组合查询查询里
                boolQuery.must(matchQuery);
            }
            //按照品牌进行过滤查询
            if (StringUtils.isNotEmpty(searchMap.get("brand"))){
                TermQueryBuilder termQuery = QueryBuilders.termQuery("brandName", searchMap.get("brand"));
                boolQuery.filter(termQuery);
            }
            //按照规格进行过滤查询
            for (String key : searchMap.keySet()) {
                if (key.startsWith("spec_")){
                    String value = searchMap.get(key).replace("%2B","+");
                    boolQuery.filter(QueryBuilders.termQuery("specMap."+key.substring(5)+".keyword",value));
                }
            }
            //根据价格进行区间查询
            if (StringUtils.isNotEmpty(searchMap.get("price"))){
                String[] prices = searchMap.get("price").split("-");
                if (prices.length == 2){  //0-500
                    boolQuery.filter(QueryBuilders.rangeQuery("price").lte(prices[1]));
                }
                //3000
                boolQuery.filter(QueryBuilders.rangeQuery("price").gte(prices[0]));
            }

            //根据字段进行升序降序查询
            if (StringUtils.isNotEmpty(searchMap.get("sortFiled")) && StringUtils.isNotEmpty(searchMap.get("sortRule"))){
                if ("ASC".equals(searchMap.get("sortRule"))){
                    nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(searchMap.get("sortFiled")).order(SortOrder.ASC));
                }else {
                    nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(searchMap.get("sortFiled")).order(SortOrder.DESC));
                }
            }

            //高亮查询
            HighlightBuilder.Field field = new HighlightBuilder.Field("name");
            field.preTags("<span style='color:red'>");
            field.postTags("</span>");
            nativeSearchQueryBuilder.withHighlightFields(field);

            nativeSearchQueryBuilder.withQuery(boolQuery); //构建查询方式(条件的构建)

            //构建分页
            String pageNum = searchMap.get("pageNum");  //当前页
            String pageSize = searchMap.get("pageSize"); //每页显示多少条
            if (StringUtils.isEmpty(pageNum)){
                pageNum = "1";
            }
            if (StringUtils.isEmpty(pageSize)){
                pageSize = "30";
            }
            nativeSearchQueryBuilder.withPageable(PageRequest.of(Integer.parseInt(pageNum)-1,Integer.parseInt(pageSize)));

            //按照品牌名称字段进行聚合
            String brandBucket = "brandBucket";
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(brandBucket).field("brandName"));
            //按照规格字段进行聚合
            String specBucket = "specBucket";
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(specBucket).field("spec.keyword"));

            AggregatedPage<SkuInfo> resultInfo = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                    List<T> list = new ArrayList<>();
                    SearchHits hits = searchResponse.getHits();  //获取所有的查询结果
                    if (hits != null){
                        for (SearchHit hit : hits) {
                            SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);//把每一条查询出来的记录封装成skuInfo对象
                            Map<String, HighlightField> map = hit.getHighlightFields();
                            if (map != null && map.size()>0) {
                                skuInfo.setName(map.get("name").getFragments()[0].toString());
                            }
                            list.add((T) skuInfo);
                        }

                    }
                    return new AggregatedPageImpl<T>(list,pageable,hits.getTotalHits(),searchResponse.getAggregations());
                }
            });
            resultMap.put("total",resultInfo.getTotalElements());  //设置总记录数
            resultMap.put("totalPages",resultInfo.getTotalPages()); //设置总页数
            resultMap.put("pageNum",resultInfo.getPageable().getPageNumber());//设置当前页码
            resultMap.put("rows",resultInfo.getContent());  //设置查询出来的数据
            StringTerms brandAggregation = (StringTerms) resultInfo.getAggregation(brandBucket);
            List<String> brandList = brandAggregation.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            resultMap.put("brandList",brandList);
            StringTerms skuAggregation = (StringTerms) resultInfo.getAggregation(specBucket);
            List<String> specList = skuAggregation.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            resultMap.put("specList",this.formatSpec(specList));

            return resultMap;
        }
        return null;
    }

    private Map<String,Set<String>> formatSpec(List<String> specList){
        Map<String,Set<String>> resultMap = new HashMap<>();
        if (specList != null && specList.size()>0){
            for (String specJson : specList) {   //遍历list中的每一个json字符串
                Map<String,String> specMap = JSON.parseObject(specJson, Map.class); //把每个json字符串都转换成map
                for (String key : specMap.keySet()) {
                    Set<String> set = resultMap.get(key);
                    if (set == null){
                        set = new HashSet<>();
                    }
                    set.add(specMap.get(key));
                    resultMap.put(key,set);
                }
            }
            return resultMap;
        }
        return null;
    }
}
