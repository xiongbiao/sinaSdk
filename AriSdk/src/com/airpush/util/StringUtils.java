package com.airpush.util;

public class StringUtils {

    /**
     * will trim the string
     * 
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (null == s) return true;
        if (s.length() == 0) return true;
        if (s.trim().length() == 0) return true;
        return false;
    }
}
