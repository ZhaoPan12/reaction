package com.net.system.service.cardpack;

import java.util.List;
import java.util.Map;

import com.net.system.model.CardUser;
import com.net.system.model.User;
import com.net.system.model.vo.CardUserVo;

public interface CardUserService {

    int delete(String id);

    int insert(CardUser cardUser);

    int insertDynamic(CardUser cardUser);

    int updateDynamic(CardUser cardUser);

    int update(CardUser cardUser);

    CardUser selectById(String id);

    List<CardUser> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
    
    CardUser selectByOrderNo(String orderNo);
    
    CardUser selectByUserCard(CardUser cardUser);
    
    List<CardUserVo> selectByUserCardVo(String  idCard);
    
    Integer selectIsCard(Map<String,Object> map);

}
