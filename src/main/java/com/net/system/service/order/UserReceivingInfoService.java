package com.net.system.service.order;

import java.util.List;
import java.util.Map;

import com.net.system.model.UserReceivingInfo;

public interface UserReceivingInfoService {

	int delUserReceivingInfo(Map<String, Object> map);

	int addUserReceivingInfo(Map<String, Object> map);

	int insertDynamic(Map<String, Object> map);

	int updateUserReceivingInfo(Map<String, Object> map);

	int update(Map<String, Object> map);

	UserReceivingInfo findUserReceivingInfoById(Map<String, Object> map);
	
	UserReceivingInfo findUserDefaultReceivingInfo(Map<String, Object> map);

	List<UserReceivingInfo> findUserReceivingInfo(Map<String, Object> map);

}
