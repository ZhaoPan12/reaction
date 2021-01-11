package com.net.system.service.imple.product;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.net.system.mapper.product.FileMapper;
import com.net.system.model.Files;
import com.net.system.service.product.FileService;
@Service
public class FileServiceImple implements FileService {
	
	@Autowired
	private FileMapper fileMapper;

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Files file) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertDynamic(Files file) {
		// TODO Auto-generated method stub
		return fileMapper.insertDynamic(file);
	}

	@Override
	public int updateDynamic(Files file) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Files file) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Files selectById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Files> findPageWithResult(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer findPageWithCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

}
