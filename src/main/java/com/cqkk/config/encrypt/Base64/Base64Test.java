package com.cqkk.config.encrypt.Base64;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Base64
 */
public class Base64Test {
    public static void main1(String[] args) throws UnsupportedEncodingException {
        final Base64.Decoder decoder = Base64.getDecoder();
        final Base64.Encoder encoder = Base64.getEncoder();
        final String text = "字串文字";
        final byte[] textByte = text.getBytes("UTF-8");
        //编码
        final String encodedText = encoder.encodeToString(textByte);
        System.out.println(encodedText);
        //解码
        System.out.println(new String(decoder.decode(encodedText), "UTF-8"));
    }

    //Java 验证
    //最后，我们用一段Java代码来验证一下Base64编码.md的转换结果:
    public static void main(String[] args) {
        String man = "Man";
        String bc = "BC";
        String a = "A";
        byte[] bytes = man.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + "|"); //77|97|110| 每个字符的字节数==>二进制数01001101|01100001|01101110|
        }
        System.out.println("");
        //将字符串转为二进制位 每个是8bit man为例:1001101|1100001|1101110| ==>01001101|01100001|01101110|
        char[] chars = man.toCharArray();
        //System.out.println(Arrays.toString(chars));//数组打印 [M, a, n]
        for (int i = 0; i < chars.length; i++) {
            String binaryString = Integer.toBinaryString(chars[i]);
            System.out.print(binaryString + "|");
        }
        System.out.println("");
        BASE64Encoder encoder = new BASE64Encoder();
        System.out.println("Man base64结果为：" + encoder.encode(man.getBytes()));
        System.out.println("BC base64结果为：" + encoder.encode(bc.getBytes()));
        System.out.println("A base64结果为：" + encoder.encode(a.getBytes()));
        //Man base64结果为：TWFu
        //BC base64结果为：QkM=
        //A base64结果为：QQ==
    }
}
