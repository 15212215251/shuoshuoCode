package com.untech.util;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
/**
 * 对外私钥和公钥不一致，针对同一支付接口
 * 
 * */
public class SecurtUtil {

	private static String isSecurt =  Common_Const.isSecurt;
	
	/*//公钥模
	private static String pubmodulus = "135627561427481681805896630716860912756862952253139291014384451998862762449042908485901642169139575722258828914435258176461892116463533355758352319664631042588064292476042599989205990552428430111729965734159110658730144698239947758639107766949611212213576685156802830085158547220340083777401585504251489379767";
	//公钥指数
	private static String pubexponent = "65537";
	//私钥模
	private static String privatemodulus = "135627561427481681805896630716860912756862952253139291014384451998862762449042908485901642169139575722258828914435258176461892116463533355758352319664631042588064292476042599989205990552428430111729965734159110658730144698239947758639107766949611212213576685156802830085158547220340083777401585504251489379767";
	//私钥指数
	private static String privateexponent = "87412798375503894865484654238819111249174271636180333447618671406685753438288652387750422869253803789195274036297524606185419856070665203074248800377105293439702304854340679304406795000560432773123244279091955408248422012563798625847693705070448410105696941917522654416398112092963256806808819142792350767513";
*/
	//生产环境使用
	//公钥模
	private static String pubmodulus = "112957909143392081727744781919098395003306301545216094407215663457421834779616765286918820604418911006801906227416166919650315443955307897773954682150539846903655345876439941051926268986582306954500250395306147612720212444196024608915175544236458273825949092154045749911970523616983120372694522974353484267813";
	//公钥指数
	private static String pubexponent = "65537";
	//私钥模
	private static String privatemodulus = "103443868297555251495064875564430934194751973248039400988788637684978348762453697576293642187769625083272836672418809825496475912039962880238677432956668381734455386838499614805413804883074742801388546506182823395631064067558720167685371929005111993004806321210083852249878871409575146065475657718304094209849";
	//私钥指数
	private static String privateexponent = "68000804644632426643123502282173634847159688595328340836458673554545328919297956863785078546980957136236940194411510519279783561080087291246817845898335082978598793833634409447167631575692513610304827999248118866729312480315536515649695909802278943721310051385874165288825760450566642222660249986423379251177";

	/**
	 * 解密
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
				encrypt = AESUtil.decrypt(map.get("encryptValue").toString().trim(),key);
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

	//解密获得密钥
	public static String  checkKey(Map<String, Object> map) {
		String encryptKey = map.get("encryptKey")==null?"":map.get("encryptKey").toString().trim();
		if("".equals(encryptKey)){
			return "";
		}
		RSAPrivateKey privateKey = RSAUtil.getPrivateKey(privatemodulus, privateexponent);
		String resultKey = "";
		try {
			resultKey = RSAUtil.decryptByPrivateKey(encryptKey, privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 解密后的明文
		if (resultKey != null && !"".equals(resultKey)) {
			return resultKey;
		} else {
			return "";
		}
	}

	/**
	 * 加密
	 * @return
	 */
	public static String encrypt(String value,String key) {
		if("false".equals(isSecurt)){
			return value;
		}
		String returnStr = AESUtil.encrypt(value,key);
		return returnStr;
	}
	
	public static void main(String[] args) {
		/*String str = encrypt("{id:08136331,sid:3c7a44aa7948b073,type:android,key:1234567890123456}", "1234567890123456");
	    System.out.println(str);
	    String key = getDefaultKey("1234567890123456");
	    System.out.println(key);
	    RSAPrivateKey privateKey = RSAUtil.getPrivateKey(privatemodulus, privateexponent);
		String resultKey = "";
		try {
			resultKey = RSAUtil.decryptByPrivateKey(key, privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(resultKey);*/
		
		Map<String,Object> map = new HashMap<String,Object>();
		String key = getDefaultKey("0234567890123456");
		map.put("encryptKey", key);
		//map.put("encryptValue", "EEBFD610A8ECF6470875E2061BC4360392636F183A1906AEE79C7D54CDEA222F5B2D78BFB70551B6738140FF85E810CE9348AF26298A3544642B8FE906CCACA36948CCCB40791D97E7A4E19FD9FA8F6331C5CF3F57DD8B88DD1EA92ACAD3A323B0B62666E2A06B8FF2307BED176BE082585E1904607C512CAAB8B4DB4C40353612D52A682F37D8B4F8684A09D33808DED9E070C19FCBF578529B106F984F64D7A165260876F7660569988523ED0A647B");
		/*Map<String,Object> map2 = SecurtUtil.decrypt(map);
		System.out.println(map2);*/
		String encryptKey = map.get("encryptKey")==null?"":map.get("encryptKey").toString().trim();
		System.out.println(encryptKey);
		privatemodulus="112957909143392081727744781919098395003306301545216094407215663457421834779616765286918820604418911006801906227416166919650315443955307897773954682150539846903655345876439941051926268986582306954500250395306147612720212444196024608915175544236458273825949092154045749911970523616983120372694522974353484267813";
		privateexponent="25915667819400389716623747515686764228904489830689064124187782714280402028568864654380142310573305856207538673351381445654548469037520935516260777893640490335669644679905175538313678675538403184040784375240509243451889626484121005289911271440731138063124496316994765185965321655590355765790321475461281256577";
		RSAPrivateKey privateKey = RSAUtil.getPrivateKey(privatemodulus, privateexponent);
		String resultKey = "";
		try {
			resultKey = RSAUtil.decryptByPrivateKey(encryptKey, privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("resultKey=="+resultKey);
	}
	
	//加密AES密钥返回
	public static String getDefaultKey(String key){
		if("false".equals(isSecurt)){
			return key;
		}
		RSAPublicKey pubKey = RSAUtil.getPublicKey(pubmodulus, pubexponent);
		try {
			return RSAUtil.encryptByPublicKey(key, pubKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
