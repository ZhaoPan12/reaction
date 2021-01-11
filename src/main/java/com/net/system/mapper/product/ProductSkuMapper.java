package com.net.system.mapper.product;




import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.OrderProduct;
import com.net.system.model.ProductSku;

@Mapper
public interface ProductSkuMapper {
	
    int delete(String skuId);
    
    int deleteByProductId(String productId);

    int insert(ProductSku productSku);

    int insertDynamic(ProductSku productSku);

    int updateDynamic(ProductSku productSku);

    int update(ProductSku productSku);

    ProductSku selectBySkuId(String skuId);

    List<ProductSku> findPageWithResult(Map<String,Object> map);
    
    int returnStock(OrderProduct orderProduct);
    
    int reduceStock(OrderProduct orderProduct);
    
    
}