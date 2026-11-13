package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 统计指定区间的营业额数据
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin到end范围内每天的日期
        List<LocalDate> datelist =new ArrayList<>();
        List<Double>turnoverList=new ArrayList<>();
        for(LocalDate it=begin;true;it=it.plusDays(1)){
            datelist.add(it);
            if(it.equals(end))break;
        }
        for (LocalDate date : datelist) {
            //查询date日期对应的营业额 营业额指：状态为已完成的订单金额合计
            LocalDateTime beginTime= LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime= LocalDateTime.of(date, LocalTime.MAX);
            Map map=new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover=orderMapper.sumByMap(map);
            turnover=turnover==null?0.0:turnover;
            turnoverList.add(turnover);
        }
        return TurnoverReportVO.builder()
                .dateList(StringUtils.collectionToDelimitedString(datelist, ","))
                .turnoverList(StringUtils.collectionToDelimitedString(turnoverList,","))
                .build();
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate>dateList=new ArrayList<>();
        List<Long>userList=new ArrayList<>();
        List<Long>newUserList=new ArrayList<>();
        Map historyMap =new HashMap();
        historyMap.put("end",LocalDateTime.of(begin.plusDays(-1),LocalTime.MAX));
        Long last=userMapper.getByRegisterTime(historyMap);
        for(LocalDate it=begin;!it.isAfter(end);it=it.plusDays(1)){
            dateList.add(it);
        }
        for (LocalDate date : dateList) {
            //查询date日期对应的营业额 营业额指：状态为已完成的订单金额合计
            LocalDateTime beginTime= LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime= LocalDateTime.of(date, LocalTime.MAX);
            Map map=new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);
            Long addCnt=userMapper.getByRegisterTime(map);
            addCnt=addCnt==null?0:addCnt;
            newUserList.add(addCnt);
            last+=addCnt;
            userList.add(last);
        }
        return UserReportVO.builder().dateList(StringUtils.collectionToDelimitedString(dateList,","))
                .totalUserList(StringUtils.collectionToDelimitedString(userList,","))
                .newUserList(StringUtils.collectionToDelimitedString(newUserList,","))
                .build();
    }

    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate>dateList=new ArrayList<>();
        List<Integer>orderCountList=new ArrayList<>();
        List<Integer>validOrderCountList=new ArrayList<>();
        for(LocalDate it=begin;!it.isAfter(end);it=it.plusDays(1)){
            dateList.add(it);
        }
        for (LocalDate date : dateList) {
            LocalDateTime beginTime= LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime= LocalDateTime.of(date, LocalTime.MAX);
            Map map=new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);
            Integer orderCount = orderMapper.countOrderByTime(map);
            map.put("status",Orders.COMPLETED);
            Integer validOrderCount = orderMapper.countOrderByTime(map);
            orderCount=orderCount==null?0:orderCount;
            validOrderCount=validOrderCount==null?0:validOrderCount;
            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }
        Integer total=0,valid=0;
        for (Integer orderCount : orderCountList) {
            total+=orderCount;
        }
        for (Integer orderCount : validOrderCountList) {
            valid+=orderCount;
        }
        OrderReportVO orderReportVO = OrderReportVO.builder()
                .dateList(StringUtils.collectionToDelimitedString(dateList,","))
                .orderCountList(StringUtils.collectionToDelimitedString(orderCountList,","))
                .validOrderCountList(StringUtils.collectionToDelimitedString(validOrderCountList,","))
                .totalOrderCount(total)
                .validOrderCount(valid)
                .orderCompletionRate(total == 0 ? 0.0 : 1.0 * valid / total)
                .build();
        return orderReportVO;
    }

    @Override
    public SalesTop10ReportVO getSalesTop10Statistics(LocalDate begin, LocalDate end) {
        return null;
    }
}