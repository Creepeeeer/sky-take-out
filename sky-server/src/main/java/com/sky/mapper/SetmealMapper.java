package com.sky.mapper;
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

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);



/**
     * 新增套餐
     * @param setmeal
*/
@AutoFill(OperationType.INSERT)
void insert(Setmeal setmeal);
/**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
*/
Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
/**
     * 根据id查询套餐
     * @param id
     * @return
*/
@Select("select * from setmeal where id = #{id}")
Setmeal getById(Long id);

/**
     * 根据id删除套餐
     * @param setmealId
*/
@Delete("delete from setmeal where id = #{id}")
void deleteById(Long setmealId);

@AutoFill(OperationType.UPDATE)
void update(Setmeal setmeal);
List<Setmeal> list(Setmeal setmeal); @Select("select sd.name, sd.copies, d.image, d.description from setmeal_dish sd left join dish d on sd.dish_id=d.id where sd.setmeal_id=#{setmealId}") List<DishItemVO> getDishItemBySetmealId(Long setmealId);}