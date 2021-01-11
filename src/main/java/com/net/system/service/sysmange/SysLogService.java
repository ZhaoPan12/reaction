package com.net.system.service.sysmange;

import java.util.List;

import com.net.system.model.SysLog;


public interface SysLogService {
    public List<SysLog> selectAll(int page, int rows);

    public int count();
}
