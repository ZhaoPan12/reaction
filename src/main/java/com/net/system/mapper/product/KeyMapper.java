package com.net.system.mapper.product;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Key;
@Mapper
public interface KeyMapper {
	
    int delete(String id);

    int insert(Key key);

    int insertDynamic(Key key);

    int updateDynamic(Key key);

    int update(Key key);

    Key selectById(String channelId);

    List<Key> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
}