package com.net.system.service.product;

import java.util.List;
import java.util.Map;

import com.net.system.model.Carousel;

public interface CarouselService {

	 int delete(String id);

	    int insert(Carousel carousel);

	    int insertDynamic(Carousel carousel);

	    int updateDynamic(Carousel carousel);

	    int update(Carousel carousel);

	    Carousel selectById(String id);

	    List<Carousel> findPageWithResult(Map<String,Object> map);

	    Integer findPageWithCount(Map<String,Object> map);
}
