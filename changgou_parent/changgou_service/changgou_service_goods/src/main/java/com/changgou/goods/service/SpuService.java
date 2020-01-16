package com.changgou.goods.service;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SpuService {

    /***
     * 查询所有
     * @return
     */
    List<Spu> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Spu findById(String id);

    /***
     * 新增
     * @param goods
     */
    void add(Goods goods);

    /***
     * 修改
     * @param goods
     */
    void update(Goods goods);

    /***
     * 删除
     * @param id
     */
    void delete(String id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<Spu> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Spu> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Spu> findPage(Map<String, Object> searchMap, int page, int size);

    /**
     * 通过spuId查询商品信息
     * @param spuId
     * @return
     */
    Goods findBySpuId(String spuId);

    /**
     * 设置商品已审核
     * @param spuId
     */
    void audit(String spuId);

    /**
     * 设置商品为下架
     * @param spuId
     */
    void pull(String spuId);

    /**
     * 设置商品上架
     * @param spuId
     */
    void put(String spuId);

    //商品还原
    void restore(String spuId);

    //物理删除
    void realDel(String spuId);
}
