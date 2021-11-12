package com.cqkk.config.encrypt.MD5;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//MDS测试
public class MD5Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "123";
        byte[] result = getMD5Bytes(s.getBytes());
        StringBuilder stringBuilder = new StringBuilder();
        for (byte temp : result) {
            if (temp >= 0 && temp < 16) {
                stringBuilder.append("0");
            }
            //首先我们要都知道, &表示按位与,只有两个位同时为1,才能得到1,
            //0x代表16进制数,0xff表示的数二进制1111 1111 占一个字节.和其进行&操作的数,最低8位,不会发生变化.
            stringBuilder.append(Integer.toHexString(temp & 0xff));
        }
        System.out.println(s + ",MD5加密后:" + stringBuilder);
    }

    private static byte[] getMD5Bytes(byte[] content) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(content);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
