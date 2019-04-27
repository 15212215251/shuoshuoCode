package com.untech.controller;

import java.math.BigDecimal;
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

import com.untech.service.PayService;
import com.untech.util.FileDzUtil;
import com.untech.util.GetRemoteIp;
import com.untech.util.RandomUtil;
import com.untech.util.RedisUtil;
import com.untech.util.ReturnUtil;
import com.untech.util.SecurtUtil;
import com.untech.util.StringUtil;
import com.untech.util.XnbShortConnection;

/**
 * PayController
 * ftpuser@2015!@#$%^
 * @author cheny
 *   
 *  主要实现第三方支付通用接口
 * 
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Scope("prototype")
@Controller
public class PayController {

	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(PayController.class.getName());
	
	private JSONObject jsonObject = new JSONObject();
	@Autowired
	private  HttpServletRequest request;
	@Autowired
	private PayService payService;
	/**
	 * 获取银行卡列表
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/queryCards")
	@ResponseBody
	public Object queryCards(@RequestBody Map<String, Object> map) {
		 
		logger.info("queryCards start :" + map);
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
		Map returnMap = new HashMap();
		try{
		    returnMap = payService.queryBankcardList(map);
		    jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "获取电子钱包金额和银行卡列表成功");
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		jsonObject.put("data", returnMap);
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("获取电子钱包金额和银行卡列表成功报文=>" + jsonObject);
		return jsonObject;
		 

	}
	
	/**
	 * queryUsernoByCardno
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/queryUsernoByCardno")
	@ResponseBody
	public Object queryUsernoByCardno(@RequestBody Map<String, Object> map) {
		 
		logger.info("queryCards start :" + map);
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
		if (StringUtil.isEmpty(map.get("cardNo")) ||  StringUtil.isEmpty(map.get("cardNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[cardNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		Map returnMap = new HashMap();
		try{
		    returnMap = payService.queryUserNoByCardno(map);
		    jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "获取银行卡绑定的用户编号");
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		jsonObject.put("data", returnMap);
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("获取银行卡绑定的用户编号=>" + jsonObject);
		return jsonObject;
		 

	}
	/**
	 * 查询收费单位列表
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/queryUnits")
	@ResponseBody
	public Object queryUnits(@RequestBody Map<String, Object> map) {
		logger.info("queryUnits start :" + map);
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
		logger.info("请求收费单位列表:" + map);
		if (StringUtil.isEmpty(map.get("userNo")) ||  StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("feeType")) ||  StringUtil.isEmpty(map.get("feeType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[feeType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		Map returnMap = new HashMap();
		try{
		    returnMap = payService.queryUnits(map);
		    jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "获取收费单位列表");
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		jsonObject.put("data", returnMap);
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("获取收费单位列表成功报文=>" + jsonObject);
		return jsonObject;
		 
	}
	
	
	/**
	 * 银行卡和电子钱包支付接口
	 * 1、公共业务的判断
	 * 2、钱包和银行卡支付分开处理
	 * @return
	 */
	@RequestMapping(value = "/payFee")
	@ResponseBody
	public Object payFee(@RequestBody Map<String, Object> map){
		logger.info("payFee start :" + map);
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		// 加解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("银行卡和电子钱包支付接口请求报文:" + map);
		System.out.println("银行卡和电子钱包支付接口请求报文=>" + map);
		if (StringUtil.isEmpty(map.get("userNo")) || StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("payType")) || StringUtil.isEmpty(map.get("payType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("totalAmt")) || StringUtil.isEmpty(map.get("totalAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[totalAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("cardAmt")) || StringUtil.isEmpty(map.get("cardAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[cardAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("balanceAmt")) || StringUtil.isEmpty(map.get("balanceAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[balanceAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("orderId")) || StringUtil.isEmpty(map.get("orderId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[orderId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("feeType")) || StringUtil.isEmpty(map.get("feeType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[feeType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("unitNo")) || StringUtil.isEmpty(map.get("unitNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[cardAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if("01".equals(map.get("payType").toString().trim())){
			if (StringUtil.isEmpty(map.get("cardNo")) || StringUtil.isEmpty(map.get("cardNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[cardNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if (StringUtil.isEmpty(map.get("mobile")) || StringUtil.isEmpty(map.get("mobile").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[mobile]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if (StringUtil.isEmpty(map.get("bankNo")) || StringUtil.isEmpty(map.get("bankNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}else if(!"01".equals(map.get("bankNo").toString().trim())){
				jsonObject.put("resCode", "1003");
				jsonObject.put("resMsg", "参数不符[bankNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			
			if (StringUtil.isEmpty(map.get("channelNo")) || StringUtil.isEmpty(map.get("channelNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}else if(!"01".equals(map.get("channelNo").toString().trim())){
				jsonObject.put("resCode", "1003");
				jsonObject.put("resMsg", "参数不符[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			//&& !"05".equals(map.get("payType").toString().trim())
		}else if(!"02".equals(map.get("payType").toString().trim()) ){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		/*if("02".equals(map.get("payType").toString().trim()) ){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "暂不提供服务");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}*/
		 
		if (StringUtil.isEmpty(map.get("paySecurt")) || StringUtil.isEmpty(map.get("paySecurt").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("backUrl")) || StringUtil.isEmpty(map.get("backUrl").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[backUrl]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		/* String check = RedisUtil.setRedis(map.get("userNo").toString(), map.get("feeType").toString(), "test");
		 if("0".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统异常[redis]");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }else if("1".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统忙,请稍后再试");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }*/
		try{
			BigDecimal totalAmt  = new BigDecimal(map.get("totalAmt").toString());
			BigDecimal cardAmt  = new BigDecimal(map.get("cardAmt").toString());
			BigDecimal balanceAmt  = new BigDecimal(map.get("balanceAmt").toString());
			if(cardAmt.add(balanceAmt).compareTo(totalAmt) != 0){
				jsonObject.put("resCode", "1004");
				jsonObject.put("resMsg", "参数金额不匹配");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if("01".equals(map.get("payType").toString().trim())){
				if(cardAmt.compareTo(totalAmt) != 0){
					jsonObject.put("resCode", "1004");
					jsonObject.put("resMsg", "参数金额不匹配");
					return ReturnUtil.getPayReturn(jsonObject, key);
				}
			}else if("02".equals(map.get("payType").toString().trim())){
				if(balanceAmt.compareTo(totalAmt) != 0){
					jsonObject.put("resCode", "1004");
					jsonObject.put("resMsg", "参数金额不匹配");
					return ReturnUtil.getPayReturn(jsonObject, key);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "参数类型不符[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		try{
			jsonObject =  (JSONObject) payService.payFee(map);
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		if("9999".equals(jsonObject.get("resCode"))){
			logger.info("map----------"+map);
			try{
				//01 银行卡支付  02 银行卡支付
				if("01".equals(map.get("payType").toString().trim())){
					jsonObject = (JSONObject)  payService.cardMain(map,"2");
				}else if("02".equals(map.get("payType").toString().trim())){
					jsonObject = (JSONObject)  payService.balanceMain(map,"2");
				}
			}catch (Exception e) {
				e.printStackTrace();
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "系统异常");
			}
		} 
		jsonObject.put("data", "");
		return ReturnUtil.getPayReturn(jsonObject, key);
	}
	
	/**
	 * 电子钱包支付接口
	 * 1、公共业务的判断
	 * 2、钱包和银行卡支付分开处理
	 * T+2 模式
	 * @return
	 */
	@RequestMapping(value = "/balanceEduPayFee")
	@ResponseBody
	public Object balanceEduPayFee(@RequestBody Map<String, Object> map){
		logger.info("balanceEduPayFee start :" + map);
		//生成一个16位的随机数；
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		// 加解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		logger.info("电子钱包支付接口请求报文:" + map);
		System.out.println("电子钱包支付接口请求报文=>" + map);
		
		if (StringUtil.isEmpty(map.get("userNo")) || StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("payType")) || StringUtil.isEmpty(map.get("payType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("totalAmt")) || StringUtil.isEmpty(map.get("totalAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[totalAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("cardAmt")) || StringUtil.isEmpty(map.get("cardAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[cardAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("balanceAmt")) || StringUtil.isEmpty(map.get("balanceAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[balanceAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("orderId")) || StringUtil.isEmpty(map.get("orderId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[orderId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("feeType")) || StringUtil.isEmpty(map.get("feeType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[feeType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("unitNo")) || StringUtil.isEmpty(map.get("unitNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[cardAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if(!"02".equals(map.get("payType").toString().trim()) ){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		 
		if (StringUtil.isEmpty(map.get("paySecurt")) || StringUtil.isEmpty(map.get("paySecurt").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[paySecurt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("backUrl")) || StringUtil.isEmpty(map.get("backUrl").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[backUrl]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		try{
			BigDecimal totalAmt  = new BigDecimal(map.get("totalAmt").toString());
			BigDecimal balanceAmt  = new BigDecimal(map.get("balanceAmt").toString());
			if("02".equals(map.get("payType").toString().trim())){
				if(balanceAmt.compareTo(totalAmt) != 0){
					jsonObject.put("resCode", "1004");
					jsonObject.put("resMsg", "参数金额不匹配");
					return ReturnUtil.getPayReturn(jsonObject, key);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "参数类型不符[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		try{
			jsonObject =  (JSONObject) payService.payFeeEdu(map);
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		if("9999".equals(jsonObject.get("resCode"))){
			logger.info("map----------"+map);
			try{
				jsonObject = (JSONObject)  payService.balanceMainEdu(map,"1");
			}catch (Exception e) {
				e.printStackTrace();
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "系统异常");
			}
		} 
		jsonObject.put("data", "");
		return ReturnUtil.getPayReturn(jsonObject, key);
	}
	
	
	/**退款交易
	 * @return
	 * 全款部分都有，但是目前只能退一次
	 */
	@RequestMapping(value = "/backFee")
	@ResponseBody
	public Object backFee(@RequestBody Map<String, Object> map){
		logger.info("backFee start :" + map);
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		// 加解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("银行卡和电子钱包支付接口请求报文:" + map);
		System.out.println("银行卡和电子钱包支付接口请求报文=>" + map);
		if (StringUtil.isEmpty(map.get("userNo")) || StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("totalAmt")) || StringUtil.isEmpty(map.get("totalAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[totalAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("orderId")) || StringUtil.isEmpty(map.get("orderId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[orderId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("feeType")) || StringUtil.isEmpty(map.get("feeType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[feeType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("oldOrderId")) || StringUtil.isEmpty(map.get("oldOrderId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[oldOrderId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		//note-->hanshiwen：教育缴费、新农合缴费等余额支付不能退款
		List EduPayMxList = payService.queryEduPayMxByOrderId(map);
	    if (null != EduPayMxList && EduPayMxList.size() != 0){
		    jsonObject.put("resCode", "3006");
		    jsonObject.put("resMsg", "该缴费暂不支持退款"); 
		    return ReturnUtil.getPayReturn(jsonObject, key);
	    }
		if (StringUtil.isEmpty(map.get("oldPayerTime")) || StringUtil.isEmpty(map.get("oldPayerTime").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[oldPayerTime]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("backUrl")) || StringUtil.isEmpty(map.get("backUrl").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数不符[backUrl]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		 String check = RedisUtil.setRedis(map.get("userNo").toString()+map.get("oldOrderId"), "B01", "test");
		 if("0".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统异常[redis]");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }else if("1".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统忙,请稍后再试");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }
		try{
			BigDecimal totalAmt  = new BigDecimal(map.get("totalAmt").toString());
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "参数类型不符[totalAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		try{
		   jsonObject =  (JSONObject) payService.backFee(map);
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		if("9999".equals(jsonObject.get("resCode"))){
			logger.info("map----------"+map);
			try{	
			    if("01".equals(map.get("payType").toString().trim())){
					jsonObject = (JSONObject)  payService.backCardMain(map);
				}else if("02".equals(map.get("payType").toString().trim())){
					jsonObject = (JSONObject)  payService.backBalanceMain(map);
				}
			}catch (Exception e) {
				e.printStackTrace();
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "系统异常");
			}
		} 
		jsonObject.put("data", "");
		return ReturnUtil.getPayReturn(jsonObject, key);
	}
	/**
	 * 订单查询接口
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/OrderStatus")
	@ResponseBody
	public Object OrderStatus(@RequestBody Map<String, Object> map) {
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("OrderStatus start :" + map);
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
		if (StringUtil.isEmpty(map.get("orderId")) ||  StringUtil.isEmpty(map.get("orderId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[orderId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		try{
			jsonObject = (JSONObject)payService.OrderStatus(map);
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("获取明细状态报文=>" + jsonObject);
		return jsonObject;

	}
	
	/**
	 * 趣医网对账  QywDz
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/QywDz")
	@ResponseBody
	public Object QywDz(@RequestBody Map<String, Object> map) {
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("queryCards start :" + map);
		//解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "0001");
			jsonObject.put("resMsg", "解密失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("请求对账:" + map);
		if (StringUtil.isEmpty(map.get("dzDate")) ||  StringUtil.isEmpty(map.get("dzDate").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[dzDate]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (map.get("unitNo") == null) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[unitNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		List mxList = null;
		map.put("feeType", "10");
		map.put("startDate", map.get("dzDate").toString().trim()+" 00:00:00");
		map.put("endDate", map.get("dzDate").toString().trim()+" 23:59:59");
		if("".equals(map.get("unitNo").toString().trim())){
		    mxList = payService.queryDzAll(map);
		}else{
			mxList = payService.queryDzUnit(map);
		}
		
		if(mxList != null && mxList.size() > 0){
			String fileName = FileDzUtil.QYWReconLx(mxList, map.get("unitNo").toString().trim(), map.get("dzDate").toString().trim());
			if("".equals(fileName)){
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "系统异常");
				jsonObject.put("fileName", "");
			}else{
				jsonObject.put("resCode", "0000");
				jsonObject.put("resMsg", "对账成功");
				jsonObject.put("fileName", fileName);
			}
		}else{
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "文件为空");
			jsonObject.put("fileName", "");
		}
		jsonObject.put("data", "");
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("获取缴费明细成功报文=>" + jsonObject);
		return jsonObject;

	}
	
	
	/**
	 * 获取消费明细
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/OrderMxs")
	@ResponseBody
	public Object OrderMxs(@RequestBody Map<String, Object> map) {
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("queryCards start :" + map);
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
		if (StringUtil.isEmpty(map.get("feeType")) ||  StringUtil.isEmpty(map.get("feeType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[feeType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("startDate"))) {
			map.put("startDate","");
		}else{
			map.put("startDate", map.get("startDate").toString()+" 00:00:00");
		}
		if (StringUtil.isEmpty(map.get("endDate"))) {
			map.put("endDate","");
		}else{
			map.put("endDate", map.get("endDate").toString()+" 23:59:59");
		}
		Map returnMap = new HashMap();
		try{
			returnMap = payService.OrderMxs(map);
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "获取缴费明细成功");
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		jsonObject.put("data", returnMap);
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("获取缴费明细成功报文=>" + jsonObject);
		return jsonObject;

	}

	
	/**
	 * 银行卡和电子钱包支付接口
	 * 1、公共业务的判断
	 * 2、钱包和银行卡支付分开处理
	 * 3、金额先打款到第三方平台
	 * @return
	 */
	@RequestMapping(value = "/payTmallFee")
	@ResponseBody
	public Object payTmallFee(@RequestBody Map<String, Object> map){
		logger.info("payTmallFee start :" + map);
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		// 加解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("payTmallFee银行卡和电子钱包支付接口请求报文:" + map);
		System.out.println("payTmallFee银行卡和电子钱包支付接口请求报文=>" + map);
		if (StringUtil.isEmpty(map.get("userNo")) || StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("payType")) || StringUtil.isEmpty(map.get("payType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("totalAmt")) || StringUtil.isEmpty(map.get("totalAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[totalAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("cardAmt")) || StringUtil.isEmpty(map.get("cardAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[cardAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("balanceAmt")) || StringUtil.isEmpty(map.get("balanceAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[balanceAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("orderId")) || StringUtil.isEmpty(map.get("orderId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[orderId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("feeType")) || StringUtil.isEmpty(map.get("feeType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[feeType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("unitNo")) || StringUtil.isEmpty(map.get("unitNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[unitNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if("01".equals(map.get("payType").toString().trim())){
			if (StringUtil.isEmpty(map.get("cardNo")) || StringUtil.isEmpty(map.get("cardNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[cardNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if (StringUtil.isEmpty(map.get("mobile")) || StringUtil.isEmpty(map.get("mobile").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[mobile]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if (StringUtil.isEmpty(map.get("bankNo")) || StringUtil.isEmpty(map.get("bankNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}else if(!"01".equals(map.get("bankNo").toString().trim())){
				jsonObject.put("resCode", "1003");
				jsonObject.put("resMsg", "参数不符[bankNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			
			if (StringUtil.isEmpty(map.get("channelNo")) || StringUtil.isEmpty(map.get("channelNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}else if(!"01".equals(map.get("channelNo").toString().trim())){
				jsonObject.put("resCode", "1003");
				jsonObject.put("resMsg", "参数不符[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			//&& !"05".equals(map.get("payType").toString().trim())
		}else if(!"02".equals(map.get("payType").toString().trim()) ){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("paySecurt")) || StringUtil.isEmpty(map.get("paySecurt").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("backUrl")) || StringUtil.isEmpty(map.get("backUrl").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[backUrl]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("tmallMerchantId")) || StringUtil.isEmpty(map.get("tmallMerchantId").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[tmallMerchantId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		 String check = RedisUtil.setRedis(map.get("userNo").toString(), map.get("unitNo").toString()+map.get("feeType").toString(), "test");
		 if("0".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统异常[redis]");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }else if("1".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统忙,请稍后再试");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }
		try{
			BigDecimal totalAmt  = new BigDecimal(map.get("totalAmt").toString());
			BigDecimal cardAmt  = new BigDecimal(map.get("cardAmt").toString());
			BigDecimal balanceAmt  = new BigDecimal(map.get("balanceAmt").toString());
			if(cardAmt.add(balanceAmt).compareTo(totalAmt) != 0){
				jsonObject.put("resCode", "1004");
				jsonObject.put("resMsg", "参数金额不匹配");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if("01".equals(map.get("payType").toString().trim())){
				if(cardAmt.compareTo(totalAmt) != 0){
					jsonObject.put("resCode", "1004");
					jsonObject.put("resMsg", "参数金额不匹配");
					return ReturnUtil.getPayReturn(jsonObject, key);
				}
			}else if("02".equals(map.get("payType").toString().trim())){
				if(balanceAmt.compareTo(totalAmt) != 0){
					jsonObject.put("resCode", "1004");
					jsonObject.put("resMsg", "参数金额不匹配");
					return ReturnUtil.getPayReturn(jsonObject, key);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "参数类型不符[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		try{
			jsonObject =  (JSONObject) payService.payTmallFee(map);
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		if("9999".equals(jsonObject.get("resCode"))){
			logger.info("map----------"+map);
			try{
				//01 银行卡支付  02 银行卡支付
				if("01".equals(map.get("payType").toString().trim())){
					jsonObject = (JSONObject)  payService.cardMain(map,"1");
				}else if("02".equals(map.get("payType").toString().trim())){
					jsonObject = (JSONObject)  payService.balanceMain(map,"1");
				}
			}catch (Exception e) {
				e.printStackTrace();
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "系统异常");
			}
		} 
		jsonObject.put("data", "");
		return ReturnUtil.getPayReturn(jsonObject, key);
	}
	
	
	/**确认收货
	 */
	@RequestMapping(value = "/payConfirmFee")
	@ResponseBody
	public Object payConfirmFee(@RequestBody Map<String, Object> map){
		logger.info("payConfirmFee start :" + map);
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		// 加解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("payConfirmFee银行卡和电子钱包支付接口请求报文:" + map);
		System.out.println("payConfirmFee银行卡和电子钱包支付接口请求报文=>" + map);
		if (StringUtil.isEmpty(map.get("userNo")) || StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("totalAmt")) || StringUtil.isEmpty(map.get("totalAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[totalAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("orderId")) || StringUtil.isEmpty(map.get("orderId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[orderId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		 
		if (StringUtil.isEmpty(map.get("backUrl")) || StringUtil.isEmpty(map.get("backUrl").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数不符[backUrl]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		 
		if (StringUtil.isEmpty(map.get("paySecurt")) || StringUtil.isEmpty(map.get("paySecurt").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数不符[paySecurt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		 
		if (StringUtil.isEmpty(map.get("payType")) || StringUtil.isEmpty(map.get("payType").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数不符[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}else{
			if(!"1".equals(map.get("payType"))){
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数不符[payType],暂支持[1 确认付款]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
		}
		
		 String check = RedisUtil.setRedis(map.get("userNo").toString(), map.get("orderId").toString(), "test");
		 if("0".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统异常[redis]");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }else if("1".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统忙,请稍后再试");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }
		try{
			BigDecimal totalAmt  = new BigDecimal(map.get("totalAmt").toString());
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "参数类型不符[totalAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		try{
		   jsonObject =  (JSONObject) payService.confirmFee(map);
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		if("9999".equals(jsonObject.get("resCode")) || "9988".equals(jsonObject.get("resCode"))){
			logger.info("map----------"+map);
			try{	
				jsonObject = (JSONObject)  payService.confirmCardMain(map,jsonObject.get("resCode").toString());
			}catch (Exception e) {
				e.printStackTrace();
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "系统异常");
			}
		} 
		jsonObject.put("data", "");
		return ReturnUtil.getPayReturn(jsonObject, key);
	}
	 
	
	
 
	@RequestMapping(value = "/payBicycleFee")
	@ResponseBody
	public Object payBicycleFee(@RequestBody Map<String, Object> map){
		logger.info("payBicycleFee start :" + map);
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		// 加解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("payBicycleFee银行卡和电子钱包支付接口请求报文:" + map);
		System.out.println("payBicycleFee银行卡和电子钱包支付接口请求报文=>" + map);
		if (StringUtil.isEmpty(map.get("userNo")) || StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("payType")) || StringUtil.isEmpty(map.get("payType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("totalAmt")) || StringUtil.isEmpty(map.get("totalAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[totalAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("cardAmt")) || StringUtil.isEmpty(map.get("cardAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[cardAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("balanceAmt")) || StringUtil.isEmpty(map.get("balanceAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[balanceAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("orderId")) || StringUtil.isEmpty(map.get("orderId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[orderId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("feeType")) || StringUtil.isEmpty(map.get("feeType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[feeType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("unitNo")) || StringUtil.isEmpty(map.get("unitNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[cardAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if("01".equals(map.get("payType").toString().trim())){
			if (StringUtil.isEmpty(map.get("cardNo")) || StringUtil.isEmpty(map.get("cardNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[cardNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if (StringUtil.isEmpty(map.get("mobile")) || StringUtil.isEmpty(map.get("mobile").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[mobile]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if (StringUtil.isEmpty(map.get("bankNo")) || StringUtil.isEmpty(map.get("bankNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}else if(!"01".equals(map.get("bankNo").toString().trim())){
				jsonObject.put("resCode", "1003");
				jsonObject.put("resMsg", "参数不符[bankNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			
			if (StringUtil.isEmpty(map.get("channelNo")) || StringUtil.isEmpty(map.get("channelNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}else if(!"01".equals(map.get("channelNo").toString().trim())){
				jsonObject.put("resCode", "1003");
				jsonObject.put("resMsg", "参数不符[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			//&& !"05".equals(map.get("payType").toString().trim())
		}else if(!"02".equals(map.get("payType").toString().trim()) ){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("paySecurt")) || StringUtil.isEmpty(map.get("paySecurt").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("backUrl")) || StringUtil.isEmpty(map.get("backUrl").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[backUrl]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		 String check = RedisUtil.setRedis(map.get("userNo").toString(), map.get("feeType").toString(), "test");
		 if("0".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统异常[redis]");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }else if("1".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统忙,请稍后再试");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }
		try{
			BigDecimal totalAmt  = new BigDecimal(map.get("totalAmt").toString());
			BigDecimal cardAmt  = new BigDecimal(map.get("cardAmt").toString());
			BigDecimal balanceAmt  = new BigDecimal(map.get("balanceAmt").toString());
			if(cardAmt.add(balanceAmt).compareTo(totalAmt) != 0){
				jsonObject.put("resCode", "1004");
				jsonObject.put("resMsg", "参数金额不匹配");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if("01".equals(map.get("payType").toString().trim())){
				if(cardAmt.compareTo(totalAmt) != 0){
					jsonObject.put("resCode", "1004");
					jsonObject.put("resMsg", "参数金额不匹配");
					return ReturnUtil.getPayReturn(jsonObject, key);
				}
			}else if("02".equals(map.get("payType").toString().trim())){
				if(balanceAmt.compareTo(totalAmt) != 0){
					jsonObject.put("resCode", "1004");
					jsonObject.put("resMsg", "参数金额不匹配");
					return ReturnUtil.getPayReturn(jsonObject, key);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "参数类型不符[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		try{
			jsonObject =  (JSONObject) payService.payBicycleFee(map);
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		jsonObject.put("data", "");
		return ReturnUtil.getPayReturn(jsonObject, key);
	}
	
 
	@RequestMapping(value = "/backBicycleFee")
	@ResponseBody
	public Object backBicycleFee(@RequestBody Map<String, Object> map){
		logger.info("backBicycleFee start :" + map);
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		// 加解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("backBicycleFee银行卡和电子钱包支付接口请求报文:" + map);
		System.out.println("backBicycleFee银行卡和电子钱包支付接口请求报文=>" + map);
		if (StringUtil.isEmpty(map.get("userNo")) || StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("totalAmt")) || StringUtil.isEmpty(map.get("totalAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[totalAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("borrowDate")) || StringUtil.isEmpty(map.get("borrowDate").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[borrowDate]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("backDate")) || StringUtil.isEmpty(map.get("backDate").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[backDate]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("orderId")) || StringUtil.isEmpty(map.get("orderId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[orderId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		/*if (StringUtil.isEmpty(map.get("paySecurt")) || StringUtil.isEmpty(map.get("paySecurt").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}*/
		if (StringUtil.isEmpty(map.get("backUrl")) || StringUtil.isEmpty(map.get("backUrl").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[backUrl]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		 String check = RedisUtil.setRedis(map.get("userNo").toString(), map.get("orderId").toString(), "test");
		 if("0".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统异常[redis]");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }else if("1".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统忙,请稍后再试");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }
		try{
			BigDecimal totalAmt  = new BigDecimal(map.get("totalAmt").toString());
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "参数类型不符[totalAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		try{
			jsonObject =  (JSONObject) payService.backBicycleFee(map);
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		jsonObject.put("data", "");
		return ReturnUtil.getPayReturn(jsonObject, key);
	}
	
	@RequestMapping(value = "/queryBicycleFee")
	@ResponseBody
	public Object queryBicycleFee(@RequestBody Map<String, Object> map){
		logger.info("queryBicycleFee start :" + map);
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		// 加解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("queryBicycleFee银行卡和电子钱包支付接口请求报文:" + map);
		System.out.println("queryBicycleFee银行卡和电子钱包支付接口请求报文=>" + map);
		if (StringUtil.isEmpty(map.get("userNo")) || StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		 
		if (StringUtil.isEmpty(map.get("orderId")) || StringUtil.isEmpty(map.get("orderId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[orderId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		try{
			jsonObject =  (JSONObject) payService.queryBicycleFee(map);
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		jsonObject.put("data", "");
		return ReturnUtil.getPayReturn(jsonObject, key);
	}
	
	
	/**
	 *对账  CommonDz
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/CommonDz")
	@ResponseBody
	public Object CommonDz(@RequestBody Map<String, Object> map) {
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("queryCards start :" + map);
		//解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "0001");
			jsonObject.put("resMsg", "解密失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("请求对账:" + map);
		if (StringUtil.isEmpty(map.get("dzDate")) ||  StringUtil.isEmpty(map.get("dzDate").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[dzDate]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("feeType")) ||  StringUtil.isEmpty(map.get("feeType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[feeType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (map.get("unitNo") == null) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[unitNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		List mxList = null;
		
		map.put("startDate", map.get("dzDate").toString().trim()+" 00:00:00");
		map.put("endDate", map.get("dzDate").toString().trim()+" 23:59:59");
		if("".equals(map.get("unitNo").toString().trim())){
		    mxList = payService.queryDzAllFeetype(map);
		}else{
			mxList = payService.queryDzUnitFeetype(map);
		}
		
		if(mxList != null && mxList.size() > 0){
			String fileName = FileDzUtil.CommonReconLx(mxList, map.get("unitNo").toString().trim(), map.get("feeType").toString().trim(),map.get("dzDate").toString().trim());
			if("".equals(fileName)){
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "系统异常");
				jsonObject.put("fileName", "");
			}else{
				jsonObject.put("resCode", "0000");
				jsonObject.put("resMsg", "对账成功");
				jsonObject.put("fileName", fileName);
			}
		}else{
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "文件为空");
			jsonObject.put("fileName", "");
		}
		jsonObject.put("data", "");
		jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObject, key);
		logger.info("获取缴费明细成功报文=>" + jsonObject);
		return jsonObject;

	}
	
	
	/**
	 * 银行卡转账接口，通过新农保缴费实现
	 * 新农保缴费将个人卡转账至存折账号，通过存折账号转账到社保对公账号
	 * @return
	 * 
	 *  inCardNo	String	收款人卡号
		inCardName	String	收款人户名
		inSocialNo	String	社保卡号
		inCardId	String	收款人身份证号码
		inCardMobile	String	收款人电话号码

	 */
	@RequestMapping(value = "/payXnbFee")
	@ResponseBody
	public Object payXnbFee(@RequestBody Map<String, Object> map){
		logger.info("payXnbFee start :" + map);
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		// 加解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("银行卡和电子钱包payXnbFee支付接口请求报文:" + map);
		System.out.println("银行卡和电子钱包支付接口请求报文=>" + map);
		if (StringUtil.isEmpty(map.get("userNo")) || StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("payType")) || StringUtil.isEmpty(map.get("payType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("totalAmt")) || StringUtil.isEmpty(map.get("totalAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[totalAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("cardAmt")) || StringUtil.isEmpty(map.get("cardAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[cardAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("balanceAmt")) || StringUtil.isEmpty(map.get("balanceAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[balanceAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("orderId")) || StringUtil.isEmpty(map.get("orderId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[orderId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("feeType")) || StringUtil.isEmpty(map.get("feeType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[feeType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		if (StringUtil.isEmpty(map.get("inCardNo")) || StringUtil.isEmpty(map.get("inCardNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[inCardNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("inCardName")) || StringUtil.isEmpty(map.get("inCardName").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[inCardName]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		/*if (StringUtil.isEmpty(map.get("inSocialNo")) || StringUtil.isEmpty(map.get("inSocialNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[inSocialNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}*/
		if (StringUtil.isEmpty(map.get("inIdNumber")) || StringUtil.isEmpty(map.get("inIdNumber").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[inIdNumber]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		/*if (StringUtil.isEmpty(map.get("inCardMobile")) || StringUtil.isEmpty(map.get("inCardMobile").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[inCardMobile]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}*/
		
		//可传空，若不为空，则需要调用进行查询验证feetype和unitno是不是同一类型
		if (StringUtil.isEmpty(map.get("unitNo")) || StringUtil.isEmpty(map.get("unitNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[unitNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if("01".equals(map.get("payType").toString().trim())){
			if (StringUtil.isEmpty(map.get("cardNo")) || StringUtil.isEmpty(map.get("cardNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[cardNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if (StringUtil.isEmpty(map.get("mobile")) || StringUtil.isEmpty(map.get("mobile").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[mobile]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if (StringUtil.isEmpty(map.get("bankNo")) || StringUtil.isEmpty(map.get("bankNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}else if(!"01".equals(map.get("bankNo").toString().trim())){
				jsonObject.put("resCode", "1003");
				jsonObject.put("resMsg", "参数不符[bankNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			
			if (StringUtil.isEmpty(map.get("channelNo")) || StringUtil.isEmpty(map.get("channelNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}else if(!"01".equals(map.get("channelNo").toString().trim())){
				jsonObject.put("resCode", "1003");
				jsonObject.put("resMsg", "参数不符[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			//&& !"05".equals(map.get("payType").toString().trim())
		}else {
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		/*if("02".equals(map.get("payType").toString().trim()) ){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "暂不提供服务");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}*/
		 
		if (StringUtil.isEmpty(map.get("paySecurt")) || StringUtil.isEmpty(map.get("paySecurt").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("backUrl")) || StringUtil.isEmpty(map.get("backUrl").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[backUrl]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		/* String check = RedisUtil.setRedis(map.get("userNo").toString(), map.get("feeType").toString(), "test");
		 if("0".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统异常[redis]");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }else if("1".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统忙,请稍后再试");
			 return ReturnUtil.getPayReturn(jsonObject, key);
		 }*/
		try{
			BigDecimal totalAmt  = new BigDecimal(map.get("totalAmt").toString());
			BigDecimal cardAmt  = new BigDecimal(map.get("cardAmt").toString());
			BigDecimal balanceAmt  = new BigDecimal(map.get("balanceAmt").toString());
			if(cardAmt.add(balanceAmt).compareTo(totalAmt) != 0){
				jsonObject.put("resCode", "1004");
				jsonObject.put("resMsg", "参数金额不匹配");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if("01".equals(map.get("payType").toString().trim())){
				if(cardAmt.compareTo(totalAmt) != 0){
					jsonObject.put("resCode", "1004");
					jsonObject.put("resMsg", "参数金额不匹配");
					return ReturnUtil.getPayReturn(jsonObject, key);
				}
			}else if("02".equals(map.get("payType").toString().trim())){
				if(balanceAmt.compareTo(totalAmt) != 0){
					jsonObject.put("resCode", "1004");
					jsonObject.put("resMsg", "参数金额不匹配");
					return ReturnUtil.getPayReturn(jsonObject, key);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "参数类型不符[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		try{
			jsonObject =  (JSONObject) payService.payXnbFee(map);
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		System.out.println("XnbShortConnection==="+map);
		if("0000".equals(jsonObject.get("resCode")) && !"".equals(map.get("unitNo").toString().trim())){
			logger.info("map----------"+map);
			//启动一个线程调用新农保缴费20170120 注释掉
//			new XnbShortConnection(map, payService).start();
		} 
		jsonObject.put("data", "");
		return ReturnUtil.getPayReturn(jsonObject, key);
	}
	
	/**
	 * taxi支付接口
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/payTaxi2Fee")
	@ResponseBody
	public Object payTaxi2Fee(@RequestBody Map<String, Object> map){
		logger.info("taxi payFee start ............" + map);
		String key = RandomUtil.generateNumber(16);
		boolean ipflag = GetRemoteIp.checkIp(request);
		if(!ipflag){
			jsonObject.put("resCode", "0002");
			jsonObject.put("resMsg", "请求非法");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		// 加解密处理
		map = SecurtUtil.decrypt(map);
		if(null == map){
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		logger.info("出租车缴费请求报文 ====》" + map);
		if (StringUtil.isEmpty(map.get("userNo")) || StringUtil.isEmpty(map.get("userNo").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[userNo]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		if (StringUtil.isEmpty(map.get("taxiCert")) || StringUtil.isEmpty(map.get("taxiCert").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[taxiCert]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		if (StringUtil.isEmpty(map.get("taxiPhone")) || StringUtil.isEmpty(map.get("taxiPhone").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[taxiPhone]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		} 
		
		//根据司机手机号码获取司机userno
		String taxiUserNo = payService.getTaxiUserNoByPhone(map.get("taxiPhone").toString());
		logger.info("taxiUserNo =====> " + taxiUserNo);
		if(taxiUserNo == null || "".equals(taxiUserNo) || "null".equals(taxiUserNo)){
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "该司机暂不支持线上支付，请进行线下支付");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		map.put("taxiUserNo", taxiUserNo);  //将taxiUserNo放入map中
		
		if (StringUtil.isEmpty(map.get("payType")) || StringUtil.isEmpty(map.get("payType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		if (StringUtil.isEmpty(map.get("totalAmt")) || StringUtil.isEmpty(map.get("totalAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[totalAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		if (StringUtil.isEmpty(map.get("cardAmt")) || StringUtil.isEmpty(map.get("cardAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[cardAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		if (StringUtil.isEmpty(map.get("balanceAmt")) || StringUtil.isEmpty(map.get("balanceAmt").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[balanceAmt]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		if (StringUtil.isEmpty(map.get("orderId")) || StringUtil.isEmpty(map.get("orderId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[orderId]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		if (StringUtil.isEmpty(map.get("feeType")) || StringUtil.isEmpty(map.get("feeType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[feeType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		 
		//01银行卡支付
		if("01".equals(map.get("payType").toString().trim())){
			//验证银行卡号
			if (StringUtil.isEmpty(map.get("cardNo")) || StringUtil.isEmpty(map.get("cardNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[cardNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			//验证手机号
			if (StringUtil.isEmpty(map.get("mobile")) || StringUtil.isEmpty(map.get("mobile").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[mobile]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			//验证银行no
			if (StringUtil.isEmpty(map.get("bankNo")) || StringUtil.isEmpty(map.get("bankNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}else if(!"01".equals(map.get("bankNo").toString().trim())){
				jsonObject.put("resCode", "1003");
				jsonObject.put("resMsg", "参数不符[bankNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			//验证交易渠道  默认 01
			if (StringUtil.isEmpty(map.get("channelNo")) || StringUtil.isEmpty(map.get("channelNo").toString().trim())) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "参数缺失[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}else if(!"01".equals(map.get("channelNo").toString().trim())){
				jsonObject.put("resCode", "1003");
				jsonObject.put("resMsg", "参数不符[channelNo]");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			
			//如果不是余额支付 02
		}else if(!"02".equals(map.get("payType").toString().trim()) ){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
			// 支付密码
		if (StringUtil.isEmpty(map.get("paySecurt")) || StringUtil.isEmpty(map.get("paySecurt").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[payType]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
			//验证回调地址
		if (StringUtil.isEmpty(map.get("backUrl")) || StringUtil.isEmpty(map.get("backUrl").toString().trim()) ) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[backUrl]");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		try{
			BigDecimal totalAmt  = new BigDecimal(map.get("totalAmt").toString());
			BigDecimal cardAmt  = new BigDecimal(map.get("cardAmt").toString());
			BigDecimal balanceAmt  = new BigDecimal(map.get("balanceAmt").toString());
			if(cardAmt.add(balanceAmt).compareTo(totalAmt) != 0){
				jsonObject.put("resCode", "1004");
				jsonObject.put("resMsg", "参数金额不匹配");
				return ReturnUtil.getPayReturn(jsonObject, key);
			}
			if("01".equals(map.get("payType").toString().trim())){
				if(cardAmt.compareTo(totalAmt) != 0){
					jsonObject.put("resCode", "1004");
					jsonObject.put("resMsg", "参数金额不匹配");
					return ReturnUtil.getPayReturn(jsonObject, key);
				}
			}else if("02".equals(map.get("payType").toString().trim())){
				if(balanceAmt.compareTo(totalAmt) != 0){
					jsonObject.put("resCode", "1004");
					jsonObject.put("resMsg", "参数金额不匹配");
					return ReturnUtil.getPayReturn(jsonObject, key);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "参数类型不符");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		try{
			jsonObject =  (JSONObject) payService.payFee(map);
		}catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
		}
		if("9999".equals(jsonObject.get("resCode"))){
			logger.info("map----------"+map);
			try{
				//01 银行卡支付  02 银行卡支付
				if("01".equals(map.get("payType").toString().trim())){
					jsonObject = (JSONObject)  payService.cardMainForTaxi(map,"1");
				}else if("02".equals(map.get("payType").toString().trim())){
					jsonObject = (JSONObject)  payService.balanceMainForTaxi(map,"1");
				}
			}catch (Exception e) {
				e.printStackTrace();
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "系统异常");
			}
		}
		logger.info("支付返回状态====》" + jsonObject.get("resCode"));
		if("0000".equals(jsonObject.get("resCode"))) {
			//将钱加入司机的余额中
			try{
				jsonObject = (JSONObject)  payService.addMoneyForTaxi(map);
				if("0000".equals(jsonObject.get("resCode"))){
					logger.info("-------》进行返现操作");
					//返现给用户
					payService.cashBackTaxi(map);
				} else{
					jsonObject.put("resCode", "40007");
					jsonObject.put("resMsg", "司机收款异常");
				}
				logger.info("--------》进行回调操作");
				//回调
				payService.callBack(map);
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "司机收款失败");
			}
		}
		
		jsonObject.put("data", "");
		logger.info("出租车缴费最终返回的报文=====》　" + jsonObject.toString());
		return ReturnUtil.getPayReturn(jsonObject, key);
	}
	
	
}
