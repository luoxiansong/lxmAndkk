package com.cqkk.config.encrypt.RSA.SecureRandom;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 彩票随机生成器
 */
public class RandomTest {
    private static final String ORGIN_STR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
//        threadLocalRandom();
        secureRandom();
    }

    //ThreadLocalRandom
    private static void threadLocalRandom() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        System.out.println("nextBoolean：" + threadLocalRandom.nextBoolean());
        System.out.println("nextFloat：" + threadLocalRandom.nextFloat());
        System.out.println("nextDouble：" + threadLocalRandom.nextDouble());
        System.out.println("nextInt：" + threadLocalRandom.nextInt());
        System.out.println("nextLong：" + threadLocalRandom.nextLong());

        // 通过链式调用，避免线程间共享
        // 生成一个 0 ~ 100 之间的随机数
        System.out.println(ThreadLocalRandom.current().nextInt(100));
    }

    //SecureRandom
    private static void secureRandom() {
        StringBuilder builder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < 12; i++) {
            builder.append(ORGIN_STR.charAt(secureRandom.nextInt(ORGIN_STR.length())));
        }
        System.out.println("随机字符串：" + builder);
    }
}
