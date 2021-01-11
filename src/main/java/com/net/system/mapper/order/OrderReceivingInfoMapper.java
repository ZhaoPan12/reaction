package com.net.system.mapper.order;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Order;
import com.net.system.model.OrderReceivingInfo;

@Mapper
public interface OrderReceivingInfoMapper {
    int delete(String id);

    int insert(OrderReceivingInfo orderReceivingInfo);

    int insertDynamic(OrderReceivingInfo orderReceivingInfo);

    int updateDynamic(OrderReceivingInfo orderReceivingInfo);

    int update(OrderReceivingInfo orderReceivingInfo);

    OrderReceivingInfo selectById(String id);

    List<OrderReceivingInfo> findPageWithResult(Map<String,Object> map);

    OrderReceivingInfo findByOrderId(Order order);
    
}