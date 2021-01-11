package com.net.system.service.imple.product;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.net.system.mapper.product.ReservationMapper;
import com.net.system.model.Reservation;
import com.net.system.service.product.ReservationService;

import cn.hutool.core.lang.UUID;

@Service
public class ReservationServiceImple implements ReservationService {
	
	@Autowired
	private  ReservationMapper reservationMapper;
	

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Reservation reservation) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertDynamic(Reservation reservation) {
		reservation.setCreatetime(new Date());
		reservation.setId(UUID.randomUUID().toString().replace("-",""));
		reservation.setStatus(1);
		// TODO Auto-generated method stub
		return reservationMapper.insertDynamic(reservation);
	}

	@Override
	public int updateDynamic(Reservation reservation) {
		// TODO Auto-generated method stub
		return reservationMapper.updateDynamic(reservation);
	}

	@Override
	public int update(Reservation reservation) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Reservation selectById(String id) {
		// TODO Auto-generated method stub
		return reservationMapper.selectById(id);
	}

	@Override
	public List<Reservation> findPageWithResult(Map<String, Object> map) {
		String page=(String) map.get("page");
		String rows=(String) map.get("limit");
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		// TODO Auto-generated method stub
		return reservationMapper.findPageWithResult(map);
	}

	@Override
	public Integer findPageWithCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer findUserReservation(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return reservationMapper.findUserReservation(map);
	}

}
