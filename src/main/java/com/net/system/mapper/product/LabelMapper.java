package com.net.system.mapper.product;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Label;
import com.net.system.model.Tup;
@Mapper
public interface LabelMapper {
    int delete(String productId);

    int insert(Label label);

    int insertDynamic(Label label);

    int updateDynamic(Label label);

    int update(Label label);

    Label selectById(String id);

    List<Label> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
    
    List<Tup> findTupList(Map<String,Object> map);
    
    List<Label> selectLabelByProductId(String productId);
}
