package com.net.system.mapper.product;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Carousel;

import java.util.List;
import java.util.Map;
@Mapper
public interface CarouselMapper {
    int delete(String id);

    int insert(Carousel carousel);

    int insertDynamic(Carousel carousel);

    int updateDynamic(Carousel carousel);

    int update(Carousel carousel);

    Carousel selectById(String id);

    List<Carousel> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
}
