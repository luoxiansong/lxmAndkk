package com.cqkk.config.encrypt.RSA.KeyPairGenertor;

import java.security.*;

//Java提供了KeyPairGenerator类。 此类用于生成公钥和私钥对。
// 要使用KeyPairGenerator类生成密钥，请按照以下步骤操作。
public class KeyPairGenertor {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        //Creating KeyPair generator object
        KeyPairGenerator instance = KeyPairGenerator.getInstance("DSA");

        //Initializing the KeyPairGenerator
        instance.initialize(2048);

        //Generating the pair of keys
        KeyPair keyPair = instance.generateKeyPair();

        //Getting the private key from the key pair
        PrivateKey privateKey = keyPair.getPrivate();

        //Getting the public key from the key pair
        PublicKey publicKey = keyPair.getPublic();

        System.out.println("KeyPairGenerator生成的私钥为：" + privateKey + ",公钥为：" + publicKey);
    }
}
