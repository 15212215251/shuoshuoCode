package com.untech.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
	/**
	 * This program generates a AES key, retrieves its raw bytes, and then
	 * reinstantiates a AES key from the key bytes. The reinstantiated key is
	 * used to initialize a AES cipher for encryption and decryption.
	 */

	private static final String AES = "AES/ECB/PKCS5Padding";//"AES";

	//public static final String CRYPT_KEY = "1234567890123456";

	/**
	 * 加密
	 * 
	 * @param encryptStr
	 * @return
	 */
	public static byte[] encrypt(byte[] src, String key) throws Exception {
		Cipher cipher = Cipher.getInstance(AES);
		SecretKeySpec securekey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		cipher.init(Cipher.ENCRYPT_MODE, securekey);// 设置密钥和加密形式
		return cipher.doFinal(src);
	}
	
	public static byte[] encrypt2(String content, String password) {  
        try {             
                KeyGenerator kgen = KeyGenerator.getInstance("AES");  
                kgen.init(128, new SecureRandom(password.getBytes()));  
                SecretKey secretKey = kgen.generateKey();  
                byte[] enCodeFormat = secretKey.getEncoded();  
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
                byte[] byteContent = content.getBytes("utf-8");  
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化  
                byte[] result = cipher.doFinal(byteContent);  
                return result; // 加密  
        } catch (NoSuchAlgorithmException e) {  
                e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
                e.printStackTrace();  
        } catch (InvalidKeyException e) {  
                e.printStackTrace();  
        } catch (UnsupportedEncodingException e) {  
                e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
                e.printStackTrace();  
        } catch (BadPaddingException e) {  
                e.printStackTrace();  
        }  
        return null;  
}  

	/**
	 * 解密
	 * 
	 * @param decryptStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, String key) throws Exception {
		Cipher cipher = Cipher.getInstance(AES);
		SecretKeySpec securekey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");// 设置加密Key
		cipher.init(Cipher.DECRYPT_MODE, securekey);// 设置密钥和解密形式
		return cipher.doFinal(src);
	}

	/**
	 * 二行制转十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String data,String key ) {
		try {
			return new String(decrypt(hex2byte(data.getBytes("UTF-8")), key));
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data,String key ) {
		try {
			return byte2hex(encrypt(data.getBytes("UTF-8"), key));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		
		String key ="";
		String value="123456abcdef";
		String idEncrypt = encrypt(value,key);
		System.out.println(idEncrypt);
		String source  = decrypt(idEncrypt, key);
		System.out.println(source);
		
		//System.out.println(new St1234567890123456ring(encrypt2(value, key)));
		/*String str1 = "{id:08136331,sid:3c7a44aa7948b073,type:android,key:1234567890123456}";
		String str = "{transactionNo:562701,userId:08136331,unitNo:34010100770002,unitName:安徽省交通厅,payamt:9.00,flag:0,feeType:08,androidId:3c7a44aa7948b073,mid:001,detail:330ml可口可乐,key:1234567890123456}";
		String idEncrypt = encrypt(str1);
		System.out.println(idEncrypt);
		String idDecrypt = decrypt("92A7EE441BD0A9EED7820DB9BF540B9EE13E80A01AEEAFA35668EA51C6FEBC58B70972FB3E3B43DF0ABDFC26B561FC28415FF11D5092F9CBE38F7581F9EF369B7495A4A74E2656D98F238735E9DCA7B1AD3BE219B216B3EE1B639668CC16701A");
			System.out.println(idDecrypt);*/
//		JSONObject jsonObject = null;
//		try {
//			jsonObject = new JSONObject(idDecrypt);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(jsonObject);
//
//		Map<String, String> data = new HashMap<String, String>();
//		// 将json字符串转换成jsonObject
//		Iterator it = jsonObject.keys();
//		// 遍历jsonObject数据，添加到Map对象
//		while (it.hasNext()) {
//			String key = String.valueOf(it.next());
//			String value = null;
//			try {
//				value = jsonObject.get(key).toString();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			data.put(key, value);
//		}
//		System.out.println(data);
	}

}
