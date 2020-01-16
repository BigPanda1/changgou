package com.changgou.goods.service.impl;

import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.SpecMapper;
import com.changgou.goods.dao.SpuMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpecService;
import com.changgou.goods.pojo.Spec;
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
public class SpecServiceImpl implements SpecService {

    @Autowired
    private SpecMapper specMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<Spec> findAll() {
        return specMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Spec findById(Integer id){
        return  specMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加
     * @param spec
     */
    @Override
    public void add(Spec spec){
        specMapper.insert(spec);
    }


    /**
     * 修改
     * @param spec
     */
    @Override
    public void update(Spec spec){
        specMapper.updateByPrimaryKey(spec);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Integer id){
        specMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<Spec> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return specMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Spec> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<Spec>)specMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<Spec> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<Spec>)specMapper.selectByExample(example);
    }

    /**
     * 查询规格通过分类名
     * @param categoryName
     * @return
     */
    @Override
    public List<Map> findSpecByCategoryName(String categoryName) {
        List<Map> list = specMapper.findSpecByCategoryName(categoryName);
        for (Map map : list) {
            String[] options = ((String) map.get("options")).split(",");
            map.put("options",options);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> findSpecByCategoryId(Integer categoryId) {

        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null){
            throw new RuntimeException("对应模板不存在");
        }

        List<Map<String, Object>> list = new ArrayList<>();

        Integer templateId = category.getTemplateId();

        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateId",templateId);
        List<Spec> specList = specMapper.selectByExample(example);

        for (Spec spec : specList) {
            Map<String, Object> data = new HashMap<>();
            data.put("name",spec.getName());
            String[] options = spec.getOptions().split(",");
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
        Example example=new Example(Spec.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 名称
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
           	}
            // 规格选项
            if(searchMap.get("options")!=null && !"".equals(searchMap.get("options"))){
                criteria.andLike("options","%"+searchMap.get("options")+"%");
           	}

            // ID
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
