package com.net.system.mapper.product;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.OrderProduct;
import com.net.system.model.Product;
import com.net.system.model.vo.ProductSkuInfoVo;
import com.net.system.model.vo.ProductSkuVo;
@Mapper
public interface ProductMapper {
    int delete(String id);

    int insert(Product product);

    int insertDynamic(Product product);

    int updateDynamic(Product product);

    int update(Product product);

    Product selectById(String id);

    List<Product> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
    
    Product selectBychannelId(String channelId);

    
    ProductSkuInfoVo findProductSkuByid(String id);

    List<Product> selectByName(Map<String, Object> map);

    List<String> selectProductListByProductName(String productName);
    
    Product selectByRightDtlCode(String productSn);
    
    int returnStock(OrderProduct orderProduct);
    
    int reduceStock(OrderProduct orderProduct);
    
   
}
