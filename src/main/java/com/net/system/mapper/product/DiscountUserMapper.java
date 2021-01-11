package com.net.system.mapper.product;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.DiscountUser;
@Mapper
public interface DiscountUserMapper {
    int delete(String id);

    int insert(DiscountUser discountUser);

    int insertDynamic(DiscountUser discountUser);

    int updateDynamic(DiscountUser discountUser);

    int update(DiscountUser discountUser);

    DiscountUser selectById(String id);

    List<DiscountUser> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
    
    int selectDiscountUserById(DiscountUser discountUser);
    
    int selectDiscountUserByDiscountId(String DiscountId);
    
    
    List<Map<String,Object>> filterCoupon(String userId);
    
    Map<String,Object> findReductionById(String id);
    
    String findCouponName(String id);
    
}