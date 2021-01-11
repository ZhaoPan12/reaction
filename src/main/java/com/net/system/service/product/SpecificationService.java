package com.net.system.service.product;

import java.util.List;
import java.util.Map;

import com.net.system.model.Specification;

public interface SpecificationService {

    int delete(Integer id);

    int insert(Specification specification);

    int insertDynamic(Specification specification);

    int updateDynamic(Specification specification);

    int update(Specification specification);

    Specification selectById(Integer id);

    List<Specification> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
}
