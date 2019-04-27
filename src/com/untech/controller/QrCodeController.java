package com.untech.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.untech.service.OnlinePayService;
import com.untech.service.OrderService;
import com.untech.service.PayService;
import com.untech.util.AESUtil;
import com.untech.util.EncodeDecodeUtil;
import com.untech.util.MD5Util;
import com.untech.util.MacUtil;
import com.untech.util.RedisUtil;
import com.untech.util.ReturnUtil;
import com.untech.util.StringUtil;

/**
 * 订单Controller
 * 
 * @author wuxw
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Scope("prototype")
@Controller
public class QrCodeController {

	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(QrCodeController.class
			.getName());
	private JSONObject jsonObject = new JSONObject();

	@Autowired
	private OrderService orderService;

	
	@Autowired
	private PayService payService;
	
	@Autowired
	private OnlinePayService onlinePayService;
	/**
	 * 支付页面
	  
	 */
	@RequestMapping(value = "/QrCodepay")
	@ResponseBody
	public ModelAndView  QrCodepay( HttpServletRequest request,HttpServletResponse response) {
		System.out.println("QrCodepayQrCodepayQrCodepayQrCodepay");
		
		String ua = ((HttpServletRequest) request).getHeader("user-agent")
        .toLowerCase();
		System.out.println(ua);
	    if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
	       System.out.println("微信打开");
	    }
		 String  orderid = request.getParameter("orderid");
		 String  sign = request.getParameter("sign");
		 String time = request.getParameter("time");
		 Map<String,Object> model = new HashMap<String,Object>();  
		 System.out.println(orderid);
		 //先校验签名是否正确
		 String signV =  MD5Util.encode(AESUtil.encrypt(orderid+time, "001122334567890A"));
		 if(!signV.equals(sign)){
			 System.out.println("签名验证失败");
			// return new ModelAndView("/error");
		 }
		 //根据订单号查找订单信息
		 Map<String,String>  queryMap = new HashMap<String, String>();
		 queryMap.put("orderId", orderid);
		 List list = orderService.queryOrderInfo(queryMap);
		 if(list != null && list.size() > 0){
			 Map map = (Map) list.get(0);
			 model.put("orderId", map.get("ORDERID"));
			 model.put("unitname", map.get("UNITNAME"));
			 model.put("totalAmt", map.get("TOTALAMT"));
			 model.put("detail", map.get("DETAIL"));
			 String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());  
			 model.put("date", date);
		 }else{
			 System.out.println("订单号不存在");
			 // return new ModelAndView("/error");
		 }
		 //获取用户银行卡列表
		 queryMap.put("userNo", "949494674dc2704f014dc27197130000");
		 try {
			Map returnMap = payService.queryBankcardList(queryMap);
			List cardList = (List) returnMap.get("cardList");
			model.put("cardList", cardList);
			if(cardList != null && cardList.size() > 0){
				for(int i=0;i<cardList.size();i++){
					Map listMap = (Map) cardList.get(i);
					System.out.println(listMap);
				}
				
			}
			System.out.println(returnMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 ModelAndView mav = new ModelAndView("pay");//实例化一个VIew的ModelAndView实例
		 mav.addObject("model", model);//添加一个带名的model对象
		 System.out.println(model);
         return mav;
	}

	 
	/**
	 * 支付页面 ,临时测试使用
	  
	 */
	@RequestMapping(value = "/QrCodepayTest")
	@ResponseBody
	public ModelAndView  QrCodepayTest( HttpServletRequest request,HttpServletResponse response) {
		System.out.println("QrCodepayQrCodepayQrCodepayQrCodepay");
		
		String ua = ((HttpServletRequest) request).getHeader("user-agent")
        .toLowerCase();
		System.out.println(ua);
	    if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
	       System.out.println("微信打开");
	    }
		 String  orderid = request.getParameter("orderid");
		 String  sign = request.getParameter("sign");
		 String time = request.getParameter("time");
		 Map<String,Object> model = new HashMap<String,Object>();  
		 System.out.println(orderid);
		 //先校验签名是否正确
		 String signV =  MD5Util.encode(AESUtil.encrypt(orderid+time, "001122334567890A"));
		 if(!signV.equals(sign)){
			 System.out.println("签名验证失败");
			// return new ModelAndView("/error");
		 }
		 //根据订单号查找订单信息
		 Map<String,String>  queryMap = new HashMap<String, String>();
		 queryMap.put("orderId", orderid);
		 List list = orderService.queryOrderInfo(queryMap);
		 if(list != null && list.size() > 0){
			 Map map = (Map) list.get(0);
			 model.put("orderId", map.get("ORDERID"));
			 model.put("unitname", map.get("UNITNAME"));
			 model.put("totalAmt", map.get("TOTALAMT"));
			 model.put("detail", map.get("DETAIL"));
			 String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());  
			 model.put("date", date);
		 }else{
			 System.out.println("订单号不存在");
			 // return new ModelAndView("/error");
		 }
		 //获取用户银行卡列表
		 String objectId = "949494674dc2704f014dc27197130000";
		 queryMap.put("userNo", objectId);
		 model.put("objectId", objectId);
		 try {
			Map returnMap = payService.queryBankcardList(queryMap);
			List cardList = (List) returnMap.get("cardList");
			model.put("cardList", cardList);
			model.put("cardSize", returnMap.get("cardCount"));
			model.put("balance", returnMap.get("balance"));
			System.out.println(returnMap.get("cardCount")+"----------");
			if(cardList != null && cardList.size() > 0){
				for(int i=0;i<cardList.size();i++){
					Map listMap = (Map) cardList.get(i);
					System.out.println(listMap);
				}
			}
			System.out.println(returnMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		 ModelAndView mav = new ModelAndView("payMain");//实例化一个VIew的ModelAndView实例
		 mav.addObject("model", model);//添加一个带名的model对象
		 System.out.println("model==="+model);
         return mav;
	}
	
	/**
	 * 支付页面 ,临时测试使用
	  
	 */
	@RequestMapping(value = "/mobileMsg")
	@ResponseBody
	public ModelAndView  mobileMsg( HttpServletRequest request,HttpServletResponse response) {
		//mobileMsg.do?cardNo="+cardNo+"&bankNo="+bankNo+"&channelNo="+channelNo+"&mobile="+mobile+"&orderId="+orderId;
		 Map<String,Object> model = new HashMap<String,Object>();  
		 String cardNo = request.getParameter("cardNo");
		 String bankNo = request.getParameter("bankNo");
		 String channelNo = request.getParameter("channelNo");
		 String mobile = request.getParameter("mobile");
		 String orderId = request.getParameter("orderId");
		 String objectId = request.getParameter("objectId");
		 model.put("cardNo", cardNo);
		 model.put("bankNo", bankNo);
		 model.put("channelNo", channelNo);
		 model.put("mobile", mobile);
		 model.put("orderId", orderId);
		 model.put("objectId", objectId);
		 ModelAndView mav = new ModelAndView("payPhone");//实例化一个VIew的ModelAndView实例
		 mav.addObject("model", model);//添加一个带名的model对象
		 System.out.println("model==="+model);
         return mav;
	}
	/**
	 * 支付页面 ,临时测试使用
	  
	 */
	@RequestMapping(value = "/sendSmsMessage")
	@ResponseBody
	public void  sendSmsMessage( HttpServletRequest request,HttpServletResponse response1) throws IOException {
		//mobileMsg.do?cardNo="+cardNo+"&bankNo="+bankNo+"&channelNo="+channelNo+"&mobile="+mobile+"&orderId="+orderId;
		 Map<String,Object> model = new HashMap<String,Object>();  
		 String mobilePhone=request.getParameter("mobilePhone");
		 String msgType =request.getParameter("msgType");
		 String objectId =  request.getParameter("objectId");
		//生成发送短信验证码
		String authCode  = RedisUtil.getRedis(mobilePhone, msgType);
		boolean flag = false;
		if(!"".equals(authCode)){
			flag = RedisUtil.setSmsRedis(objectId, mobilePhone, msgType, authCode);
		}else{
			Random random = new Random();
			authCode="";
			for(int i=0;i<6;i++){
				authCode += random.nextInt(10);
			}
			System.out.print(authCode);
			flag = RedisUtil.setSmsRedis(objectId, mobilePhone, msgType, authCode);
			
		}
		 String content = authCode+"，温馨提示：请您在30分钟内完成短信校验，谢谢合作！";
		//调用发送短信接口
		/*
		SocketClientToo client = new SocketClientToo();
		System.out.println("6666|YDYH|"+mobilePhone+"|"+content+"|"+"--------------------");
		String response = client.sendMessage("6666|YDYH|"+mobilePhone+"|"+content+"|");
		System.out.println(response+"--------------------");
		if(response != null && response.length() != 4){
			if("0000".equals(response.split("\\|")[0])){
				flag =  true;
			}
		}*/
		
		//flag = SendSmsUtil.sendMsg("YDYH", mobilePhone, content);
		flag = true; 
		model.put("flag", flag);
		String json = net.sf.json.JSONArray.fromObject(model).toString();
        PrintWriter writer = response1.getWriter(); 
        writer.print(json);  
        writer.flush();  
        writer.close();
	}
	
	
	/**
	 * 支付页面 ,临时测试使用
	  
	 */
	@RequestMapping(value = "/validSmsMessage")
	@ResponseBody
	public void  validSmsMessage( HttpServletRequest request,HttpServletResponse response1) throws IOException {
		//mobileMsg.do?cardNo="+cardNo+"&bankNo="+bankNo+"&channelNo="+channelNo+"&mobile="+mobile+"&orderId="+orderId;
		 Map<String,Object> model = new HashMap<String,Object>();  
		 String mobilePhone=request.getParameter("mobilePhone");
		 String msgType =request.getParameter("msgType");
		 String messageCode =  request.getParameter("messageCode");
		//生成发送短信验证码
		String authCode  = RedisUtil.getRedis(mobilePhone, msgType);
		boolean flag = false;
		if(messageCode.equals(authCode)){
			RedisUtil.delRedis(mobilePhone, msgType);
			flag = true;
		} 
		model.put("flag", flag);
		String json = net.sf.json.JSONArray.fromObject(model).toString();
        PrintWriter writer = response1.getWriter(); 
        writer.print(json);  
        writer.flush();  
        writer.close();
	}
	
	
	/**
	 * 支付页面 ,临时测试使用
	  
	 */
	@RequestMapping(value = "/paySecurt")
	@ResponseBody
	public ModelAndView  psySecurt( HttpServletRequest request,HttpServletResponse response) {
		//mobileMsg.do?cardNo="+cardNo+"&bankNo="+bankNo+"&channelNo="+channelNo+"&mobile="+mobile+"&orderId="+orderId;
		 Map<String,Object> model = new HashMap<String,Object>();  
		 String cardNo = request.getParameter("cardNo");
		 String bankNo = request.getParameter("bankNo");
		 String channelNo = request.getParameter("channelNo");
		 String mobile = request.getParameter("mobile");
		 String orderId = request.getParameter("orderId");
		 String objectId = request.getParameter("objectId");
		 model.put("cardNo", cardNo);
		 model.put("bankNo", bankNo);
		 model.put("channelNo", channelNo);
		 model.put("mobile", mobile);
		 model.put("orderId", orderId);
		 model.put("objectId", objectId);
		 ModelAndView mav = new ModelAndView("paySecurt");//实例化一个VIew的ModelAndView实例
		 mav.addObject("model", model);//添加一个带名的model对象
		 System.out.println("model==="+model);
         return mav;
	}
	
	/**
	 * 支付页面 ,临时测试使用
	 * @throws IOException 
	  
	 */
	@RequestMapping(value = "/securtQrcodeManager")
	@ResponseBody
	public void  securtManager( HttpServletRequest request,HttpServletResponse response) throws IOException {
		 Map<String,Object> model = new HashMap<String,Object>();  
		 String paySecurt = request.getParameter("paySecurt");
		 String flag = request.getParameter("flag");
		 String objectId = request.getParameter("objectId");
		 Map map = new HashMap();
		 map.put("paySecurt",  MacUtil.TMAC("0123456789ABCDEF","0000000000000000",paySecurt));
		 map.put("flag", flag);
		 map.put("userNo", objectId);
		 boolean modelflag = false;
		 try {
			Map returnMap  = onlinePayService.securtManager(map);
			if("0000".equals(returnMap.get("resCode"))){
				modelflag = true;
			}
		 } catch (Exception e) {
			e.printStackTrace();
		 }
		 model.put("flag", modelflag);
		 String json = net.sf.json.JSONArray.fromObject(model).toString();
	     PrintWriter writer = response.getWriter(); 
	     writer.print(json);  
	     writer.flush();  
	     writer.close();
	}
	
	/**
	 * 银行卡和电子钱包支付接口
	 * 1、公共业务的判断
	 * 2、钱包和银行卡支付分开处理
	 * @return
	 */
	@RequestMapping(value = "/payQrcodeFee")
	@ResponseBody
	public ModelAndView payFee(HttpServletRequest request,HttpServletResponse response){
		 
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", request.getParameter("objectId"));
		map.put("orderId", request.getParameter("orderId"));
		map.put("bankNo", "01");
		map.put("channelNo", "01");
		map.put("backUrl", "");
		map.put("cardNo", request.getParameter("cardNo"));
		map.put("mobile", request.getParameter("mobile"));
		map.put("payEncrypt",   MacUtil.TMAC("0123456789ABCDEF","0000000000000000",request.getParameter("paySecurt")));
		
		Map<String,String>  queryMap = new HashMap<String, String>();
		queryMap.put("orderId", request.getParameter("orderId"));
	    List list = orderService.queryOrderInfo(queryMap);
		if(list != null && list.size() > 0){
			 map.put("totalamt", ((Map)list.get(0)).get("TOTALAMT"));
			 map.put("feeType", ((Map)list.get(0)).get("FEETYPE"));
		}
		String cardNo = request.getParameter("cardNo");
		if("".equals(cardNo)){
			map.put("paytype", "02");
			map.put("cardMoney","0.00" );
			map.put("payMoney", map.get("totalamt"));
		}else{
			map.put("paytype", "01");
			map.put("cardMoney", map.get("totalamt"));
			map.put("payMoney", "0.00");
		}
	  
		// 支付
		try {
			Map payMap = orderService.queryOrderDetail(map);
			if (payMap.get("FLAG").toString().equals("0")) {
				if (map.get("paytype").toString().equals("01")) {// 银行卡支付
					jsonObject = (JSONObject) orderService.cardPayment(map);
				} else if (map.get("paytype").toString().equals("02")) {// 余额支付
					jsonObject = (JSONObject) orderService.balancePayment(map);
				}
			} else {
				jsonObject.put("resCode", "1002");
				jsonObject.put("resMsg", "该订单已支付");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "支付异常");
		}
		jsonObject.put("data", "");
		System.out.println(jsonObject);
		Map<String,Object> model = new HashMap<String,Object>(); 
		if("2002".equals(jsonObject.get("resCode"))){
			ModelAndView mav = new ModelAndView("paysuccess");//实例化一个VIew的ModelAndView实例
			mav.addObject("model", jsonObject);//添加一个带名的model对象
			System.out.println("model==="+model);
	        return mav;
		}else{
			ModelAndView mav = new ModelAndView("payerror");//实例化一个VIew的ModelAndView实例
			mav.addObject("model", jsonObject);//添加一个带名的model对象
			System.out.println("model==="+model);
	        return mav;
		}
		
	}
	
	
}
