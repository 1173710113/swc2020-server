package com.example.demo.utils;

import java.util.List;

public class ValidateUtil {

    /**
     * 检查字符串是否为空
     * @param str
     * @return
     */

    public static boolean isEmpty(String str) {
        if(str == null || str.equals("")) {
            return true;
        }
        return false;
    }


    public static boolean isEmptys(List<String> list){
        for(int i = 0; i <list.size(); i++) {
            if(isEmpty(list.get(i))) {
                return true;
            }
        }
        return false;
    }
}
