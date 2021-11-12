package com.cqkk.config.BinaryConversion.或与非;

//与或
public class JavaAndOr {

    //++ i 是先加后赋值；i ++ 是先赋值后加；++i和i++都是分两步完成的。
    public static void main(String[] args) {
        int i = 0;
        if (10 == 10 & (++i) != 1) {
            System.out.println("结果为真" + i);
        } else {
            System.out.println("结果为假" + i);
        }
        //打印：结果为假1

        int i6 = 0;
        if (10 == 10 & (i6++) != 1) {
            System.out.println("结果为真" + i6);
        } else {
            System.out.println("结果为假" + i6);
        }
        //打印：结果为真1
        //小结：说明后面的表达式执行了，不影响判断结果。

        int i1 = 0;
        if (10 != 10 & (i1++) == 1) {
            System.out.println("结果为真" + i1);
        } else {
            System.out.println("结果为假" + i1);
        }
        //打印：结果为假1
        //小结：说明后面的表达式执行了，不影响判断结果。

        int i2 = 0;
        if (10 != 10 && (i2++) == 1) {
            System.out.println("结果为真" + i2);
        } else {
            System.out.println("结果为假" + i2);
        }
        // 打印：结果为假0
        //小结：说明后面的表达式没有执行。

        int i3 = 0;
        if (10 == 10 | (i3++) != 1) {
            System.out.println("结果为真" + i3);
        } else {
            System.out.println("结果为假" + i3);
        }
        //打印：结果为真1
        //小结：说明后面的表达式执行了，但不影响判断结果。（与 & 相似）

        int i4 = 0;
        if (10 != 10 | (i++) == 1) {
            System.out.println("结果为真" + i4);
        } else {
            System.out.println("结果为假" + i4);
        }
        //打印：结果为假1
        //小结：说明后面的表达式执行了，但并不影响结果。

        int i5 = 0;
        if (10 == 10 || (i5++) != 0) {
            System.out.println("结果为真" + i5);
        } else {
            System.out.println("结果为假" + i5);
        }
        // 打印：结果为真   0
    }
}
