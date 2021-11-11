#Java密码学KeyPairGenerator类
Java提供了KeyPairGenerator类。 此类用于生成公钥和私钥对。 要使用KeyPairGenerator类生成密钥.
第1步：
创建KeyPairGenerator对象KeyPairGenerator类提供getInstance()方法，该方法接受表示所需密钥生成算法的String变量，
并返回生成密钥的KeyPairGenerator对象。
使用getInstance()方法创建KeyPairGenerator对象，如下所示。
//Creating KeyPair generator object
#KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
第2步：
初始化KeyPairGenerator对象KeyPairGenerator类提供了一个名为initialize()的方法，该方法用于初始化密钥对生成器。 
此方法接受表示密钥大小的整数值。
使用此方法初始化在上一步中创建的KeyPairGenerator对象，如下所示。
//Creating KeyPair generator object
#KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
//Initializing the KeyPairGenerator
#keyPairGen.initialize(2048);
第3步：
生成KeyPairGenerator可以使用KeyPairGenerator类的generateKeyPair()方法生成KeyPair。 使用此方法生成密钥对，如下所示。
//Generate the pair of keys
#KeyPair pair = keyPairGen.generateKeyPair();
第4步：
获取私钥/公钥可以使用getPrivate()方法从生成的密钥对对象中获取私钥，如下所示。
//Getting the private key from the key pair
#PrivateKey privKey = pair.getPrivate();
可以使用getPublic()方法从生成的KeyPair对象获取公钥，如下所示。
//Getting the public key from the key pair
#PublicKey publicKey = pair.getPublic();

