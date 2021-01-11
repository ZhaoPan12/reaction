package com.net.system.mapper.product;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.ProductSkuVal;

@Mapper
public interface ProductSkuValMapper {
    int delete(Integer id);
    
    int deleteByKeyId(Integer id);

    int insert(ProductSkuVal productSkuVal);

    int insertDynamic(ProductSkuVal productSkuVal);

    int updateDynamic(ProductSkuVal productSkuVal);

    int update(ProductSkuVal productSkuVal);

    ProductSkuVal selectById(Integer id);

    List<ProductSkuVal> findPageWithResult(Map<String, Object> map);

    
    String finSkuId(Map<String,Object> map);

}