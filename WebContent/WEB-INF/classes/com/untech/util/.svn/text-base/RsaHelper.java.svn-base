package com.untech.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * (加密明文、解密密文长度如果超过了指定长度，我们会采用分段加解密的方式，为了与.net进行互通，我们约定先进行分段，后进行加解密，之间用-分割，2
 * 边加密分割采用117位字节长度) rsa加解密工具类(可以与。net互通,注意密钥对需要由.net来提供)
 * 
 * @author komojoemary_suiyue
 * @version [版本号, 2013-8-7]
 */
public class RsaHelper
{

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    
    /**
     * RSA最大加密明字符大小
     */
    private static final int MAX_ENCRYPT_FU = 36;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 分隔符
     */
    private static String split = "★";

  /*private String publicKey = "<RSAKeyValue><Modulus>5m9m14XH3oqLJ8bNGw9e4rGpXpcktv9MSkHSVFVMjHbfv+SJ5v0ubqQxa5YjLN4vc49z7SVju8s0X4gZ6AzZTn06jzWOgyPRV54Q4I0DCYadWW4Ze3e+BOtwgVU1Og3qHKn8vygoj40J6U85Z/PTJu3hN1m75Zr195ju7g9v4Hk=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
   
    private String privateKey = "<RSAKeyValue><Modulus>5m9m14XH3oqLJ8bNGw9e4rGpXpcktv9MSkHSVFVMjHbfv+SJ5v0ubqQxa5YjLN4vc49z7SVju8s0X4gZ6AzZTn06jzWOgyPRV54Q4I0DCYadWW4Ze3e+BOtwgVU1Og3qHKn8vygoj40J6U85Z/PTJu3hN1m75Zr195ju7g9v4Hk=</Modulus><Exponent>AQAB</Exponent><P>/hf2dnK7rNfl3lbqghWcpFdu778hUpIEBixCDL5WiBtpkZdpSw90aERmHJYaW2RGvGRi6zSftLh00KHsPcNUMw==</P><Q>6Cn/jOLrPapDTEp1Fkq+uz++1Do0eeX7HYqi9rY29CqShzCeI7LEYOoSwYuAJ3xA/DuCdQENPSoJ9KFbO4Wsow==</Q><DP>ga1rHIJro8e/yhxjrKYo/nqc5ICQGhrpMNlPkD9n3CjZVPOISkWF7FzUHEzDANeJfkZhcZa21z24aG3rKo5Qnw==</DP><DQ>MNGsCB8rYlMsRZ2ek2pyQwO7h/sZT8y5ilO9wu08Dwnot/7UMiOEQfDWstY3w5XQQHnvC9WFyCfP4h4QBissyw==</DQ><InverseQ>EG02S7SADhH1EVT9DD0Z62Y0uY7gIYvxX/uq+IzKSCwB8M2G7Qv9xgZQaQlLpCaeKbux3Y59hHM+KpamGL19Kg==</InverseQ><D>vmaYHEbPAgOJvaEXQl+t8DQKFT1fudEysTy31LTyXjGu6XiltXXHUuZaa2IPyHgBz0Nd7znwsW/S44iql0Fen1kzKioEL3svANui63O3o5xdDeExVM6zOf1wUUh/oldovPweChyoAdMtUzgvCbJk1sYDJf++Nr0FeNW1RB1XG30=</D></RSAKeyValue>";
*/
    
    private static String publicKey = "<RSAKeyValue><Modulus>AJAdZEOv05dDtBb+llkVZZW3BhA5i2OJjV9+xVulNDse13DC4yn24j3zNKQuLjdggYZafv8RgcefqR/zdRq3I74N8y3y317gCu1q5N5/SOeeaAkHWWqqd5h2Xe1PWxlck3f8ilrz9tJ4rORHFB1zWvNwBl8TsKEvIqf/BtgjvGGx</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
    
    private static String privateKey = "<RSAKeyValue><Modulus>AJAdZEOv05dDtBb+llkVZZW3BhA5i2OJjV9+xVulNDse13DC4yn24j3zNKQuLjdggYZafv8RgcefqR/zdRq3I74N8y3y317gCu1q5N5/SOeeaAkHWWqqd5h2Xe1PWxlck3f8ilrz9tJ4rORHFB1zWvNwBl8TsKEvIqf/BtgjvGGx</Modulus><Exponent>AQAB</Exponent><P>AMayX9QnZnf4f1rQLFWWCOlgzg2aUEA5OWShNM+mb5PkkqEX+4udYQvwZqb4eNaOKhKCIaf7JUv+uGCVYGBcuMM=</P><Q>ALmtRi4svrevCVRme7HTztkPcjttBFcSJXHxmSJd0PZJ4SJHucR+Lv6ZzaoKYDmgTxwU2xDT9fsL98rUNZ04NHs=</Q><DP>AMBL7VZjc1X0VzzKb7284I8Msx6TE8u0Fgl/wp6cUyvscMQbc8Tg4QUu+gnqOvhayfjznCL4elYcBNDkgEuCMQU=</DP><DQ>Jo51uVbvT6NYU59oZjfuhyJu/SoZMZ+CCRQ7UWib4NjcmEq+p6/wQExd6cZ3zt+cLd+i0e7B8Jy+mu7QJ1Kpiw==</DQ><InverseQ>IUARbYimhS+m/Wq8Tun9s2d7PTdwJxN3urJcoFwaQ+/ypqHk2nX+mrxWRXmkvGT9BkW6fchvMyJAOTp4ejiD+A==</InverseQ><D>M3DaVjLPoyG+iaqsPfYeQZeH6Yiw0YHK3bhhd/n7L9MtcYmPDm6aiSRjuOrnAktHUI7C4Ab+vSOnecksFrIiZm5ep9pbx+bHQ7CeWcDl9UI+KwCMSmbjlVoNZg+qLVDEUAKbz24ymH65kPeTaEbCHI141bG86FBVJdf6PR9jW/U=</D></RSAKeyValue>";

    /**
     * 
     * 生成RSA密钥对(默认密钥长度为1024)
     * 
     * @return
     */

    public static KeyPair generateRSAKeyPair() {
        return generateRSAKeyPair(1024);
    }

    /**
     * 
     * 生成RSA密钥对
     * 
     * @param keyLength
     * 
     *            密钥长度，范围：512～2048
     * @return
     */

    public static KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String encodePublicKeyToXml(PublicKey key) {
        if (!RSAPublicKey.class.isInstance(key)) {
            return null;
        }
        RSAPublicKey pubKey = (RSAPublicKey) key;
        StringBuilder sb = new StringBuilder();
        sb.append("<RSAKeyValue>");
        sb.append("<Modulus>").append(encode(pubKey.getModulus().toByteArray())).append("</Modulus>");
        sb.append("<Exponent>").append(encode(pubKey.getPublicExponent().toByteArray())).append("</Exponent>");
        sb.append("</RSAKeyValue>");
        return sb.toString();
    }

    /*public static void main(String[] args) {
		String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBqhbEt13ddpjZKbB9YvFPPGrbWBIOzS2QG3/th63FQhekc5+Z1yniCAzR5g0OU02ETX+ivpW2xMAsvJhyjSUv7Y/yak7uUrQFXKY0x5fAdHQ+Ge939qfPRHz5WfyAhwoXQCqnvnSVCmAUrqk/z6BzzT67Rmi9LRWQijDnFlM2ZQIDAQAB";
		BigInteger modulus = new BigInteger(1, decode(pub));
		BigInteger exp = new BigInteger(1, decode("AQAB"));
		System.out.println(modulus);
		String source  ="1234567890123456";
		RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus,exp);
        KeyFactory keyf;
        try {
            keyf = KeyFactory.getInstance("RSA");
            RSAPublicKey key =  (RSAPublicKey)keyf.generatePublic(rsaPubKey);
            String securt  = encryptByPublicKey(source, key);
            System.out.println("securt==="+securt);
            
            
            //String pri = "MIICXQIBAAKBgQDBqhbEt13ddpjZKbB9YvFPPGrbWBIOzS2QG3/th63FQhekc5+Z1yniCAzR5g0OU02ETX+ivpW2xMAsvJhyjSUv7Y/yak7uUrQFXKY0x5fAdHQ+Ge939qfPRHz5WfyAhwoXQCqnvnSVCmAUrqk/z6BzzT67Rmi9LRWQijDnFlM2ZQIDAQABAoGBAL11moSkJq3rwihhQonH6+7qWldvV2snirV6UbKck4Fw4RQ/ERo9hnncCj6l4ovrLuCJuJ4L+QcugBrD/hfCwWbkVXgBa9nQKTt2F3VNyUAjnizPhtgyawM2M12yP4zVT/YJVT5uN+DlSz+EGQtpV6UlGTgY/VWWLgt3UvRWFmXZAkEA/rHO+cTikv7EuyCR3Xq4AClhpJPtLWZyqvuM7EgswV6ejhjsJxJAc4bRmYBtpzmCratIE+d4DqwmYCKqAJfTuwJBAMKoM37VmCmmsBtpyM9d6OYk9X657Vdv7aHnf4Yo7FhNK4gnoXbK/NAbKlhnym0RTOzC0ydNnZ59Nn2oMp6OrF8CQQC4Q3/QqCab3oIKN/gP2lcAlu+zl+oB7JqNJUzBLT5j78aaW2GOqt/CNQmzGn06fJsUdlTBQbdUNivVZX7EyxM9AkBVq0c2m/Si6Bx7hBv/v5nfqAZvZo6hO75+0e6a8enKlvQAsUhrirynLj19uPgq9kE/tiLOGmezN/JCbUMR60LLAkBC8gUrm8y87HB5YFBTEVETFgQoeSrEM0ub8uJH9QIganC0RF6sbzst9KrHfsdob1Dek7bOQBmM9NzGLvcIjFag";
            String pri = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMGqFsS3Xd12mNkpsH1i8U88attYEg7NLZAbf+2HrcVCF6Rzn5nXKeIIDNHmDQ5TTYRNf6K+lbbEwCy8mHKNJS/tj/JqTu5StAVcpjTHl8B0dD4Z73f2p89EfPlZ/ICHChdAKqe+dJUKYBSuqT/PoHPNPrtGaL0tFZCKMOcWUzZlAgMBAAECgYEAvXWahKQmrevCKGFCicfr7upaV29XayeKtXpRspyTgXDhFD8RGj2GedwKPqXii+su4Im4ngv5By6AGsP+F8LBZuRVeAFr2dApO3YXdU3JQCOeLM+G2DJrAzYzXbI/jNVP9glVPm434OVLP4QZC2lXpSUZOBj9VZYuC3dS9FYWZdkCQQD+sc75xOKS/sS7IJHdergAKWGkk+0tZnKq+4zsSCzBXp6OGOwnEkBzhtGZgG2nOYKtq0gT53gOrCZgIqoAl9O7AkEAwqgzftWYKaawG2nIz13o5iT1frntV2/toed/hijsWE0riCehdsr80BsqWGfKbRFM7MLTJ02dnn02fagyno6sXwJBALhDf9CoJpveggo3+A/aVwCW77OX6gHsmo0lTMEtPmPvxppbYY6q38I1CbMafTp8mxR2VMFBt1Q2K9VlfsTLEz0CQFWrRzab9KLoHHuEG/+/md+oBm9mjqE7vn7R7prx6cqW9ACxSGuKvKcuPX24+Cr2QT+2Is4aZ7M38kJtQxHrQssCQELyBSubzLzscHlgUFMRURMWBCh5KsQzS5vy4kf1AiBqcLREXqxvOy30qsd+x2hvUN6Tts5AGYz03MYu9wiMVqA=";
            BigInteger b1 = new BigInteger(1, decode(pub));
			BigInteger b2 = new BigInteger(decode(pri));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
			RSAPrivateKey key2 =  (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
			 String securt2  = decryptByPrivateKey(securt, key2);
	            System.out.println("securt2==="+securt2);
        }
        catch (Exception e) {
           e.printStackTrace();
        }
		
		
	}*/
    
    public static PublicKey decodePublicKeyFromXml(String xml) {
        xml = xml.replaceAll("\r", "").replaceAll("\n", "");
        BigInteger modulus = new BigInteger(1, decode(GetMiddleString(xml, "<Modulus>", "</Modulus>")));
        BigInteger publicExponent = new BigInteger(1, decode(GetMiddleString(xml, "<Exponent>", "</Exponent>")));
        RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);
        KeyFactory keyf;
        try {
            keyf = KeyFactory.getInstance("RSA");
            return keyf.generatePublic(rsaPubKey);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static PrivateKey decodePrivateKeyFromXml(String xml) {
        xml = xml.replaceAll("\r", "").replaceAll("\n", "");
        BigInteger modulus = new BigInteger(1, decode(GetMiddleString(xml, "<Modulus>", "</Modulus>")));
        BigInteger publicExponent = new BigInteger(1, decode(GetMiddleString(xml, "<Exponent>", "</Exponent>")));
        BigInteger privateExponent = new BigInteger(1, decode(GetMiddleString(xml, "<D>", "</D>")));
        BigInteger primeP = new BigInteger(1, decode(GetMiddleString(xml, "<P>", "</P>")));
        BigInteger primeQ = new BigInteger(1, decode(GetMiddleString(xml, "<Q>", "</Q>")));
        BigInteger primeExponentP = new BigInteger(1, decode(GetMiddleString(xml, "<DP>", "</DP>")));
        BigInteger primeExponentQ = new BigInteger(1, decode(GetMiddleString(xml, "<DQ>", "</DQ>")));
        BigInteger crtCoefficient = new BigInteger(1, decode(GetMiddleString(xml, "<InverseQ>", "</InverseQ>")));
        RSAPrivateCrtKeySpec rsaPriKey = new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP,
                primeQ, primeExponentP, primeExponentQ, crtCoefficient);
        KeyFactory keyf;
        try {
            keyf = KeyFactory.getInstance("RSA");
            return keyf.generatePrivate(rsaPriKey);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String encodePrivateKeyToXml(PrivateKey key) {
        if (!RSAPrivateCrtKey.class.isInstance(key)) {
            return null;
        }
        RSAPrivateCrtKey priKey = (RSAPrivateCrtKey) key;
        StringBuilder sb = new StringBuilder();
        sb.append("<RSAKeyValue>");
        sb.append("<Modulus>").append(encode(priKey.getModulus().toByteArray())).append("</Modulus>");
        sb.append("<Exponent>").append(encode(priKey.getPublicExponent().toByteArray())).append("</Exponent>");
        sb.append("<P>").append(encode(priKey.getPrimeP().toByteArray())).append("</P>");
        sb.append("<Q>").append(encode(priKey.getPrimeQ().toByteArray())).append("</Q>");
        sb.append("<DP>").append(encode(priKey.getPrimeExponentP().toByteArray())).append("</DP>");
        sb.append("<DQ>").append(encode(priKey.getPrimeExponentQ().toByteArray())).append("</DQ>");
        sb.append("<InverseQ>").append(encode(priKey.getCrtCoefficient().toByteArray())).append("</InverseQ>");
        sb.append("<D>").append(encode(priKey.getPrivateExponent().toByteArray())).append("</D>");
        sb.append("</RSAKeyValue>");
        return sb.toString();
    }

    /**
     * 公钥加密
     * 
     * @param data1
     *            要加密的明文
     * @param publicKey
     *            公钥字符串
     * @return String 加密后的数据
     * @throws Exception
     */
    public static String encryptByPublicKey(String data1, String publicKey) throws Exception {
        RSAPublicKey publicKey1 = (RSAPublicKey) RsaHelper.decodePublicKeyFromXml(publicKey);
        return encryptByPublicKey(data1, publicKey1);
    }

    /**
     * 公钥加密
     * 
     * @param data1
     *            要加密的明文
     * @param publicKey
     *            公钥对象
     * @return String 加密后的数据
     * @throws Exception
     */
    public static String encryptByPublicKey(String data1, RSAPublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int length = data1.length();
        StringBuffer sb = new StringBuffer();
        int start = 0;
        int j=0;        	
        String  data2  = "";
        while (length - start > 0) {
            if(length - start >= MAX_ENCRYPT_FU){
            	data2 = data1.substring(start,start+MAX_ENCRYPT_FU);
            }else {
            	data2 = data1.substring(start,length);
            }
	        byte[] data = data2.getBytes("utf-8");
	        int inputLen = data.length;
	        int offSet = 0;
	        byte[] cache;
	        int i = 0;
	        // 对数据分段加密
	        while (inputLen - offSet > 0) {
	            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
	                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
	            }
	            else {
	                cache = cipher.doFinal(data, offSet, inputLen - offSet);
	            }
	            sb.append((new BASE64Encoder()).encodeBuffer(cache) + split);
	            i++;
	            offSet = i * MAX_ENCRYPT_BLOCK;
	        }
	        j++;
	        start = j*MAX_ENCRYPT_FU;
        }
        
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 私钥解密
     * 
     * @param data
     *            要解密的数据
     * @param privateKey
     *            私钥字符串
     * @return String 解密后的数据
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String privateKey) throws Exception {
        RSAPrivateKey rSAPrivateKey = (RSAPrivateKey) RsaHelper.decodePrivateKeyFromXml(privateKey);
        return decryptByPrivateKey(data, rSAPrivateKey);
    }

    /**
     * 私钥解密
     * 
     * @param data
     *            要解密的数据
     * @param privateKey
     *            私钥对象
     * @return String 解密后的数据
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
        String[] trueValue = data.split(split);
        StringBuffer sb = new StringBuffer();
        for (String item : trueValue) {
            sb.append(decryptByPrivateKey1(item, privateKey));
        }
        return sb.toString();
    }

    /**
     * 私钥解密
     * 
     * @param data
     *            要解密的数据
     * @param privateKey
     *            私钥对象
     * @return String 解密后的数据
     * @throws Exception
     */
    private static String decryptByPrivateKey1(String data, RSAPrivateKey privateKey) throws Exception {
        byte[] encryptedData = (new BASE64Decoder()).decodeBuffer(data);
        // 解密
        // 注意Cipher初始化时的参数“RSA/ECB/PKCS1Padding”,代表和.NET用相同的填充算法，如果是标准RSA加密，则参数为“RSA”
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            }
            else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData, "utf-8");
    }

    /**
     * 
     * 根据指定私钥对数据进行签名(默认签名算法为"SHA1withRSA")
     * 
     * @param data
     *            要签名的数据
     * @param priKey
     *            私钥
     * @return
     */

    public static byte[] signData(byte[] data, PrivateKey priKey) {
        return signData(data, priKey, "SHA1withRSA");
    }

    /**
     * 
     * 根据指定私钥和算法对数据进行签名
     * 
     * @param data
     *            要签名的数据
     * @param priKey
     *            私钥
     * @param algorithm
     *            签名算法
     * @return
     */

    public static byte[] signData(byte[] data, PrivateKey priKey, String algorithm) {
        try {
            Signature signature = Signature.getInstance(algorithm);
            signature.initSign(priKey);
            signature.update(data);
            return signature.sign();
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * 
     * 用指定的公钥进行签名验证(默认签名算法为"SHA1withRSA")
     * 
     * @param data
     *            数据
     * @param sign
     *            签名结果
     * @param pubKey
     *            公钥
     * @return
     */

    public static boolean verifySign(byte[] data, byte[] sign, PublicKey pubKey) {
        return verifySign(data, sign, pubKey, "SHA1withRSA");
    }

    /**
     * 
     * @param data
     *            数据
     * @param sign
     *            签名结果
     * @param pubKey
     *            公钥
     * @param algorithm
     *            签名算法
     * @return
     */

    public static boolean verifySign(byte[] data, byte[] sign, PublicKey pubKey, String algorithm) {
        try {
            Signature signature = Signature.getInstance(algorithm);
            signature.initVerify(pubKey);
            signature.update(data);
            return signature.verify(sign);
        }
        catch (Exception ex) {
            return false;
        }
    }

    private static String encode(byte[] byteArray) {
        sun.misc.BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
        return base64Encoder.encode(byteArray);
    }

    private static byte[] decode(String base64EncodedString) {
        sun.misc.BASE64Decoder base64Decoder = new sun.misc.BASE64Decoder();
        try {
            return base64Decoder.decodeBuffer(base64EncodedString);
        }
        catch (IOException e) {
            return null;
        }
    }

    private static String GetMiddleString(String source, String strHead, String strTail) {
        return GetMiddleString(source, strHead, strTail, false);
    }

    private static String GetMiddleString(String source, String strHead, String strTail, boolean KeepHeadAndTail) {
        try {
            int indexHead, indexTail;

            if (strHead == null || strHead.isEmpty()) {
                indexHead = 0;
            }
            else {
                indexHead = source.indexOf(strHead);
            }

            if (strTail == null || strTail.isEmpty()) {
                indexTail = source.length();
            }
            else {
                indexTail = source.indexOf(strTail, indexHead + strHead.length());
            }
            if (indexTail < 0) {
                indexTail = source.length();
            }

            String rtnStr = "";
            if ((indexHead >= 0) && (indexTail >= 0)) {
                if (KeepHeadAndTail) {
                    rtnStr = source.substring(indexHead, indexTail + strTail.length());
                }
                else {
                    rtnStr = source.substring(indexHead + strHead.length(), indexTail);
                }
            }
            return rtnStr;
        }
        catch (Exception ex) {
            return "";
        }
    }

    public static void main(String[] args) throws Exception {
    
	     /*KeyPair kp = RsaHelper.generateRSAKeyPair();
	    
	     PublicKey pubKey = kp.getPublic();
	    
	     PrivateKey priKey = kp.getPrivate();
	    
	     String pubKeyXml = RsaHelper.encodePublicKeyToXml(pubKey);
	    
	     String priKeyXml = RsaHelper.encodePrivateKeyToXml(priKey);
	    
	     System.out.println("====公钥====");
	    
	     System.out.println(pubKeyXml);
	    
	     System.out.println("====私钥====");
	    
	     System.out.println(priKeyXml);
	    
	     PublicKey pubKey2 = RsaHelper.decodePublicKeyFromXml(pubKeyXml);
	    
	     PrivateKey priKey2 = RsaHelper.decodePrivateKeyFromXml(priKeyXml);
	    
	     System.out.println("====公钥对比====");
	    
	     System.out.println(pubKey.toString());
	    
	     System.out.println("------");
	    
	     System.out.println(pubKey2.toString());
	    
	     System.out.println("====私钥对比====");
	    
	     System.out.println(priKey.toString());
	    
	     System.out.println("------");
	    
	     System.out.println(priKey2.toString());*/
	     
    	// String pubKeyXml = "<RSAKeyValue><Modulus>AJAdZEOv05dDtBb+llkVZZW3BhA5i2OJjV9+xVulNDse13DC4yn24j3zNKQuLjdggYZafv8RgcefqR/zdRq3I74N8y3y317gCu1q5N5/SOeeaAkHWWqqd5h2Xe1PWxlck3f8ilrz9tJ4rORHFB1zWvNwBl8TsKEvIqf/BtgjvGGx</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
	    // String priKeyXml ="<RSAKeyValue><Modulus>AJAdZEOv05dDtBb+llkVZZW3BhA5i2OJjV9+xVulNDse13DC4yn24j3zNKQuLjdggYZafv8RgcefqR/zdRq3I74N8y3y317gCu1q5N5/SOeeaAkHWWqqd5h2Xe1PWxlck3f8ilrz9tJ4rORHFB1zWvNwBl8TsKEvIqf/BtgjvGGx</Modulus><Exponent>AQAB</Exponent><P>AMayX9QnZnf4f1rQLFWWCOlgzg2aUEA5OWShNM+mb5PkkqEX+4udYQvwZqb4eNaOKhKCIaf7JUv+uGCVYGBcuMM=</P><Q>ALmtRi4svrevCVRme7HTztkPcjttBFcSJXHxmSJd0PZJ4SJHucR+Lv6ZzaoKYDmgTxwU2xDT9fsL98rUNZ04NHs=</Q><DP>AMBL7VZjc1X0VzzKb7284I8Msx6TE8u0Fgl/wp6cUyvscMQbc8Tg4QUu+gnqOvhayfjznCL4elYcBNDkgEuCMQU=</DP><DQ>Jo51uVbvT6NYU59oZjfuhyJu/SoZMZ+CCRQ7UWib4NjcmEq+p6/wQExd6cZ3zt+cLd+i0e7B8Jy+mu7QJ1Kpiw==</DQ><InverseQ>IUARbYimhS+m/Wq8Tun9s2d7PTdwJxN3urJcoFwaQ+/ypqHk2nX+mrxWRXmkvGT9BkW6fchvMyJAOTp4ejiD+A==</InverseQ><D>M3DaVjLPoyG+iaqsPfYeQZeH6Yiw0YHK3bhhd/n7L9MtcYmPDm6aiSRjuOrnAktHUI7C4Ab+vSOnecksFrIiZm5ep9pbx+bHQ7CeWcDl9UI+KwCMSmbjlVoNZg+qLVDEUAKbz24ymH65kPeTaEbCHI141bG86FBVJdf6PR9jW/U=</D></RSAKeyValue>";
    	 System.out.println("----------------------");
	     String key = "5003090187567183";
	    	 //RandomUtil.generateNumber(16);
	     System.out.println("key is =="+key);
	     String keyMi = RsaHelper.encryptByPublicKey(key, publicKey);
	     System.out.println(keyMi);
	     String keyMing = RsaHelper.decryptByPrivateKey(keyMi, privateKey);
	     System.out.println(keyMing);
	     
	     /*try {
	    
	     String pubKeyXml3 =
	     "<RSAKeyValue><Modulus>rHESyuI3ny4MLsqDBalW9ySaodCL0e6Bsrl01Q5G1qm2wjUoGULazZSNqZY+JQNjU92tW3Snk5RPIkv+wDj+uOT9LTUjQImltHnzqMvbt06GipVXDOyBLTa7G/zRIe/CrjyJ+XEYX2xIhpe5ayowl3HHUpZ71jRNioyxaVVZ8S0=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
	    
	     String priKeyXml3 =
	     "<RSAKeyValue><Modulus>rHESyuI3ny4MLsqDBalW9ySaodCL0e6Bsrl01Q5G1qm2wjUoGULazZSNqZY+JQNjU92tW3Snk5RPIkv+wDj+uOT9LTUjQImltHnzqMvbt06GipVXDOyBLTa7G/zRIe/CrjyJ+XEYX2xIhpe5ayowl3HHUpZ71jRNioyxaVVZ8S0=</Modulus><Exponent>AQAB</Exponent><P>5a7uM+IeY8QMVQl0q88ZTqWbB555l7+366cUIClTN8z2ZXzTnWFCNoQzUrG14FouJFYumFZD12Ni5MkJK6gqSw==</P><Q>wDMhwwO4kz82uSG+FlCBr06fYk2COTg0TofmSp/5OrVqgkBIe7FgpTpVGzGLk0mvOLcy6UZftq//W0Saow6nZw==</Q><DP>FbjDgliiMyE5YVlxlUYSyKNU1BWivj09caXte1UtL5vMubBiewHVtz4tdGamIr+kmX8lDPcrl1Uo5yY0HdLbnQ==</DP><DQ>kIjjJsgxkWnEOUyKqjU4kSDK8x3ehDEkBLpmEFBlGCU9R14YJAyr5RUM0zpbABQ1VK1P9+UYLUYE/hmFQIHQmQ==</DQ><InverseQ>pxQDThwSnUZ4EaNaCPl1ovYypdQUZaZ/Sld1+0n8FEjkmRcGP1R9VMuj1ViPZg3rvm2GeP8Xv1SJqJUVueWiGA==</InverseQ><D>DxBNoPWEAF7IZ6n/KhZx52MGMw6BuFQKdm9m+lml7Iik03BLUXGapYzNlzvtr9QM8D2UMEIPhX/WLdvPpEEWVzGnD7XpLXjGwfu1ZkJRcXPEZEZ2subh5ZBqOWCFWKv5WwgGYWuYDLHfrBlBgSFWR8cZuyqkmMsWl4CiadXqGA0=</D></RSAKeyValue>";
	    
	     System.out.println((new Date()).toLocaleString() + ": 加载公钥中。。。");
	    
	     PublicKey pubKey3 = RsaHelper.decodePublicKeyFromXml(pubKeyXml3);
	    
	     System.out.println((new Date()).toLocaleString() + ": 加载私钥中。。。");
	    
	     PrivateKey priKey3 = RsaHelper.decodePrivateKeyFromXml(priKeyXml3);
	    
	     String dataStr = "Java与.NET和平共处万岁！";
	    
	     byte[] dataByteArray = dataStr.getBytes("utf-8");
	    
	     System.out.println("data的Base64表示："
	    
	     + encode(dataByteArray));
	    
	     System.out.println((new Date()).toLocaleString() + ": 加密中。。。"); // 加密
	    
	     byte[] encryptedDataByteArray = RsaHelper.encryptData(
	    
	     dataByteArray, pubKey3);
	    
	     System.out.println("encryptedData的Base64表示："
	    
	     + encode(encryptedDataByteArray));
	    
	     System.out.println((new Date()).toLocaleString() + ": 解密中。。。"); // 解密
	    
	     // byte[]
	    
	     byte[] decryptedDataByteArray = RsaHelper.decryptData(
	    
	     encryptedDataByteArray, priKey3);
	    
	     System.out.println(new String(decryptedDataByteArray, "utf-8"));// 签名
	    
	     System.out.println((new Date()).toLocaleString() + ": 签名中。。。");
	    
	     byte[] signDataByteArray = RsaHelper.signData(dataByteArray,
	    
	     priKey3);
	    
	     System.out.println("signData的Base64表示："
	    
	     + encode(signDataByteArray)); // 验签
	    
	     System.out.println((new Date()).toLocaleString() + ": 验签中。。。");
	    
	     boolean isMatch = RsaHelper.verifySign(dataByteArray,
	    
	     signDataByteArray, pubKey3);
	    
	     System.out.println("验签结果：" + isMatch);
	    
	     }
	     catch (Exception ex) {
	    
	     ex.printStackTrace();
	    
	     }*/
     }

}
