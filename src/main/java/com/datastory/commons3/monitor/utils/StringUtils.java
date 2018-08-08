package com.datastory.commons3.monitor.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串的工具类
 */
public class StringUtils {

    /**
     * 判断一个字符串是否为空，若为空，返回true，否则返回false
     * @param value
     * @return
     */
    public static boolean isNullStr(String value){
        if(value == null || "".equals(value.trim())){
            return true;
        }
        return false;
    }

    /**
     * 获取系统时间
     * @return
     */
    public static Date getNowDate(){
        Date date = new Date();
        return date;
    }

    /**
     * 获取指定格式的系统字符串
     * @param format
     * @return
     */
    public static String getNowDateStr(String format){
        DateFormat df = new SimpleDateFormat(format);
        String dateValue = df.format(new Date());
        return dateValue;
    }

}
