package com.cqkk.psnpro.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: psnpro
 * @description:
 * @author: Mr.Luo
 * @create: 2021-05-21 22:54
 **/
public class DateTools {

    public static String getNowDate() {
        String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return nowDate;
    }

    public static void main(String[] args) {
        System.out.println("当前日期为 :" + getNowDate());
    }
}
