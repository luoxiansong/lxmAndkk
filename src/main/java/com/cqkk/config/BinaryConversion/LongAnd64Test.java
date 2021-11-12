package com.cqkk.config.BinaryConversion;

//如何证明Long是64位的？
//其实最简单的方式便是：我们看看Long类型的最大值，用2进制表示转换成字符串看看长度就行了
//提示：1、在计算机中,负数以其正值的补码形式表达,方法为其绝对值求反加1；2、用同样方法可以看出Integer类型是占用32位（4个字节）
public class LongAnd64Test {
    public static void main(String[] args) {
        long l = 100L;
        //如果不是最大值 前面都是0  输出的时候就不会有那么长了（所以下面使用最大/最小值示例）
        System.out.println(Long.toBinaryString(l)); //1100100
        System.out.println(Long.toBinaryString(l).length()); //7

        System.out.println("---------------------------------------");

        l = Long.MAX_VALUE; // 2的63次方 - 1
        //正数长度为63为（首位为符号位，0代表正数，省略了所以长度是63）
        //111111111111111111111111111111111111111111111111111111111111111
        System.out.println(Long.toBinaryString(l));
        System.out.println(Long.toBinaryString(l).length()); //63

        System.out.println("---------------------------------------");

        l = Long.MIN_VALUE; // -2的63次方
        //负数长度为64位（首位为符号位，1代表负数）
        //1000000000000000000000000000000000000000000000000000000000000000
        System.out.println(Long.toBinaryString(l));
        System.out.println(Long.toBinaryString(l).length()); //64
    }
}
