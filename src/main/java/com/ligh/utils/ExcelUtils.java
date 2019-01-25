package com.ligh.utils;

/**
 * Created by ${ligh} on 2019/1/25 上午8:46
 */
public class ExcelUtils {

    public static boolean validateExcel(String filename){
        String s = filename.split("\\.")[1];
        if(s.equals("excel") || s.equals("xls")){
            return true;
        }else {
            return false;
        }
    }
}
