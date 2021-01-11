package com.net.system.service.imple.order;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.net.system.mapper.order.UserReceivingInfoMapper;
import com.net.system.model.UserReceivingInfo;
import com.net.system.service.order.UserReceivingInfoService;

import javassist.expr.NewArray;

@Service
public class UserReceivingInfoServiceImpl implements UserReceivingInfoService {

	@Resource
	private UserReceivingInfoMapper userReceivingInfoMapper;

	@Override
	public int delUserReceivingInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userReceivingInfoMapper.delete(map);
	}

	
	@Override
	public int addUserReceivingInfo(Map<String, Object> map) {
		// 判断新增的收货地址是否为默认
		if (Boolean.parseBoolean(map.get("isDefault").toString()) == true) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("isDefault","false");
			param.put("userId", map.get("userId").toString());
			userReceivingInfoMapper.updateDynamic(param);
		}
		map.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("createTime",new Date());
		map.put("updateTime",new Date());
		return userReceivingInfoMapper.insert(map);
	}

	@Override
	public int insertDynamic(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateUserReceivingInfo(Map<String, Object> map) {
		
		//判断修改地址是否为默认地址
		if(null!=map.get("isDefault")&&Boolean.parseBoolean(map.get("isDefault").toString()) == true) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("isDefault","false");
			param.put("userId", map.get("userId").toString());
			userReceivingInfoMapper.updateDynamic(param);
		}
		map.remove("userId");
		map.put("updateTime",new Date());
		return userReceivingInfoMapper.updateDynamic(map);
	}

	@Override
	public int update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UserReceivingInfo findUserReceivingInfoById(Map<String, Object> map) {
		return userReceivingInfoMapper.selectById(map);
	}
	
	@Override
	public UserReceivingInfo findUserDefaultReceivingInfo(Map<String, Object> map) {
		return userReceivingInfoMapper.selectByDefault(map);
	}

	@Override
	public List<UserReceivingInfo> findUserReceivingInfo(Map<String, Object> map) {
		return userReceivingInfoMapper.findUserReceivingInfo(map);
	}

}
