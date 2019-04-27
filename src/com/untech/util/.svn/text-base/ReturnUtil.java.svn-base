package com.untech.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class ReturnUtil {
	
	private static String isSecurt = Common_Const.isSecurt;
	
	private static final Logger logger = Logger.getLogger(ReturnUtil.class.getName());
	
	
	public static Object getReturn(JSONObject jsonObject,String key){
		if(!"false".equals(isSecurt)){
			String value = EncodeDecodeUtil.encrypt(jsonObject.toString(),key);
			jsonObject = new JSONObject();
			jsonObject.put("value", value);
			jsonObject.put("key", EncodeDecodeUtil.getDefaultKey(key));
			logger.info(jsonObject);
			return jsonObject;
		}else{
			logger.info(jsonObject);
			return jsonObject;
		}
		
	}
	
	public static Object getPayReturn(JSONObject jsonObject,String key){
		logger.info("jsonObject==="+jsonObject);
		if(!"false".equals(isSecurt)){
			String value = SecurtUtil.encrypt(jsonObject.toString(),key);
			jsonObject = new JSONObject();
			jsonObject.put("encryptValue", value);
			jsonObject.put("encryptKey", SecurtUtil.getDefaultKey(key));
			logger.info(jsonObject);
			return jsonObject;
		}else{
			logger.info(jsonObject);
			return jsonObject;
		}
		
	}
	
	public static Map<String,String> getNotifyReturn(Map<String,String> map,String key){
		if(!"false".equals(isSecurt)){
			String value = SecurtUtil.encrypt(map.toString(),key);
			map = new HashMap<String,String>();
			map.put("encryptValue", value);
			map.put("encryptKey", SecurtUtil.getDefaultKey(key));
			logger.info(map);
			return map;
		}else{
			logger.info(map);
			return map;
		}
		
	}

}
