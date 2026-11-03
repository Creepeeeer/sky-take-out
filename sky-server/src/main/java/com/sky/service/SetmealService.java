package com.sky.service;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.apache.ibatis.annotations.Mapper; import org.apache.ibatis.annotations.Select; import org.apache.ibatis.annotations.Delete;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
public interface SetmealService {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

/**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
*/
PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
/**
     * 批量删除套餐
     * @param ids
*/
void deleteBatch(List<Long> ids);
/**
     * 根据id查询套餐和关联的菜品数据
     * @param id
     * @return
*/
SetmealVO getByIdWithDish(Long id);

/**
     * 修改套餐
     * @param setmealDTO
*/
void update(SetmealDTO setmealDTO);
/**
     * 套餐起售、停售
     * @param status
     * @param id
*/
void startOrStop(Integer status, Long id);
List<Setmeal> list(Setmeal setmeal); List<DishItemVO> getDishItemById(Long id);}