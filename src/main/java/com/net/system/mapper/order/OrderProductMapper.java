package com.net.system.mapper.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Order;
import com.net.system.model.OrderProduct;
@Mapper
public interface OrderProductMapper {
    int delete(String id);

    int insert(OrderProduct orderProduct);

    int insertDynamic(OrderProduct orderProduct);

    int updateDynamic(OrderProduct orderProduct);

    int update(OrderProduct orderProduct);

    OrderProduct selectById(String id);

    List<OrderProduct> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);

    
    OrderProduct findByOrderId(Order order);

    
    List<OrderProduct> findOrderProduct(String id);
    
    Integer countSalesVolume(String id);

}
