package com.net.system.mapper.cardpack;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.WriteOff;
import com.net.system.model.vo.WriteOffVo;
@Mapper
public interface WriteOffMapper {
    int delete(String id);

    int insert(WriteOff writeOff);

    int insertDynamic(WriteOff writeOff);

    int updateDynamic(WriteOff writeOff);

    int update(WriteOff writeOff);

    WriteOff selectById(Map<String,Object> map);

    List<WriteOff> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
    
    WriteOff selectByWriteOff(WriteOff writeOff);
    
    List<WriteOffVo> findWithResult(WriteOff writeOff);
}