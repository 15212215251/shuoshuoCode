package com.untech.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.untech.service.PayCodeService;
import com.untech.util.GetRemoteIp;
import com.untech.util.RandomUtil;
import com.untech.util.ReturnUtil;
import com.untech.util.SecurtUtil;
import com.untech.util.StringUtil;

/**
 * OnlinePayController
 * 
 * @author cheny
 *   
 *  主要实现我家亳州付款码功能
 * 
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Scope("prototype")
@Controller
@RequestMapping(value = "/payCode")
public class PayCodeController {

	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(PayCodeController.class.getName());
	
	private JSONObject jsonObject = new JSONObject();
	@Autowired
	private  HttpServletRequest request;
	
	@Autowired
	private PayCodeService payCodeService;
	 
	/**
	 * querryLimit 获取限额列表
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/querryLimit")
	@ResponseBody
	public Object querryLimit(@RequestBody Map<String, Object> map) {
		 
		logger.info("querryLimit start :" + map);
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
		logger.info("获取限额列表:" + map);
		if (StringUtil.isEmpty(map.get("userNo")) ||  StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		try{
			Map returnMap = payCodeService.querryLimit(map);
			jsonObject.put("data", returnMap);
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "限额查询成功");
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("获取限额列表成功报文=>" + jsonObject);
		return jsonObject;

	}
	 
	/**
	 * setLimit 设置限额
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/setLimit")
	@ResponseBody
	public Object setLimit(@RequestBody Map<String, Object> map) {
		 
		logger.info("setLimit start :" + map);
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
		logger.info("设置限额:" + map);
		if (StringUtil.isEmpty(map.get("userNo")) ||  StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("flag")) ||  StringUtil.isEmpty(map.get("flag").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[flag]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("limit")) ||  StringUtil.isEmpty(map.get("limit").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[limit]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		String flag = map.get("flag").toString().trim();
		//设置限额
		if("1".equals(flag) || "2".equals(flag)){
			try{
				Map returnMap = payCodeService.setLimit(map);
				jsonObject.put("resCode", returnMap.get("resCode")==null?"4007":returnMap.get("resCode"));
				jsonObject.put("resMsg",returnMap.get("resMsg")==null?"系统异常":returnMap.get("resMsg"));
			}catch (Exception e) {
				e.printStackTrace();
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "系统异常");
			}
		}else{
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[flag]");
			return ReturnUtil.getPayReturn(jsonObject, key);
			
		}
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("设置限额成功报文=>" + jsonObject);
		return jsonObject;

	}
	
	/**
	 * codemanager 生成付款码  每分钟自动刷新生成付款码，离线暂不可以使用
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/codemanager")
	@ResponseBody
	public Object codemanager(@RequestBody Map<String, Object> map) {
		 
		logger.info("codemanager start :" + map);
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
		logger.info("设置限额:" + map);
		if (StringUtil.isEmpty(map.get("userNo")) ||  StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("deviceId")) ||  StringUtil.isEmpty(map.get("deviceId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[deviceId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
	    try {
			Map returnMap = payCodeService.codemanager(map);
			jsonObject.put("data", returnMap);
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "限额查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "付款码生成失败");
		}
	    
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("设置限额成功报文=>" + jsonObject);
		return jsonObject;

	}
	 
	 
	
}
