package test;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import com.untech.util.RSAUtil;
  
/** 
 * @author Administrator 
 *  
 */  
public class PKj2C {  
	
	 static String pubmodulus = "135627561427481681805896630716860912756862952253139291014384451998862762449042908485901642169139575722258828914435258176461892116463533355758352319664631042588064292476042599989205990552428430111729965734159110658730144698239947758639107766949611212213576685156802830085158547220340083777401585504251489379767";
	//公钥指数
	 static String pubexponent = "65537";
	//私钥模
	 static String privatemodulus = "135627561427481681805896630716860912756862952253139291014384451998862762449042908485901642169139575722258828914435258176461892116463533355758352319664631042588064292476042599989205990552428430111729965734159110658730144698239947758639107766949611212213576685156802830085158547220340083777401585504251489379767";
	//私钥指数
	  static String privateexponent = "87412798375503894865484654238819111249174271636180333447618671406685753438288652387750422869253803789195274036297524606185419856070665203074248800377105293439702304854340679304406795000560432773123244279091955408248422012563798625847693705070448410105696941917522654416398112092963256806808819142792350767513";

    public static void main(String[] args) throws Exception {  
        //HashMap<String, Object> map = RSAUtil.getKeys();  
       /* RSAPublicKey publicKey = (RSAPublicKey) map.get("PUBLIC");  
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get("PRIVATE");
        RSAPublicKey publicKey = (RSAPublicKey) map.get("public");  
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private"); */
       
    	RSAPublicKey publicKey = RSAUtil.getPublicKey(pubmodulus, pubexponent);
    	BigInteger b1 = new BigInteger(privatemodulus);
		BigInteger b2 = new BigInteger(privateexponent);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
		PrivateKey key =   keyFactory.generatePrivate(keySpec);
    	RSAPrivateKey privateKey = RSAUtil.getPrivateKey(privatemodulus, privateexponent);
    	
        String publicKeyString = getRSAPublicKeyAsNetFormat(publicKey.getEncoded());  
        String privateKeyString = getRSAPrivateKeyAsNetFormat(privateKey.getEncoded());  
          
       // System.out.println(encodeBase64(publicKey.getEncoded()));//此处为客户端加密时需要的公钥字符串  
       // System.out.println("===================================================================");
       // System.out.println(encodePublicKeyToXml(publicKey));  
      //  System.out.println("===================================================================");
        System.out.println("publicKeyString==="+publicKeyString);  
        System.out.println("===================================================================");
        System.out.println(privateKeyString);  
        System.out.println("===================================================================");
    }  
  
    /**获取密钥对 
     * @return 
     * @throws NoSuchAlgorithmException 
     */  
    public static HashMap<String, Object> getKeys()  
            throws NoSuchAlgorithmException {  
        HashMap<String, Object> map = new HashMap<String, Object>();  
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");  
        keyPairGen.initialize(1024);  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
        map.put("PUBLIC", publicKey);  
        map.put("PRIVATE", privateKey);  
        return map;  
    }  
  
    /** 
     * 私钥转换成C#格式 
     * @param encodedPrivkey 
     * @return 
     */  
    private static String getRSAPrivateKeyAsNetFormat(byte[] encodedPrivateKey) {  
        try {  
            StringBuffer buff = new StringBuffer(1024);  
  
            PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);  
            //PKCS1Padding
           /* BigInteger b1 = new BigInteger(privatemodulus);
			BigInteger b2 = new BigInteger(privateexponent);
            RSAPrivateKeySpec pvkKeySpec = new RSAPrivateKeySpec(b1, b2);*/
           // PKCS1EncodedKeySpec  pvkKeySpec = new PKCS1EncodedKeySpec(encodedPrivateKey);  
            
            BigInteger b1 = new BigInteger(privatemodulus);
    		BigInteger b2 = new BigInteger(privateexponent);
    		
            
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
    		PrivateKey key =   keyFactory.generatePrivate(keySpec);
    		//RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey)key;  
            //RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory.generatePrivate(pvkKeySpec);  
  
          /*  RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            RSAPrivateKey pvkKey =  (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
            */
            buff.append("<RSAKeyValue>");  
            buff.append("<Modulus>"  
                    + encodeBase64(removeMSZero(new BigInteger(privatemodulus)
                            .toByteArray())) + "</Modulus>");  
  
            buff.append("<Exponent>"  
                    + encodeBase64(removeMSZero(new BigInteger(privateexponent)  
                            .toByteArray())) + "</Exponent>");  
  
           /* buff.append("<P>"  
                    + encodeBase64(removeMSZero(pvkKey.getPrimeP()  
                            .toByteArray())) + "</P>");  
  
            buff.append("<Q>"  
                    + encodeBase64(removeMSZero(pvkKey.getPrimeQ()  
                            .toByteArray())) + "</Q>");  
  
            buff.append("<DP>"  
                    + encodeBase64(removeMSZero(pvkKey.getPrimeExponentP()  
                            .toByteArray())) + "</DP>");  
  
            buff.append("<DQ>"  
                    + encodeBase64(removeMSZero(pvkKey.getPrimeExponentQ()  
                            .toByteArray())) + "</DQ>");  
  
            buff.append("<InverseQ>"  
                    + encodeBase64(removeMSZero(pvkKey.getCrtCoefficient()  
                            .toByteArray())) + "</InverseQ>");  
  
            buff.append("<D>"  
                    + encodeBase64(removeMSZero(pvkKey.getPrivateExponent()  
                            .toByteArray())) + "</D>");  */
            buff.append("</RSAKeyValue>");  
  
            return buff.toString();  
        } catch (Exception e) {  
            System.err.println(e);  
            e.printStackTrace();
            return null;  
        }  
    }  
  
    /** 
     * 公钥转成C#格式 
     * @param encodedPrivkey 
     * @return 
     */  
    private static String getRSAPublicKeyAsNetFormat(byte[] encodedPublicKey) {  
        try {  
            StringBuffer buff = new StringBuffer(1024);  
              
            //Only RSAPublicKeySpec and X509EncodedKeySpec supported for RSA public keys  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            RSAPublicKey pukKey = (RSAPublicKey) keyFactory  
                    .generatePublic(new X509EncodedKeySpec(encodedPublicKey));  
  
            buff.append("<RSAKeyValue>");  
            buff.append("<Modulus>"  
                    + encodeBase64(removeMSZero(pukKey.getModulus()  
                            .toByteArray())) + "</Modulus>");  
            buff.append("<Exponent>"  
                    + encodeBase64(removeMSZero(pukKey.getPublicExponent()  
                            .toByteArray())) + "</Exponent>");  
            buff.append("</RSAKeyValue>");  
            return buff.toString();  
        } catch (Exception e) {  
            System.err.println(e);  
            return null;  
        }  
    }  
  
    /** 
     * 公钥转换成C#格式 
     * @param key 
     * @return 
     * @throws Exception 
     */  
    public static String encodePublicKeyToXml(PublicKey key) throws Exception {  
        if (!RSAPublicKey.class.isInstance(key)) {  
            return null;  
        }  
        RSAPublicKey pubKey = (RSAPublicKey) key;  
        StringBuilder sb = new StringBuilder();  
  
        sb.append("<RSAKeyValue>");  
        sb.append("<Modulus>")  
                .append(encodeBase64(removeMSZero(pubKey.getModulus()  
                        .toByteArray()))).append("</Modulus>");  
        sb.append("<Exponent>")  
                .append(encodeBase64(removeMSZero(pubKey.getPublicExponent()  
                        .toByteArray()))).append("</Exponent>");  
        sb.append("</RSAKeyValue>");  
        return sb.toString();  
    }  
  
    /** 
     * @param data 
     * @return 
     */  
    private static byte[] removeMSZero(byte[] data) {  
        byte[] data1;  
        int len = data.length;  
        if (data[0] == 0) {  
            data1 = new byte[data.length - 1];  
            System.arraycopy(data, 1, data1, 0, len - 1);  
        } else  
            data1 = data;  
  
        return data1;  
    }  
  
    /** 
     * base64编码 
     * @param input 
     * @return 
     * @throws Exception 
     */  
    public static String encodeBase64(byte[] input) throws Exception {  
        Class clazz = Class  
                .forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");  
        Method mainMethod = clazz.getMethod("encode", byte[].class);  
        mainMethod.setAccessible(true);  
        Object retObj = mainMethod.invoke(null, new Object[] { input });  
        return (String) retObj;  
    }  
  
    /** 
     * base64解码  
     * @param input 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decodeBase64(String input) throws Exception {  
        Class clazz = Class  
                .forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");  
        Method mainMethod = clazz.getMethod("decode", String.class);  
        mainMethod.setAccessible(true);  
        Object retObj = mainMethod.invoke(null, input);  
        return (byte[]) retObj;  
    }  
  
    public static String byteToString(byte[] b)  
            throws UnsupportedEncodingException {  
        return new String(b, "utf-8");  
    }  
} 