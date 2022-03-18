package com.git.blog.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 一些时间计算的工具类，与业务相关
 * @author authorZhao
 * @since 2021-03-25
 */
public class TimeUtils {

    public static final long DAY_MILLI_SECOND = 24 * 60 * 60 * 1000;
    public static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getTimeDesc(Date planDeployTime){
        if(planDeployTime==null){
            return "延期";
        }
        long l = planDeployTime.getTime() - System.currentTimeMillis();
        if(l>=0){
            return "进度正常";
        }
        long day = l/DAY_MILLI_SECOND +1;
        return "延期"+day+"天";
    }

    /**
     * 获取今天零点
     * @return
     */
    public static Date getTodayZero(){
        LocalDate now = LocalDate.now();
        ZonedDateTime zonedDateTime = now.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static void main(String[] args) {
        Date todayZero = getTodayZero();
        System.out.println("todayZero = " + todayZero);

    }


}
