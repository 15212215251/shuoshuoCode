package com.untech.util;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.untech.util.merchant.HttpClientSender;
public class PayNotify {
	private static final Logger logger = Logger.getLogger(PayNotify.class.getName());
	public static Map<String,String> notify(Map<String,String> map ) {
		try{
			Map<String,String> returnMap = new HashMap<String,String>();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("userNo", map.get("USERNO"));
			jsonObj.put("transType", map.get("TRANSTYPE"));
			jsonObj.put("payType", map.get("MONEYFLAG"));
			jsonObj.put("totalAmt", map.get("PAYAMT"));
			jsonObj.put("cardAmt", map.get("CARDAMT"));
			jsonObj.put("balanceAmt", map.get("MONEY"));
			jsonObj.put("orderId", map.get("ORDERID"));
			jsonObj.put("feeType", map.get("FEETYPE"));
			jsonObj.put("unitNo", map.get("UNITNO"));
			jsonObj.put("cardNo", map.get("CARDNO"));
			jsonObj.put("mobile", map.get("MOBILEPHONE"));
			jsonObj.put("channelNo","01");
			String status = map.get("flag");
			if(!"0".equals(status)){
				jsonObj.put("status", "1");
			}else{
				jsonObj.put("status", "0");
			}
			jsonObj.put("payerTime", map.get("PAYERTIME"));
			jsonObj.put("seqNo", (map.get("COREDATE1")==null?"": map.get("COREDATE1"))+ (map.get("COREFLOW1")==null?"":map.get("COREFLOW1")));
			jsonObj.put("backAmt", map.get("BACKAMT"));
			logger.info("jsonObj.toString()"+jsonObj.toString());
			String key = RandomUtil.generateNumber(16);
			JSONObject js = (JSONObject)ReturnUtil.getPayReturn(jsonObj, key);
			HttpClientMessageSenderByJson send = new HttpClientMessageSenderByJson();
			String resp = send.send(js.toString(),map.get("NOTE5"));
			logger.info("resp"+resp);
			return returnMap;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**商户回掉
	 * 
	 *  notifyDate	String	是	20	通知发送时间
		notifyType	String	是	32	通知类型（默认值1）
		notifyId	String	是	128	通知ID
		partnerId	String	是	32	合作伙伴标识
		signType	String	是	10	签名方式。默认值：RSA
		sign	String	是	256	签名字符串
		outTradeNo	String	是	64	第三方订单ID（需保证唯一）。
		tradeNo	String	是	64	支付平台订单ID
		tradeCreateDate	String	否	20	订单创建时间
		tradeStatus	String	否	32	订单状态。
		totalAmount	String	否	12	订单总金额
		receiptAmount	String	否	12	实收金额
		payDate	String	否	20	支付完成时间
		mobile	String	否	11	用户手机号码
		userNo	String	是	50	用户唯一识别号
	 * 
	 * 
	 * */
	public static Map<String,String> PayCodenotify(Map<String,String> map ) {
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			Map<String,String> returnMap = new HashMap<String,String>();
			returnMap.put("notifyDate", formatter.format(new Date()));//通知发送时间
			returnMap.put("notifyType", "1");//通知类型（默认值1）
			returnMap.put("notifyId", "");//通知ID
			returnMap.put("partnerId", map.get("partnerId"));//合作伙伴标识
			returnMap.put("signType", "RSA");//签名方式。默认值：RSA
			returnMap.put("sign", "");//签名字符串
			returnMap.put("outTradeNo", map.get("ORDERID"));//第三方订单ID（需保证唯一）
			returnMap.put("tradeNo", map.get("SEQNO"));//支付平台订单ID
			returnMap.put("tradeCreateDate", map.get("PAYERTIME"));//订单创建时间
			String status  = map.get("FLAG");
			if("1".equals(status) || "2".equals(status) ){
				returnMap.put("tradeStatus", "6");//订单状态。
			}else{
				returnMap.put("tradeStatus", "5");//订单状态。
			}
			returnMap.put("totalAmount", map.get("PAYAMT"));//订单总金额
			returnMap.put("receiptAmount", map.get("PAYAMT"));//实收金额
			returnMap.put("payDate", map.get("TRAFFICTIME"));//支付完成时间
			returnMap.put("mobile","");//用户手机号码
			returnMap.put("userNo", map.get("USERNO"));//用户唯一识别号
			HttpClientSender send = new HttpClientSender();
			String resp = send.send(returnMap,map.get("NOTE5"));
			logger.info("resp"+resp);
			return returnMap;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Map<String,String> taxiNotify(Map<String,String> map ) {
		try{
			Map<String,String> returnMap = new HashMap<String,String>();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("userNo", map.get("USERNO"));
			jsonObj.put("transType", map.get("TRANSTYPE"));
			jsonObj.put("payType", map.get("MONEYFLAG"));
			jsonObj.put("totalAmt", map.get("PAYAMT"));
			jsonObj.put("cardAmt", map.get("CARDAMT"));
			jsonObj.put("balanceAmt", map.get("MONEY"));
			jsonObj.put("orderId", map.get("ORDERID"));
			jsonObj.put("feeType", map.get("FEETYPE"));
			jsonObj.put("unitNo", map.get("UNITNO"));
			jsonObj.put("cardNo", map.get("CARDNO"));
			jsonObj.put("mobile", map.get("MOBILEPHONE"));
			jsonObj.put("taxiUserNo", map.get("taxiUserNo"));
			jsonObj.put("channelNo","01");
			String status = map.get("flag");
			if(!"0".equals(status)){  //status == 0初始， status == 1 已缴费
				jsonObj.put("status", "1");
			}else{
				jsonObj.put("status", "0");
			}
			jsonObj.put("payerTime", map.get("PAYERTIME"));
			jsonObj.put("seqNo", (map.get("COREDATE1")==null?"": map.get("COREDATE1"))+ (map.get("COREFLOW1")==null?"":map.get("COREFLOW1")));
			jsonObj.put("backAmt", map.get("BACKAMT"));
			logger.info("jsonObj.toString()"+jsonObj.toString());
			String key = RandomUtil.generateNumber(16);
			JSONObject js = (JSONObject)ReturnUtil.getPayReturn(jsonObj, key);
			
			//此处需要实现异步回调的功能   失败最多调用5次回调成功停止
			logger.info("异步回调开始--------------------------");
			SyncNotifyForTaxi sync = new SyncNotifyForTaxi(js.toString(), map.get("NOTE5"));
			Thread thread = new Thread(sync);
			thread.start();
			return returnMap;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}
