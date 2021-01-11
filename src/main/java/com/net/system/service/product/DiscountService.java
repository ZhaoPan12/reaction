package com.net.system.service.product;

import java.util.List;
import java.util.Map;

import com.net.system.model.Discount;
import com.net.system.model.DiscountUser;

public interface DiscountService {

    int delete(String id);

    int insert(Discount discount);

    int insertDynamic(Discount discount);

    int updateDynamic(Discount discount);

    int update(Discount discount);

    Discount selectById(String id);

    List<Discount> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);

    List<Discount> selectMyDiscountList(Map<String, Object> map);

    List<Discount> selectIsReceiveDiscountList(Map<String, Object> map);
}
