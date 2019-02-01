package com.profclub.common.util;

/**
 * Created by ARTHUR on 5/28/2017.
 */
public class StringHelper {

    public static boolean isBlank(String val) {
        return val == null || val.length() == 0;
    }

    public static boolean isNotBlank(String val) {
        return !isBlank(val);
    }
}
