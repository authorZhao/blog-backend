package com.git.blog.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author t430
 */
@Slf4j
public class JwtUtil {

    private static final String SERVICE = "DEVOPS-ADMIN-SERVER";

    private static final String IDENTIFICATION = "IDENTIFICATION";

    private static final Map<String, Algorithm> algorithmMap = new HashMap<>(15);

    private static final List<Algorithm> algorithmList = new ArrayList<>();

    private static Algorithm algorithm(String secretKey) {
        return algorithmMap.computeIfAbsent(secretKey, k -> Algorithm.HMAC256(secretKey));
    }


    /**
     * 创建token
     *
     * @param value      信息
     * @param role       角色
     * @param subject    主题
     * @param expireTime 过期时间，单位时
     * @return
     */
    public static String createToken(String value, String role, String subject, int expireTime, String secretKey) {

        try {
            Algorithm algorithm = algorithm(secretKey);
            Map<String, Object> map = new HashMap<>(2);
            //token提前5分钟生成，防止时间不一样
            Date nowDate = new Date(System.currentTimeMillis()-5*60*1000);

            //2小过期
            Date expireDate = getAfterDate(nowDate, 0, 0, 0, expireTime, 0, 0);
            map.put("alg", "HS256");
            map.put("typ", "JWT");
            String token = JWT.create()
                    /*设置头部信息 Header*/
                    .withHeader(map)
                    /*设置 载荷 Payload*/
                    .withClaim(IDENTIFICATION, value)
                    //签名是有谁生成 例如 服务器
                    .withIssuer(SERVICE)
                    //签名的主题
                    .withSubject(subject)
                    //定义在什么时间之前，该jwt都是不可用的.
                    //.withNotBefore(new Date())
                    //签名的观众 也可以理解谁接受签名的
                    .withAudience(role)
                    //生成签名的时间
                    .withIssuedAt(nowDate)
                    //签名过期的时间
                    .withExpiresAt(expireDate)
                    /*签名 Signature */
                    .sign(algorithm);
            //log.info("token生成 {}", token);
            return token;
        } catch (Exception exception) {
            log.error(String.format("createToken fail value: %s", value), exception);
        }
        return null;
    }

    /**
     * 返回一定时间后的日期
     *
     * @param date   开始计时的时间
     * @param year   增加的年
     * @param month  增加的月
     * @param day    增加的日
     * @param hour   增加的小时
     * @param minute 增加的分钟
     * @param second 增加的秒
     * @return
     */
    public static Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = new GregorianCalendar();

        cal.setTime(date);
        if (year != 0) {
            cal.add(Calendar.YEAR, year);
        }
        if (month != 0) {
            cal.add(Calendar.MONTH, month);
        }
        if (day != 0) {
            cal.add(Calendar.DATE, day);
        }
        if (hour != 0) {
            cal.add(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute != 0) {
            cal.add(Calendar.MINUTE, minute);
        }
        if (second != 0) {
            cal.add(Calendar.SECOND, second);
        }
        return cal.getTime();
    }

    public static TokenEntity verifyToken(String token, String secret) {
        if (token == null || token.isEmpty()) {
            log.warn("verifyToken fail, token is empty");
            return TokenEntity.getFailInstance();
        }
        try {
            Algorithm algorithm = algorithm(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(SERVICE)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            String subject = jwt.getSubject();
            Map<String, Claim> claims = jwt.getClaims();
            Claim claim = claims.get(IDENTIFICATION);
            List<String> audience = jwt.getAudience();
            return TokenEntity.getInstance(claim.asString(), subject, audience, 1);
        } catch (JWTVerificationException exception) {
            log.warn(String.format("verifyToken fail, token:%s, message:%s", token, exception.getMessage()));
        }
        return TokenEntity.getFailInstance();
    }


    public static String getClaim(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String[] splitString = token.split("\\.");
        String base64EncodedBody = splitString[1];
        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));
        try {
            JSONObject jwtBody = JSON.parseObject(body);
            return jwtBody.getString(IDENTIFICATION);
        } catch (Exception ex) {
            log.error(String.format("get token claim fail ,token:%s", token), ex);
            return null;
        }
    }

    public static void main(String[] args) {
//        TokenEntity tokenEntity = DtJwtUtil.verifyToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJhdWQiOiLmlbDmja7kuK3lv4PlhoXpg6jkvb_nlKh0b2tlbiIsImlzcyI6IkRDLVVTRVItU0VSVkVSIiwiSURFTlRJRklDQVRJT04iOiIyODQ1MTk3IiwiZXhwIjoxNTg0NDg4ODI2LCJpYXQiOjE1ODQ0NDU2MjZ9.Yf_CJkPRKTE4-_p1mxv35zqY37od2c10AA8Nzrbayoc", "eacd1d122637476bbe2f043da09bbdfa");
//        TokenEntity tokenEntity = DtJwtUtil.verifyToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJhdWQiOiLmlbDmja7kuK3lv4PlhoXpg6jkvb_nlKh0b2tlbiIsImlzcyI6IkRDLVVTRVItU0VSVkVSIiwiSURFTlRJRklDQVRJT04iOiIyODQ1MTk3IiwiZXhwIjoxNTg0NTU0MjQ3LCJpYXQiOjE1ODQ1MTEwNDd9.qIAtGyqzyLADFCPFl1z7TZOxLX7-VZJB2tknaTN0TYQ", "eacd1d122637476bbe2f043da09bbdfa");
//        System.out.println(tokenEntity);
        System.out.println(JwtUtil.createToken("4", "mock", "user", 12, "abcdefg"));

        String claim = getClaim("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoibW9jayIsImlzcyI6IkRFVk9QUy1BRE1JTi1TRVJWRVIiLCJJREVOVElGSUNBVElPTiI6IjIiLCJleHAiOjE2MTIzMjg1NjcsImlhdCI6MTYxMjMyMTM2N30.lB0rkSc4AKU5VUi4eiCZTzvIPyfParLl21Rl_n-YLHY");

        System.out.println("claim = " + claim);
    }

     /**
     * @author kaka
     */
    @Data
    public static class TokenEntity {

        private String claim;

        private String subject;

        private List<String> role;

        /**
         * 1、有效 2、失效
         */
        private int status;

        public TokenEntity() {
        }

        public TokenEntity(String claim, String subject, List<String> role, int status) {
            this.claim = claim;
            this.subject = subject;
            this.role = role;
            this.status = status;
        }

        public TokenEntity(int status) {
            this.status = status;
        }

        public static TokenEntity getInstance(String claim, String subject, List<String> role, int status) {
            return new TokenEntity(claim, subject, role, status);
        }

        public static TokenEntity getFailInstance() {
            return new TokenEntity(2);
        }

        public boolean isFail() {
            return status == 2;
        }

        public boolean isSuccess() {
            return !isFail();
        }
    }

}


