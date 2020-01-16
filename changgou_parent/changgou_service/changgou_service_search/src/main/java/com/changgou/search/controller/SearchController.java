package com.changgou.search.controller;

import com.changgou.entity.Page;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/list")
    public String list(@RequestParam Map<String,String> searchMap, Model model){
        this.handelSearchMap(searchMap);

        Map resultMap = searchService.search(searchMap);
        model.addAttribute("resultMap",resultMap);
        model.addAttribute("searchMap",searchMap);

        Page<SkuInfo> page = new Page<SkuInfo>(Long.parseLong(String.valueOf(resultMap.get("total"))),Integer.parseInt(String.valueOf(resultMap.get("totalPages"))),Page.pageSize);
        model.addAttribute("page",page);

        StringBuilder url = new StringBuilder("/search/list");
        if (searchMap != null && searchMap.size()>0){
            url.append("?");
            for (String key : searchMap.keySet()) {
                if (!"pageNum".equals(key) && !"pageSize".equals(key) && !"sortRule".equals(key) && !"sortFiled".equals(key)){
                    url.append(key).append("=").append(searchMap.get(key)).append("&");
                }
            }
            String url2 = url.toString();
            url2 = url2.substring(0,url2.length()-1);
            model.addAttribute("url",url2);
        }else {
            model.addAttribute("url",url);
        }
        return "search";
    }

    @GetMapping
    @ResponseBody
    public Map search(@RequestParam Map<String,String> searchMap){
        this.handelSearchMap(searchMap);
        Map resultMap = searchService.search(searchMap);
        return resultMap;
    }

    //查询条件进行编码处理
    private void handelSearchMap(Map<String, String> searchMap) {
        Set<Map.Entry<String, String>> entries = searchMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if (entry.getKey().startsWith("spec_")){
                searchMap.put(entry.getKey(),entry.getValue().replace("+","%2B"));
            }
        }
    }
}
