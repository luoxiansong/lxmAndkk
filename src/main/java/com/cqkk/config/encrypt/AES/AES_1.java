package com.cqkk.config.encrypt.AES;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.Charset;
import java.security.SecureRandom;

/**
 * Java 代码实现 AES 加密/解密 一般步骤：先根据原始的密码（字节数组/字符串）生成 AES密钥对象；再使用 AES密钥对象 加密/解密 数据。
 *
 * @author lkk
 */
public class AES_1 {
    static Charset charset1 = Charset.forName("UTF-8");

    public static void main(String[] args) throws Exception {
        String content = "Hello world!";        // 原文内容
        String key = "123456";                  // AES加密/解密用的原始密码

        long timeStart = System.currentTimeMillis();
        // 加密数据, 返回密文
        byte[] cipherBytes = encrypt(content.getBytes(), key.getBytes());
        long timeEnd = System.currentTimeMillis();
        System.out.println("加密后的结果为：" + new String(cipherBytes, charset1));
        // 解密数据, 返回明文
        byte[] plainBytes = decrypt(cipherBytes, key.getBytes());
        // 输出解密后的明文: "Hello world!"
        System.out.println("解密后的结果为：" + new String(plainBytes));
        System.out.println("加密用时：" + (timeEnd - timeStart));
    }

    /**
     * 生成密钥对象
     */
    private static SecretKey generateKey(byte[] key) throws Exception {
        // 根据指定的 RNG 算法, 创建安全随机数生成器
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // 设置 密钥key的字节数组 作为安全随机数生成器的种子
        random.setSeed(key);
        // 创建 AES算法生成器
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        // 初始化算法生成器
        gen.init(128, random);
        // 生成 AES密钥对象, 也可以直接创建密钥对象: return new SecretKeySpec(key, ALGORITHM);
        return gen.generateKey();
    }

    /**
     * 数据加密: 明文 -> 密文
     */
    public static byte[] encrypt(byte[] plainBytes, byte[] key) throws Exception {
        // 生成密钥对象
        SecretKey secKey = generateKey(key);
        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化密码器（加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, secKey);
        // 加密数据, 返回密文
        return cipher.doFinal(plainBytes);
    }

    /**
     * 数据解密: 密文 -> 明文
     */
    public static byte[] decrypt(byte[] cipherBytes, byte[] key) throws Exception {
        // 生成密钥对象
        SecretKey secKey = generateKey(key);
        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化密码器（解密模型）
        cipher.init(Cipher.DECRYPT_MODE, secKey);
        // 解密数据, 返回明文
        return cipher.doFinal(cipherBytes);
    }

}
