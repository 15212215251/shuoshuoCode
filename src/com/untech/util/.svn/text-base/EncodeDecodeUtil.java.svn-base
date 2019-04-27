package com.untech.util;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

public class EncodeDecodeUtil {

	private static String isSecurt = Common_Const.isSecurt;
	//公钥模
	private static String pubmodulus = "135627561427481681805896630716860912756862952253139291014384451998862762449042908485901642169139575722258828914435258176461892116463533355758352319664631042588064292476042599989205990552428430111729965734159110658730144698239947758639107766949611212213576685156802830085158547220340083777401585504251489379767";
	//公钥指数
	private static String pubexponent = "65537";
	//私钥模
	private static String privatemodulus = "135627561427481681805896630716860912756862952253139291014384451998862762449042908485901642169139575722258828914435258176461892116463533355758352319664631042588064292476042599989205990552428430111729965734159110658730144698239947758639107766949611212213576685156802830085158547220340083777401585504251489379767";
	// 私钥指数
	private static String privateexponent = "87412798375503894865484654238819111249174271636180333447618671406685753438288652387750422869253803789195274036297524606185419856070665203074248800377105293439702304854340679304406795000560432773123244279091955408248422012563798625847693705070448410105696941917522654416398112092963256806808819142792350767513";

	/**
	 * 解密
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map decrypt(Map<String, Object> map) {
		if("false".equals(isSecurt)){
			return map;
		}
		String encrypt = null;
		try {
			String key = checkKey(map);
			if (!"".equals(key)) {
				encrypt = AESUtil.decrypt(map.get("value").toString(),key);
				if (null == encrypt) {
					return null;
				}else{
					JSONObject jsonObject = null;
					jsonObject = new JSONObject(encrypt);
					Map<String, String> data = new HashMap<String, String>();
					// 将json字符串转换成jsonObject
					Iterator it = jsonObject.keys();
					// 遍历jsonObject数据，添加到Map对象
					while (it.hasNext()) {
						String keys = String.valueOf(it.next());
						String value = null;
						value = jsonObject.get(keys).toString();
						data.put(keys, value);
					}
					return data;
				}
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String  checkKey(Map<String, Object> map) {
		try {
			for (String key : map.keySet()) {
				if (key.equals("key")) {
					RSAPrivateKey privateKey = RSAUtil.getPrivateKey(privatemodulus, privateexponent);
					String resultKey = RSAUtil.decryptByPrivateKey(map.get(key).toString(), privateKey);
					// 解密后的明文
					if (resultKey!=null && !"".equals(resultKey)) {
						return resultKey;
					} else {
						return "";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return  "";
		}
		return  "";
	}

	/**
	 * 加密
	 * 
	 * @return
	 */
	public static String encrypt(String value,String key ) {
		if("false".equals(isSecurt)){
			return value;
		}
		String returnStr = AESUtil.encrypt(value,key);
		return returnStr;
	}
	
	public static String getDefaultKey(String key){
		if("false".equals(isSecurt)){
			return key;
		}
		//解密
//		RSAPrivateKey pk = RSAUtil.getPrivateKey(mo, privat);
//		System.out.println(RSAUtil.decryptByPrivateKey(ss, pk));
		
		RSAPublicKey pubKey = RSAUtil.getPublicKey(pubmodulus, pubexponent);
		try {
			return RSAUtil.encryptByPublicKey(key, pubKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
	public static String getRandomKey(){
		int n = 0 ;
		while(n < 100000){
			n = (int)(Math.random()*1000000);
		}
		return new Integer(n).toString();
	}
	public static void main(String[] args) {
		/*int n = 0 ; 7726329226424085
		while(n < 100000){
			n = (int)(Math.random()*1000000);
		}
		System.out.println( new Integer(n).toString());*/
		String key = "330DE9FBFEE8A5AD4E950671AF65801EC7148CD03E91A1B58E7D8AC601C6978CD205ECDBC807A20B80016B4124AA81067FAF1F0FBE5134DCECAA45F3DB274E4A4AD1862C2917A9460E6DBE3908ED271F0BF96B7158ECF77A7D0C7C98CE9E8E4A16F0C56A8F2E9CEB38BA33169261DA8D53037FE2AEE5E252237A765932B35727";
		RSAPrivateKey privateKey = RSAUtil.getPrivateKey(privatemodulus, privateexponent);
		try {
			String resultKey = RSAUtil.decryptByPrivateKey(key, privateKey);
			System.out.println(resultKey);
			
			String encrypt = AESUtil.decrypt("3108F4F1BD93BE4D9BDF0C0FE2120936BBAED36010F1A42137479F90A83BFC16C144B9C97EFA48D5BA97362F0E418262",resultKey);
	        System.out.println(encrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
