package com.untech.util;

import java.security.PrivateKey;
import java.security.PublicKey;


import com.sun.xml.internal.fastinfoset.sax.Properties;


/**
 * 这个是key的工具类，用来管理我们的key
 * @author sheng.liuzs
 *
 */
public class KeyUtil {
    /**
     *
     * 这个类用来得到支付宝的公钥，返回一个PublicKey
     * @return
     * @throws Exception
     */
    public static PublicKey getAlipayPubKey() throws Exception {
        KeyReader keyReader = new KeyReader();
        // 注意这里java在读取文件路径中有空格的情况下是会抛异常的。
        String filename = keyReader.getClass().getResource("alipay.cer").getFile();
        PublicKey pubKey = (PublicKey) keyReader.fromCerStoredFile(filename);
        // System.out.println("PublicKey => " + new String(Base64.encodeBase64(pubKey.getEncoded())));
        return pubKey;
    }

    /**
     *
     * 这个类用来得到模拟银行的公钥，返回一个PublicKey
     * @return
     * @throws Exception
     */
    public static PublicKey getMOCKPubKey() throws Exception {
        KeyReader keyReader = new KeyReader();
        // 注意这里java在读取文件路径中有空格的情况下是会抛异常的。
        //String filename = keyReader.getClass().getResource("bankcert.cer").getFile();
        String filename = Common_Const.RSAFILEPATH+"bankcert.cer";
        PublicKey pubKey = (PublicKey) keyReader.fromCerStoredFile(filename);
        // System.out.println("PublicKey => " + new String(Base64.encodeBase64(pubKey.getEncoded())));
        return pubKey;
    }



    /**
     *
     * 这个类用来得到模拟银行的公钥，返回一个PublicKey
     * @return
     * @throws Exception
     */
    public static PublicKey getKATONGubKey() throws Exception {
        KeyReader keyReader = new KeyReader();
        // 注意这里java在读取文件路径中有空格的情况下是会抛异常的。
        String filename = keyReader.getClass().getResource("Katong.cer").getFile();
        PublicKey pubKey = (PublicKey) keyReader.fromCerStoredFile(filename);
        // System.out.println("PublicKey => " + new String(Base64.encodeBase64(pubKey.getEncoded())));
        return pubKey;
    }


    /**
     *
     * 这个类用来得到模拟银行的私钥，返回一个PrivateKey
     * @return
     * @throws Exception
     */
    public static PrivateKey getMOCKPriKey() throws Exception {
        KeyReader keyReader = new KeyReader();
        // 注意这里java在读取文件路径中有空格的情况下是会抛异常的。
        System.out.println(keyReader.getClass());
        //String filename = keyReader.getClass().getResource("D:/merchant.pfx").getFile();
        String filename = Common_Const.RSAFILEPATH+"merchant.pfx";
        PrivateKey priKey = keyReader.readPrivateKeyfromPKCS12StoredFile(filename, "123456");
        System.out.println("privateKey => ");
        return priKey;
    }
}
