package com.net.system.service.imple.cardpack;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.net.system.mapper.cardpack.CardUserMapper;
import com.net.system.model.CardUser;
import com.net.system.model.vo.CardUserVo;
import com.net.system.service.cardpack.CardUserService;

import cn.hutool.core.lang.UUID;

@Service
public class CardUserServiceImple implements CardUserService {
	
	@Autowired
	private CardUserMapper cardUserMapper;

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(CardUser cardUser) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertDynamic(CardUser cardUser) {
		cardUser.setAddtime(new Date());
		cardUser.setId(UUID.randomUUID().toString().replace("-", ""));
		// TODO Auto-generated method stub
		return cardUserMapper.insertDynamic(cardUser);
	}

	@Override
	public int updateDynamic(CardUser cardUser) {
		// TODO Auto-generated method stub
		return cardUserMapper.updateDynamic(cardUser);
	}

	@Override
	public int update(CardUser cardUser) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CardUser selectById(String id) {
		// TODO Auto-generated method stub
		return cardUserMapper.selectById(id);
	}

	@Override
	public List<CardUser> findPageWithResult(Map<String, Object> map) {
		String page=(String) map.get("page");
		String rows=(String) map.get("limit");
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		// TODO Auto-generated method stub
		return cardUserMapper.findPageWithResult(map);
	}

	@Override
	public Integer findPageWithCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return cardUserMapper.findPageWithCount(map);
	}

	@Override
	public CardUser selectByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		return cardUserMapper.selectByOrderNo(orderNo);
	}

	@Override
	public CardUser selectByUserCard(CardUser cardUser) {
		// TODO Auto-generated method stub
		return cardUserMapper.selectByUserCard(cardUser);
	}

	@Override
	public List<CardUserVo> selectByUserCardVo(String idCard) {
		// TODO Auto-generated method stub
		return cardUserMapper.selectByUserCardVo(idCard);
	}

	@Override
	public Integer selectIsCard(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return cardUserMapper.selectIsCard(map);
	}

}
