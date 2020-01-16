package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.ESManagerMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.ESManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ESManagerServiceImpl implements ESManagerService {

    @Autowired
    private ESManagerMapper esManagerMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Override
    public void createMappingAndIndex() {

        elasticsearchTemplate.createIndex(SkuInfo.class);

        elasticsearchTemplate.putMapping(SkuInfo.class);
    }

    @Override
    public void importAll() {

        List<Sku> skuList = skuFeign.findList("all");
        if (skuList == null || skuList.size()<=0){
           throw new RuntimeException("未查到数据");
        }

        String json_list = JSON.toJSONString(skuList);
        List<SkuInfo> skuInfoList = JSON.parseArray(json_list, SkuInfo.class);
        for (SkuInfo skuInfo : skuInfoList) {
            String spec = skuInfo.getSpec();
            Map specMap = JSON.parseObject(spec, Map.class);
            skuInfo.setSpecMap(specMap);
        }

        esManagerMapper.saveAll(skuInfoList);

    }

    @Override
    public void importSkuListBySpuId(String spuId) {

        List<Sku> skuList = skuFeign.findList(spuId);
        if (skuList == null || skuList.size()<=0){
            throw new RuntimeException("未查到数据");
        }

        String json = JSON.toJSONString(skuList);
        List<SkuInfo> skuInfoList = JSON.parseArray(json, SkuInfo.class);
        for (SkuInfo skuInfo : skuInfoList) {
            String spec = skuInfo.getSpec();
            Map map = JSON.parseObject(spec, Map.class);
            skuInfo.setSpecMap(map);
        }

        esManagerMapper.saveAll(skuInfoList);
    }

    @Override
    public void delSkuListBySpuId(String spuId) {
        List<Sku> skuList = skuFeign.findList(spuId);
        if (skuList == null || skuList.size()<=0){
            throw new RuntimeException("未查到数据");
        }

        for (Sku sku : skuList) {
            esManagerMapper.deleteById(Long.parseLong(sku.getId()));
        }
        System.out.println("执行成功");
    }
}
