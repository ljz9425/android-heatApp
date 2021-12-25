package com.whisht.heatapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luqiye on 2018/12/7.
 */

public class StringUtils {

    /**
     * 汉字
     */
    public static String[] CN_CHARS = new String[] { "零", "一", "二", "三", "四",
            "五", "六", "七", "八", "九" };

    /**
     * 判断参数是否为空
     * @param input
     * @return
     */
    public static boolean isEmpty(String input){
        boolean result = false;
        if(input == null || "".equals(input) || "null".equals(input) || "NULL".equals(input)){
            result = true;
        }
        return result;
    }
    /**
     * 电话号码验证
     * @param  str
     * @return 验证通过返回true
     */
    public static boolean isPhone(final String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }
    public static boolean isMobilPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }
    }

    public static String numberToChars(String str) {
        String result = "";
        for(int i=0; i<str.length(); i++) {
            if(str.charAt(i)>='0'&&str.charAt(i)<='9') {
                result += CN_CHARS[Integer.parseInt(String.valueOf(str.charAt(i)))];
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }
}
