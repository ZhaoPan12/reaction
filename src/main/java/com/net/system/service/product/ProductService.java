package com.net.system.service.product;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.net.system.model.Label;
import com.net.system.model.Product;
import com.net.system.model.ProductSku;
import com.net.system.model.Tup;
import com.net.system.model.vo.ProductSkuInfoVo;
import com.net.system.model.vo.ProductSkuVo;
import com.net.system.model.vo.ProeratorVo;

public interface ProductService {
	
	ProductSku findSkuById(String id);
	
    int delete(String id);

    int insert(Product product);

    int insertDynamic(Product product);

    int updateDynamic(Product product);

    int update(ProeratorVo proeratorVo)throws IllegalStateException, IOException;

    Product selectById(String id);

    List<Product> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
    
    List<Tup> findTupList(Map<String,Object> map);
    
    int insertDynamic(Label label);
    
    List<Label> selectLabelByProductId(String productId);
    
    List<Product> findProductCard(Map<String,Object> map);
    
    Product selectBychannelId(String channelId);
    
    Map<String,Object> findProductSkuByid(String id);

	List<Product> selectByName(Map<String, Object> map);

	List<String> selectByProductName(String productName);

	Product selectByRightDtlCode(String rightDtlCode);

}
