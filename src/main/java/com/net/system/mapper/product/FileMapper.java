package com.net.system.mapper.product;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Files;
@Mapper
public interface FileMapper {
    int delete(String id);

    int insert(Files file);

    int insertDynamic(Files file);

    int updateDynamic(Files file);

    int update(Files file);

    Files selectById(String id);

    List<Files> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
    
    int deleteByProductId(String productId);
}