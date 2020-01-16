package com.changgou.config.controller;

import com.changgou.config.pojo.Provinces;
import com.changgou.config.service.ProvinceCityAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pro")
public class ProvinceCityAreaController {

    @Autowired
    private ProvinceCityAreaService provinceCityAreaService;

    @GetMapping
    public List<Provinces> findPro(){

        return provinceCityAreaService.findPro();
    }
}
