package com.cqkk.config.encrypt.RSA;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用RSA一般需要产生公钥和私钥，当采用公钥加密时，使用私钥解密；采用私钥加密时，使用公钥解密。
 */
public class RSAEncrypt {
    private static Map<Integer, String> keyMap = new HashMap<>();  //用于封装随机产生的公钥与私钥

    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        genKeyPair();
        //加密字符串
        String message = "df723820";
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        String messageEn = encrypt(message, keyMap.get(0));
        System.out.println(message + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn, keyMap.get(1));
        System.out.println("还原后的字符串为:" + messageDe);
    }

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0, publicKeyString);  //0表示公钥
        keyMap.put(1, privateKeyString);  //1表示私钥
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }
    //在程序中，我们首先利用genKeyPair()函数生成公钥和私钥并将其保存到Map集合中。然后，基于产生的公钥对明文进行加密。针对已经已经加密的密文，我们再次使用私钥解密，得到明文。
    //上述程序的输出结果为：
    //
    //随机生成的公钥为:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCG77PYUAcCpANyUmsHJfuDIia9FcITsuu9lnfbE2BbEwd4SOxPBFTwEWTZ9/e+mtjP97KFEBohGkwy+VHE5KocypBv0O7YgEevwMgpvxyYY0v104CB/k0yjCFV7lc7FxY5VgEKrMiXTIkMr1ukCnWVvapvRCS6IFcsT/kkjPgfDQIDAQAB
    //随机生成的私钥为:MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIbvs9hQBwKkA3JSawcl+4MiJr0VwhOy672Wd9sTYFsTB3hI7E8EVPARZNn3976a2M/3soUQGiEaTDL5UcTkqhzKkG/Q7tiAR6/AyCm/HJhjS/XTgIH+TTKMIVXuVzsXFjlWAQqsyJdMiQyvW6QKdZW9qm9EJLogVyxP+SSM+B8NAgMBAAECgYEAhj0FH9dNghUE0MCpdS0WL/jTrRxuPQase6mrhyiZnUErF0EExf87OLE1MZr8voRx2UNEOBgyxmfREozyCfyqNg1OdGYEHSyuJ9wglkhq8GVYO8IzI29Mqej0MSprtsE0BPAKBHRU/DWP19ej5bv5ZnAhLs10K7uVEsuGwJJYcMECQQDibedUr7tnGfojyjFY0vCAaVwgS0vXfno7WQyAXUz0Fv8Uy1q9nyF0RrkeA8BOk7S4ljE77ufX0rr2qL7kHW8pAkEAmI718EnQCKKJUjrQUl4iG/lYoNwW2QnxTGZmESyFwkS95PTt8K4GVHpICqRNP1JJBNxVSEVts/eA4zrxPAoBRQJBAJxxEsOQJwq1B/5yVGXqWABgyyYE4AGjgRBAFkMaM3Dx8ouLdMZOi+6qbnwuW0/u/Y4LNzkRd13GWybQsBMrwwECQEULptmavpG55kaWIcS1n+BjSK59DcYrDs+SJK2vJdaXwA4IoEvmpyzCrypJ1EBNYIjXo61y5sSlxuqQua9/o7UCQGYdM3/mF/FEC3wxdfQq0Pw/Pwn8RQxg1natRfoTyzOJDfE/YUYGjIEe2pQtDI1s+IRCwrXOB0cySbpaSHCjr5U=
    //df723820 加密后的字符串为:HRMm2XsytNJjmnZgn+2pFZWyTn56tsp7/yII6jdo3Wb19uy8GFtFujMekcUWqwFsO9vbvUOyD21MgI85BOtD7tsHHlj5xdtPiFEHkY1qiWIHTPpxA+WICSlw9ZY4zY0IaoxhrMAb8c9ohsrbBbyGcmUSFFdZq8CuPfj99IDLLic=
    //还原后的字符串为:df723820
}

