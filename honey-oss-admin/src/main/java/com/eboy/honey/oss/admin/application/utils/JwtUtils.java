package com.eboy.honey.oss.admin.application.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.eboy.honey.oss.admin.application.constant.JWTConstant;
import com.eboy.honey.oss.admin.application.entity.po.UserPo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangzhijie
 * @date 2020/10/14 11:36
 */
public class JwtUtils {

    /**
     * 过期时间
     */
    private static final long EXPIRE_TIME = 15 * 60 * 1000;

    /**
     * 生成签名，15分钟过期
     */
    public static String sign(UserPo user) {
        try {
            // 设置过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(JWTConstant.SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withClaim("userId", user.getUserId())
                    .withClaim("userName", user.getUserName())
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
