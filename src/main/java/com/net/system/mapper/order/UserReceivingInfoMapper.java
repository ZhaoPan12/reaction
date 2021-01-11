package com.net.system.mapper.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.UserReceivingInfo;

@Mapper
public interface UserReceivingInfoMapper {
	
	    int delete(Map<String,Object> map);

	    int insert(Map<String,Object> map);

	    int insertDynamic(Map<String,Object> map);

	    int updateDynamic(Map<String,Object> map);

	    int update(Map<String,Object> map);

	    UserReceivingInfo selectById(Map<String,Object> map);
	    
	    UserReceivingInfo selectByDefault(Map<String,Object> map);

	    List<UserReceivingInfo> findUserReceivingInfo(Map<String,Object> map);



}
