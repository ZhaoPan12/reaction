package com.net.system.service.imple.sysmange;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.net.system.mapper.sysmange.RoleOperatorMapper;
import com.net.system.model.RoleOperator;
import com.net.system.service.sysmange.RoleOperatorService;

@Service
public class RoleOperatorServiceImple implements RoleOperatorService{

		@Resource
	    private RoleOperatorMapper roleOperatorMapper;

	    public int insert(RoleOperator roleOperator) {
	        return roleOperatorMapper.insert(roleOperator);
	    }
}
