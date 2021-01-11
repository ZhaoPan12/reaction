package com.net.system.service.cardpack;

import java.util.List;
import java.util.Map;

import com.net.system.model.WriteOff;
import com.net.system.model.vo.WriteOffVo;

public interface WriteOffService {
    int delete(String id);

    int insert(WriteOff writeOff);

    int insertDynamic(WriteOff writeOff);

    int updateDynamic(WriteOff writeOff);

    int update(WriteOff writeOff);

    WriteOff selectById(Map<String,Object> map);

    List<WriteOff> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
    
    List<WriteOffVo> findWithResult(WriteOff writeOff);
}
