package com.net.system.mapper.cardpack;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.mall.RechargeRecord;
@Mapper
public interface RechargeRecordMapper {
    int delete(String id);

    int insert(RechargeRecord rechargeRecord);

    int insertDynamic(RechargeRecord rechargeRecord);

    int updateDynamic(RechargeRecord rechargeRecord);

    int update(RechargeRecord rechargeRecord);

    RechargeRecord selectByOrderNo(String orderNo);

    List<RechargeRecord> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
    
    Integer updateOrderStatus(RechargeRecord rechargeRecord);
}
