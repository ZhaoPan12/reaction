package com.net.system.mapper.product;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.ProductAttrKey;
import com.net.system.model.vo.ProductAttrKeyVo;

@Mapper
public interface ProductAttrKeyMapper {
	
    int delete(Integer id);

    int insert(ProductAttrKey productAttrKey);

    int insertDynamic(ProductAttrKey productAttrKey);

    int updateDynamic(ProductAttrKey productAttrKey);

    int update(ProductAttrKey productAttrKey);

    ProductAttrKey selectById(Integer id);

    List<ProductAttrKeyVo> findPageWithResult(Map<String,Object> map);

}