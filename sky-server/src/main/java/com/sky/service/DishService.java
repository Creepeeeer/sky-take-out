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

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品和对应口味
     * @param dishDTO
     */
    public void save(DishDTO dishDTO);

    public PageResult page(DishPageQueryDTO dishPageQueryDTO);

    void delete(List<Long> ids);

    DishVO selectById(Long id);

    void update(DishDTO dishDTO);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);


/**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
*/
List<Dish> list(Long categoryId);

    void updateStatus(Integer status, Long id);
}