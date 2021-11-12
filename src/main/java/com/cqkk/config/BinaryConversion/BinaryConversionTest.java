package com.cqkk.config.BinaryConversion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//进制转换
public class BinaryConversionTest {
    public static void main(String[] args) {
        //当int是4字节即32位时，最大数为(2^31)-1=2147483647，是10位数
        //int是有符号的，范围是-2147483648~2147483647 超过这个数则会抛错
        System.out.println(Integer.parseInt("21", 8));//8进制转换为10进制 返回int值 8*2+1*1
        System.out.println(Integer.parseInt("11", 10));//10进制转换为10进制
        System.out.println(Integer.parseInt("FE", 16));//16进制转换为10进制 254 16*15+1*14
        System.out.println(Integer.parseInt("1010010111111", 2));//2进制转换为10进制

        System.out.println(Integer.toHexString(31));//10进制转为16进制 1f
        System.out.println(Integer.toOctalString(31));//10进制转为8进制 37
        System.out.println(Integer.toBinaryString(31));//10进制转为2进制 11111

        test();
    }

    //输入一个10进制数字并把它转换成16进制
    public static void test() {
        int input;//存放输入数据
        //创建输入字符串的实例
        BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入一个的整数：");
        String x = null;
        try {
            x = strin.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        input = Integer.parseInt(x);
        System.out.println("你输入的数字是：" + input);//输出从键盘接收到的数字

        System.out.println("它的16进制是：" + Integer.toHexString(input));//用toHexString把10进制转换成16进制
    }
}
