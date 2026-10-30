package com.sky.controller.admin;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.vo.*;
import com.sky.result.*;
import com.sky.mapper.*;
import com.sky.service.*;
import com.sky.constant.*;
import com.sky.exception.*;
import com.sky.annotation.AutoFill;
import com.sky.enumeration.OperationType;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.apache.ibatis.annotations.Mapper; import org.apache.ibatis.annotations.Select; import org.apache.ibatis.annotations.Delete;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}",dishDTO);
        dishService.save(dishDTO);

        //清理缓存数据
        String key="dish_"+dishDTO.getCategoryId();
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}",dishPageQueryDTO);
        PageResult pageResult=dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品批量删除，{}",ids);
        dishService.delete(ids);

        //将所有的菜品缓存数据清理掉，所有以dish_开头的key
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);


        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO>selectById(@PathVariable Long id){
        log.info("根据id查询菜品，{}",id);
        DishVO dishVO=dishService.selectById(id);
        return  Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}",dishDTO);
        dishService.update(dishDTO);

        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success();
    }


/**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
*/
@GetMapping("/list")
@ApiOperation("根据分类id查询菜品")
public Result<List<Dish>> list(Long categoryId){
    List<Dish> list = dishService.list(categoryId);
    return Result.success(list);
}

@PostMapping("/status/{status}")
@ApiOperation("菜品起售停售")
public Result updateStatus(@PathVariable Integer status,Long id){
    log.info("菜品id为：{}",id+"修改菜品的状态为:"+(status==1?"起售":"停售"));

    dishService.updateStatus(status,id);
    Set keys = redisTemplate.keys("dish_*");
    redisTemplate.delete(keys);
    return Result.success();
}
}