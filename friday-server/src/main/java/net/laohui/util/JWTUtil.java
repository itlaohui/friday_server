package net.laohui.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laohui
 * @date 2022/04/22
 * @description: JWT工具类
 * @version: 1.0
 */
@Log4j2
public class JWTUtil {
    //过期时间
    private static final long EXPIRE_TIME = 60 * 60 * 24 * 1000;//默认24小时
    //私钥
    private static final String TOKEN_SECRET = "privateKey";
    /**
     * 生成签名，24小时过期
     * @param **username**
     * @param **password**
     * @return
     */
    public static String createToken(Map<String, Object> data) {
        try {
            // 设置过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withClaim("userInfo", data)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成token，自定义过期时间 毫秒
     * @param **username**
     * @param **password**
     * @return
     */
    public static String createToken(Map<String, Object> data, long expireDate) {
        try {
            // 设置过期时间
            Date date = new Date(System.currentTimeMillis() + expireDate);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");
            // 返回token字符串
            return JWT.create().
                    withHeader(header)
                    .withClaim("userInfo", data)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 检验token是否正确
     * @param **token**
     * @return
     */
    public static Map<String, Object> verifyToken(String token){
        if (token == null || token.equals("")) {
            return null;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("userInfo").asMap();
        } catch (Exception e){
            log.error("发生错误，检验token是否正确:{}", e.getMessage());
            return null;
        }
    }
}

