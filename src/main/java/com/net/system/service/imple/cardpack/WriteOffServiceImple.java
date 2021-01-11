package com.net.system.service.imple.cardpack;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.net.system.mapper.cardpack.WriteOffMapper;
import com.net.system.model.WriteOff;
import com.net.system.model.vo.WriteOffVo;
import com.net.system.service.cardpack.WriteOffService;

import cn.hutool.core.lang.UUID;

@Service
public class WriteOffServiceImple implements WriteOffService{
	
	@Resource
	private WriteOffMapper writeOffMapper;

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(WriteOff writeOff) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertDynamic(WriteOff writeOff) {
		writeOff.setId(UUID.randomUUID().toString().replace("-", ""));
		// TODO Auto-generated method stub
		return writeOffMapper.insertDynamic(writeOff);
	}

	@Override
	public int updateDynamic(WriteOff writeOff) {
		writeOff.setId(UUID.randomUUID().toString().replace("-", ""));
		// TODO Auto-generated method stub
		return writeOffMapper.insertDynamic(writeOff);
	}

	@Override
	public int update(WriteOff writeOff) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public WriteOff selectById(Map<String,Object> map) {
		// TODO Auto-generated method stub
		return writeOffMapper.selectById(map);
	}

	@Override
	public List<WriteOff> findPageWithResult(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer findPageWithCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WriteOffVo> findWithResult(WriteOff writeOff) {
		// TODO Auto-generated method stub
		return writeOffMapper.findWithResult(writeOff);
	}

}
