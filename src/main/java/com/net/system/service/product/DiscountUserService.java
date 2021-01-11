package com.net.system.service.product;

import java.util.List;
import java.util.Map;

import com.net.common.util.ResultBean;
import com.net.system.model.Discount;
import com.net.system.model.DiscountUser;
import com.net.system.model.OrderProduct;

public interface DiscountUserService {

    int delete(String id);

    ResultBean receive(DiscountUser discountUser);

    int insertDynamic(DiscountUser discountUser);

    int updateDynamic(DiscountUser discountUser);

    int update(DiscountUser discountUser);

    DiscountUser selectById(String id);

    List<DiscountUser> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);

    ResultBean insertDiscount(DiscountUser discountUser);
    
    List<Map<String, Object>> filterCoupon(List<OrderProduct> orderProducts,String userId,double total);
}
