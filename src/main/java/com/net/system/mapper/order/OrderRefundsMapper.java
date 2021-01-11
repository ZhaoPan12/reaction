package com.net.system.mapper.order;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.OrderRefunds;
import com.net.system.model.vo.OrderRefundsVo;

@Mapper
public interface OrderRefundsMapper {
	
    int delete(String id);

    int insert(OrderRefunds orderRefunds);

    int insertDynamic(OrderRefunds orderRefunds);

    int updateDynamic(OrderRefunds orderRefunds);

    int update(OrderRefunds orderRefunds);

    OrderRefundsVo selectById(String id);

    List<Map<String,Object>> findPageWithResult(Map<String,Object> map);
    
    OrderRefundsVo findByOrderId(String orderId);
    

   
}