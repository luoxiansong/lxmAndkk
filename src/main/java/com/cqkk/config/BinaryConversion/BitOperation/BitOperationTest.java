package com.cqkk.config.BinaryConversion.BitOperation;

//测试位运算
public class BitOperationTest {

    public static void main(String[] args) {
        //&：按位与
        // 2 -> 10
        // 3 -> 11
        // 与后结果：10（二进制数）
        System.out.println(Integer.toBinaryString(2 & 3));

        //|：按位或
        // 2 -> 10
        // 3 -> 11
        // 或后结果：11（二进制数）
        System.out.println(Integer.toBinaryString(2 | 3));

        //~：按位非
        // 2 -> 10（其实是00000000000000000000000000000010  共32位）
        // 非后结果：     11111111111111111111111111111101 共32位
        System.out.println(Integer.toBinaryString(~2));

        //^：按位异或
        // 2 -> 10
        // 3 -> 11
        // 异或后结果：01（二进制数）
        System.out.println(Integer.toBinaryString(2 ^ 3));

        //<<：按位左移
        // 2 -> 10
        // 左移3位结果：10000（二进制数）
        System.out.println(Integer.toBinaryString(2 << 3));

        //>>：按位右移
        // 2 -> 10
        // 右移3位结果：0（二进制数）
        // 位数不够全被移没了，所以最终打印0
        System.out.println(Integer.toBinaryString(2 >> 3));
        // 100 -> 1100100
        // 右移3位结果：1100
        System.out.println(Integer.toBinaryString(100 >> 3));

        //复合运算
        //这里指的复合运算指的就是和=号一起来使用，类似于+= -=等。
        // 2 -> 10
        // 3 -> 11
        // 与后结果：10（二进制数）
        int i = 2;
        i &= 3; // 此效果同 i = i & 3
        System.out.println(Integer.toBinaryString(i)); //打印：10

        //位运算的使用场景
        //--1.判断一个数的奇偶性--
        // 1 -> 1（二进制表示。所以它的前31位都是0哦~~~）
        int n = 5;
        int m = 6;
        System.out.println((n & 1) != 1);
        System.out.println((m & 1) != 1);

        //--2.不借助第三方变量方式交换两个数的值
        //方式一：传统方式
        int a = 3, b = 5;
        System.out.println(a + "-------" + b);
        a = a + b;
        b = a - b;
        a = a - b;
        System.out.println(a + "-------" + b);
        //使用这种方式的好处是容易理解，坏处是：a+b,可能会超出int型的最大范围，造成精度丢失导致错误，所以生产环境强烈建议采用下面的方式二。

        //方式二：位运算方式
        // 这里使用最大值演示，以证明这样方式是不会溢出的
        int a_1 = Integer.MAX_VALUE, b_1 = Integer.MAX_VALUE - 10;
        System.out.println(a_1 + "-------" + b_1); // 2147483647-------2147483637
        a_1 = a_1 ^ b_1;
        b_1 = a_1 ^ b_1;
        a_1 = a_1 ^ b_1;
        System.out.println(a_1 + "-------" + b_1); // 2147483637-------2147483647
        //它的根本原理就是利用了位运算的可逆性，使用异或运算来操作。
    }
}

