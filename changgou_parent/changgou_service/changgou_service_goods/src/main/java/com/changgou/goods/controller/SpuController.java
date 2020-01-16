package com.changgou.goods.controller;
import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.service.SpuService;
import com.changgou.goods.pojo.Spu;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin
@RequestMapping("/spu")
public class SpuController {


    @Autowired
    private SpuService spuService;

    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result findAll(){
        List<Spu> spuList = spuService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",spuList) ;
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable String id){
//        Spu spu = spuService.findById(id);
        Goods goods = spuService.findBySpuId(id);
        return new Result(true,StatusCode.OK,"查询成功",goods);
    }


    /***
     * 新增数据
     * @param goods
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Goods goods){
        spuService.add(goods);
        return new Result(true,StatusCode.OK,"添加成功");
    }


    /***
     * 修改数据
     * @param goods
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody Goods goods,@PathVariable String id){
        spuService.update(goods);
        return new Result(true,StatusCode.OK,"修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable String id){
        spuService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search" )
    public Result findList(@RequestParam Map searchMap){
        List<Spu> list = spuService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result findPage(@RequestParam Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<Spu> pageList = spuService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }

    @PutMapping("/audit/{spuId}")
    public Result audit(@PathVariable("spuId") String spuId){
        spuService.audit(spuId);
        return new Result(true,StatusCode.OK,"商品审核成功");
    }

    @PutMapping("/pull/{spuId}")
    public Result pull(@PathVariable("spuId") String spuId){
        spuService.pull(spuId);
        return new Result(true,StatusCode.OK,"商品下架成功");
    }

    @PutMapping("/put/{spuId}")
    public Result put(@PathVariable("spuId") String spuId){
        spuService.put(spuId);
        return new Result(true,StatusCode.OK,"商品上架成功");
    }

    @PutMapping("/restore/{spuId}")
    public Result restore(@PathVariable("spuId") String spuId){
        spuService.restore(spuId);
        return new Result(true,StatusCode.OK,"商品还原成功");
    }

    @DeleteMapping("/realDel/{spuId}")
    public Result realDel(@PathVariable("spuId") String spuId){
        spuService.realDel(spuId);
        return new Result(true,StatusCode.OK,"商品已彻底删除");
    }


}
