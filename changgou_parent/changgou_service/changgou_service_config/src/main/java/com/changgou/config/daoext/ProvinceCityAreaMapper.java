package com.changgou.config.daoext;

import com.changgou.config.pojo.Provinces;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProvinceCityAreaMapper {

    List<Provinces> findPro();
}
