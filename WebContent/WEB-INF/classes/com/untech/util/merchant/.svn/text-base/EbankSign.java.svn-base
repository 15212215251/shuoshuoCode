package com.untech.util.merchant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 

public class EbankSign {
	
	  /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara) {
		String private_key = sPara.get("private_key");
		sPara.remove("private_key");
    	String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        System.out.println("签名参数："+prestr);
        String mysign = "";
        if(sPara.get("signType").equals("RSA") ){
        	mysign = RSA.sign(prestr, private_key, sPara.get("charset"));
        }
        return mysign;
    }
	
    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    public static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.signparaFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara);

        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", sPara.get("signType"));

        return sPara;
    }

    
    
    public static boolean verify(Map<String, String> sParaTem,String sign,String pubkey){
    	boolean isSign = false;
    	Map<String, String> params =  paraFilter(sParaTem);
    	String content = createLinkString(params);
    	
    	System.out.println("content:"+content);
    	
    	isSign = RSA.verify(content, sign, pubkey, "utf-8");
    	
    	System.out.println("isSign："+isSign);
    	
    	return isSign;
    }
    
    
    
    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        System.out.println(keys);
        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }
    
    
    
    /** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (key.equalsIgnoreCase("sign")
                || key.equalsIgnoreCase("signType")
            		) {
                continue;
            }
            if(value == null){
            	value = "";
            }
            result.put(key, value);
        }

        return result;
    }
    
    
   public static void main(String[] args) {
	 /*  Map<String,String> params = new HashMap<String,String>();
		params.put("notifyDate", "");
		params.put("notifyDype", "");
		params.put("notifyId", "");
		params.put("partnerId", "");
		params.put("signType", "");
		params.put("sign", "");
		params.put("outTradeNo", "");
		params.put("tradeNo", "");
		params.put("tradeCreateDate", "");
		params.put("tradeStatus", "");
		params.put("totalAmout", "");
		params.put("receiptAmout", "");
		params.put("payDate", "");
		params.put("mobile", "");	   
		String a = createLinkString(params);
		System.out.println(a);*/
	   String signStr = "1234567890";
	   String private_key="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJSthTdevJqT3Xi2yCbmsImiezdat+TmBWJvahYKzqeCx2tWS6oYKchVCTNMWhtl8RP0Wffo76Afi1scuUIW0pG3HVMt+QLGx/Gj/IU4FFZwspj0v00ov1MyMpDjPu5e2+ksZCRjt9+8eztx/uCfbtDpMyYSSpHecziSucLYkVFBAgMBAAECgYA9uNeQCAVWSsJrpb3r4GMzkD+E2GV1YkSj+8rXwb7O7czp85ZP094sq5i5Mh5KN4TgmtVqxMoN+Gp69a63EBB0Cch+TCBT+/1IcMGmP5SXMtt6GUOXV4hqbE7gdVxnlI5VWI1UYbOKRakMThuXPtkOPemOlW4pD0J/g6kQeANRTQJBAMWVO3qfEw9Gjp9QAhsdUzGj4ZRqPt5VDXNrucVCBg1ZceQtp/4g0gHZCCDCQRiAhoFk4L7nm7iXu5sj252nUPMCQQDAorrL21Un7PztL/OD1rjOBq5fvgyKFL3u8Lk2RlRDQ8NUWDNB+fcDU8FNh89U8hAdatjWDA/GZPONNR4V/AH7AkAsjydG1DE8NLFdhHG1I7vragYprnxXL99EtHrE3cr3n8BrsVA9o7E48XU/zg9YM0QiUlnNM/aOfsm0hzW0LFO5AkEArPZv+nJpN3wWbXFMmOfG71YYY2blI2YUU+JYql7jcbH6RFUdUPP70kT1n4N9qavexEsXfrk9wMkL1ZIH/X1U2QJBAMPV7sIUp1EXuv3uCRHksFo3UOH7EORQXY7GdoyEEwbyRRjf0kOk2WgschSPfcJi1rzju7T5T8WYW0ohh3X19t8=";
	   String pub_key="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCjFZG5fea3PdGIiQzvFflaSe8WQ+exy3HTlMsnZNFezcleQDJrGKkxjQcXTw6PVCjWOkZhZpMDI6kNBwJiEPjMZxJW+Jy9Ll7yB9sCnf2tYHIKW3Tfsy3f+e/+m1spfXqbxaKnIlPfoXzQDmRGKCNE7lZFCU0yLJxR9DMMXms8wIDAQAB";
	   String sign = RSA.sign(signStr, private_key, "utf-8");
	   System.out.println("sign===="+sign);
		boolean isSign = RSA.verify(signStr, sign, pub_key, "utf-8");
    	System.out.println("isSign=="+isSign);
	   
	   
   }
}
