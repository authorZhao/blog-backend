package com.git.blog.util;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * @author authorZhao
 * @since 2025-03-31
 */
@Slf4j
public class IpUtil {

    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        boolean ipBlank = StringUtils.isBlank(ip);
        if (ipBlank || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ipBlank || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipBlank || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipBlank || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipBlank || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null) {
            //取第一个ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }




//    /**
//     * ip配置初始化
//     */
//    public static void init(String path) throws IOException {
//        InputStream inputStream = null;
//        try {
//            inputStream = new PathMatchingResourcePatternResolver().getResource(path).getInputStream();
//            //city = new City(inputStream);
//        } catch (Exception e) {
//            log.error("ip信息初始化失败", e);
//        } finally {
//            if (inputStream != null) {
//                inputStream.close();
//            }
//        }
//    }
//
//    /**
//     * 根据ip地址查询城市信息
//     *
//     * @param ipStr
//     * @return
//     * @throws Exception
//     */
//    public static CityInfo findCityByIp(String ipStr) {
//        CityInfo cityInfo = null;
//        if (city == null || ipStr == null || isIPv6(ipStr)) {
//            return null;
//        }
//        try {
//            cityInfo = city.findInfo(ipStr, "CN");
//        } catch (Exception e) {
//            log.error("findCityByIp error,ipStr={}", ipStr, e);
//        }
//        return cityInfo;
//    }
//
//
//    /**
//     * @param ipAddress
//     * @return
//     */
//    public static long ipToLong(String ipAddress) {
//        long result = 0;
//        String[] ipAddressInArray = ipAddress.split("\\.");
//        for (int i = 3; i >= 0; i--) {
//            long ip = Long.parseLong(ipAddressInArray[3 - i]);
//            // left shifting 24,16,8,0 and bitwise OR
//            // 1. 192 << 24
//            // 1. 168 << 16
//            // 1. 1 << 8
//            // 1. 2 << 0
//            result |= ip << (i * 8);
//        }
//        return result;
//    }
//
//    /**
//     * @param ip
//     * @return
//     */
//    public static String longToIp(long ip) {
//        StringBuilder result = new StringBuilder(15);
//        for (int i = 0; i < 4; i++) {
//            result.insert(0, Long.toString(ip & 0xff));
//            if (i < 3) {
//                result.insert(0, '.');
//            }
//            ip = ip >> 8;
//        }
//        return result.toString();
//    }
//
//    /**
//     * @param ip
//     * @return
//     */
//    public static String longToIp2(long ip) {
//        return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
//    }
//
//    /**
//     * 获取当前机器的IP
//     *
//     * @return
//     */
//    public static String getIpAddress() {
//        try {
//            for (Enumeration<NetworkInterface> enumNic = NetworkInterface.getNetworkInterfaces();
//                 enumNic.hasMoreElements(); ) {
//                NetworkInterface ifc = enumNic.nextElement();
//                if (ifc.isUp()) {
//                    for (Enumeration<InetAddress> enumAddr = ifc.getInetAddresses();
//                         enumAddr.hasMoreElements(); ) {
//                        InetAddress address = enumAddr.nextElement();
//                        if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
//                            return address.getHostAddress();
//                        }
//                    }
//                }
//            }
//            return InetAddress.getLocalHost().getHostAddress();
//        } catch (IOException e) {
//            //log.warn("Unable to find non-loopback address", e);
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 对比方法
//     *
//     * @param serviceUrl
//     * @return
//     */
//    public static boolean ipCompare(List<URI> serviceUrl) {
//        try {
//            String localIpStr = getIpAddress();
//            long localIpLong = ipToLong(localIpStr);
//            int size = serviceUrl.size();
//            if (size == 0) {
//                return false;
//            }
//
//            Long[] longHost = new Long[size];
//            for (int i = 0; i < serviceUrl.size(); i++) {
//                String host = serviceUrl.get(i).getHost();
//                longHost[i] = ipToLong(host);
//            }
//            Arrays.sort(longHost);
//            log.info("ip大小,我的ip={},long={},全部ip={}", localIpStr, localIpLong, JSON.toJSONString(longHost));
//            if (localIpLong == longHost[0]) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//
//    private static boolean isIPv6(String ipStr) {
//        return ipStr.contains(":");
//    }
}
