package com.untech.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;


public class PayUtil_YDYH {
	
	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(PayUtil_YDYH.class.getName());
	
	//退费方法
	public  Map<String,String>  backMoney(Map<String,String> map ){
		/**
		 * 转账报文,调用中台(中间账号转账至商户账号)
		 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|原转账流水号(唯一)|备注|加密串|
		 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
		 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
		 */
		Map<String,String>  returnMap = new LinkedHashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("0009|WJBZ|" + map.get("orderId").toString() + "|");
		sb.append(map.get("outCardNo").toString() + "|"+map.get("outCardName").toString() + "|");
		sb.append(map.get("inCardNo").toString() + "|"+map.get("inCardName").toString() + "|");
		sb.append(map.get("totalAmt").toString()+"|");
		sb.append(map.get("oldOrderId").toString()+"|"+(map.get("remark")==null?"":map.get("remark").toString())+"|");
		logger.info("退费方法请求==="+sb.toString());
		SocketClient client = new SocketClient();
		String returnMsg = client.sendMessage(sb.toString());
		/** returnMsg测试 **/
		//String returnMsg = "0000|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|20150101|05341234|20150102|05351235|111|备注|加密串|";
		logger.info("退费方法返回==="+returnMsg);
		if (returnMsg == null || "".equals(returnMsg.trim()) || returnMsg.length() == 4) {
			Map<String,String> queryMap  = queryMoney(map);
			 if("0000".equals(queryMap.get("flag"))){
				 if(!"0".equals(queryMap.get("status"))){
					 returnMap.put("flag", "0001");
					 returnMap.put("errorCode", "1605");
					 returnMap.put("errorMessage", "交易失败");
					 returnMap.put("errorDetail", "交易失败");
				 }else {
					 returnMap.put("flag", "0000"); 
					 returnMap.put("middleflow1", returnMap.get("middleflow1")); 
					 returnMap.put("middledate1", returnMap.get("middledate1")); 
					 returnMap.put("coreflow1", returnMap.get("coreflow1")); 
					 returnMap.put("coredate1", returnMap.get("coredate1")); 
				 }
			}else{
				 returnMap.put("flag", "9999");
				 returnMap.put("errorCode", "9999");
				 returnMap.put("errorMessage", "系统异常");
				 returnMap.put("errorDetail", "系统异常");
		    }
			 return returnMap;
		}else{
			String[] response = returnMsg.split("\\|");
			if ("0000".equals(response[0])) {
				returnMap.put("flag", "0000");
				returnMap.put("resCode", response[0]);
				returnMap.put("middleflow1", response[11]);
				returnMap.put("middledate1", response[10]);
				returnMap.put("coreflow1", response[13]);
				returnMap.put("coredate1", response[12]);
				return returnMap;
			}else if ("8888".equals(response[0])) {
				returnMap.put("flag", "8888");
				returnMap.put("resCode", "4008");
				returnMap.put("resMsg", "报文被篡改");
				returnMap.put("errorMessage", "报文被篡改");
				return returnMap;
			} else {
				returnMap.put("flag", "0001");
				returnMap.put("resCode", response[0]);
				returnMap.put("resMsg", response[1]);
				returnMap.put("errorMessage",  response[1]);
				return returnMap;
			}
		}
		
	}
	
	
	//转账接口
	public  Map<String,String>  payMoney(Map<String,String> map ){
		/**
		 * 转账报文,调用中台(中间账号转账至商户账号)
		 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
		 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
		 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
		 */
		Map<String,String>  returnMap = new LinkedHashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("0002|WJBZ|" + map.get("orderId").toString() + "|");
		sb.append(map.get("outCardNo").toString() + "|"+map.get("outCardName").toString() + "|");
		sb.append(map.get("inCardNo").toString() + "|"+map.get("inCardName").toString() + "|");
		sb.append(map.get("totalAmt").toString()+"|");
		sb.append((map.get("remark")==null?"":map.get("remark").toString())+"|");
		logger.info("转账方法请求==="+sb.toString());
		SocketClient client = new SocketClient();
		String returnMsg = client.sendMessage(sb.toString());
		/** returnMsg测试 **/
		//String returnMsg = "0000|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|20150101|05341234|20150102|05351235|备注|加密串|";
		logger.info("转账方法响应==="+returnMsg);
		if (returnMsg == null || "".equals(returnMsg.trim()) || returnMsg.length() == 4) {
			 Map<String,String> queryMap  = queryMoney(map);
			 if("0000".equals(queryMap.get("flag"))){
				 if(!"0".equals(queryMap.get("status"))){
					 returnMap.put("flag", "0001");
					 returnMap.put("errorCode", "1605");
					 returnMap.put("errorMessage", "交易失败");
					 returnMap.put("errorDetail", "交易失败");
				 }else {
					 returnMap.put("flag", "0000"); 
					 returnMap.put("middleflow1", returnMap.get("middleflow1")); 
					 returnMap.put("middledate1", returnMap.get("middledate1")); 
					 returnMap.put("coreflow1", returnMap.get("coreflow1")); 
					 returnMap.put("coredate1", returnMap.get("coredate1")); 
				 }
			}else{
				 returnMap.put("flag", "9999");
				 returnMap.put("errorCode", "9999");
				 returnMap.put("errorMessage", "系统异常");
				 returnMap.put("errorDetail", "系统异常");
		    }
			 return returnMap;
		}else{
			String[] response = returnMsg.split("\\|");
			if ("0000".equals(response[0])) {
				returnMap.put("flag", "0000");
				returnMap.put("middleflow1", response[11]);
				returnMap.put("middledate1", response[10]);
				returnMap.put("coreflow1", response[13]);
				returnMap.put("coredate1", response[12]);
				return returnMap;
			}else if ("8888".equals(response[0])) {
				returnMap.put("flag", "8888");
				returnMap.put("resCode", "4008");
				returnMap.put("resMsg", "报文被篡改");
				returnMap.put("errorMessage","报文被篡改");
				return returnMap;
			} else {
				returnMap.put("flag", "0001");
				returnMap.put("resCode", "4009");
				returnMap.put("resMsg", response[1]);
				returnMap.put("errorMessage", response[1]);
				return returnMap;
			}
		}
		
	}
	
	
	//冲正
	public  Map<String,String>  InValidMoney(Map<String,String> map ){
		/**
		 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
		 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|
		 *  中间业务日期|中间业务流水号|核心日期|核心流水号|备注|加密串|
		 */
		Map<String,String>  returnMap = new LinkedHashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("0008|WJBZ|" + map.get("orderId").toString() + "|");
		sb.append(map.get("outCardNo").toString() + "|"+map.get("outCardName").toString() + "|");
		sb.append(map.get("inCardNo").toString() + "|"+map.get("inCardName").toString() + "|");
		sb.append(map.get("totalAmt").toString()+"|");
		sb.append((map.get("remark")==null?"":map.get("remark").toString())+"|");
		logger.info("转账方法请求==="+sb.toString());
		SocketClient client = new SocketClient();
		String returnMsg = client.sendMessage(sb.toString());
		/** returnMsg测试 **/
		//String returnMsg = "0000|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|20150101|05341234|20150102|05351235|备注|加密串|";
		logger.info("转账方法响应==="+returnMsg);
		if (returnMsg == null || "".equals(returnMsg.trim()) || returnMsg.length() == 4) {
			Map<String,String> queryMap  = queryMoney(map);
			 if("0000".equals(queryMap.get("flag"))){
				 if(!"0".equals(queryMap.get("status"))){
					 returnMap.put("flag", "0001");
					 returnMap.put("errorCode", "1605");
					 returnMap.put("errorMessage", "交易失败");
					 returnMap.put("errorDetail", "交易失败");
				 }else {
					 returnMap.put("flag", "0000"); 
					 returnMap.put("middleflow1", returnMap.get("middleflow1")); 
					 returnMap.put("middledate1", returnMap.get("middledate1")); 
					 returnMap.put("coreflow1", returnMap.get("coreflow1")); 
					 returnMap.put("coredate1", returnMap.get("coredate1")); 
				 }
			}else{
				 returnMap.put("flag", "9999");
				 returnMap.put("errorCode", "9999");
				 returnMap.put("errorMessage", "系统异常");
				 returnMap.put("errorDetail", "系统异常");
		    }
			return returnMap;
		}else{
			String[] response = returnMsg.split("\\|");
			if ("0000".equals(response[0])) {
				returnMap.put("flag", "0000");
				returnMap.put("middleflow1", response[11]);
				returnMap.put("middledate1", response[10]);
				returnMap.put("coreflow1", response[13]);
				returnMap.put("coredate1", response[12]);
				return returnMap;
			}else if ("8888".equals(response[0])) {
				returnMap.put("flag", "8888");
				returnMap.put("resCode", "4008");
				returnMap.put("resMsg", "报文被篡改");
				return returnMap;
			} else {
				returnMap.put("flag", "0001");
				returnMap.put("resCode", "4009");
				returnMap.put("resMsg", response[1]);
				return returnMap;
			}
		}
		
	}
	
	//查询
	public  Map<String,String>  queryMoney(Map<String,String> map ){
		/**
		 * 上送报文：交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
		 * 下送报文：返回码|返回信息|状态（0 成功 1失败 2冲正）|交易类型|系统名称|转账流水号|转出账号|转出户名|
		 *  转入账号|转入户名|转账金额|中间业务日期|中间业务流水号|核心日期|核心流水号|备注|加密串|
		 */
		Map<String,String>  returnMap = new LinkedHashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("0003|WJBZ|" + map.get("orderId").toString() + "|");
		sb.append(map.get("outCardNo").toString() + "|"+map.get("outCardName").toString() + "|");
		sb.append(map.get("inCardNo").toString() + "|"+map.get("inCardName").toString() + "|");
		sb.append(map.get("totalAmt").toString()+"|");
		sb.append((map.get("remark")==null?"":map.get("remark").toString())+"|");
		logger.info("转账方法请求==="+sb.toString());
		SocketClient client = new SocketClient();
		String returnMsg = client.sendMessage(sb.toString());
		/** returnMsg测试 **///（0 成功 1失败 2冲正）|
		//String returnMsg = "0000|返回信息|0|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|20150101|05341234|20150102|05351235|备注|加密串|";
		logger.info("转账方法响应==="+returnMsg);
		if (returnMsg == null || "".equals(returnMsg.trim()) || returnMsg.length() == 4) {
			returnMap.put("flag", "9999");
			returnMap.put("resCode", "4001");
			returnMap.put("resMsg", "系统异常");
			return returnMap;
		}else{
			String[] response = returnMsg.split("\\|");
			if ("0000".equals(response[0])) {
				returnMap.put("flag", "0000");
				returnMap.put("status", response[2]);
				returnMap.put("middleflow1", response[12]);
				returnMap.put("middledate1", response[11]);
				returnMap.put("coreflow1", response[14]);
				returnMap.put("coredate1", response[13]);
				return returnMap;
			}else if ("8888".equals(response[0])) {
				returnMap.put("flag", "8888");
				returnMap.put("resCode", "4008");
				returnMap.put("resMsg", "报文被篡改");
				return returnMap;
			} else {
				returnMap.put("flag", "0001");
				returnMap.put("resCode", "4009");
				returnMap.put("resMsg", response[1]);
				return returnMap;
			}
		}
		
	}
	
	
	//余额查询
	public  Map<String,String>  queryBalance(Map<String,String> map ){
		/**
		 * StringBuffer buffer = new StringBuffer("0007|WJBZ|"+commer.getAcctno()+"|"+commer.getAcctname()+"|");
		SocketClient client = new SocketClient();
		String returnStr  = client.sendMessage(buffer.toString());
		 */
		Map<String,String>  returnMap = new LinkedHashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("0007|WJBZ|" + map.get("outCardNo").toString() + "|");
		sb.append(map.get("outCardName").toString() + "|");
		logger.info("余额查询方法请求==="+sb.toString());
		SocketClient client = new SocketClient();
		String returnMsg = client.sendMessage(sb.toString());
		//String returnMsg = "0000|返回信息|0|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|20150101|05341234|20150102|05351235|备注|加密串|";
		logger.info("转账方法响应==="+returnMsg);
		if (returnMsg == null || "".equals(returnMsg.trim()) || returnMsg.length() == 4) {
			returnMap.put("flag", "9999");
			returnMap.put("resCode", "4001");
			returnMap.put("resMsg", "系统异常");
			return returnMap;
		}else{
			String[] response = returnMsg.split("\\|");
			if ("0000".equals(response[0])) {
				returnMap.put("flag", "0000");
				returnMap.put("resCode", "0000");
				returnMap.put("resMsg", "余额查询成功");
				returnMap.put("balance", new BigDecimal(response[7])+"");
				System.out.println(response[7]);
				System.out.println(new BigDecimal(response[7]));
				return returnMap;
			}else if ("8888".equals(response[0])) {
				returnMap.put("flag", "8888");
				returnMap.put("resCode", "4008");
				returnMap.put("resMsg", "报文被篡改");
				return returnMap;
			} else {
				returnMap.put("flag", "0001");
				returnMap.put("resCode", "4009");
				returnMap.put("resMsg", response[1]);
				return returnMap;
			}
		}
		
	}
	
	
	//转账接口，对私到对私
	public  Map<String,String>  payMoney0016(Map<String,String> map ){
		/**
		 * 转账报文,调用中台(中间账号转账至商户账号)
		 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
		 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
		 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
		 */
		Map<String,String>  returnMap = new LinkedHashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("0016|WJBZ|" + map.get("orderId").toString() + "|");
		sb.append(map.get("outCardNo").toString() + "|"+map.get("outCardName").toString() + "|");
		sb.append(map.get("inCardNo").toString() + "|"+map.get("inCardName").toString() + "|");
		sb.append(map.get("totalAmt").toString()+"|");
		sb.append((map.get("remark")==null?"":map.get("remark").toString())+"|");
		logger.info("转账方法请求==="+sb.toString());
		SocketClient client = new SocketClient();
		String returnMsg = client.sendMessage(sb.toString());
		/** returnMsg测试 **/
		//String returnMsg = "0000|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|20150101|05341234|20150102|05351235|备注|加密串|";
		logger.info("转账方法响应==="+returnMsg);
		if (returnMsg == null || "".equals(returnMsg.trim()) || returnMsg.length() == 4) {
			 Map<String,String> queryMap  = queryMoney(map);
			 if("0000".equals(queryMap.get("flag"))){
				 if(!"0".equals(queryMap.get("status"))){
					 returnMap.put("flag", "0001");
					 returnMap.put("errorCode", "1605");
					 returnMap.put("errorMessage", "交易失败");
					 returnMap.put("errorDetail", "交易失败");
				 }else {
					 returnMap.put("flag", "0000"); 
					 returnMap.put("middleflow1", returnMap.get("middleflow1")); 
					 returnMap.put("middledate1", returnMap.get("middledate1")); 
					 returnMap.put("coreflow1", returnMap.get("coreflow1")); 
					 returnMap.put("coredate1", returnMap.get("coredate1")); 
				 }
			}else{
				 returnMap.put("flag", "9999");
				 returnMap.put("errorCode", "9999");
				 returnMap.put("errorMessage", "系统异常");
				 returnMap.put("errorDetail", "系统异常");
		    }
			 return returnMap;
		}else{
			String[] response = returnMsg.split("\\|");
			if ("0000".equals(response[0])) {
				returnMap.put("flag", "0000");
				returnMap.put("middleflow1", response[11]);
				returnMap.put("middledate1", response[10]);
				returnMap.put("coreflow1", response[13]);
				returnMap.put("coredate1", response[12]);
				return returnMap;
			}else if ("8888".equals(response[0])) {
				returnMap.put("flag", "8888");
				returnMap.put("resCode", "4008");
				returnMap.put("resMsg", "报文被篡改");
				returnMap.put("errorMessage","报文被篡改");
				return returnMap;
			} else {
				returnMap.put("flag", "0001");
				returnMap.put("resCode", "4009");
				returnMap.put("resMsg", response[1]);
				returnMap.put("errorMessage", response[1]);
				return returnMap;
			}
		}
		
	}
	
	//转账接口，对私到对公
	public  Map<String,String>  payMoney0017(Map<String,String> map ){
		/**
		 * 转账报文,调用中台(中间账号转账至商户账号)
		 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
		 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
		 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
		 */
		Map<String,String>  returnMap = new LinkedHashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("0017|WJBZ|" + map.get("orderId").toString() + "|");
		sb.append(map.get("outCardNo").toString() + "|"+map.get("outCardName").toString() + "|");
		sb.append(map.get("inCardNo").toString() + "|"+map.get("inCardName").toString() + "|");
		sb.append(map.get("totalAmt").toString()+"|");
		sb.append((map.get("remark")==null?"":map.get("remark").toString())+"|");
		logger.info("转账方法请求==="+sb.toString());
		SocketClient client = new SocketClient();
		String returnMsg = client.sendMessage(sb.toString());
		/** returnMsg测试 **/
		//String returnMsg = "0000|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|20150101|05341234|20150102|05351235|备注|加密串|";
		logger.info("转账方法响应==="+returnMsg);
		if (returnMsg == null || "".equals(returnMsg.trim()) || returnMsg.length() == 4) {
			 Map<String,String> queryMap  = queryMoney(map);
			 if("0000".equals(queryMap.get("flag"))){
				 if(!"0".equals(queryMap.get("status"))){
					 returnMap.put("flag", "0001");
					 returnMap.put("errorCode", "1605");
					 returnMap.put("errorMessage", "交易失败");
					 returnMap.put("errorDetail", "交易失败");
				 }else {
					 returnMap.put("flag", "0000"); 
					 returnMap.put("middleflow1", returnMap.get("middleflow1")); 
					 returnMap.put("middledate1", returnMap.get("middledate1")); 
					 returnMap.put("coreflow1", returnMap.get("coreflow1")); 
					 returnMap.put("coredate1", returnMap.get("coredate1")); 
				 }
			}else{
				 returnMap.put("flag", "9999");
				 returnMap.put("errorCode", "9999");
				 returnMap.put("errorMessage", "系统异常");
				 returnMap.put("errorDetail", "系统异常");
		    }
			 return returnMap;
		}else{
			String[] response = returnMsg.split("\\|");
			if ("0000".equals(response[0])) {
				returnMap.put("flag", "0000");
				returnMap.put("middleflow1", response[11]);
				returnMap.put("middledate1", response[10]);
				returnMap.put("coreflow1", response[13]);
				returnMap.put("coredate1", response[12]);
				return returnMap;
			}else if ("1118".equals(response[0])) {
				returnMap.put("flag", "1118");
				returnMap.put("resCode", "1118");
				returnMap.put("resMsg", "1118");
				returnMap.put("errorMessage","1118");
				return returnMap;
			}else if ("8888".equals(response[0])) {
				returnMap.put("flag", "8888");
				returnMap.put("resCode", "4008");
				returnMap.put("resMsg", "报文被篡改");
				returnMap.put("errorMessage","报文被篡改");
				return returnMap;
			} else {
				returnMap.put("flag", "0001");
				returnMap.put("resCode", "4009");
				returnMap.put("resMsg", response[1]);
				returnMap.put("errorMessage", response[1]);
				return returnMap;
			}
		}
		
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		/**
		 * 转账报文,调用中台(中间账号转账至商户账号)
		 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|原转账流水号(唯一)|备注|加密串|
		 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
		 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
		 * 
		 * 0016|WJBZ|BSDT20170110151611211163|6229538108106360895|朱家珀|10031679554410000000024|朱家珀|3000|342124196104053718|
		   0016|WJBZ|BSDT20170110151927773340|6229538108106360895|朱家珀|10031679554410000000024|朱家珀|3000|342124196104053718|
		   0016|WJBZ|BSDT20170110160956686066|6229538108106360895|朱家珀|10031679554410000000024|朱家珀|3000|342124196104053718|
		 */
		Map<String,String>  map = new LinkedHashMap<String,String>();
		/*map.put("orderId", "BBSDT20170110151611211163");
		map.put("outCardNo", "10031679554410000000024");
		map.put("outCardName", "朱家珀");
		map.put("inCardNo", "6229538108106360895");
		map.put("inCardName", "朱家珀");
		map.put("totalAmt", "3000");
		map.put("oldOrderId", "BSDT20170110151611211163");
		map.put("remark", "342124196104053718");*/
		
		
		/*map.put("orderId", "BBSDT20170110151927773340");
		map.put("outCardNo", "10031679554410000000024");
		map.put("outCardName", "朱家珀");
		map.put("inCardNo", "6229538108106360895");
		map.put("inCardName", "朱家珀");
		map.put("totalAmt", "3000");
		map.put("oldOrderId", "BSDT20170110151927773340");
		map.put("remark", "342124196104053718");*/
		
		/*map.put("orderId", "BBSDT20170110160956686066");
		map.put("outCardNo", "10031679554410000000024");
		map.put("outCardName", "朱家珀");
		map.put("inCardNo", "6229538108106360895");
		map.put("inCardName", "朱家珀");
		map.put("totalAmt", "3000");
		map.put("oldOrderId", "BSDT20170110160956686066");
		map.put("remark", "342124196104053718");*/
		
		//0017|WJBZ|BSDT20170111104216175657A|10037385407210000000024|刘长征|
		//20000303212410300000026|涡阳县城乡居民社会养老保险收入户|100|342124196510301457|
		/*map.put("orderId", "BBSDT20170111104216175657A");
		map.put("outCardNo", "20000303212410300000026");
		map.put("outCardName", "涡阳县城乡居民社会养老保险收入户");
		map.put("inCardNo", "10037385407210000000024");
		map.put("inCardName", "刘长征");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170111104216175657A");
		map.put("remark", "342124196510301457");*/
		
		
		
		//0016|WJBZ|BSDT20170111104216175657|6229538108107224868|张小远|10037385407210000000024|刘长征|100|342124196510301457|
		/*map.put("orderId", "BBSDT20170111104216175657");
		map.put("outCardNo", "10037385407210000000024");
		map.put("outCardName", "刘长征");
		map.put("inCardNo", "6229538108107224868");
		map.put("inCardName", "张小远");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170111104216175657");
		map.put("remark", "342124196510301457");*/
		
		//0016|WJBZ|BSDT20170111100732980157|6229538108107224868|张小远|10034871170310000000049|邵兰|100|341223196802161124|

		/*map.put("orderId", "BBSDT20170111100732980157");
		map.put("outCardNo", "10034871170310000000049");
		map.put("outCardName", "邵兰");
		map.put("inCardNo", "6229538108107224868");
		map.put("inCardName", "张小远");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170111100732980157");
		map.put("remark", "341223196802161124");*/
		//0016|WJBZ|BSDT20170111095156699890|6229538108107224868|张小远|10042045658510000000016|郑莉娜|100|341223197503041127|
		/*map.put("orderId", "BBSDT20170111095156699890");
		map.put("outCardNo", "10042045658510000000016");
		map.put("outCardName", "郑莉娜");
		map.put("inCardNo", "6229538108107224868");
		map.put("inCardName", "张小远");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170111095156699890");
		map.put("remark", "341223197503041127");*/
		
		
		
		// 0016|WJBZ|BSDT20170111092407157335|6229538108107224868|张小远|10023809233410000000024|武彩英|100|342124196402071420|
		/*map.put("orderId", "BBSDT20170111092407157335");
		map.put("outCardNo", "10023809233410000000024");
		map.put("outCardName", "武彩英");
		map.put("inCardNo", "6229538108107224868");
		map.put("inCardName", "张小远");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170111092407157335");
		map.put("remark", "341223197503041127");*/
		
		
		//0017|WJBZ|BSDT20170111101946616078A|10041616248410000000016|杨信影|20000303212410300000026|涡阳县城乡居民社会养老保险收入户|100|34122319860208192X|
		/*map.put("orderId", "BBSDT20170111101946616078A");
		map.put("outCardNo", "20000303212410300000026");
		map.put("outCardName", "涡阳县城乡居民社会养老保险收入户");
		map.put("inCardNo", "10041616248410000000016");
		map.put("inCardName", "杨信影");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170111101946616078A");
		map.put("remark", "34122319860208192X");*/
		
		
		
		//0016|WJBZ|BSDT20170111101946616078|6217788368100004244|李珊珊|10041616248410000000016|杨信影|100|34122319860208192X|
	/*	map.put("orderId", "BBSDT20170111101946616078");
		map.put("outCardNo", "10041616248410000000016");
		map.put("outCardName", "杨信影");
		map.put("inCardNo", "6217788368100004244");
		map.put("inCardName", "李珊珊");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170111101946616078");
		map.put("remark", "34122319860208192X");*/
		
		//0016|WJBZ|BSDT20170111095143508896|6217788368100004244|李珊珊|10041616543810000000016|张富民|100|341621199401030716|
		/*map.put("orderId", "BBSDT20170111095143508896");
		map.put("outCardNo", "10041616543810000000016");
		map.put("outCardName", "张富民");
		map.put("inCardNo", "6217788368100004244");
		map.put("inCardName", "李珊珊");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170111095143508896");
		map.put("remark", "34122319860208192X");*/
		
		//0016|WJBZ|BSDT20170110191139487353|6217788318305983841|何曼丽|10045561836810000000016|闫丽苹|100|341281196807206663|
		/*map.put("orderId", "BBSDT20170110191139487353");
		map.put("outCardNo", "10045561836810000000016");
		map.put("outCardName", "闫丽苹");
		map.put("inCardNo", "6217788318305983841");
		map.put("inCardName", "何曼丽");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170110191139487353");
		map.put("remark", "341281196807206663");*/
		
		
		// 0017|WJBZ|BSDT20170110183459645824A|10045561035310000000016|王秀兰|20000302942410300000464|亳州市谯城区人力资源和社会保障局|100|341281196808086608|
		/*map.put("orderId", "BBSDT20170110183459645824A");
		map.put("outCardNo", "20000302942410300000464");
		map.put("outCardName", "亳州市谯城区人力资源和社会保障局");
		map.put("inCardNo", "10045561035310000000016");
		map.put("inCardName", "王秀兰");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170110183459645824A");
		map.put("remark", "341281196808086608");
		*/
		
		//0016|WJBZ|BSDT20170110183459645824|6217788318305983841|何曼丽|10045561035310000000016|王秀兰|100|341281196808086608|
		/*map.put("orderId", "BBSDT20170110183459645824");
		map.put("outCardNo", "10045561035310000000016");
		map.put("outCardName", "王秀兰");
		map.put("inCardNo", "6217788318305983841");
		map.put("inCardName", "何曼丽");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170110183459645824");
		map.put("remark", "341281196808086608");*/
		
		//0017|WJBZ|BSDT20170110205502945248A|10042071905210000000016|王杰|20000303212410300000026|涡阳县城乡居民社会养老保险收入户|100|342124198202154511|
		/*map.put("orderId", "BBSDT20170110205502945248A");
		map.put("outCardNo", "20000303212410300000026");
		map.put("outCardName", "涡阳县城乡居民社会养老保险收入户");
		map.put("inCardNo", "10042071905210000000016");
		map.put("inCardName", "王杰");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170110205502945248A");
		map.put("remark", "341281196808086608");*/
		
		
		//0016|WJBZ|BSDT20170110205502945248|6217788368102869370|韩震国|10042071905210000000016|王杰|100|342124198202154511|
		/*map.put("orderId", "BBSDT20170110205502945248");
		map.put("outCardNo", "10042071905210000000016");
		map.put("outCardName", "王杰");
		map.put("inCardNo", "6217788368102869370");
		map.put("inCardName", "韩震国");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170110205502945248");
		map.put("remark", "342124198202154511");*/
		
		
		//0016|WJBZ|BSDT20170110203940123371|6217788368102869370|韩震国|10020954319710000000129|孙艳芳|100|341223198307064588|
		/*map.put("orderId", "BBSDT20170110203940123371");
		map.put("outCardNo", "10020954319710000000129");
		map.put("outCardName", "孙艳芳");
		map.put("inCardNo", "6217788368102869370");
		map.put("inCardName", "韩震国");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170110203940123371");
		map.put("remark", "341223198307064588");*/
		
		
		
		//0016|WJBZ|BSDT20170110202038150766|6217788368106193256|张延|10041961042910000000016|邓素贤|100|341223196607014524|
		
		/*map.put("orderId", "BBSDT20170110202038150766");
		map.put("outCardNo", "10041961042910000000016");
		map.put("outCardName", "邓素贤");
		map.put("inCardNo", "6217788368106193256");
		map.put("inCardName", "张延");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170110202038150766");
		map.put("remark", "341223196607014524");*/
		
		
		//0017|WJBZ|BSDT20170108154624935022A|10041826506310000000016|田秀英|20000303212410300000026|涡阳县城乡居民社会养老保险收入户|100|342124196610158029|
		/*map.put("orderId", "BBSDT20170108154624935022A");
		map.put("outCardNo", "20000303212410300000026");
		map.put("outCardName", "涡阳县城乡居民社会养老保险收入户");
		map.put("inCardNo", "10041826506310000000016");
		map.put("inCardName", "田秀英");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170108154624935022A");
		map.put("remark", "342124196610158029");*/
		
		/*// 0016|WJBZ|BSDT20170108154624935022|6229538108106946958|邢辉|10041826506310000000016|田秀英|100|342124196610158029|
		map.put("orderId", "BBSDT20170108154624935022");
		map.put("outCardNo", "10041826506310000000016");
		map.put("outCardName", "田秀英");
		map.put("inCardNo", "6229538108106946958");
		map.put("inCardName", "邢辉");
		map.put("totalAmt", "100");
		map.put("oldOrderId", "BSDT20170108154624935022");
		map.put("remark", "342124196610158029");*/
//		StringBuffer sb = new StringBuffer();
//		sb.append("0009|WJBZ|" + map.get("orderId").toString() + "|");
//		sb.append(map.get("outCardNo").toString() + "|"+map.get("outCardName").toString() + "|");
//		sb.append(map.get("inCardNo").toString() + "|"+map.get("inCardName").toString() + "|");
//		sb.append(map.get("totalAmt").toString()+"|");
//		sb.append(map.get("oldOrderId").toString()+"|"+(map.get("remark")==null?"":map.get("remark").toString())+"|");
//		System.out.println("退费方法请求==="+sb.toString());
//		0009退款交易 String str ="0003|WJBZ|TWJBZ052018113000126|20000416644510300000059|亳州讯飞信息科技有限公司|6217788368108392419|赵新|100.00|WJBZ052018113000126|20181203手工退款|";
	//	String str ="0003|WJBZ|TWJBZWIRE20190127200701S5BX|20000559230510300000155|安徽广电信息网络股份有限公司蒙城分公司|6217788318304802414|朱磊|276.0|WJBZWIRE20190127200701S5BX|20190128手工退款|";
		String str ="0003|WJBZ|TWJBZ022019031501527|20000419257610300000067|亳州市自来水公司|6217788318302656283|王运动|3560|WJBZ022019031501527|20190318手工退款|";
		//0000|转账查询成功|0|0003|WJBZ|BSDT20190215110451570514|6217788368102518753|宋根|10033477082210000000024|宋金峰|200|20190215|05014315|20190215|99998700913706|核心转账成功|b990b83747ef87a04e1867358a0902f9|
		SocketClient client = new SocketClient();
		String returnMsg = client.sendMessage(str.toString());
		System.out.println(returnMsg);
		
		/*String str ="0000|�?��成功|0009|WJBZ|BBSDT20170110160956686066|10031679554410000000024|朱家�?6229538108106360895|朱家�?3000|20170110|05134094|20170110|9870025120|342124196104053718|deddebeac1bc248ba1ae05d3c83dcf63|";
		
		System.out.println(new String(str.getBytes(),"GBK"));
		String []  st = str.split("\\|");
		for(int i=0;i<st.length;i++){
			System.out.println("st["+i+"]===="+st[i]);
		}*/
		
		/*Map<String,String>  returnMap = new LinkedHashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("0007|WJBZ|10042045658510000000016|");
		sb.append("郑莉娜|");
		System.out.println("余额查询方法请求==="+sb.toString());
		SocketClient client = new SocketClient();
		String returnMsg = client.sendMessage(sb.toString());
		System.out.println("余额查询方法请求==="+returnMsg);*/
	}
	
	public static  int get(int i){
       while(i<100){
    	   i++;
			System.out.println(i+"-------");
			if(i==50){
				return i;
			}
		}
       return 100;
	}

}
