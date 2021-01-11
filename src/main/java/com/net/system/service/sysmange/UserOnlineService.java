package com.net.system.service.sysmange;

import java.util.List;

import com.net.system.model.UserOnline;


public interface UserOnlineService {


    public List<UserOnline> list();

    public void forceLogout(String sessionId);

    public int count();
}
