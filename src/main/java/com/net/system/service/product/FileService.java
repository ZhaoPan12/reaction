package com.net.system.service.product;

import java.util.List;
import java.util.Map;

import com.net.system.model.Files;
import com.net.system.model.Product;

public interface FileService {
	   int delete(String id);

	    int insert(Files file);

	    int insertDynamic(Files file);

	    int updateDynamic(Files file);

	    int update(Files file);

	    Files selectById(String id);

	    List<Files> findPageWithResult(Map<String,Object> map);

	    Integer findPageWithCount(Map<String,Object> map);

}
