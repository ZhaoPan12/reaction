package com.net.system.service.sysmange;

import com.net.common.constants.AuthcTypeEnum;
import com.net.system.model.UserAuths;

public interface UserAuthsService {


    public int deleteByPrimaryKey(Integer id);

    public int insert(UserAuths record);

    public UserAuths selectByPrimaryKey(Integer id);

    public int updateByPrimaryKey(UserAuths record);

	public UserAuths selectOneByIdentityTypeAndUserId(AuthcTypeEnum authcTypeEnum, Integer userId);

    public UserAuths selectOneByIdentityTypeAndIdentifier(AuthcTypeEnum authcTypeEnum, String identifier);

    public int deleteByUserId(Integer userId);
}

