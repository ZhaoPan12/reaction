package com.net.system.mapper.product;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Tichtext;
@Mapper
public interface TichtextMapper {
    int delete(String id);

    int insert(Tichtext tichtext);

    int insertDynamic(Tichtext tichtext);

    int updateDynamic(Tichtext tichtext);

    int update(Tichtext tichtext);

    Tichtext selectById(String id);

    List<Tichtext> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
}