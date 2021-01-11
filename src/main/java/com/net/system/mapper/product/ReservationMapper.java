package com.net.system.mapper.product;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Reservation;
@Mapper
public interface ReservationMapper {
    int delete(String id);

    int insert(Reservation reservation);

    int insertDynamic(Reservation reservation);

    int updateDynamic(Reservation reservation);

    int update(Reservation reservation);

    Reservation selectById(String id);

    List<Reservation> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
    
    Integer findUserReservation(Map<String,Object> map);
}