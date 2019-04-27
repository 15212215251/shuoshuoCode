package com.untech.controller;

import java.util.HashMap;
import java.util.List;
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

import com.untech.service.OnlinePayService;
import com.untech.service.PayService;
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
 *  主要实现网上办事大厅用户的签约
 * 
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Scope("prototype")
@Controller
public class OnlinePayController {

	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(OnlinePayController.class.getName());
	
	private JSONObject jsonObject = new JSONObject();
	@Autowired
	private  HttpServletRequest request;
	@Autowired
	private PayService payService;
	
	@Autowired
	private OnlinePayService onlinePayService;
	/**
	 * 获取银行卡类型列表
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/cardType")
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
		logger.info("请求银行卡列表:" + map);
		if (StringUtil.isEmpty(map.get("userNo")) ||  StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("cardNo")) ||  StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[cardNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		String cardNo  = map.get("cardNo").toString().trim();
		if(cardNo.length() < 16){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "卡号必须大于等于16位");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		jsonObject.put("binType", "");
		jsonObject.put("binName", "");
		try{
			cardNo = cardNo.substring(0,6);
			logger.info("cardNo:" + cardNo);
		    List list = onlinePayService.queryCardType(cardNo);
		    if(list != null && list.size() > 0 ){
		    	Map<String,String> binMap  =  (Map<String, String>) list.get(0);
 		    	jsonObject.put("binType", binMap.get("BINTYPE"));
				jsonObject.put("binName",  binMap.get("NAME"));
		    	jsonObject.put("resCode", "0000");
				jsonObject.put("resMsg", "获取银行卡类型查询成功");
		    }else{
		    	jsonObject.put("resCode", "4006");
				jsonObject.put("resMsg", "暂支持药都银行借记卡");
		    }
		    
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("获取银行卡类型成功报文=>" + jsonObject);
		return jsonObject;

	}
	
	
	/**
	 * 银行卡签约、解约
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/cardSign")
	@ResponseBody
	public Object cardSign(@RequestBody Map<String, Object> map) {
		 
		logger.info("cardSign start :" + map);
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
		logger.info("请求银行卡签约、解约列表:" + map);
		if (StringUtil.isEmpty(map.get("userNo")) ||  StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("cardNo")) ||  StringUtil.isEmpty(map.get("cardNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[cardNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		String signType = map.get("signType").toString().trim();
		if("1".equals(signType)){
			if (StringUtil.isEmpty(map.get("cardName")) ||  StringUtil.isEmpty(map.get("cardName").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[cardName]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}else{
				map.put("cardName",map.get("cardName").toString().trim());
			}
			if (StringUtil.isEmpty(map.get("cardType")) ||  StringUtil.isEmpty(map.get("cardType").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[cardType]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if(!"D".equals(map.get("cardType").toString().trim())){
				jsonObject.put("resCode", "1003");
				jsonObject.put("resMsg", "参数不符[cardType]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if (StringUtil.isEmpty(map.get("certType")) ||  StringUtil.isEmpty(map.get("certType").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[certType]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if(!"1".equals(map.get("certType").toString().trim())){
				jsonObject.put("resCode", "1003");
				jsonObject.put("resMsg", "参数不符[certType]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if (StringUtil.isEmpty(map.get("certNo")) ||  StringUtil.isEmpty(map.get("certNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[certNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if (StringUtil.isEmpty(map.get("mobileMsg")) ||  StringUtil.isEmpty(map.get("mobileMsg").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[mobileMsg]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
		}
		
		if (StringUtil.isEmpty(map.get("mobilePhone")) ||  StringUtil.isEmpty(map.get("mobilePhone").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[mobilePhone]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		if (StringUtil.isEmpty(map.get("channerNo")) ||  StringUtil.isEmpty(map.get("channerNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[channerNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if(!"01".equals(map.get("channerNo").toString().trim())){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[channerNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("signType")) ||  StringUtil.isEmpty(map.get("signType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[signType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if(!"1".equals(map.get("signType").toString().trim()) && !"2".equals(map.get("signType").toString().trim())){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[signType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		String cardNo  = map.get("cardNo").toString().trim();
		try{
			
			Map<String,String> returnMap = new HashMap<String, String>();
			if("1".equals(signType)){
				returnMap = onlinePayService.cardSign(map);
			}else if("2".equals(signType)){
				returnMap =  onlinePayService.cardSignDis(map);
			}
		    jsonObject.put("resCode", returnMap.get("resCode"));
		    jsonObject.put("resMsg", returnMap.get("resMsg"));
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("签约解约成功报文=>" + jsonObject);
		return jsonObject;

	}
	
	
	/**
	 * 银行卡签约查询
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/cardSignStatus")
	@ResponseBody
	public Object cardSignStatus(@RequestBody Map<String, Object> map) {
		 
		logger.info("cardSign start :" + map);
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
		logger.info("请求银行卡签约、解约列表:" + map);
		if (StringUtil.isEmpty(map.get("userNo")) ||  StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("cardNo")) ||  StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[cardNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		try{
			Map<String,String> returnMap = new HashMap<String, String>();
			returnMap = onlinePayService.cardSignStatus(map);
		    jsonObject.put("resCode", returnMap.get("resCode"));
		    jsonObject.put("resMsg", returnMap.get("resMsg"));
		    jsonObject.put("status", returnMap.get("status"));
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			jsonObject.put("status", "4");
		}
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("签约解约查询成功报文=>" + jsonObject);
		return jsonObject;

	}
	
	/**
	 * 支付密码管理
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/securtManager")
	@ResponseBody
	public Object securtManager(@RequestBody Map<String, Object> map) {
		 
		logger.info("securtManager start :" + map);
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
		logger.info("请求支付密码管理列表:" + map);
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
		if(!"1".equals(map.get("flag").toString())){
			if (StringUtil.isEmpty(map.get("paySecurt")) ||  StringUtil.isEmpty(map.get("paySecurt").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[paySecurt]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
		}
		String flag  = map.get("flag").toString();
		try{
			Map<String,String> returnMap = new HashMap<String, String>();
			returnMap = onlinePayService.securtManager(map);
            if("1".equals(flag)){
            	jsonObject.put("status", returnMap.get("status"));
			}
		    jsonObject.put("resCode", returnMap.get("resCode"));
		    jsonObject.put("resMsg", returnMap.get("resMsg"));
		    
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			jsonObject.put("status", "4");
		}
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("支付密码设置报文=>" + jsonObject);
		return jsonObject;

	}
	 
	
}
