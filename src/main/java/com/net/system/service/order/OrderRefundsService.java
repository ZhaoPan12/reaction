package com.net.system.service.order;
import java.util.List;
import java.util.Map;

import com.net.system.model.BusinessAddress;
import com.net.system.model.OrderRefunds;
import com.net.system.model.vo.OrderRefundsVo;

public interface OrderRefundsService {
	
	 int applyRefunds(OrderRefundsVo orderRefundsVo);
	 
	 int cancelRefunds(String id);
	 
	 int updateStatus(OrderRefunds orderRefunds);
	 
	 List<Map<String,Object>> findPageWithResult(Map<String,Object> map);
	 
	 BusinessAddress findBusinessAddress();

}
