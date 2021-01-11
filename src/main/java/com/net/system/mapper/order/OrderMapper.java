package com.net.system.mapper.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Order;
import com.net.system.model.vo.OrderVo;
import com.net.system.model.vo.ShopOrderVo;
@Mapper
public interface OrderMapper {
	
    int delete(String id);

    int insert(Order order);

    int insertDynamic(Order order);

    int updateDynamic(Order order);

    int update(Order order);

    Order selectById(String id);

    List<Order> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);

    
    List<OrderVo> findPageWithResultByType(Map<String,Object> map);

    Integer updateStatus(Map<String,Object> map);
    
    int updateOrderStatus(Order order);
    
    Order selectByOrderNo(String serialNumber);
    
    List<Order> findCommodityOrder(Map<String,Object> map);
    
    ShopOrderVo findCommodityOrderById(Map<String,Object> map);

    List<Order> selectByMemberId(String memberId);
}
