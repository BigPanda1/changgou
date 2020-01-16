package com.changgou.goods.daoetc;

import com.changgou.goods.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryNodeMapper {

    List<CategoryNode> findCategoryNodeList();
}
