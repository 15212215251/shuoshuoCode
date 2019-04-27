package com.untech.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;

 

public class PayUtil_AHNJ {
	
	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(PayUtil_AHNJ.class.getName());
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	//退货方法
	public  Map<String,String>  backMoney(Map<String,String> map ){
		 Map<String,Object>  paymap = new LinkedHashMap<String,Object>();
		 Map<String,String>  returnMap = new LinkedHashMap<String,String>();
		 paymap.put("version", "1.0.1");//版本号
		 paymap.put("merchantId", map.get("merchantId"));//商户编号
		 paymap.put("certId","0001");//数字签名
		 paymap.put("serialNo",map.get("orderId"));//商户的订单号
		 paymap.put("date", map.get("payerTime"));//交易日期和时间
		 paymap.put("signNo", map.get("signNo"));//协议号
		 paymap.put("charge",  "0");//手续费
		 paymap.put("amount",  new BigDecimal(map.get("totalAmt")).multiply(new BigDecimal(100)).intValue()); //交易金额
		 paymap.put("currency", "156");//货币类型
		 paymap.put("originalSerialNo",map.get("oldOrderId")); 
		 paymap.put("originalDate", map.get("oldPayerTime")); 
		 paymap.put("Extension", ""); 
		 String creport=XmlUtil.Map2xmlQuick(paymap, "SRReq",IdSequence.getMessgeId(map.get("userNo"), "SRReq"), "SRReq");
		 logger.info("creport==="+creport);
		 Map<String,Object> returnMaps = SignAndVertyUtil.SignXml(creport, "SRReq");
		 logger.info("returnMaps==="+returnMaps);
		 ResponseModel model  = (ResponseModel)returnMaps.get("Error");
		 Map<String, Object> map_3  = new HashMap<String, Object>();
		 if(model == null){
			map_3 = (Map<String, Object>)returnMaps.get("SRRes");
			if(map_3 == null){  //没收到返回的情况，需要增加查询接口确保是否成功
				 Map<String,String> queryMap  = SingQueryReq(map);
				 map.put("type", "1");
				 if("0000".equals(queryMap.get("flag"))){
					 if("0000".equals(queryMap.get("status")) || "0002".equals(queryMap.get("status")) || "0003".equals(queryMap.get("status"))){
						 returnMap.put("flag", "0001");
						 returnMap.put("errorCode", "1605");
						 returnMap.put("errorMessage", "交易失败");
						 returnMap.put("errorDetail", "交易失败");
					 }else if("0001".equals(queryMap.get("status"))){
						 returnMap.put("flag", "0000"); 
						 returnMap.put("merchantId", map_3.get("merchantId").toString()); 
						 returnMap.put("serialNo", map_3.get("serialNo").toString()); 
						 returnMap.put("signNo", map_3.get("signNo").toString()); 
					 }else{
						 returnMap.put("flag", "9999");
						 returnMap.put("errorCode", "9999");
						 returnMap.put("errorMessage", "系统异常");
						 returnMap.put("errorDetail", "系统异常");
					 }
				 }else{
					 returnMap.put("flag", "9999");
					 returnMap.put("errorCode", "9999");
					 returnMap.put("errorMessage", "系统异常");
					 returnMap.put("errorDetail", "系统异常");
			    }
			}else{
				 returnMap.put("flag", "0000"); 
				 returnMap.put("merchantId", map_3.get("merchantId").toString()); 
				 returnMap.put("serialNo", map_3.get("serialNo").toString()); 
				 returnMap.put("signNo", map_3.get("signNo").toString()); 
			}
		}else{  //返回错误
			 returnMap.put("flag", "0001");
			 returnMap.put("errorCode", model.getErrorCode());
			 returnMap.put("errorMessage", model.getErrorMessage());
			 returnMap.put("errorDetail", model.getErrorDetail());
		}	
		 return returnMap;
	}
	
	/**支付接口 */
	public Map<String,String>  payMoney(Map<String,String> map ){
		 Map<String,Object>  paymap = new LinkedHashMap<String,Object>();
		 Map<String,String>  returnMap = new LinkedHashMap<String,String>();
		 String mId = IdSequence.getMessgeId(map.get("userNo").toString(), "APReq");
		 paymap.put("version", "1.0.1");//版本号
		 paymap.put("merchantId",map.get("merchantId"));//商户编号/*********************/
		 paymap.put("certId","0001");//数字签名
		 paymap.put("serialNo", map.get("orderId").toString());//商户的订单号
		 paymap.put("date",map.get("payerTime") );//交易日期和时间
		 paymap.put("signNo", map.get("signNo"));//协议号
		 paymap.put("charge", "0");//手续费
		 paymap.put("amount", ((new BigDecimal(map.get("cardAmt").toString()).multiply(new BigDecimal(100))).intValue())+""); //交易金额，银行卡的交易金额
		 paymap.put("currency", "156");//货币类型
		 paymap.put("mtoken", "");//短信验证码
		 // paymap.put("Extension", "快捷支付");//支付备注
		 //发往安徽农金系统
		 //生成安徽农金系统快捷支付支付报文XML
		 String creport=XmlUtil.Map2xmlQuick(paymap, "APReq",mId, "APReq");
		 logger.info("creport==="+creport);
		 Map<String,Object> signReturnMap = SignAndVertyUtil.SignXml(creport, "APReq");
		 logger.info("signReturnMap==="+signReturnMap);
		 ResponseModel model  = (ResponseModel)signReturnMap.get("Error");
		 Map<String, Object> map_3  = new HashMap<String, Object>();
		 if( model == null){
			map_3 = (Map<String, Object>)signReturnMap.get("APRes");
			if( map_3 == null){
				map.put("type", "2");
				Map<String,String> queryMap  = SingQueryReq(map);
				if("0000".equals(queryMap.get("flag"))){
					 if("0000".equals(queryMap.get("status")) || "0002".equals(queryMap.get("status")) || "0003".equals(queryMap.get("status"))){
						 returnMap.put("flag", "0001");
						 returnMap.put("errorCode", "1605");
						 returnMap.put("errorMessage", "交易失败");
						 returnMap.put("errorDetail", "交易失败");
					 }else if("0001".equals(queryMap.get("status"))){
						 returnMap.put("flag", "0000"); 
						 returnMap.put("merchantId", map_3.get("merchantId").toString()); 
						 returnMap.put("serialNo", map_3.get("serialNo").toString()); 
						 returnMap.put("signNo", map_3.get("signNo").toString()); 
					 }else{
						 returnMap.put("flag", "9999");
						 returnMap.put("errorCode", "9999");
						 returnMap.put("errorMessage", "系统异常");
						 returnMap.put("errorDetail", "系统异常");
					 }
				}else{
					 returnMap.put("flag", "9999");
					 returnMap.put("errorCode", "9999");
					 returnMap.put("errorMessage", "系统异常");
					 returnMap.put("errorDetail", "系统异常");
			    }
			} else{
				 returnMap.put("flag", "0000"); 
				 returnMap.put("merchantId", map_3.get("merchantId").toString()); 
				 returnMap.put("serialNo", map_3.get("serialNo").toString()); 
				 returnMap.put("signNo", map_3.get("signNo").toString()); 
			}
		}else{  //返回错误
			 returnMap.put("flag", "0001");
			 returnMap.put("errorCode", model.getErrorCode());
			 returnMap.put("errorMessage", model.getErrorMessage());
			 returnMap.put("errorDetail", model.getErrorDetail());
		}	
		 return returnMap;
	}
	
	 /********安徽农金系统认证支付单笔实时查询发送请求报文******/
	 public  Map<String,String> SingQueryReq(Map<String,String> map){
		Map<String,Object> payMap=new LinkedHashMap<String,Object>();
		Map<String,String>  returnMap = new LinkedHashMap<String,String>();
		payMap.put("version","1.0.1");
		payMap.put("merchantId",map.get("merchantId").toString());
		payMap.put("certId","0001");
		payMap.put("date",map.get("payerTime").toString());
		payMap.put("type",map.get("type").toString());//0 提现  1 支付  2退货
		payMap.put("serialNo",map.get("orderId").toString());
		String creport=XmlUtil.Map2xmlQuick(payMap, "STQReq",IdSequence.getMessgeId(map.get("userNo"), "STQReq"), "STQReq");
		logger.info("creport==="+creport);
		Map<String,Object> signReturnMap = SignAndVertyUtil.SignXml(creport, "STQReq"); 
		logger.info("signReturnMap==="+signReturnMap);
		ResponseModel model  = (ResponseModel)signReturnMap.get("Error");
		Map<String, Object> map_3  = new HashMap<String, Object>();
		if( model == null){
			map_3 = (Map<String, Object>)signReturnMap.get("STQRes");
			if( map_3 == null){
				 returnMap.put("flag", "9999");
				 returnMap.put("errorCode", "9999");
				 returnMap.put("errorMessage", "系统异常");
				 returnMap.put("errorDetail", "系统异常");
			} else{
				 returnMap.put("flag", "0000"); 
				 returnMap.put("status", map_3.get("status").toString());//0000 失败   0001 成功  0002 银行处理中  0003 银行无此订单
				 returnMap.put("merchantId", map_3.get("merchantId").toString()); 
				 returnMap.put("serialNo", map_3.get("serialNo").toString()); 
				 returnMap.put("signNo", map_3.get("signNo").toString()); 
			}
		}else{  //返回错误
			 returnMap.put("flag", "0001");
			 returnMap.put("errorCode", model.getErrorCode());
			 returnMap.put("errorMessage", model.getErrorMessage());
			 returnMap.put("errorDetail", model.getErrorDetail());
		}	
		return returnMap;
		 
	 }
	 
	 //银行卡签约查询
	public  Map<String,Object> querySignStatus(Map map){
		logger.info("querySignStatus==========");
		String methodName = "CSQReq";
		String mId = IdSequence.getMessgeId(map.get("userNo").toString(), "CSQReq");
		String id = "CSQReq";
		Map<String,Object> map1 = new HashMap<String, Object>();
		map1.put("version", "1.0.1");//版本
		map1.put("merchantId", map.get("merchantId").toString());//商户编号
		map1.put("certId", "0001");//数字证书标识
		map1.put("date", formatter.format(new Date()));//报文发送时间时间
		map1.put("bankCardNo", map.get("cardNo").toString());//银行卡卡号
		//版本2.1只有参数银行卡卡号
		map1.put("certificateType", map.get("certType").toString());//证件类型 目前只支持 1：身份证
		map1.put("certificateNo", map.get("certNo").toString());//证件号码
		map1.put("mobilePhone", map.get("mobilePhone").toString());//手机号码
		map1.put("Extension", "");//消息扩展
		String data = XmlUtil.Map2xmlQuick(map1, methodName, mId, id);
		logger.info("银行卡签约查询发送安徽农金的xml：" + data);
		Map<String,Object> returnMap = SignAndVertyUtil.SignXml(data, "CSQReq");
		//通用错误信息,需要判断签约成功和返回已签约的状态返回
		Map<String,Object> map_3 = (Map<String, Object>)returnMap.get("CSQRes");
		Map<String,Object>  returnMap2 = new HashMap<String,Object>();
		returnMap2.put("flag", "9999");
		returnMap2.put("errorMessage","签约查询异常");
		if(map_3 != null){
			 String sts = map_3.get("status").toString();
			 returnMap2.put("flag", "0000");
			 returnMap2.put("errorMessage","签约查询成功");
			 if("1".equals(sts)){
				String signNo = map_3.get("signNo").toString();
				returnMap2.put("signNo", signNo);
			 }
		}
		return returnMap2;
	}
		
	 
	 ///银行卡签约
	 public   Map<String,String> signCard(Map<String,String> map){
		    Map<String,String>  returnMap2 = new LinkedHashMap<String,String>();
		    Map<String,Object> map1 = new HashMap<String, Object>();
			String methodName = "CSReq";
			String mId = IdSequence.getMessgeId(map.get("userNo").toString(), "CSReq");   //待确定唯一性
			String id = "CSReq";
			map1.put("version", "1.0.1");//版本
			map1.put("merchantId", map.get("merchantId").toString());//商户编号，从数据库获取
			map1.put("certId", "0001");//数字证书标识
			map1.put("date", formatter.format(new Date()));//报文发送时间时间
			map1.put("accountName", map.get("cardName"));//银行卡户名
			map1.put("bankCardNo", map.get("cardNo"));//银行卡卡号
			map1.put("bankCardType", map.get("cardType"));//银行卡类型  目前只支持 D：借记卡
			map1.put("certificateType", map.get("certType"));//证件类型 目前只支持 1：身份证
			map1.put("certificateNo", map.get("certNo"));//证件号码
			map1.put("mobilePhone", map.get("mobilePhone"));//手机号码
			map1.put("Extension", "");//消息扩展
			String data = XmlUtil.Map2xmlQuick(map1, methodName, mId, id);
			logger.info("银行卡签约发送安徽农金的xml：" + data);
			String signNo = "";//协议号
			Map<String,Object> returnMap = SignAndVertyUtil.SignXml(data, "CSReq");
			//通用错误信息,需要判断签约成功和返回已签约的状态返回
			ResponseModel model  = (ResponseModel)returnMap.get("Error");
			Map<String, Object> map_3  = new HashMap<String, Object>();
			returnMap2.put("flag", "9999");
			returnMap2.put("errorMessage", "系统异常");
			if(model == null){
				map_3 = (Map<String, Object>)returnMap.get("CSRes");
				if(map_3 != null){
					signNo = map_3.get("signNo").toString();
					returnMap2.put("flag", "0000");
					returnMap2.put("errorMessage","签约成功");
					returnMap2.put("signNo", signNo);
				}
				
			}else{
				String errorCode = model.getErrorCode();
				if("1820".equals(errorCode)){  //需要调用查询查询协议号
					returnMap = querySignStatus(map);
					if("0000".equals(returnMap.get("flag").toString())){
						 signNo = returnMap.get("signNo").toString();
						 returnMap2.put("flag", "0000");
						 returnMap2.put("errorMessage","签约成功");
						 returnMap2.put("signNo", signNo);
					}
				}else{
					returnMap2.put("flag", "9999");
					returnMap2.put("errorMessage", model.getErrorMessage()+"。"+ model.getErrorDetail());
				}
			}
			return returnMap2;
		 
	 }
	 
	 
	 ///银行卡解约
	 public   Map<String,String> signCardDis(Map<String,String> map){
		    Map<String,String>  returnMap2 = new LinkedHashMap<String,String>();
		    String merchantId = map.get("merchantId");
			Map<String,Object> map1 = new HashMap<String, Object>();
			String methodName = "CSCReq";
			String mId = IdSequence.getMessgeId(map.get("userNo").toString(), "CSCReq");
			String id = "CSCReq";
			map1.put("version", "1.0.1");//版本
			map1.put("merchantId", merchantId);//商户编号
			map1.put("certId", "0001");//数字证书标识
			map1.put("date", formatter.format(new Date()));//报文发送时间时间
			map1.put("signNo", map.get("signNo")); 
			map1.put("Extension", "");//消息扩展
			String data = XmlUtil.Map2xmlQuick(map1, methodName, mId, id);
			logger.info("银行卡解约发送安徽农金的xml：" + data);
			String  signNo  = "";
			String status = map.get("status");
			returnMap2.put("flag", "9999");
			returnMap2.put("errorMessage","解约失败");
			Map<String,Object> returnMap = SignAndVertyUtil.SignXml(data, "CSCReq");
			//通用错误信息,需要判断签约成功和返回已签约的状态返回
			ResponseModel model  = (ResponseModel)returnMap.get("Error");
			Map<String, Object>  map_3 = new HashMap<String, Object>();
			if(model == null){
				map_3 = (Map<String, Object>)returnMap.get("CSCRes");
				if(map_3 != null ){
					//signNo = map_3.get("signNo").toString();
					//status= "1";
					returnMap2.put("flag", "0000");
					returnMap2.put("errorMessage","解约成功");
				}
				
			}else{
				String errorCode = model.getErrorCode();
				if("1818".equals(errorCode)){   
					//status = "1";
					returnMap2.put("flag", "0000");
					returnMap2.put("errorMessage","解约成功");
				}
			}
	      return returnMap2;
	 
	 }
	    

}
