package com.net.system.mapper.order;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Order;
import com.net.system.model.OrderLogistics;

@Mapper
public interface OrderLogisticsMapper {
	
    int delete(String id);
    
    int deleteByOrderId(String id);

    int insert(OrderLogistics orderLogistics);

    int insertDynamic(OrderLogistics orderLogistics);

    int updateDynamic(OrderLogistics orderLogistics);

    int update(OrderLogistics orderLogistics);

    OrderLogistics selectById(String id);

    List<OrderLogistics> findPageWithResult(Map<String,Object> map);
    
    
    OrderLogistics findByOrderId(Order order);

}