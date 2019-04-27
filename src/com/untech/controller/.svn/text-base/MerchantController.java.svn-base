package com.untech.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.SimpleFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.java2d.pipe.SpanShapeRenderer.Simple;

import com.untech.service.MerchantService;
import com.untech.util.merchant.EbankSign;
import com.untech.util.merchant.JsonTools;

import freemarker.template.SimpleDate;

/**
 * MerchantController
 * @author cheny
 *  主要实现扫码支付对接
 * 
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Scope("prototype")
@Controller
@RequestMapping(value = "/api/merchant/sc/trade/")
public class MerchantController {

	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(MerchantController.class.getName());
	
	private JSONObject jsonObject = new JSONObject();
	
	@Autowired
	private MerchantService merchantService;
	 
	/**
	 * create 5.1.2	付款码支付订单创建
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/create")
	@ResponseBody
	public void create(HttpServletRequest request,HttpServletResponse response) throws Exception {
		 
		  System.out.println("encoding:"+request.getCharacterEncoding());  
		  request.setCharacterEncoding("UTF-8");  
		  InputStream is = request.getInputStream();
		  System.out.println(request.getHeaderNames());
		  System.out.println(request.getHeader("referer"));
		  Map<String, String> headMap = new HashMap<String, String>();
		  Enumeration headerNames = request.getHeaderNames();
	      while (headerNames.hasMoreElements()) {
	         String key = (String) headerNames.nextElement();
	         String value = request.getHeader(key);
	         headMap.put(key, value);
	      }
		  System.out.println("headMap============="+headMap);
		  System.out.println("contenttype"+request.getContentType());  
		  response.setContentType("application/json;charset=utf-8");  
		  String jsonStr = "";  
		  StringBuilder sb = new StringBuilder();;  
		  try {  
			  jsonStr = convertStreamToString(is);  
			  sb.append(jsonStr);  
			  System.out.println(sb+"------------报文体字符串");
			  Map bodyMap = JsonTools.parseJSON2Map(jsonStr);
			  System.out.println("报文体字符串==="+bodyMap);
			  Set set = bodyMap.entrySet();        
			  Iterator i = set.iterator();        
			  while(i.hasNext()){     
			       Map.Entry<String, String> entry1=(Map.Entry<String, String>)i.next();   
			       headMap.put(entry1.getKey(),entry1.getValue());   
			  }   
		  } catch (Exception e1) {  
		      e1.printStackTrace();  
		  }  
		  List ebankList = merchantService.queryMerchantParam(headMap);
		  String pubkey = "";
		  if(ebankList != null && ebankList.size() > 0){
				pubkey = ((Map)ebankList.get(0)).get("PUBKEY").toString();
		  }
		  String sign = headMap.get("sign");
		  boolean flag  = EbankSign.verify(headMap, sign, pubkey);
		  System.out.println("flag===签名验证结果================"+flag);
		  
		  Map returnMap = new HashMap();
		  if(flag){//签名验证通过
			  //查询付款码是否使用或存在
			  SimpleDateFormat format = new SimpleDateFormat();
			  
			  
			  //查询订单号是否唯一
			  
			  
		  }else{//签名验证失败
			  
		  }
		  
		  OutputStream out = response.getOutputStream();  
		  OutputStreamWriter ouputw = new OutputStreamWriter(out,"UTF-8");   
		  String str2 = sb.toString();  
		  System.out.println("返回json字符串*********:"+str2);  
		  ouputw.write(str2);  
		  ouputw.flush();  
		  ouputw.close();

	}
	
	public static String convertStreamToString(InputStream is) throws Exception{  
	     BufferedReader in = new BufferedReader(new InputStreamReader(is,"utf-8"));  
	     StringBuffer buffer = new StringBuffer();  
	     String line = "";  
	     while ((line = in.readLine()) != null){  
	       buffer.append(line).append("\n");  
	     }  
	     return buffer.toString();  
	  }  
	 
	 
	
}
