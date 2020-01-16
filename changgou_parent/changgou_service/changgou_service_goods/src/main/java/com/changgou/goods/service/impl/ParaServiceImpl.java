package com.changgou.goods.service.impl;

import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.ParaMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.service.ParaService;
import com.changgou.goods.pojo.Para;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParaServiceImpl implements ParaService {

    @Autowired
    private ParaMapper paraMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<Para> findAll() {
        return paraMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Para findById(Integer id){
        return  paraMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加
     * @param para
     */
    @Override
    public void add(Para para){
        paraMapper.insert(para);
    }


    /**
     * 修改
     * @param para
     */
    @Override
    public void update(Para para){
        paraMapper.updateByPrimaryKey(para);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Integer id){
        paraMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<Para> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return paraMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Para> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<Para>)paraMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<Para> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<Para>)paraMapper.selectByExample(example);
    }

    @Override
    public List<Map<String, Object>> findParaByCategoryId(Integer categoryId) {

        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null){
            throw new RuntimeException("对应模板不存在");
        }

        List<Map<String, Object>> list = new ArrayList<>();

        Integer templateId = category.getTemplateId();

        Example example = new Example(Para.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateId",templateId);
        List<Para> paraList = paraMapper.selectByExample(example);

        for (Para para : paraList) {
            Map<String, Object> data = new HashMap<>();
            data.put("name",para.getName());
            String[] options = para.getOptions().split(",");
            data.put("options",options);
            list.add(data);
        }
        return list;
    }

    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Para.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 名称
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
           	}
            // 选项
            if(searchMap.get("options")!=null && !"".equals(searchMap.get("options"))){
                criteria.andLike("options","%"+searchMap.get("options")+"%");
           	}

            // id
            if(searchMap.get("id")!=null ){
                criteria.andEqualTo("id",searchMap.get("id"));
            }
            // 排序
            if(searchMap.get("seq")!=null ){
                criteria.andEqualTo("seq",searchMap.get("seq"));
            }
            // 模板ID
            if(searchMap.get("templateId")!=null ){
                criteria.andEqualTo("templateId",searchMap.get("templateId"));
            }

        }
        return example;
    }

}
