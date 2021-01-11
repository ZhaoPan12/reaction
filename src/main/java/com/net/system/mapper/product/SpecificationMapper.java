package com.net.system.mapper.product;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Specification;

@Mapper
public interface SpecificationMapper {
    int delete(Integer id);

    int insert(Specification specification);

    int insertDynamic(Specification specification);

    int updateDynamic(Specification specification);

    int update(Specification specification);

    Specification selectById(Integer id);

    List<Specification> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
}
