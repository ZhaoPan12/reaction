package com.net.system.mapper.cardpack;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.CardUser;
import com.net.system.model.vo.CardUserVo;
@Mapper
public interface CardUserMapper {
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
    //更改购买卡的状态
    Integer updateByUserCardStatus(CardUser cardUser);
    
    Integer selectIsCard(Map<String,Object> map);
}