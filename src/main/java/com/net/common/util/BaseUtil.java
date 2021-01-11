package com.net.common.util;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.net.system.model.User;

public class BaseUtil {

	// 过期时间，单位毫秒
	private static final long EXPIRE_TIME = 7*24*3600*1000;
	
	public static String getToken(User user) {
        String token="";
        // 生成过期时间
    	Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
        token= JWT.create()
                .withKeyId(String.valueOf(user.getUserId()))
                .withIssuer("www.ikertimes.com")
                .withExpiresAt(date)
                .withIssuedAt(new Date())
                .withJWTId("jwt.ikertimes.com")
                .withClaim("session_key", user.getSessionId())
                .withAudience(String.valueOf(user.getUserId()))
                .sign(Algorithm.HMAC256(user.getSkey()));
        return token;
    }
	
	/**
	 * @Title: getUsername
	 * @Description: 获取token中的信息无需secret解密也能获得
	 * @Author 刘仁
	 * @DateTime 2019年4月1日 下午4:42:39
	 * @param token
	 * @return
	 */
	public static String getUsername(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getKeyId().toString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	
	/**
     * 校验token
     *
     * @param secret 密钥
     * @param username 用户名
     * @param token token
     * @return 是否合法
     */
    public static boolean verify(String skey, String session_key, String token) {
        Algorithm algorithm = Algorithm.HMAC256(skey);
		JWT.require(algorithm)
		        .withClaim("session_key", session_key)
		        .build()
		        .verify(token);
		return true;
    }
}
