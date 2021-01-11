package com.net.system.mapper.product;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.ProductSkuKey;
import com.net.system.model.vo.ProductSkuKeyVo;

@Mapper
public interface ProductSkuKeyMapper {
    int delete(Integer id);

    int insert(ProductSkuKey productSkuKey);

    int insertDynamic(ProductSkuKey productSkuKey);

    int updateDynamic(ProductSkuKey productSkuKey);

    int update(ProductSkuKey productSkuKey);

    ProductSkuKey selectById(String id);

    List<ProductSkuKey> findPageWithResult(Map<String,Object> map);
    
    List<ProductSkuKeyVo> findSpecsByProductId(ProductSkuKey productSkuKey);

    
}