package com.net.system.service.imple.sysmange;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.net.system.mapper.sysmange.SysLogMapper;
import com.net.system.model.SysLog;
import com.net.system.service.sysmange.SysLogService;

@Service
public class SysLogServiceImple implements SysLogService{

	@Resource
    private SysLogMapper sysLogMapper;

    public List<SysLog> selectAll(int page, int rows) {
        PageHelper.startPage(page, rows);
        return sysLogMapper.selectAll();
    }

    public int count() {
        return sysLogMapper.count();
    }
}
