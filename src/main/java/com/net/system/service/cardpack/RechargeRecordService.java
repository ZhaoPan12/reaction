package com.net.system.service.cardpack;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.net.common.util.ResultBean;
import com.net.system.model.mall.RechargeRecord;

public interface RechargeRecordService {

    int delete(String id);

    int insert(RechargeRecord rechargeRecord);

    int insertDynamic(RechargeRecord rechargeRecord);

    int updateDynamic(RechargeRecord rechargeRecord);

    int update(RechargeRecord rechargeRecord);

    RechargeRecord selectByOrderNo(String orderNo);

    List<RechargeRecord> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
    
    ResultBean RechargePay(Map<String,Object> map,HttpServletRequest request);
    
    ResultBean updateOrderStatus(RechargeRecord rechargeRecord);
}
