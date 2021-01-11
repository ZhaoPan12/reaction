package com.net.system.mapper.product;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Discount;
@Mapper
public interface DiscountMapper {
    int delete(String id);

    int insert(Discount discount);

    int insertDynamic(Discount discount);

    int updateDynamic(Discount discount);

    int update(Discount discount);

    Discount selectById(String id);
    
    Discount selectDiscountById(String id);

    List<Discount> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);

    List<Discount> selectMyDiscountList(Map<String, Object> map);

    List<Discount> selectIsReceiveDiscountList(Map<String, Object> map);
}