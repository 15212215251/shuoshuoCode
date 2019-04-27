package com.untech.controller;

import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.untech.util.GetRemoteIp;
import com.untech.util.RandomUtil;
import com.untech.util.RedisUtil;
import com.untech.util.ReturnUtil;
import com.untech.util.SecurtUtil;
import com.untech.util.SocketClientToo;
import com.untech.util.StringUtil;

/**
 * OnlinePayController
 * 
 * @author cheny
 *   
 *  主要实现网上办事大厅用户的签约
 * 
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Scope("prototype")
@Controller
public class SendMsgController {

	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(SendMsgController.class.getName());
	
	private JSONObject jsonObject = new JSONObject();
	@Autowired
	private  HttpServletRequest request;
	 
	/**
	 * getMessage 获取短信验证码
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getMessage")
	@ResponseBody
	public Object cardType(@RequestBody Map<String, Object> map) {
		 
		logger.info("cardType start :" + map);
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		//解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "0001");
			jsonObject.put("resMsg", "解密失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("获取短信验证码:" + map);
		if (StringUtil.isEmpty(map.get("userNo")) ||  StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("mobilePhone")) ||  StringUtil.isEmpty(map.get("mobilePhone").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[mobilePhone]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		String mobilePhone = map.get("mobilePhone").toString().trim();
		if(mobilePhone.length() != 11 ){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[mobilePhone]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("msgType")) ||  StringUtil.isEmpty(map.get("msgType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[msgType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		String msgType = map.get("msgType").toString().trim();
		if(!"00".equals(msgType) && !"01".equals(msgType) && !"02".equals(msgType)&& !"03".equals(msgType)&& !"04".equals(msgType) ){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[msgType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		//生成发送短信验证码
		String authCode  = RedisUtil.getRedis(mobilePhone, msgType);
		boolean flag = false;
		if(!"".equals(authCode)){
			flag = RedisUtil.setSmsRedis(map.get("userNo").toString(), mobilePhone, msgType, authCode);
		}else{
			Random random = new Random();
			authCode="";
			for(int i=0;i<6;i++){
				authCode += random.nextInt(10);
			}
			System.out.print(authCode);
			flag = RedisUtil.setSmsRedis(map.get("userNo").toString(), mobilePhone, msgType, authCode);
			
		}
		System.out.println("flag=========="+flag);
		if(flag){
			String content = authCode+"，温馨提示：请您在30分钟内完成短信校验，谢谢合作！";
			//调用发送短信接口
			//flag = SendSmsUtil.sendMsg(authCode, mobilePhone, content);
			SocketClientToo client = new SocketClientToo();
			System.out.println("6666|YDYH|"+mobilePhone+"|"+content+"|"+"--------------------");
			String response = client.sendMessage("6666|YDYH|"+mobilePhone+"|"+content+"|");
			System.out.println(response+"--------------------");
			if(response != null && response.length() != 4){
				if("0000".equals(response.split("\\|")[0])){
					flag =  true;
				}
			}
		}
		if(flag){
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "短信发送成功");
		}else{
			jsonObject.put("resCode", "9999");
			jsonObject.put("resMsg", "短信发送失败");
		}
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("获取短信验证码成功报文=>" + jsonObject);
		return jsonObject;

	}
	
	
	/**
	 * getMessage 校验短信验证码
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/ValidMessage")
	@ResponseBody
	public Object ValidMessage(@RequestBody Map<String, Object> map) {
		 
		logger.info("ValidMessage start :" + map);
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		//解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "0001");
			jsonObject.put("resMsg", "解密失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("验证短信验证码列表:" + map);
		if (StringUtil.isEmpty(map.get("userNo")) ||  StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("mobilePhone")) ||  StringUtil.isEmpty(map.get("mobilePhone").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[mobilePhone]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		String mobilePhone = map.get("mobilePhone").toString().trim();
		if(mobilePhone.length() != 11 ){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[mobilePhone]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("msgType")) ||  StringUtil.isEmpty(map.get("msgType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[msgType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		String msgType = map.get("msgType").toString().trim();
		if(!"00".equals(msgType) && !"01".equals(msgType) && !"02".equals(msgType)&& !"03".equals(msgType) && !"04".equals(msgType) ){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[msgType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("messageCode")) ||  StringUtil.isEmpty(map.get("messageCode").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[messageCode]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		//生成发送短信验证码
		String authCode  = RedisUtil.getRedis(mobilePhone, msgType);
		String messageCode  = map.get("messageCode").toString().trim();
		if(messageCode.equals(authCode)){
			RedisUtil.delRedis(mobilePhone, msgType);
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "短信校验成功");
		}else{
			jsonObject.put("resCode", "9999");
			jsonObject.put("resMsg", "短信校验失败");
		}
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("校验短信验证码=>" + jsonObject);
		return jsonObject;

	}
	 
	 
	
}
