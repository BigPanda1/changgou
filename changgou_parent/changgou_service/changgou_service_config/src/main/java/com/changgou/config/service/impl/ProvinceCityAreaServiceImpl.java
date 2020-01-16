package com.changgou.config.service.impl;

import com.changgou.config.daoext.ProvinceCityAreaMapper;
import com.changgou.config.pojo.Provinces;
import com.changgou.config.service.ProvinceCityAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceCityAreaServiceImpl implements ProvinceCityAreaService {

    @Autowired
    private ProvinceCityAreaMapper provinceCityAreaMapper;

    @Override
    public List<Provinces> findPro() {
        List<Provinces> provincesList = provinceCityAreaMapper.findPro();
        return provincesList;
    }
}
