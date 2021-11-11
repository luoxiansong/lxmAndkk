#KeyFactory类详解
密钥工厂用于将密钥（Key 类型的不透明加密密钥）转换成密钥规范（底层密钥材料的透明表示），反之亦然。
密钥工厂是双向的。也就是说，它们允许根据给定的密钥规范（密钥材料）构建不透明的密钥对象，也允许获取以恰当格式表示的密钥对象的底层密钥材料。
对于同一个密钥可以存在多个兼容的密钥规范。例如，可以使用 DSAPublicKeySpec 或 X509EncodedKeySpec 指定 DSA 公钥。
密钥工厂可用于兼容密钥规范之间的转换。
以下是一个如何使用密钥工厂根据其编码实例化 DSA 公钥的示例。假定 Alice 收到了 Bob 的数字签名。
Bob 也向她发送其公钥（以编码的格式）来验证他的签名。然后 Alice 执行以下操作：

X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(bobEncodedPubKey);
KeyFactory keyFactory = KeyFactory.getInstance("DSA");
PublicKey bobPubKey = keyFactory.generatePublic(bobPubKeySpec);
Signature sig = Signature.getInstance("DSA");
sig.initVerify(bobPubKey);
sig.update(data);
sig.verify(signature);


