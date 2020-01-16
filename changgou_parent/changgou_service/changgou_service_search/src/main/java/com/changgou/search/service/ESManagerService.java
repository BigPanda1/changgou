package com.changgou.search.service;

public interface ESManagerService {

    void createMappingAndIndex();

    void importAll();

    void importSkuListBySpuId(String spuId);

    void delSkuListBySpuId(String spuId);
}
