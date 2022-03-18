package com.git.blog.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

/**
 * @author authorZhao
 * @since 2021-01-15
 */
public class SignUtil {

    public static String dingTalkSign(Long timestamp,String secret){
        //String secret = "SEC8cfdcc7684ba69600c1b7dfc195e4ee4f3aed0905b452289b0efd96319264dfd";
        String sign = "";
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return sign;
    }

    public static void main(String[] args) {
        System.out.println("dingTalkSign(System.currentTimeMillis()) = " + dingTalkSign(System.currentTimeMillis(),"SEC8cfdcc7684ba69600c1b7dfc195e4ee4f3aed0905b452289b0efd96319264dfd"));
    }
}
