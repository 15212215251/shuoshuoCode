package com.untech.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.untech.dao.AppMoneyDao;
import com.untech.dao.AppPaymxDao;
import com.untech.dao.AppSecurtDao;
import com.untech.dao.AppSignDao;
import com.untech.dao.AppUnitDao;
import com.untech.dao.CommerDao;
import com.untech.dao.OrderDao;
import com.untech.service.PayService;
import com.untech.util.IdSequence;
import com.untech.util.PayNotify;
import com.untech.util.PayUtil_AHNJ;
import com.untech.util.PayUtil_YDYH;
import com.untech.util.RedisUtil;
import com.untech.util.ResponseModel;
import com.untech.util.RestUtil;
import com.untech.util.ShortConnection;
import com.untech.util.SignAndVertyUtil;
import com.untech.util.SocketClient;
import com.untech.util.XmlUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("payService")
@Transactional
public class PayServiceImpl implements PayService {
	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(PayServiceImpl.class.getName());
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private AppMoneyDao appMoneyDao;
	
	@Autowired
	private AppSignDao appSignDao;
	
	@Autowired
	private AppUnitDao appUnitDao;
	

	@Autowired
	private AppSecurtDao appSecurtDao;

	@Autowired
	private CommerDao commerDao;
	
	@Autowired
	private AppPaymxDao appPaymxDao;
	
	public Map queryUserNoByCardno(Map map){
		Map returnMap = new HashMap();
		List signList = appSignDao.queryBankcardList(map);// 查询签约银行卡
		if(signList != null && signList.size() > 0){
			for (int i = 0; i < signList.size(); i++) {//channelNo
				returnMap.put("userNo", ((Map)signList.get(i)).get("USERNO").toString());
			}
		}
		return returnMap;
	}
	
	 
	// map  userNo
	public Map queryBankcardList(Map map) throws Exception {
		Map returnMap = new HashMap();
		map.put("USERID", map.get("userNo"));
		map.put("id", map.get("userNo"));
		List balanceList = appMoneyDao.queryBalance(map);// 查询电子钱包金额
		List signList = appSignDao.queryBankcardList(map);// 查询签约银行卡
		List payTypeList = orderDao.queryLastOrder(map);// 最近消费
		List securtList = appSecurtDao.getAppSecurt(map);
		if(securtList  != null && securtList.size() > 0){
			int cnts = Integer.parseInt(((Map)securtList.get(0)).get("CNTS").toString());
			if(cnts >= 3 ){
				returnMap.put("securtStatus", "2");
			}else{
				returnMap.put("securtStatus", "1");
			}
		}else{
			returnMap.put("securtStatus", "0");
		}
		if (balanceList != null && balanceList.size() > 0) {
			returnMap.put("balance", ((Map) balanceList.get(0)).get("TOTALAMT").toString());
		}else{
			returnMap.put("balance","0.00");
		}
		Map cardMap = new HashMap();
		String cardno= "";
		if (payTypeList != null && payTypeList.size() > 0) {// 有最近消费
			if ("02".equals(((Map) payTypeList.get(0)).get("MONEYFLAG").toString())){// 最近消费是余额支付
				// (01银行卡 02余额支付 03积分支付 04银行卡+积分 05银行卡+余额 06余额+积分 07银行卡+余额+积分)
				//0 未支付  1 电子钱包支付 2 银行卡支付
				returnMap.put("lastPay", "1");
			} else {//最近消费是银行卡支付
				if("".equals(((Map) payTypeList.get(0)).get("ACCTNO").toString())){
					returnMap.put("lastPay", "1");
				}else{
					returnMap.put("lastPay", "2");
					cardno = ((Map) payTypeList.get(0)).get("ACCTNO").toString();
					cardMap.put("cardNo", ((Map) payTypeList.get(0)).get("ACCTNO").toString());
					cardMap.put("bankNo", ((Map) payTypeList.get(0)).get("BANK").toString());
					cardMap.put("mobile", ((Map) payTypeList.get(0)).get("PHONE").toString());
					cardMap.put("channelNo","01");
				}
			}
		} else {// 没有最近消费
			returnMap.put("lastPay", "0");
		}
		List cardList = new ArrayList();
		if(signList != null && signList.size() > 0){
			returnMap.put("cardCount", signList.size());
			if(cardMap.get("cardNo") != null ){
				cardList.add(cardMap);
			}
			for (int i = 0; i < signList.size(); i++) {//channelNo
				if(!((Map)signList.get(i)).get("ACCTNO").toString().equals(cardno)){
					cardMap = new HashMap();
					cardMap.put("cardNo", ((Map) signList.get(i)).get("ACCTNO").toString());
					cardMap.put("bankNo", ((Map) signList.get(i)).get("BANK").toString());
					cardMap.put("mobile", ((Map) signList.get(i)).get("PHONE").toString());
					cardMap.put("channelNo","01");
					cardList.add(cardMap);
				}
			}
		}else{
			returnMap.put("lastPay", "1");
			returnMap.put("cardCount", "0");
		}
		returnMap.put("cardList", cardList);
		return returnMap;
	}


	// map  userNo  feeType
	public Map queryUnits(Map map)  throws Exception{
		List balanceList = appUnitDao.queryUnits(map);
		List unitList = new ArrayList();
		Map returnMap = new HashMap();
		if(balanceList != null && balanceList.size() > 0){
			returnMap.put("unitCount", balanceList.size());
			for(int i=0;i<balanceList.size();i++){
				Map cardMap = new HashMap();
				cardMap.put("unitNo", ((Map) balanceList.get(i)).get("UNITNO").toString());
				cardMap.put("unitName", ((Map) balanceList.get(i)).get("UNITNAME").toString());
				unitList.add(cardMap);
			}
		}else{
			returnMap.put("unitCount", "0");
		}
		returnMap.put("unitList", unitList);
		returnMap.put("feeType", map.get("feeType").toString());
		return returnMap;
	}
	 
	

	/**
	 * 支付
	 */
	public Object payFee(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		map.put("id", map.get("userNo"));
		map.put("USERID", map.get("userNo"));
		// 需要校验用户支付密码是否正确
		List list = appSecurtDao.getAppSecurt(map);
		if (list == null || list.size() == 0) {
			jsonObject.put("resCode", "2001");
			jsonObject.put("resMsg", "未设置支付密码");
			return jsonObject;
		}
		Map securtMap = (Map) list.get(0);
		int cnts  = Integer.parseInt(securtMap.get("CNTS").toString());
		// 支付密码锁住
		if ( cnts >= 3) {
			jsonObject.put("resCode", "2002");
			jsonObject.put("resMsg", "支付密码次数超限，请重置支付密码【短信验证方式】");
			return jsonObject;
		}
		// 支付密码不正确
		if (!securtMap.get("SECURT").toString().equals(map.get("paySecurt").toString())) {
			appSecurtDao.updateSecurt(map);
			jsonObject.put("resCode", "2003");
			jsonObject.put("resMsg", "支付密码不正确，还有"+(2-cnts)+"次机会");
			return jsonObject;
		} else {// 密码正确更新错误次数为0
			if(cnts != 0){
				appSecurtDao.updateCnts(map);
			}
		}
		//判断订单号是否存在
		List orderList = orderDao.queryOrder(map);
		if(orderList != null && orderList.size() > 0){
			jsonObject.put("resCode", "3004");
			jsonObject.put("resMsg", "订单号重复");
			return jsonObject;
		}
		String check = RedisUtil.setRedis(map.get("userNo").toString(), map.get("feeType").toString()+map.get("unitNo").toString()+map.get("totalAmt").toString(), map.get("orderId").toString());
		if("0".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统异常[redis]");
 		     return jsonObject;
		}else if("1".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统忙,请稍后再试");
 		     return jsonObject;
		}
		if("01".equals(map.get("payType").toString())){
			return cardPayment(map);
		}else if("02".equals(map.get("payType").toString())){
			return balancePayment(map);
		}
		return null;
	}
	
	/**
	 * 支付
	 */
	public Object payFeeEdu(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		map.put("id", map.get("userNo"));
		map.put("USERID", map.get("userNo"));
		// 需要校验用户支付密码是否正确
		List list = appSecurtDao.getAppSecurt(map);
		if (list == null || list.size() == 0) {
			jsonObject.put("resCode", "2001");
			jsonObject.put("resMsg", "未设置支付密码");
			return jsonObject;
		}
		Map securtMap = (Map) list.get(0);
		int cnts  = Integer.parseInt(securtMap.get("CNTS").toString());
		// 支付密码锁住
		if ( cnts >= 3) {
			jsonObject.put("resCode", "2002");
			jsonObject.put("resMsg", "支付密码次数超限，请重置支付密码【短信验证方式】");
			return jsonObject;
		}
		// 支付密码不正确
		if (!securtMap.get("SECURT").toString().equals(map.get("paySecurt").toString())) {
			appSecurtDao.updateSecurt(map);
			jsonObject.put("resCode", "2003");
			jsonObject.put("resMsg", "支付密码不正确，还有"+(2-cnts)+"次机会");
			return jsonObject;
		} else {// 密码正确更新错误次数为0
			if(cnts != 0){
				appSecurtDao.updateCnts(map);
			}
		}
		//判断订单号是否存在
		List orderList = orderDao.queryOrder(map);
		if(orderList != null && orderList.size() > 0){
			jsonObject.put("resCode", "3004");
			jsonObject.put("resMsg", "订单号重复");
			return jsonObject;
		}
		String check = RedisUtil.setRedis(map.get("userNo").toString(), map.get("feeType").toString()+map.get("unitNo").toString()+map.get("totalAmt").toString(), map.get("orderId").toString());
		if("0".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统异常[redis]");
 		     return jsonObject;
		}else if("1".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统忙,请稍后再试");
 		     return jsonObject;
		}
		if("02".equals(map.get("payType").toString())){
			return balancePaymentEdu(map);
		}else if("01".equals(map.get("payType").toString())){
			jsonObject.put("resCode", "1003");
			jsonObject.put("resMsg", "参数不符[payType]");
			return jsonObject;
		}
		return null;
	}
	
	
	/**
	 * 支付
	 */
	public Object payTmallFee(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		map.put("id", map.get("userNo"));
		map.put("USERID", map.get("userNo"));
		// 需要校验用户支付密码是否正确
		List list = appSecurtDao.getAppSecurt(map);
		if (list == null || list.size() == 0) {
			jsonObject.put("resCode", "2001");
			jsonObject.put("resMsg", "未设置支付密码");
			return jsonObject;
		}
		Map securtMap = (Map) list.get(0);
		int cnts  = Integer.parseInt(securtMap.get("CNTS").toString());
		// 支付密码锁住
		if ( cnts >= 3) {
			jsonObject.put("resCode", "2002");
			jsonObject.put("resMsg", "支付密码次数超限，请重置支付密码【短信验证方式】");
			return jsonObject;
		}
		// 支付密码不正确
		if (!securtMap.get("SECURT").toString().equals(map.get("paySecurt").toString())) {
			appSecurtDao.updateSecurt(map);
			jsonObject.put("resCode", "2003");
			jsonObject.put("resMsg", "支付密码不正确，还有"+(2-cnts)+"次机会");
			return jsonObject;
		} else {// 密码正确更新错误次数为0
			if(cnts != 0){
				appSecurtDao.updateCnts(map);
			}
		}
		//判断订单号是否存在
		List orderList = orderDao.queryOrder(map);
		if(orderList != null && orderList.size() > 0){
			jsonObject.put("resCode", "3004");
			jsonObject.put("resMsg", "订单号重复");
			return jsonObject;
		}
		String check = RedisUtil.setRedis(map.get("userNo").toString(), map.get("feeType").toString()+map.get("unitNo").toString()+map.get("totalAmt").toString(), map.get("orderId").toString());
		if("0".equals(check)){
			 jsonObject.put("flag", "4007");
			 jsonObject.put("reason", "系统异常[redis]");
  		     return jsonObject;
		}else if("1".equals(check)){
			 jsonObject.put("flag", "4007");
			 jsonObject.put("reason", "系统忙,请稍后再试");
  		     return jsonObject;
		}
		if("01".equals(map.get("payType").toString())){
			return cardPaymentTmall(map);
		}else if("02".equals(map.get("payType").toString())){
			return balancePaymentTmall(map);
		}
		return null;
	}
	
	
	/**
	 * 余额支付
	 */
	
	public Object balancePayment(Map map) throws Exception{
		
		JSONObject jsonObject = new JSONObject();
		// 初始化返回数据
		jsonObject.put("orderId", map.get("orderId").toString());
		String payerTime = formatter.format(new Date()); 
		jsonObject.put("payerTime",payerTime);
		jsonObject.put("seqNo","");
		List moneyList = appMoneyDao.queryBalance(map);// 查询余额
		// 判断余额是否够
		if (moneyList != null && moneyList.size() > 0) {
			//BigDecimal totalAmt = new BigDecimal(map.get("totalAmt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal balanceAmt = new BigDecimal(map.get("balanceAmt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
			//钱包余额
			BigDecimal balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
			if (balance.compareTo(balanceAmt) >= 0 ) {// 余额足够
				/**
				 * 转账报文,调用中台(中间账号转账至商户账号)
				 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
				 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
				 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
				 */
				StringBuffer sb = new StringBuffer();
				sb.append("0002|WJBZ|" + map.get("orderId").toString() + "|");
				List unitList = appUnitDao.queryUnitByUnitNo(map);
				List inList = null;
				String unitName = "";
				String merchantId = "";
				if(unitList != null && unitList.size()>0){
					// 获取转入账号信息
					inList = commerDao.queryCommer(((Map)unitList.get(0)).get("MERCHANTID").toString());/*商户编号*/
					unitName = ((Map)unitList.get(0)).get("UNITNAME").toString();
					merchantId = ((Map)unitList.get(0)).get("MERCHANTID").toString();
				}else{
					jsonObject.put("resCode", "3002");
					 jsonObject.put("resMsg", "商户信息未配置");
					 return jsonObject;
				}
				// 获取转出账号,从市政府对公账号
				List outList = commerDao.queryCommerByType("99");
				if (null == inList || inList.size() <= 0) {
					jsonObject.put("resCode", "3002");
					jsonObject.put("resMsg", "商户号未配置");
					return jsonObject;
				} else {
					sb.append(((Map) outList.get(0)).get("ACCTNO").toString() + "|" + ((Map) outList.get(0)).get("ACCTNAME").toString() + "|" + ((Map) inList.get(0)).get("ACCTNO").toString() + "|" + ((Map) inList.get(0)).get("ACCTNAME").toString() + "|");
					// 中台转账金额
					sb.append(new BigDecimal(map.get("totalAmt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP) + "|" + "|");
					//执行数据库保存操作
					map.put("payerTime",payerTime);
					map.put("unitName",unitName);
					map.put("flag","0");
					map.put("merchantId",merchantId);
					map.put("transType","1");
					map.put("status","0");
					map.put("oldOrderId","");
					map.put("tmallMerchantId", "");
				    int result = orderDao.insertPaymx(map);
						//orderDao.insertPaymx(map);
					logger.info("保存支付明细：" + result);
					map.put("sb", sb.toString());
					jsonObject.put("resCode", "9999");
					return jsonObject;
				}
			} else {// 余额不够
				jsonObject.put("resCode", "4002");
				jsonObject.put("resMsg", "电子钱包金额不足");
				return jsonObject;
			}
		}else{// 余额不够
			jsonObject.put("resCode", "4002");
			jsonObject.put("resMsg", "电子钱包金额不足");
			return jsonObject;
		}
		 
	}
	
	/**
	 * 教育余额支付
	 */
	
	public Object balancePaymentEdu(Map map) throws Exception{
		
		JSONObject jsonObject = new JSONObject();
		// 初始化返回数据
		jsonObject.put("orderId", map.get("orderId").toString());
		String payerTime = formatter.format(new Date()); 
		jsonObject.put("payerTime",payerTime);
		jsonObject.put("seqNo","");
		List moneyList = appMoneyDao.queryBalance(map);// 查询余额
		// 判断余额是否够
		if (moneyList != null && moneyList.size() > 0) {
			BigDecimal balanceAmt = new BigDecimal(map.get("balanceAmt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
			if (balance.compareTo(balanceAmt) >= 0 ) {// 余额足够
				/**
				 * 转账报文,调用中台(中间账号转账至商户账号)
				 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
				 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
				 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
				 */
				StringBuffer sb = new StringBuffer();
				sb.append("0002|WJBZ|" + map.get("orderId").toString() + "|");
				List unitList = appUnitDao.queryUnitByUnitNo(map);
				List inList = null;
				String unitName = "";
				String merchantId = "";
				if(unitList != null && unitList.size()>0){
					// 获取转入账号信息
					inList = commerDao.queryCommer(((Map)unitList.get(0)).get("MERCHANTID").toString());/*商户编号*/
					unitName = ((Map)unitList.get(0)).get("UNITNAME").toString();
					merchantId = ((Map)unitList.get(0)).get("MERCHANTID").toString();
					//需要后台在appunit表配置支持这个
					String note1 = ((Map)unitList.get(0)).get("NOTE1")==null?"":((Map)unitList.get(0)).get("NOTE1").toString();
					if(!"true".equals(note1)){
						jsonObject.put("resCode", "3002");
						jsonObject.put("resMsg", "商户不支持钱包缴费(T+2)模式");
						return jsonObject;
					}
				}else{
					jsonObject.put("resCode", "3002");
					jsonObject.put("resMsg", "商户信息未配置");
					return jsonObject;
				}
				// 获取转出账号,从市政府对公账号
				List outList = commerDao.queryCommerByType("99");
				if (null == inList || inList.size() <= 0) {
					jsonObject.put("resCode", "3002");
					jsonObject.put("resMsg", "商户号未配置");
					return jsonObject;
				} else {
					sb.append(((Map) outList.get(0)).get("ACCTNO").toString() + "|" + ((Map) outList.get(0)).get("ACCTNAME").toString() + "|" + ((Map) inList.get(0)).get("ACCTNO").toString() + "|" + ((Map) inList.get(0)).get("ACCTNAME").toString() + "|");
					// 中台转账金额
					sb.append(new BigDecimal(map.get("totalAmt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP) + "|" + "|");
					//执行数据库保存操作
					map.put("payerTime",payerTime);
					map.put("unitName",unitName);
					map.put("flag","0");
					map.put("merchantId",merchantId);
					map.put("transType","1");
					map.put("status","0");
					map.put("oldOrderId","");
					map.put("tmallMerchantId", "");
					map.put("note2", "true");
					logger.info("首次更新paymx==>note2=true");
					int result = orderDao.insertPaymxForEdu(map);
					logger.info("保存支付明细：" + result);
					map.put("sb", sb.toString());
					jsonObject.put("resCode", "9999");
					return jsonObject;
				}
			} else {// 余额不够
				jsonObject.put("resCode", "4002");
				jsonObject.put("resMsg", "电子钱包金额不足");
				return jsonObject;
			}
		}else{// 余额不够
			jsonObject.put("resCode", "4002");
			jsonObject.put("resMsg", "电子钱包金额不足");
			return jsonObject;
		}
		 
	}
	
	/**
	 * 余额支付
	 */
	
	public Object balancePaymentTmall(Map map) throws Exception{
		
		JSONObject jsonObject = new JSONObject();
		// 初始化返回数据
		jsonObject.put("orderId", map.get("orderId").toString());
		String payerTime = formatter.format(new Date()); 
		jsonObject.put("payerTime",payerTime);
		jsonObject.put("seqNo","");
		List moneyList = appMoneyDao.queryBalance(map);// 查询余额
		// 判断余额是否够
		if (moneyList != null && moneyList.size() > 0) {
			//BigDecimal totalAmt = new BigDecimal(map.get("totalAmt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal balanceAmt = new BigDecimal(map.get("balanceAmt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
			if (balance.compareTo(balanceAmt) >= 0 ) {// 余额足够
				/**
				 * 转账报文,调用中台(中间账号转账至商户账号)
				 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
				 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
				 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
				 */
				StringBuffer sb = new StringBuffer();
				sb.append("0002|WJBZ|" + map.get("orderId").toString() + "|");
				List unitList = appUnitDao.queryUnitByUnitNo(map);
				List inList = null;
				String unitName = "";
				String merchantId = "";
				String tmallMerchantId = "";
				if(unitList != null && unitList.size()>0){
					 Map m = (Map)unitList.get(0);
					 tmallMerchantId = m.get("NOTE2")==null?"":m.get("NOTE2").toString();
					 if(!"".equals(tmallMerchantId) &&  tmallMerchantId != null  ){
						 if(!tmallMerchantId.equals(map.get("tmallMerchantId"))){
							 jsonObject.put("resCode", "3010");
							 jsonObject.put("resMsg", "商户信息配置有误");
							 return jsonObject;
						 }
					 }else{
						 map.put("tmallMerchantId", "");
						 jsonObject.put("resCode", "3002");
						 jsonObject.put("resMsg", "商户号未配置");
						 return jsonObject;
					 }
					// 获取转入账号信息
					inList = commerDao.queryCommer(tmallMerchantId);/*商户编号*/
					unitName = ((Map)unitList.get(0)).get("UNITNAME").toString();
					merchantId = ((Map)unitList.get(0)).get("MERCHANTID").toString();
				}else{
					jsonObject.put("resCode", "3002");
					 jsonObject.put("resMsg", "商户信息未配置");
					 return jsonObject;
				}
				// 获取转出账号,从市政府对公账号
				List outList = commerDao.queryCommerByType("99");
				if (null == inList || inList.size() <= 0) {
					jsonObject.put("resCode", "3002");
					jsonObject.put("resMsg", "商户号未配置");
					return jsonObject;
				} else {
					sb.append(((Map) outList.get(0)).get("ACCTNO").toString() + "|" + ((Map) outList.get(0)).get("ACCTNAME").toString() + "|" + ((Map) inList.get(0)).get("ACCTNO").toString() + "|" + ((Map) inList.get(0)).get("ACCTNAME").toString() + "|");
					// 中台转账金额
					sb.append(new BigDecimal(map.get("totalAmt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP) + "|" + "|");
					//执行数据库保存操作
					map.put("payerTime",payerTime);
					map.put("unitName",unitName);
					map.put("flag","0");
					map.put("merchantId",merchantId);
					map.put("transType","1");
					map.put("status","0");
					map.put("oldOrderId","");
				    int result = orderDao.insertPaymx(map);
						//orderDao.insertPaymx(map);
					logger.info("保存支付明细：" + result);
					map.put("sb", sb.toString());
					jsonObject.put("resCode", "9999");
					return jsonObject;
				}
			} else {// 余额不够
				jsonObject.put("resCode", "4002");
				jsonObject.put("resMsg", "电子钱包金额不足");
				return jsonObject;
			}
		}else{// 余额不够
			jsonObject.put("resCode", "4002");
			jsonObject.put("resMsg", "电子钱包金额不足");
			return jsonObject;
		}
		 
	}
	//
	public Object cardPayment(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		// 初始化返回数据
		jsonObject.put("orderId", map.get("orderId").toString());
		String payerTime = formatter.format(new Date()); 
		jsonObject.put("payerTime",payerTime);
		jsonObject.put("seqNo","");
		//校验卡号、手机号是否匹配
		map.put("acctNo", map.get("cardNo").toString());
		List cardList = appSignDao.querySignByCard(map);
		if(cardList != null && cardList.size()>0){
			Map cardMap = (Map) cardList.get(0);
			String phone = cardMap.get("PHONE").toString();
			if(!map.get("mobile").toString().equals(phone)){
				jsonObject.put("resCode", "4010");
				jsonObject.put("resMsg", "与银行预留手机不一致");
				return jsonObject;
			}
			map.put("outCardNo",((Map)cardList.get(0)).get("ACCTNO"));
			map.put("outCardName",((Map)cardList.get(0)).get("ACCTNAME"));
		}else{
			jsonObject.put("resCode", "4004");
			jsonObject.put("resMsg", "银行卡未签约");
			return jsonObject;
		}
		/*System.out.println(map);
		List signList = appSignDao.querySign(map);
		String signNo = "";
		if(signList != null && signList.size() > 0){
			Map m = (Map) signList.get(0);
			signNo = m.get("SIGNNO") == null ? "" : m.get("SIGNNO").toString();
			String status = m.get("STATUS") == null ? "" : m.get("STATUS").toString();
			if(!"0".equals(status)){
				jsonObject.put("resCode", "4004");
				jsonObject.put("resMsg", "银行卡未签约");
				return jsonObject;
			}
		 }else{
			 jsonObject.put("resCode", "4005");
			 jsonObject.put("resMsg", "银行卡签约协议号不存在");
			 return jsonObject;
		 } */
		 
		//对公账户验证
		 List unitList = appUnitDao.queryUnitByUnitNo(map);
		 String merchantId  = "";
		 String unitName = "";
		 if(unitList != null && unitList.size()>0){
			 merchantId  = ((Map)unitList.get(0)).get("MERCHANTID").toString();
			 unitName = ((Map)unitList.get(0)).get("UNITNAME").toString();
			 Map m = (Map)unitList.get(0);
			 // 获取转入
			 List inList = commerDao.queryCommer(merchantId);/*商户编号*/
			 if (inList == null || inList.size() == 0) {
				 jsonObject.put("resCode", "3002");
				 jsonObject.put("resMsg", "商户号未配置");
				 
				 return jsonObject;
			 }else{
				 map.put("inCardNo",((Map)inList.get(0)).get("ACCTNO"));
				 map.put("inCardName",((Map)inList.get(0)).get("ACCTNAME"));
			 }
		 }else{
			 jsonObject.put("resCode", "3002");
			 jsonObject.put("resMsg", "商户号未配置");
			 return jsonObject;
		 }
		 
		map.put("tmallMerchantId", "");
		 
		//执行数据库保存操作
		 /**
		  * sb.append(map.get("outCardNo").toString() + "|"+map.get("outCardName").toString() + "|");
		sb.append(map.get("inCardNo").toString() + "|"+map.get("inCardName").toString() + "|");
		  * */
		 
		map.put("payerTime",payerTime);
		map.put("unitName",unitName);
		map.put("flag","0");
		map.put("merchantId",merchantId);
		map.put("transType","1");
		map.put("status","1");
		map.put("oldOrderId","");
		int result = orderDao.insertPaymx(map);
		map.put("merchantId", merchantId);
		map.put("payerTime", payerTime);
		//map.put("signNo", signNo);
		if(result == 1){
			jsonObject.put("resCode", "9999");
		}else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		}
		logger.info("保存支付明细：" + result);
		return 	jsonObject;
		
	}
	
	
	public Object cardPaymentTmall(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		// 初始化返回数据
		jsonObject.put("orderId", map.get("orderId").toString());
		String payerTime = formatter.format(new Date()); 
		jsonObject.put("payerTime",payerTime);
		jsonObject.put("seqNo","");
		//校验卡号、手机号是否匹配
		map.put("acctNo", map.get("cardNo").toString());
		List cardList = appSignDao.querySignByCard(map);
		if(cardList != null && cardList.size()>0){
			Map cardMap = (Map) cardList.get(0);
			String phone = cardMap.get("PHONE").toString();
			if(!map.get("mobile").toString().equals(phone)){
				jsonObject.put("resCode", "4010");
				jsonObject.put("resMsg", "与银行预留手机不一致");
				return jsonObject;
			}
			map.put("outCardNo",((Map)cardList.get(0)).get("ACCTNO"));
			map.put("outCardName",((Map)cardList.get(0)).get("ACCTNAME"));
		}else{
			jsonObject.put("resCode", "4004");
			jsonObject.put("resMsg", "银行卡未签约");
			return jsonObject;
		}
		 
		 List unitList = appUnitDao.queryUnitByUnitNo(map);
		 String merchantId  = "";
		 String unitName = "";
		 String tmallMerchantId = "";
		 if(unitList != null && unitList.size()>0){
			 merchantId  = ((Map)unitList.get(0)).get("MERCHANTID").toString();
			 unitName = ((Map)unitList.get(0)).get("UNITNAME").toString();
			 Map m = (Map)unitList.get(0);
			 tmallMerchantId = m.get("NOTE2")==null?"":m.get("NOTE2").toString();
			 
			 //判断支付宝模式的账号信息
			 System.out.println(tmallMerchantId+"----tmallMerchantId");
			 if(!"".equals(tmallMerchantId) &&  tmallMerchantId != null  ){
				 if(!tmallMerchantId.equals(map.get("tmallMerchantId"))){
					 jsonObject.put("resCode", "3010");
					 jsonObject.put("resMsg", "商户信息配置有误");
					 return jsonObject;
				 }
				 // 获取转入
				 List inList = commerDao.queryCommer(tmallMerchantId);/*商户编号*/
				 if (inList == null || inList.size() == 0) {
					 jsonObject.put("resCode", "3002");
					 jsonObject.put("resMsg", "商户号未配置");
					 
					 return jsonObject;
				 }else{
					 map.put("inCardNo",((Map)inList.get(0)).get("ACCTNO"));
					 map.put("inCardName",((Map)inList.get(0)).get("ACCTNAME"));
				 }
			 }else{
				 map.put("tmallMerchantId", "");
				 jsonObject.put("resCode", "3002");
				 jsonObject.put("resMsg", "商户号未配置");
				 return jsonObject;
				 
			 }
			 
		 }else{
			 jsonObject.put("resCode", "3002");
			 jsonObject.put("resMsg", "商户号未配置");
			 return jsonObject;
		 }
		 
		
		 
		//执行数据库保存操作
		 /**
		  * sb.append(map.get("outCardNo").toString() + "|"+map.get("outCardName").toString() + "|");
		sb.append(map.get("inCardNo").toString() + "|"+map.get("inCardName").toString() + "|");
		  * */
		 
		map.put("payerTime",payerTime);
		map.put("unitName",unitName);
		map.put("flag","0");
		map.put("merchantId",merchantId);
		map.put("transType","1");
		map.put("status","1");
		map.put("oldOrderId","");
		int result = orderDao.insertPaymx(map);
		map.put("merchantId", merchantId);
		map.put("payerTime", payerTime);
		//map.put("signNo", signNo);
		if(result == 1){
			jsonObject.put("resCode", "9999");
		}else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		}
		logger.info("保存支付明细：" + result);
		return 	jsonObject;
		
	}
	
	public Object cardPayment_ahnj(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		// 初始化返回数据
		jsonObject.put("orderId", map.get("orderId").toString());
		String payerTime = formatter.format(new Date()); 
		jsonObject.put("payerTime",payerTime);
		jsonObject.put("seqNo","");
		//校验卡号、手机号是否匹配
		map.put("acctNo", map.get("cardNo").toString());
		List cardList = appSignDao.querySignByCard(map);
		if(cardList != null && cardList.size()>0){
			Map cardMap = (Map) cardList.get(0);
			String phone = cardMap.get("PHONE").toString();
			if(!map.get("mobile").toString().equals(phone)){
				jsonObject.put("resCode", "4010");
				jsonObject.put("resMsg", "与银行预留手机不一致");
				return jsonObject;
			}
		}else{
			jsonObject.put("resCode", "4004");
			jsonObject.put("resMsg", "银行卡未签约");
			return jsonObject;
		}
		System.out.println(map);
		List signList = appSignDao.querySign(map);
		String signNo = "";
		if(signList != null && signList.size() > 0){
			Map m = (Map) signList.get(0);
			signNo = m.get("SIGNNO") == null ? "" : m.get("SIGNNO").toString();
			String status = m.get("STATUS") == null ? "" : m.get("STATUS").toString();
			if(!"0".equals(status)){
				jsonObject.put("resCode", "4004");
				jsonObject.put("resMsg", "银行卡未签约");
				return jsonObject;
			}
		 }else{
			 jsonObject.put("resCode", "4005");
			 jsonObject.put("resMsg", "银行卡签约协议号不存在");
			 return jsonObject;
		 } 
		 
		 List unitList = appUnitDao.queryUnitByUnitNo(map);
		 String merchantId  = "";
		 String unitName = "";
		 if(unitList != null && unitList.size()>0){
			 merchantId  = ((Map)unitList.get(0)).get("MERCHANTID").toString();
			 unitName = ((Map)unitList.get(0)).get("UNITNAME").toString();
			 // 获取转入
			 List inList = commerDao.queryCommer(merchantId);/*商户编号*/
			 if (inList == null || inList.size() == 0) {
				 jsonObject.put("resCode", "3002");
				 jsonObject.put("resMsg", "商户号未配置");
				 return jsonObject;
			 }
		 }else{
			 jsonObject.put("resCode", "3002");
			 jsonObject.put("resMsg", "商户号未配置");
			 return jsonObject;
		 }
		 
		//执行数据库保存操作
		map.put("payerTime",payerTime);
		map.put("unitName",unitName);
		map.put("flag","0");
		map.put("merchantId",merchantId);
		map.put("transType","1");
		map.put("status","1");
		map.put("oldOrderId","");
		map.put("tmallMerchantId", "");
		int result = orderDao.insertPaymx(map);
		map.put("merchantId", merchantId);
		map.put("payerTime", payerTime);
		map.put("signNo", signNo);
		if(result == 1){
			jsonObject.put("resCode", "9999");
		}else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		}
		logger.info("保存支付明细：" + result);
		return 	jsonObject;
		
	}
	//支付  银行卡扣款更新状态   走安徽农金
	public Object cardMain_AHNJ(Map map) throws Exception{
		 JSONObject jsonObject = new JSONObject();
		 PayUtil_AHNJ ahnj = new PayUtil_AHNJ();
		 Map<String,String> returnMap = ahnj.payMoney(map);
		 if("0000".equals(returnMap.get("flag"))){
			//如果银行支付成功修改状态
			map.put("flag", "2");
			int updateStatus = orderDao.updatePayMxStatusByCard(map);
			logger.debug("updateStatus === "+updateStatus);
			//回调接口 
			List orderList = orderDao.queryOrder(map);
			if(orderList != null && orderList.size() > 0){
				Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
				Map notifyMap = PayNotify.notify(orderMap);
				logger.debug("notifyMap === "+notifyMap);
			}
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "支付成功");
			jsonObject.put("orderId",map.get("orderId").toString());
			jsonObject.put("payerTime",map.get("payerTime").toString());
			jsonObject.put("seqNo","");
			return jsonObject;
		 }else if("0001".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
	   		jsonObject.put("resMsg", returnMap.get("errorMessage"));
	   		return jsonObject;
		 }else if("9999".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
			jsonObject.put("resMsg", returnMap.get("errorMessage"));
			return jsonObject;
		 }else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		 }
	}
	
	//支付  银行卡扣款更新状态   走安徽农金
	/**
	 * map.put("middleflow1", returnMap.get("middleflow1"));
			map.put("middledate1", returnMap.get("middledate1"));
			map.put("coreflow1", returnMap.get("coreflow1"));
			map.put("coredate1", returnMap.get("coredate1"));
	 * */
	public Object cardMain(Map map,String flag) throws Exception{
		 JSONObject jsonObject = new JSONObject();
		 PayUtil_YDYH ahnj = new PayUtil_YDYH();
		 Map<String,String> returnMap = ahnj.payMoney(map);
		 if("0000".equals(returnMap.get("flag"))){
			//如果银行支付成功修改状态
			map.put("flag", flag);
			map.put("status", "1");
			map.put("middleflow1", returnMap.get("middleflow1"));
			map.put("middledate1", returnMap.get("middledate1"));
			map.put("coreflow1", returnMap.get("coreflow1"));
			map.put("coredate1", returnMap.get("coredate1"));
			int updateStatus = orderDao.updatePayMxStatusByCard(map);
			logger.debug("updateStatus === "+updateStatus);
			//回调接口 
			List orderList = orderDao.queryOrder(map);
			if(orderList != null && orderList.size() > 0){
				Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
				Map notifyMap = PayNotify.notify(orderMap);
				logger.debug("notifyMap === "+notifyMap);
			}
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "支付成功");
			jsonObject.put("orderId",map.get("orderId").toString());
			jsonObject.put("payerTime",map.get("payerTime").toString());
			jsonObject.put("seqNo","");
			return jsonObject;
		 }else if("0001".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
	   		jsonObject.put("resMsg", returnMap.get("errorMessage"));
	   		return jsonObject;
		 }else if("9999".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
			jsonObject.put("resMsg", returnMap.get("errorMessage"));
			return jsonObject;
		 }else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		 }
	}
	
	
	 
	
	//支付  银行卡扣款更新状态
	public Object cardMain_bak(Map map) {
		 JSONObject jsonObject = new JSONObject();
		 //拼装前往安徽农金支付的报文
		 Map<String,Object>  paymap = new LinkedHashMap<String,Object>();
		 String mId = IdSequence.getMessgeId(map.get("id").toString(), "APReq");
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
		Map<String,Object> signReturnMap = SignAndVertyUtil.SignXml(creport, "APReq");
		ResponseModel model  = (ResponseModel)signReturnMap.get("Error");
		Map<String, Object> map_3  = new HashMap<String, Object>();
		if( model == null){
			map_3 = (Map<String, Object>)signReturnMap.get("APRes");
			if(  map_3 == null){
				jsonObject.put("resCode", "4001");
				jsonObject.put("resMsg", "银行扣款返回错误，请联系管理员");
				return jsonObject;
			} else{
				//如果银行支付成功修改状态
				map.put("flag", "2");
				orderDao.updatePayMxStatusByCard(map);
				//回调接口 
				List orderList = orderDao.queryOrder(map);
				if(orderList != null && orderList.size() > 0){
					Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
					Map notifyMap = PayNotify.notify(orderMap);
					logger.debug("notifyMap === "+notifyMap);
				}
				jsonObject.put("resCode", "0000");
				jsonObject.put("resMsg", "支付成功");
				jsonObject.put("orderId",map.get("orderId").toString());
				jsonObject.put("payerTime",map.get("payerTime").toString());
				jsonObject.put("seqNo","");
				return jsonObject;
			}
		}else{
			 logger.info("支付失败");
	    	 logger.info("订单号:"+map.get("orderId").toString()+"支付返回码:"+model.getErrorCode()+"支付返回信息"+model.getErrorMessage()+"支付失败!");
	    	 jsonObject.put("resCode", "4006");
	    	 jsonObject.put("resMsg", model.getErrorDetail());
	    	 return jsonObject;
		}
	}
	
	//余额支付和更新
	public Object balanceMain_bak(Map map){
		/**
		 * 转账报文,调用中台(中间账号转账至商户账号)
		 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
		 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
		 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
		 */
		String  sb = map.get("sb").toString();
		String[] request  = sb.split("\\|");
		BigDecimal balanceAmt =  new BigDecimal(map.get("balanceAmt").toString());
		JSONObject jsonObject = new JSONObject();
		// 发送给银行端
		SocketClient client = new SocketClient();
//		String returnMsg = client.sendMessage(sb.toString());
		/** returnMsg测试 **/
		String returnMsg = "0000|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|20150101|05341234|20150102|05351235|备注|加密串|";
		// 校验报文
		if (returnMsg == null || "".equals(returnMsg.trim()) || returnMsg.length() == 4) {
			if ("2190".equals(returnMsg)) { // 超时调用冲正
				StringBuffer centBuffcz = new StringBuffer();
				centBuffcz.append("0008|WJBZ|" + map.get("orderId").toString() + "|");
				centBuffcz.append(request[3] + "|" + request[4]  + "|" + request[5]  + "|" + request[6]  + "|");
				centBuffcz.append(new BigDecimal(map.get("totalAmt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP) + "|" + "|");
				String czStr = client.sendMessage(centBuffcz.toString());
				logger.info("冲正返回报文：" + czStr);
			}
			
			jsonObject.put("resCode", "4001");
			jsonObject.put("resMsg", "银行扣款返回错误，请联系管理员");
			return jsonObject;
		} else {
			String[] response = returnMsg.split("\\|");
			if ("0000".equals(response[0])) {
				// 如果银行支付成功修改状态
				// 更新APP_PAYMX
				map.put("flag", "2");
				map.put("status", "1");
				map.put("middleflow1", response[11]);
				map.put("middledate1", response[10]);
				map.put("coreflow1", response[13]);
				map.put("coredate1", response[12]);
				System.out.println("支付参数＝＝＝＝＝＝》" + map);
				orderDao.updatePayMxStatusByMoney(map);

				/** 使用余额进行支付需要更新账户余额表和账户余额明细表 **/
				List moneyList = appMoneyDao.queryBalance(map);// 查询余额
				BigDecimal balance = new BigDecimal(0.00);
				// 更新账户余额表APP_MONEY
				if (moneyList != null && moneyList.size() > 0) {
				    balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
					BigDecimal qywamt = new BigDecimal(((Map) moneyList.get(0)).get("QYWAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 趣医网返现
					BigDecimal jfamt = new BigDecimal(((Map) moneyList.get(0)).get("JFAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 积分金额
					BigDecimal qtamt = new BigDecimal(((Map) moneyList.get(0)).get("QTAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 其他金额
					BigDecimal yhkamt = new BigDecimal(((Map) moneyList.get(0)).get("YHKAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 银行卡转入金额
					
					if (qywamt.compareTo(balanceAmt) >= 0) {
						map.put("qywamt",qywamt.subtract(balanceAmt).setScale(2,BigDecimal.ROUND_HALF_UP));
					} else if (qywamt.add(jfamt).compareTo(balanceAmt) >= 0) {
						map.put("qywamt", new BigDecimal(0.00));
						map.put("jfamt", jfamt.subtract(balanceAmt.subtract(qywamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
					} else if (qywamt.add(jfamt).add(qtamt).compareTo(balanceAmt) >= 0) {
						map.put("qywamt", new BigDecimal(0.00));
						map.put("jfmat", new BigDecimal(0.00));
						map.put("qtamt", qtamt.subtract(balanceAmt.subtract(qywamt)).subtract(jfamt).setScale(2, BigDecimal.ROUND_HALF_UP));
					} else if (qywamt.add(jfamt).add(qtamt).add(yhkamt).compareTo(balanceAmt) >= 0) {
						map.put("qywamt", new BigDecimal(0.00));
						map.put("jfamt", new BigDecimal(0.00));
						map.put("qtamt", new BigDecimal(0.00));
						map.put("yhkamt", yhkamt.subtract(balanceAmt.subtract(qywamt).subtract(jfamt).subtract(qtamt)));
					}
					map.put("totalamt", balance.subtract(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
					appMoneyDao.updateMoney(map);
				}

				// 更新账户余额明细表APP_MONEYMX
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				Map moneyMxMap = new HashMap();
				moneyMxMap.put("orderId", map.get("orderId").toString() + "0");
				moneyMxMap.put("zforderId", map.get("orderId").toString());
				moneyMxMap.put("payertime", formatter.format(new Date()));
				moneyMxMap.put("userNo", map.get("id").toString());
				moneyMxMap.put("mobile", "");
				moneyMxMap.put("fantime", "");
				moneyMxMap.put("transamt1", balanceAmt);
				moneyMxMap.put("transamt2", map.get("totalAmt").toString());
				moneyMxMap.put("transamt3", balance.subtract(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
				moneyMxMap.put("type", "7");
				moneyMxMap.put("transamt5", "0.00");
				moneyMxMap.put("cardno", "");
				moneyMxMap.put("channerno", "01");
				moneyMxMap.put("transtype", "2");
				moneyMxMap.put("feetype", map.get("feeType").toString());
				moneyMxMap.put("status", "1");
				moneyMxMap.put("middleflow", response[11]);
				moneyMxMap.put("middledate", response[10]);
				moneyMxMap.put("coreflow", response[13]);
				moneyMxMap.put("coredate", response[12]);
				moneyMxMap.put("dzstatus", "0");
				moneyMxMap.put("dzdate", "");
				moneyMxMap.put("dztime", "");
				moneyMxMap.put("note1", "");
				moneyMxMap.put("note2", "");
				moneyMxMap.put("note3", "1");
				moneyMxMap.put("note4", "");
				moneyMxMap.put("note5", "");
				appMoneyDao.insertMoneyMx(moneyMxMap);
				//回调接口 
				List orderList = orderDao.queryOrder(map);
				if(orderList != null && orderList.size() > 0){
					Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
					Map notifyMap = PayNotify.notify(orderMap);
					logger.debug("notifyMap === "+notifyMap);
				}
				jsonObject.put("resCode", "0000");
				jsonObject.put("resMsg", "支付成功");
				jsonObject.put("orderId",map.get("orderId").toString());
				jsonObject.put("payerTime",map.get("payerTime").toString());
				jsonObject.put("seqNo",response[12]+response[13]);
				return jsonObject;
			} else if ("8888".equals(response[0])) {
				jsonObject.put("resCode", "4008");
				jsonObject.put("resMsg", "报文被篡改");
				return jsonObject;
			} else {
				jsonObject.put("resCode", "4009");
				jsonObject.put("resMsg", response[1]);
				return jsonObject;
			}
		}
	}
	
	//余额支付和更新
	public Object balanceMain(Map map,String flag)throws Exception{
		/**
		 * 转账报文,调用中台(中间账号转账至商户账号)
		 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
		 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
		 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
		 */
		String  sb = map.get("sb").toString();
		String[] request  = sb.split("\\|");
		BigDecimal balanceAmt =  new BigDecimal(map.get("balanceAmt").toString());
		map.put("orderId", request[2]);
		map.put("outCardNo", request[3]);
		map.put("outCardName", request[4]);
		map.put("inCardNo", request[5]);
		map.put("inCardName", request[6]);
		map.put("totalAmt", request[7]);
		map.put("remark", "");
		JSONObject jsonObject = new JSONObject();
		PayUtil_YDYH ahnj = new PayUtil_YDYH();
		Map<String,String> returnMap = ahnj.payMoney(map);
		if("0000".equals(returnMap.get("flag"))){
			// 如果银行支付成功修改状态
			// 更新APP_PAYMX
			map.put("flag",flag);
			map.put("status", "1");
			map.put("middleflow1", returnMap.get("middleflow1"));
			map.put("middledate1", returnMap.get("middledate1"));
			map.put("coreflow1", returnMap.get("coreflow1"));
			map.put("coredate1", returnMap.get("coredate1"));
			System.out.println("支付参数＝＝＝＝＝＝》" + map);
			int updateStatus = orderDao.updatePayMxStatusByMoney(map);
			logger.debug("updateStatus === "+updateStatus);
			/** 使用余额进行支付需要更新账户余额表和账户余额明细表 **/
			List moneyList = appMoneyDao.queryBalance(map);// 查询余额
			BigDecimal balance = new BigDecimal(0.00);
			// 更新账户余额表APP_MONEY
			if (moneyList != null && moneyList.size() > 0) {
			    balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
				BigDecimal qywamt = new BigDecimal(((Map) moneyList.get(0)).get("QYWAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 趣医网返现
				BigDecimal jfamt = new BigDecimal(((Map) moneyList.get(0)).get("JFAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 积分金额
				BigDecimal qtamt = new BigDecimal(((Map) moneyList.get(0)).get("QTAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 其他金额
				BigDecimal yhkamt = new BigDecimal(((Map) moneyList.get(0)).get("YHKAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 银行卡转入金额
				
				if (qywamt.compareTo(balanceAmt) >= 0) {
					map.put("qywamt",qywamt.subtract(balanceAmt).setScale(2,BigDecimal.ROUND_HALF_UP));
				} else if (qywamt.add(jfamt).compareTo(balanceAmt) >= 0) {
					map.put("qywamt", new BigDecimal(0.00));
					map.put("jfamt", jfamt.subtract(balanceAmt.subtract(qywamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
				} else if (qywamt.add(jfamt).add(qtamt).compareTo(balanceAmt) >= 0) {
					map.put("qywamt", new BigDecimal(0.00));
					map.put("jfamt", new BigDecimal(0.00));
					map.put("qtamt", qtamt.subtract(balanceAmt.subtract(qywamt).subtract(jfamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
				} else if (qywamt.add(jfamt).add(qtamt).add(yhkamt).compareTo(balanceAmt) >= 0) {
					map.put("qywamt", new BigDecimal(0.00));
					map.put("jfamt", new BigDecimal(0.00));
					map.put("qtamt", new BigDecimal(0.00));
					map.put("yhkamt", yhkamt.subtract(balanceAmt.subtract(qywamt).subtract(jfamt).subtract(qtamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				map.put("totalamt", balance.subtract(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
				updateStatus = appMoneyDao.updateMoney(map);
				logger.debug("updateStatus === "+updateStatus);
			}

			// 更新账户余额明细表APP_MONEYMX
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			Map moneyMxMap = new HashMap();
			moneyMxMap.put("orderId", map.get("orderId").toString() + "0");
			moneyMxMap.put("zforderId", map.get("orderId").toString());
			moneyMxMap.put("payertime", formatter.format(new Date()));
			moneyMxMap.put("userNo", map.get("id").toString());
			moneyMxMap.put("mobile", "");
			moneyMxMap.put("fantime", "");
			moneyMxMap.put("transamt1", balanceAmt);
			moneyMxMap.put("transamt2", map.get("totalAmt").toString());
			moneyMxMap.put("transamt3", balance.subtract(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
			moneyMxMap.put("type", "7");
			moneyMxMap.put("transamt5", "0.00");
			moneyMxMap.put("cardno", "");
			moneyMxMap.put("channerno", "01");
			moneyMxMap.put("transtype", "2");
			moneyMxMap.put("feetype", map.get("feeType").toString());
			moneyMxMap.put("status", "1");
			moneyMxMap.put("middleflow", returnMap.get("middleflow1"));
			moneyMxMap.put("middledate", returnMap.get("middledate1"));
			moneyMxMap.put("coreflow", returnMap.get("coreflow1"));
			moneyMxMap.put("coredate", returnMap.get("coredate1"));
			moneyMxMap.put("dzstatus", "0");
			moneyMxMap.put("dzdate", "");
			moneyMxMap.put("dztime", "");
			moneyMxMap.put("note1", "");
			moneyMxMap.put("note2", "");
			moneyMxMap.put("note3", "1");
			moneyMxMap.put("note4", "");
			moneyMxMap.put("note5", "");
			updateStatus = appMoneyDao.insertMoneyMx(moneyMxMap);
			logger.debug("updateStatus === "+updateStatus);
			//回调接口 
			List orderList = orderDao.queryOrder(map);
			if(orderList != null && orderList.size() > 0){
				Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
				Map notifyMap = PayNotify.notify(orderMap);
				logger.debug("notifyMap === "+notifyMap);
			}
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "支付成功");
			jsonObject.put("orderId",map.get("orderId").toString());
			jsonObject.put("payerTime",map.get("payerTime").toString());
			jsonObject.put("seqNo",returnMap.get("coredate1")+returnMap.get("coreflow1"));
			return jsonObject;
		 }else if("0001".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
		    jsonObject.put("resMsg", returnMap.get("errorMessage"));
		    return jsonObject;
		 }else if("8888".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4008");
	    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
	    	return jsonObject;
		 }else if("9999".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
	    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
	    	return jsonObject;
		 }else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		 }
	}
	
	public Object balanceMainEdu(Map map, String flag) throws Exception {
		/**
		 * 转账报文,调用中台(中间账号转账至商户账号)
		 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
		 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
		 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
		 */
		BigDecimal balanceAmt =  new BigDecimal(map.get("balanceAmt").toString());
		BigDecimal balance = new BigDecimal(0.00);
		JSONObject jsonObject = new JSONObject();
		/** 使用余额进行支付需要更新账户余额表和账户余额明细表 **/
		
		//更新余额表此处不需要改变
		List moneyList = appMoneyDao.queryBalance(map);// 查询余额
		// 更新账户余额表APP_MONEY此处能够保证余额表能够首先的完成工作；
		if (moneyList != null && moneyList.size() > 0) {
		    balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
			BigDecimal qywamt = new BigDecimal(((Map) moneyList.get(0)).get("QYWAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 趣医网返现
			BigDecimal jfamt = new BigDecimal(((Map) moneyList.get(0)).get("JFAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 积分金额
			BigDecimal qtamt = new BigDecimal(((Map) moneyList.get(0)).get("QTAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 其他金额
			BigDecimal yhkamt = new BigDecimal(((Map) moneyList.get(0)).get("YHKAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 银行卡转入金额
			
			if (qywamt.compareTo(balanceAmt) >= 0) {
				map.put("qywamt",qywamt.subtract(balanceAmt).setScale(2,BigDecimal.ROUND_HALF_UP));
			} else if (qywamt.add(jfamt).compareTo(balanceAmt) >= 0) {
				map.put("qywamt", new BigDecimal(0.00));
				map.put("jfamt", jfamt.subtract(balanceAmt.subtract(qywamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
			} else if (qywamt.add(jfamt).add(qtamt).compareTo(balanceAmt) >= 0) {
				map.put("qywamt", new BigDecimal(0.00));
				map.put("jfamt", new BigDecimal(0.00));
				map.put("qtamt", qtamt.subtract(balanceAmt.subtract(qywamt).subtract(jfamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
			} else if (qywamt.add(jfamt).add(qtamt).add(yhkamt).compareTo(balanceAmt) >= 0) {
				map.put("qywamt", new BigDecimal(0.00));
				map.put("jfamt", new BigDecimal(0.00));
				map.put("qtamt", new BigDecimal(0.00));
				map.put("yhkamt", yhkamt.subtract(balanceAmt.subtract(qywamt).subtract(jfamt).subtract(qtamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			map.put("totalamt", balance.subtract(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
			int updateStatus = appMoneyDao.updateMoney(map);
			logger.debug("updateStatus === "+updateStatus);
		}

		// 更新账户余额明细表APP_MONEYMX；
		Map moneyMxMap = new HashMap();
		moneyMxMap.put("orderId", map.get("orderId").toString() + "0");
		moneyMxMap.put("zforderId", map.get("orderId").toString());
		moneyMxMap.put("payertime", formatter.format(new Date()));
		moneyMxMap.put("userNo", map.get("id").toString());
		moneyMxMap.put("mobile", "");
		moneyMxMap.put("fantime", "");
		moneyMxMap.put("transamt1", balanceAmt);
		moneyMxMap.put("transamt2", map.get("totalAmt").toString());
		moneyMxMap.put("transamt3", balance.subtract(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
		moneyMxMap.put("type", "7");
		moneyMxMap.put("transamt5", "0.00");
		moneyMxMap.put("cardno", "");
		moneyMxMap.put("channerno", "01");
		moneyMxMap.put("transtype", "2");
		moneyMxMap.put("feetype", map.get("feeType").toString());
		moneyMxMap.put("status", "1"); //已扣款
		moneyMxMap.put("middleflow", "");
		moneyMxMap.put("middledate", "");
		moneyMxMap.put("coreflow", "");
		moneyMxMap.put("coredate", "");
		moneyMxMap.put("dzstatus", "0");
		moneyMxMap.put("dzdate", "");
		moneyMxMap.put("dztime", "");
		moneyMxMap.put("note1", "");
		moneyMxMap.put("note2", "");
		moneyMxMap.put("note3", "1");
		moneyMxMap.put("note4", "");
		moneyMxMap.put("note5", "");
		//插入一条moneymx记录；
		int updateStatus = appMoneyDao.insertMoneyMx(moneyMxMap);
		logger.debug("updateStatus === "+updateStatus);
		//更新flag = 1
		int updatePayMx = appPaymxDao.updatePaymxFlag1ByOrderId(map);
		logger.debug("updatePayMx === "+updatePayMx);
		//这样就能保证调用者能够看到对应的支付成功从而在页面进行相应的处理；
		jsonObject.put("resCode", "0000");
		jsonObject.put("resMsg", "支付成功");
		jsonObject.put("orderId",map.get("orderId").toString());
		jsonObject.put("seqNo","");
		jsonObject.put("payerTime",map.get("payerTime").toString());
		logger.info(map.get("orderId").toString() + "==订单余额扣款成功");
		return jsonObject;
		
	}

 
    //查询订单明细	
	public Map OrderMxs(Map map) throws Exception{
		Map returnMap = new HashMap();
		List mxList = orderDao.queryPaymxInfo(map);
		returnMap.put("mxList", mxList);
		return returnMap;
	}

    /*退货*/
	public Object backFee(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		//判断订单号是否存在
		List orderList = orderDao.queryOrder(map);
		if(orderList != null && orderList.size() > 0){
			jsonObject.put("resCode", "3004");
			jsonObject.put("resMsg", "订单号重复");
			return jsonObject;
		}
		Map<String, String> orderMap = new HashMap<String, String>();
		orderMap.put("orderId", map.get("oldOrderId").toString());
		orderList = orderDao.queryOrder(orderMap);
		if(orderList == null ||  orderList.size() <= 0){
			jsonObject.put("resCode", "3005");
			jsonObject.put("resMsg", "退费原订单不存在");
			return jsonObject;
		}
		Map oldOrderMap = (Map)orderList.get(0);
		String FEETYPE = oldOrderMap.get("FEETYPE").toString();
		/*if("26".equals(FEETYPE.trim())){
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "新农保暂不支持退费");
			return jsonObject;
		}*/
		
		String check = RedisUtil.setRedis(oldOrderMap.get("USERNO").toString(), map.get("feeType").toString()+map.get("oldOrderId").toString()+map.get("totalAmt").toString(), map.get("orderId").toString());
		if("0".equals(check)){
			 jsonObject.put("flag", "4007");
			 jsonObject.put("reason", "系统异常[redis]");
  		     return jsonObject;
		}else if("1".equals(check)){
			 jsonObject.put("flag", "4007");
			 jsonObject.put("reason", "系统忙,请稍后再试");
  		     return jsonObject;
		}
		
		BigDecimal totalAmt  = new BigDecimal(map.get("totalAmt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);;
		BigDecimal payAmt = new BigDecimal(oldOrderMap.get("PAYAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);;
		BigDecimal backAmt = new BigDecimal(oldOrderMap.get("BACKAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);;
		String transType = oldOrderMap.get("TRANSTYPE").toString();
		String flag = oldOrderMap.get("FLAG").toString();
		if(!"1".equals(transType)){
			jsonObject.put("resCode", "3006");
			jsonObject.put("resMsg", "退费类型不符");
			return jsonObject;
		}
		if("0".equals(flag)){ 
			jsonObject.put("resCode", "3007");
			jsonObject.put("resMsg", "退费原订单未成功");
			return jsonObject;
		}
		if(totalAmt.add(backAmt).compareTo(payAmt) > 0){ 
			jsonObject.put("resCode", "3008");
			jsonObject.put("resMsg", "退费金额大于原订单金额");
			return jsonObject;
		}
		
		//保存退费明细
		String payerTime = formatter.format(new Date()); 
		map.put("payerTime",payerTime);
		map.put("userNo", oldOrderMap.get("USERNO").toString());
		map.put("unitNo", oldOrderMap.get("UNITNO").toString());
		map.put("unitName",oldOrderMap.get("UNITNAME").toString());
		map.put("cardNo", oldOrderMap.get("CARDNO").toString());
		map.put("mobile", oldOrderMap.get("MOBILEPHONE").toString());
		map.put("channelNo", oldOrderMap.get("CHANNERNO").toString());
		map.put("flag","0");
		map.put("backAmt", backAmt);
		map.put("merchantId",oldOrderMap.get("NOTE1").toString());
		map.put("transType","2");
		map.put("detail",oldOrderMap.get("DETAIL").toString());
		String moneyFlag = oldOrderMap.get("MONEYFLAG").toString();
		//银行卡支付
		if("01".equals(moneyFlag) && !("26".equals(FEETYPE) || "13".equals(FEETYPE))){
			//查询银行协议号等信息
			map.put("balanceAmt","0.00");
			map.put("cardAmt",map.get("totalAmt").toString());
			map.put("totalAmt",map.get("totalAmt").toString());
			map.put("payType",moneyFlag);
			map.put("status","1");
			/*List signList = appSignDao.querySign(map);
			if(signList != null && signList.size() > 0){
				Map m = (Map) signList.get(0);
				String signNo = m.get("SIGNNO") == null ? "" : m.get("SIGNNO").toString();
				String status = m.get("STATUS") == null ? "" : m.get("STATUS").toString();
				map.put("signNo", signNo);
				if(!"0".equals(status)){
					jsonObject.put("resCode", "4004");
					jsonObject.put("resMsg", "银行卡未签约");
					return jsonObject;
				}
			}else{
				 jsonObject.put("resCode", "4005");
				 jsonObject.put("resMsg", "银行卡签约协议号不存在");
				 return jsonObject;
			} */
			String merchantId = oldOrderMap.get("NOTE1").toString();
			List outList = commerDao.queryCommer(merchantId);/*商户编号  退款来表示收款*/ 
			// 获取转出账号,从市政府对公账号
			//List inList = commerDao.queryCommerByType("99");
			List inList = appSignDao.queryCardName(map);
			if (null == outList || outList.size() <= 0 || null == inList || inList.size() <= 0) {
				jsonObject.put("resCode", "3002");
				jsonObject.put("resMsg", "商户号未配置");
				return jsonObject;
			} else {
				map.put("outCardNo",((Map) outList.get(0)).get("ACCTNO").toString());
				map.put("outCardName",((Map) outList.get(0)).get("ACCTNAME").toString());
				map.put("inCardNo",((Map) inList.get(0)).get("ACCTNO").toString());
				map.put("inCardName",((Map) inList.get(0)).get("ACCTNAME").toString());
			}
		}else if("01".equals(moneyFlag) && "26".equals(FEETYPE)){  //对私到对私
			//查询银行协议号等信息
			map.put("balanceAmt","0.00");
			map.put("cardAmt",map.get("totalAmt").toString());
			map.put("totalAmt",map.get("totalAmt").toString());
			map.put("payType",moneyFlag);
			map.put("status","1");
			String ACCOUNTNO =  oldOrderMap.get("ACCOUNTNO").toString();
			String ACCOUNTNAME =  oldOrderMap.get("ACCOUNTNAME").toString();
			List inList = appSignDao.queryCardName(map);
			if (null == inList || inList.size() <= 0) {
				jsonObject.put("resCode", "3002");
				jsonObject.put("resMsg", "查询用户信息失败");
				return jsonObject;
			} else {
				map.put("outCardNo",ACCOUNTNO);
				map.put("outCardName",ACCOUNTNAME);
				map.put("inCardNo",((Map) inList.get(0)).get("ACCTNO").toString());
				map.put("inCardName",((Map) inList.get(0)).get("ACCTNAME").toString());
			}
		}else if("02".equals(moneyFlag) && !("26".equals(FEETYPE) || "13".equals(FEETYPE)) ){
			map.put("balanceAmt",map.get("totalAmt").toString());
			map.put("cardAmt","0.00");
			map.put("totalAmt",map.get("totalAmt").toString());
			map.put("payType",moneyFlag);
			map.put("status","0");
			String merchantId = oldOrderMap.get("NOTE1").toString();
			List outList = commerDao.queryCommer(merchantId);/*商户编号  退款来表示收款*/ 
			// 获取转出账号,从市政府对公账号
			List inList = commerDao.queryCommerByType("99");
			if (null == outList || outList.size() <= 0 || null == inList || inList.size() <= 0) {
				jsonObject.put("resCode", "3002");
				jsonObject.put("resMsg", "商户号未配置");
				return jsonObject;
			} else {
				map.put("outCardNo",((Map) outList.get(0)).get("ACCTNO").toString());
				map.put("outCardName",((Map) outList.get(0)).get("ACCTNAME").toString());
				map.put("inCardNo",((Map) inList.get(0)).get("ACCTNO").toString());
				map.put("inCardName",((Map) inList.get(0)).get("ACCTNAME").toString());
			}
		}else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "暂不支持退费");
			return jsonObject;
		}
		map.put("tmallMerchantId", "");
	    int result = orderDao.insertPaymx(map);
	    if(result == 1){
			jsonObject.put("resCode", "9999");
		}else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		}
		return jsonObject;
	}
	
	
	//退货交易
	public Object backCardMain_ahnj(Map map) throws Exception{
		 JSONObject jsonObject = new JSONObject();
		 PayUtil_AHNJ ahnj = new PayUtil_AHNJ();
		 Map<String,String> returnMap = ahnj.backMoney(map);
		 if("0000".equals(returnMap.get("flag"))){
			//如果银行支付成功修改状态
			map.put("flag", "2");
			orderDao.updatePayMxStatusByCard(map);
			orderDao.updatePayMxByCardBack(map);
			//回调接口 
			List orderList = orderDao.queryOrder(map);
			if(orderList != null && orderList.size() > 0){
				Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
				Map notifyMap = PayNotify.notify(orderMap);
				logger.debug("notifyMap === "+notifyMap);
			}
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "退费成功");
			jsonObject.put("orderId",map.get("orderId").toString());
			jsonObject.put("payerTime",map.get("payerTime").toString());
			jsonObject.put("seqNo","");
			jsonObject.put("totalAmt",map.get("totalAmt").toString());
			jsonObject.put("backAmt",new BigDecimal(map.get("backAmt").toString()).add(new BigDecimal(map.get("totalAmt").toString())));
			return jsonObject;
		 }else if("0001".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
	    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
	    	return jsonObject;
		 }else if("9999".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
	    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
	    	return jsonObject;
		 }else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		 }
	}
	
	//退货交易
	public Object backCardMain(Map map) throws Exception{
		 JSONObject jsonObject = new JSONObject();
		 PayUtil_YDYH ahnj = new PayUtil_YDYH();
		 Map<String,String> returnMap = ahnj.backMoney(map);
		 if("0000".equals(returnMap.get("flag"))){
			//如果银行支付成功修改状态
			map.put("flag", "2");
			map.put("status", "1");
			map.put("middleflow1", returnMap.get("middleflow1"));
			map.put("middledate1", returnMap.get("middledate1"));
			map.put("coreflow1", returnMap.get("coreflow1"));
			map.put("coredate1", returnMap.get("coredate1"));
			orderDao.updatePayMxStatusByCard(map);
			orderDao.updatePayMxByCardBack(map);
			//回调接口 ，开启线程去回调
			List orderList = orderDao.queryOrder(map);
			if(orderList != null && orderList.size() > 0){
				Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
				//Map notifyMap = PayNotify.notify(orderMap);
				//logger.debug("notifyMap === "+notifyMap);
				new ShortConnection(orderMap).start();
			}
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "退费成功");
			jsonObject.put("orderId",map.get("orderId").toString());
			jsonObject.put("payerTime",map.get("payerTime").toString());
			jsonObject.put("seqNo","");
			jsonObject.put("totalAmt",map.get("totalAmt").toString());
			jsonObject.put("backAmt",new BigDecimal(map.get("backAmt").toString()).add(new BigDecimal(map.get("totalAmt").toString())));
			return jsonObject;
		 }else if("0001".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
	    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
	    	return jsonObject;
		 }else if("9999".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
	    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
	    	return jsonObject;
		 }else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		 }
	}
	
	//退费走余额
	public Object backBalanceMain(Map map) throws Exception{
		 
		 JSONObject jsonObject = new JSONObject();
		 PayUtil_YDYH ahnj = new PayUtil_YDYH();
		 map.put("remark", "");
		 Map<String,String> returnMap = ahnj.backMoney(map);
		 if("0000".equals(returnMap.get("flag"))){
			// 如果银行支付成功修改状态
			// 更新APP_PAYMX
			map.put("flag", "2");
			map.put("status", "1");
			map.put("middleflow1",returnMap.get("middleflow1"));
			map.put("middledate1",returnMap.get("middledate1"));
			map.put("coreflow1", returnMap.get("coreflow1"));
			map.put("coredate1", returnMap.get("coredate1"));
			System.out.println("支付参数＝＝＝＝＝＝》" + map);
			//更新明细表
			orderDao.updatePayMxStatusByMoney(map);
			orderDao.updatePayMxByCardBack(map);
			//更新余额表
			appMoneyDao.updateBalance(map);
			BigDecimal balance   = new BigDecimal("0.00");
			List moneyList = appMoneyDao.queryBalance(map);// 查询余额
			if(moneyList != null && moneyList.size() > 0){
				balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
			}
			// 更新账户余额明细表APP_MONEYMX
			BigDecimal balanceAmt  = new BigDecimal(map.get("totalAmt").toString() );
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			Map moneyMxMap = new HashMap();
			moneyMxMap.put("orderId", map.get("orderId").toString() + "0");
			moneyMxMap.put("zforderId", map.get("orderId").toString());
			moneyMxMap.put("payertime", formatter.format(new Date()));
			moneyMxMap.put("userNo", map.get("userNo").toString());
			moneyMxMap.put("mobile", "");
			moneyMxMap.put("fantime", "");
			moneyMxMap.put("transamt1", balanceAmt);
			moneyMxMap.put("transamt2", map.get("totalAmt").toString());
			moneyMxMap.put("transamt3", balance.setScale(2, BigDecimal.ROUND_HALF_UP));
			moneyMxMap.put("type", "8");
			moneyMxMap.put("transamt5", "0.00");
			moneyMxMap.put("cardno", "");
			moneyMxMap.put("channerno", "01");
			moneyMxMap.put("transtype", "1");
			moneyMxMap.put("feetype", map.get("feeType").toString());
			moneyMxMap.put("status", "1");
			moneyMxMap.put("middleflow", returnMap.get("middleflow1"));
			moneyMxMap.put("middledate", returnMap.get("middledate1"));
			moneyMxMap.put("coreflow", returnMap.get("coreflow1"));
			moneyMxMap.put("coredate", returnMap.get("coredate1"));
			moneyMxMap.put("dzstatus", "0");
			moneyMxMap.put("dzdate", "");
			moneyMxMap.put("dztime", "");
			moneyMxMap.put("note1", "");
			moneyMxMap.put("note2", "");
			moneyMxMap.put("note3", "1");
			moneyMxMap.put("note4", "");
			moneyMxMap.put("note5", "");
			appMoneyDao.insertMoneyMx(moneyMxMap);
			//回调接口 
			List orderList = orderDao.queryOrder(map);
			if(orderList != null && orderList.size() > 0){
				Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
				//Map notifyMap = PayNotify.notify(orderMap);
				//logger.debug("notifyMap === "+notifyMap);
				new ShortConnection(orderMap).start();
			}
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "退费成功");
			jsonObject.put("orderId",map.get("orderId").toString());
			jsonObject.put("payerTime",map.get("payerTime").toString());
			jsonObject.put("seqNo",returnMap.get("coredate1")+returnMap.get("coreflow1"));
			jsonObject.put("totalAmt",map.get("totalAmt").toString());
			jsonObject.put("backAmt",new BigDecimal(map.get("backAmt").toString()).add(new BigDecimal(map.get("totalAmt").toString())));
			return jsonObject;
		 }else if("0001".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
	    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
	    	return jsonObject;
		 }else if("8888".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4008");
	    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
	    	return jsonObject;
		}else if("9999".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
	    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
	    	return jsonObject;
		 }else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		 }
		
	 
	 
	}


	public Object OrderStatus(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		List orderList = orderDao.queryOrder(map);
		if(orderList != null && orderList.size() > 0){
			Map orderMap = (Map)orderList.get(0);
		    String flag = orderMap.get("FLAG").toString();
		    String transType = orderMap.get("TRANSTYPE").toString();
		    jsonObject.put("transType", transType);
		    jsonObject.put("totalAmt", orderMap.get("PAYAMT").toString());
	    	jsonObject.put("orderId", orderMap.get("ORDERID").toString());
	    	jsonObject.put("payerTime", orderMap.get("PAYERTIME").toString());
	    	jsonObject.put("status", "0");
	    	jsonObject.put("backAmt", "0.00");
	    	jsonObject.put("seqNo", "");
	    	
	    	map.put("userNo",  orderMap.get("USERNO").toString());
	    	map.put("id",  orderMap.get("USERNO").toString());
	    	map.put("cardNo",  orderMap.get("CARDNO").toString());
	    	String  oldOrderId = orderMap.get("TRAFFICADDR").toString();
			String  totalAmt = orderMap.get("PAYAMT").toString();
			String feeType = orderMap.get("FEETYPE").toString();
			map.put("oldOrderId", oldOrderId);
			map.put("totalAmt", totalAmt);
			map.put("feeType", feeType);
	    	
		    if(!"0".equals(flag)){
		    	jsonObject.put("status", "1");
		    	jsonObject.put("backAmt", orderMap.get("BACKAMT").toString());
		    	jsonObject.put("seqNo", (orderMap.get("COREDATE1")==null?"":orderMap.get("COREDATE1").toString())+(orderMap.get("COREFLOW1")==null?"":orderMap.get("COREFLOW1").toString()));
		    	jsonObject.put("resCode", "0000");
				jsonObject.put("resMsg", "订单状态查询成功");
				return jsonObject;
		    }else{
		    	//教育缴费、新农合缴费等的余额支付--查询直接显示数据库中的结果
				List EduPayMxList = queryEduPayMxByOrderId(map);
			    if (null != EduPayMxList && EduPayMxList.size() != 0){
			    	jsonObject.put("status", orderMap.get("FLAG").toString());
			    	jsonObject.put("backAmt", orderMap.get("BACKAMT").toString());
			    	jsonObject.put("seqNo", (orderMap.get("COREDATE1")==null?"":orderMap.get("COREDATE1").toString())+(orderMap.get("COREFLOW1")==null?"":orderMap.get("COREFLOW1").toString()));
			    	jsonObject.put("resCode", "0000");
					jsonObject.put("resMsg", "订单状态查询成功");
					return jsonObject;
			    }
		    	//需要查询状态
		    	//判断支付方式，如果是银行卡支付走安徽农金查询状态，如果是电子钱包支付走药都银行中台查询卡状态
		    	String moneyFlag = orderMap.get("MONEYFLAG").toString();
		    	String merchantId = orderMap.get("NOTE1").toString();
		    	if("01".equals(moneyFlag)){
		    		//查询安徽农金
		    		/*PayUtil_AHNJ ahnj = new PayUtil_AHNJ();
		    		map.put("merchantId", merchantId);
		    		map.put("payerTime", orderMap.get("PAYERTIME").toString());
		    		map.put("type", orderMap.get("TRANSTYPE").toString());
		    		Map<String,String> queryMap  = ahnj.SingQueryReq(map);
					if("0000".equals(queryMap.get("flag"))){
						 if("0000".equals(queryMap.get("status"))  || "0003".equals(queryMap.get("status"))){
							 jsonObject.put("status", "0");
						 }else if("0001".equals(queryMap.get("status"))){
							 jsonObject.put("status", "1");
							 //更新数据库数据为成功
							 map.put("flag", "2");
							 orderDao.updatePayMxStatusByCard(map);
							 if("2".equals(transType)){//更新
								 orderDao.updatePayMxByCardBack(map);
							 }
							 
						 }else if("0002".equals(queryMap.get("status"))){
							 jsonObject.put("status", "2");
						 }else{
							 jsonObject.put("status", "2");
						 }
					}else{
						jsonObject.put("status", "2");
				    }*/
		    		 PayUtil_YDYH ydyh = new PayUtil_YDYH(); 
		    		 List outList = commerDao.queryCommer(orderMap.get("NOTE1").toString());/*商户编号*/
		    		 if("13".equals(feeType)){
		    			 outList = commerDao.queryCommer(orderMap.get("NOTE4").toString());/*商户编号*/
		    		 }
		    		 List inList = appSignDao.queryCardNameNew(map);
					 if (outList != null && outList.size() > 0 && inList!= null &&   inList.size() > 0) {
						 if("26".equals(feeType)){
							 if("1".equals(transType)){//转账
								 map.put("outCardNo", ((Map) inList.get(0)).get("ACCTNO").toString());
								 map.put("outCardName", ((Map) inList.get(0)).get("ACCTNAME").toString());
								 map.put("inCardNo", (orderMap.get("ACCOUNTNO").toString()));
								 map.put("inCardName", (orderMap.get("ACCOUNTNAME").toString()));
							 }
						 }else{
						 if("1".equals(transType)){//转账
							 map.put("outCardNo", ((Map) inList.get(0)).get("ACCTNO").toString());
							 map.put("outCardName", ((Map) inList.get(0)).get("ACCTNAME").toString());
							 map.put("inCardNo", ((Map) outList.get(0)).get("ACCTNO").toString());
								 map.put("inCardName", ((Map) outList.get(0)).get("ACCTNAME").toString());
						 }else  if("2".equals(transType)){//退费
							map.put("inCardNo", ((Map) inList.get(0)).get("ACCTNO").toString());
							map.put("inCardName", ((Map) inList.get(0)).get("ACCTNAME").toString());
							map.put("outCardNo", ((Map) outList.get(0)).get("ACCTNO").toString());
								map.put("outCardName", ((Map) outList.get(0)).get("ACCTNAME").toString());
						 }
						 }
						
					 }else{
						 jsonObject.put("resCode", "3002");
						 jsonObject.put("resMsg", "商户号未配置");
						 return jsonObject;
					 }
		    		 Map<String,String> returnMap  = ydyh.queryMoney(map);
					 if("0000".equals(returnMap.get("flag"))){
						 if(!"0".equals(returnMap.get("status"))){
							 jsonObject.put("status", "0");
						 }else {
							 jsonObject.put("status", "1");
							 //更新数据库数据为成功
							 // 更新APP_PAYMX
							 map.put("flag", "2");
							 map.put("status", "1");
							 map.put("middleflow1",returnMap.get("middleflow1"));
							 map.put("middledate1",returnMap.get("middledate1"));
							 map.put("coreflow1", returnMap.get("coreflow1"));
							 map.put("coredate1", returnMap.get("coredate1"));
							 orderDao.updatePayMxStatusByCard(map);
							 if("2".equals(transType)){//更新
								 orderDao.updatePayMxByCardBack(map);
							 }
						 }
					 }else{
						jsonObject.put("status", "2");
					 }
					jsonObject.put("resCode", "0000");
					jsonObject.put("resMsg", "订单状态查询成功");
		    	}else if("02".equals(moneyFlag)){
		    		 PayUtil_YDYH ydyh = new PayUtil_YDYH(); 
		    		 List inList = commerDao.queryCommer(orderMap.get("NOTE1").toString());/*商户编号*/
		    		 List outList = commerDao.queryCommerByType("99");
					 if (outList != null && outList.size() > 0 && inList!= null &&   inList.size() > 0) {
						 if("1".equals(transType)){
							map.put("inCardNo", ((Map) inList.get(0)).get("ACCTNO").toString());
							map.put("inCardName", ((Map) inList.get(0)).get("ACCTNAME").toString());
							map.put("outCardNo", ((Map) outList.get(0)).get("ACCTNO").toString());
							map.put("outCardName", ((Map) outList.get(0)).get("ACCTNO").toString());
						 }else  if("2".equals(transType)){
							map.put("outCardNo", ((Map) inList.get(0)).get("ACCTNO").toString());
							map.put("outCardName", ((Map) inList.get(0)).get("ACCTNAME").toString());
							map.put("inCardNo", ((Map) outList.get(0)).get("ACCTNO").toString());
							map.put("inCardName", ((Map) outList.get(0)).get("ACCTNO").toString());
						 }
					 }else{
						 jsonObject.put("resCode", "3002");
						 jsonObject.put("resMsg", "商户号未配置");
						 return jsonObject;
					 }
		    		 Map<String,String> returnMap  = ydyh.queryMoney(map);
					 if("0000".equals(returnMap.get("flag"))){
						 if(!"0".equals(returnMap.get("status"))){
							 jsonObject.put("status", "0");
						 }else {
							 jsonObject.put("status", "1");
							 //更新数据库数据为成功
							 // 更新APP_PAYMX
							 map.put("flag", "2");
							 map.put("status", "1");
							 map.put("middleflow1",returnMap.get("middleflow1"));
							 map.put("middledate1",returnMap.get("middledate1"));
							 map.put("coreflow1", returnMap.get("coreflow1"));
							 map.put("coredate1", returnMap.get("coredate1"));
							 System.out.println("支付参数＝＝＝＝＝＝》" + map);
							 //更新明细表
							 orderDao.updatePayMxStatusByMoney(map);
							 if("1".equals(transType)){
								 /** 使用余额进行支付需要更新账户余额表和账户余额明细表 **/
									List moneyList = appMoneyDao.queryBalance(map);// 查询余额
									BigDecimal balance = new BigDecimal(0.00);
									// 更新账户余额表APP_MONEY
									BigDecimal balanceAmt = new BigDecimal(totalAmt).setScale(2, BigDecimal.ROUND_HALF_UP);//消费金额
									if (moneyList != null && moneyList.size() > 0) {
									    balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
										BigDecimal qywamt = new BigDecimal(((Map) moneyList.get(0)).get("QYWAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 趣医网返现
										BigDecimal jfamt = new BigDecimal(((Map) moneyList.get(0)).get("JFAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 积分金额
										BigDecimal qtamt = new BigDecimal(((Map) moneyList.get(0)).get("QTAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 其他金额
										BigDecimal yhkamt = new BigDecimal(((Map) moneyList.get(0)).get("YHKAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 银行卡转入金额

										if (qywamt.compareTo(balanceAmt) >= 0) {
											map.put("qywamt",qywamt.subtract(balanceAmt).setScale(2,BigDecimal.ROUND_HALF_UP));
										} else if (qywamt.add(jfamt).compareTo(balanceAmt) >= 0) {
											map.put("qywamt", new BigDecimal(0.00));
											map.put("jfamt", jfamt.subtract(balanceAmt.subtract(qywamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
										} else if (qywamt.add(jfamt).add(qtamt).compareTo(balanceAmt) >= 0) {
											map.put("qywamt", new BigDecimal(0.00));
											map.put("jfamt", new BigDecimal(0.00));
											map.put("qtamt", qtamt.subtract(balanceAmt.subtract(qywamt).subtract(jfamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
										} else if (qywamt.add(jfamt).add(qtamt).add(yhkamt).compareTo(balanceAmt) >= 0) {
											map.put("qywamt", new BigDecimal(0.00));
											map.put("jfamt", new BigDecimal(0.00));
											map.put("qtamt", new BigDecimal(0.00));
											map.put("yhkamt", yhkamt.subtract(balanceAmt.subtract(qywamt).subtract(jfamt).subtract(qtamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
										}
										map.put("totalamt", balance.subtract(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
										appMoneyDao.updateMoney(map);
									}

									// 更新账户余额明细表APP_MONEYMX
									//(#{orderId},#{zforderId},#{payertime},#{userNo},#{mobile},#{fantime},#{transamt1},#{transamt2},
									//#{transamt3},#{type},#{cardno},#{channerno},#{transtype},#{feetype},#{status},#{middleflow},
									//#{middledate},#{coreflow},#{coredate},#{dzstatus},#{dzdate},#{dztime},#{note1},#{note2},#{note3},
									//#{note4},#{note5},#{transamt5},0.00,0.00)

									SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
									Map moneyMxMap = new HashMap();
									moneyMxMap.put("orderId", map.get("orderId").toString() + "0");
									moneyMxMap.put("zforderId", map.get("orderId").toString());
									moneyMxMap.put("payertime", formatter.format(new Date()));
									moneyMxMap.put("userNo", map.get("id").toString());
									moneyMxMap.put("mobile", "");
									moneyMxMap.put("fantime", "");
									moneyMxMap.put("transamt1", balanceAmt);
									moneyMxMap.put("transamt2", map.get("totalAmt").toString());
									moneyMxMap.put("transamt3", balance.subtract(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
									moneyMxMap.put("type", "7");
									moneyMxMap.put("transamt5", "0.00");
									moneyMxMap.put("cardno", "");
									moneyMxMap.put("channerno", "01");
									moneyMxMap.put("transtype", "2");
									moneyMxMap.put("feetype", map.get("feeType").toString());
									moneyMxMap.put("status", "1");
									moneyMxMap.put("middleflow", returnMap.get("middleflow1"));
									moneyMxMap.put("middledate", returnMap.get("middledate1"));
									moneyMxMap.put("coreflow", returnMap.get("coreflow1"));
									moneyMxMap.put("coredate", returnMap.get("coredate1"));
									moneyMxMap.put("dzstatus", "0");
									moneyMxMap.put("dzdate", "");
									moneyMxMap.put("dztime", "");
									moneyMxMap.put("note1", "");
									moneyMxMap.put("note2", "");
									moneyMxMap.put("note3", "1");
									moneyMxMap.put("note4", "");
									moneyMxMap.put("note5", "");
									System.out.println(moneyMxMap);
									appMoneyDao.insertMoneyMx(moneyMxMap);
									
									jsonObject.put("seqNo", (returnMap.get("coredate1")==null?"":returnMap.get("coredate1"))+(returnMap.get("coreflow1")==null?"":returnMap.get("coreflow1")));
							 }else if("2".equals(transType)){
								 //更新退货金额
								 orderDao.updatePayMxByCardBack(map);
								 //更新余额表
								 appMoneyDao.updateBalance(map);
								 BigDecimal balance   = new BigDecimal("0.00");
								 List moneyList = appMoneyDao.queryBalance(map);// 查询余额
								 if(moneyList != null && moneyList.size() > 0){
									balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
								 }
								 // 更新账户余额明细表APP_MONEYMX
								 BigDecimal balanceAmt  = new BigDecimal(map.get("totalAmt").toString() );
								 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
								 Map moneyMxMap = new HashMap();
								 moneyMxMap.put("orderId", map.get("orderId").toString() + "0");
								 moneyMxMap.put("zforderId", map.get("orderId").toString());
								 moneyMxMap.put("payertime", formatter.format(new Date()));
								 moneyMxMap.put("userNo", map.get("userNo").toString());
								 moneyMxMap.put("mobile", "");
								 moneyMxMap.put("fantime", "");
								 moneyMxMap.put("transamt1", balanceAmt);
								 moneyMxMap.put("transamt2", map.get("totalAmt").toString());
								 moneyMxMap.put("transamt3", balance.setScale(2, BigDecimal.ROUND_HALF_UP));
								 moneyMxMap.put("type", "8");
								 moneyMxMap.put("transamt5", "0.00");
								 moneyMxMap.put("cardno", "");
								 moneyMxMap.put("channerno", "01");
								 moneyMxMap.put("transtype", "1");
								 moneyMxMap.put("feetype", map.get("feeType").toString());
								 moneyMxMap.put("status", "1");
								 moneyMxMap.put("middleflow", returnMap.get("middleflow1"));
								 moneyMxMap.put("middledate", returnMap.get("middledate1"));
								 moneyMxMap.put("coreflow", returnMap.get("coreflow1"));
								 moneyMxMap.put("coredate", returnMap.get("coredate1"));
								 moneyMxMap.put("dzstatus", "0");
								 moneyMxMap.put("dzdate", "");
								 moneyMxMap.put("dztime", "");
								 moneyMxMap.put("note1", "");
								 moneyMxMap.put("note2", "");
								 moneyMxMap.put("note3", "1");
								 moneyMxMap.put("note4", "");
								 moneyMxMap.put("note5", "");
								 appMoneyDao.insertMoneyMx(moneyMxMap);
								jsonObject.put("seqNo", (returnMap.get("coredate1")==null?"":returnMap.get("coredate1"))+(returnMap.get("coreflow1")==null?"":returnMap.get("coreflow1")));

							 }
						 }
					}else{
						jsonObject.put("status", "2");
				    }
					jsonObject.put("resCode", "0000");
					jsonObject.put("resMsg", "订单状态查询成功");
		    	}
		    }
		}else{
			jsonObject.put("resCode", "3009");
			jsonObject.put("resMsg", "订单号不存在");
			return jsonObject;
		}
		return jsonObject;
	}
	
	 /*确认收货*/
	public Object confirmFee(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		//判断订单号是否存在
		List orderList = orderDao.queryOrder(map);
		if(orderList == null || orderList.size() <= 0){
			jsonObject.put("resCode", "3005");
			jsonObject.put("resMsg", "确认订单不存在");
			return jsonObject;
		}
		
		Map oldOrderMap = (Map)orderList.get(0);
		String check = RedisUtil.setRedis(oldOrderMap.get("USERNO").toString(), map.get("totalAmt").toString(), map.get("orderId").toString());
		if("0".equals(check)){
			 jsonObject.put("flag", "4007");
			 jsonObject.put("reason", "系统异常[redis]");
  		     return jsonObject;
		}else if("1".equals(check)){
			 jsonObject.put("flag", "4007");
			 jsonObject.put("reason", "系统忙,请稍后再试");
  		     return jsonObject;
		}
		//查找是否有确认收货记录
		Map<String,String> map2 = new HashMap<String,String>();
		String newOrderId = map.get("orderId").toString()+"01";
		map2.put("orderId", newOrderId);
		List orderList2 = orderDao.queryOrder(map2);
		 
		BigDecimal totalAmt  = new BigDecimal(map.get("totalAmt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);;
		if(totalAmt.compareTo(new BigDecimal(oldOrderMap.get("PAYAMT").toString())) != 0 ){
			jsonObject.put("resCode", "3009");
			jsonObject.put("resMsg", "确认金额不符");
			return jsonObject;
		}
		String flag = oldOrderMap.get("FLAG").toString();
		if("0".equals(flag)){ 
			jsonObject.put("resCode", "3007");
			jsonObject.put("resMsg", "退费原订单未成功");
			return jsonObject;
		}else if("2".equals(flag)){ 
			jsonObject.put("resCode", "3011");
			jsonObject.put("resMsg", "订单确认成功");
			return jsonObject;
		}
		if(orderList2 != null && orderList2.size() > 0){
			String merchantId = oldOrderMap.get("NOTE4").toString();
			List outList = commerDao.queryCommer(merchantId);/*过渡商户*/ 
			// 获取转出账号,从市政府对公账号
			//List inList = commerDao.queryCommerByType("99");
			List inList = commerDao.queryCommer(oldOrderMap.get("NOTE1").toString());/*真实收款商户*/ 
			if (null == outList || outList.size() <= 0 || null == inList || inList.size() <= 0) {
				jsonObject.put("resCode", "3002");
				jsonObject.put("resMsg", "商户号未配置");
				return jsonObject;
			} else {
				map.put("outCardNo",((Map) outList.get(0)).get("ACCTNO").toString());
				map.put("outCardName",((Map) outList.get(0)).get("ACCTNAME").toString());
				map.put("inCardNo",((Map) inList.get(0)).get("ACCTNO").toString());
				map.put("inCardName",((Map) inList.get(0)).get("ACCTNAME").toString());
			}
			
			map.put("oldOrderId",map.get("orderId"));
			map.put("orderId",newOrderId);
			map.put("payerTime",oldOrderMap.get("PAYERTIME").toString());
			//查找是否存在
			jsonObject.put("resCode", "9988");
		}else{
			//  VALUES (#{},#{},#{},#{},#{},#{},#{},#{},#{},#{},#{},
           // #{},#{},0.00,#{},#{},#{},0.00,0.00,#{},#{},#{},#{},#{},#{tmallMerchantId} )
			//保存确认收货交易明细
			String payerTime = formatter.format(new Date()); 
			map.put("oldOrderId",map.get("orderId"));
			map.put("orderId",newOrderId);
			map.put("payerTime",payerTime);
			map.put("userNo", oldOrderMap.get("USERNO").toString());
			map.put("unitNo", oldOrderMap.get("UNITNO").toString());
			map.put("unitName",oldOrderMap.get("UNITNAME").toString());
			map.put("cardNo", oldOrderMap.get("CARDNO").toString());
			map.put("mobile", oldOrderMap.get("MOBILEPHONE").toString());
			map.put("channelNo", oldOrderMap.get("CHANNERNO").toString());
			map.put("flag","0");
			map.put("backAmt", "0.00");
			map.put("feeType", oldOrderMap.get("FEETYPE").toString());
			map.put("merchantId",oldOrderMap.get("NOTE1").toString());
			map.put("transType","3");  //1 支付  2 退货 3  确认支付
			map.put("detail",oldOrderMap.get("DETAIL").toString());
			String moneyFlag = oldOrderMap.get("MONEYFLAG").toString();
			//查询银行协议号等信息
			map.put("balanceAmt","0.00");
			map.put("cardAmt",map.get("totalAmt").toString());
			map.put("totalAmt",map.get("totalAmt").toString());
			map.put("payType",moneyFlag);
			map.put("status","1");
			map.put("tmallMerchantId", oldOrderMap.get("NOTE4").toString());
			
			String merchantId = oldOrderMap.get("NOTE4").toString();
			List outList = commerDao.queryCommer(merchantId);/*过渡商户*/ 
			// 获取转出账号,从市政府对公账号
			//List inList = commerDao.queryCommerByType("99");
			List inList = commerDao.queryCommer(oldOrderMap.get("NOTE1").toString());/*真实收款商户*/ 
			if (null == outList || outList.size() <= 0 || null == inList || inList.size() <= 0) {
				jsonObject.put("resCode", "3002");
				jsonObject.put("resMsg", "商户号未配置");
				return jsonObject;
			} else {
				map.put("outCardNo",((Map) outList.get(0)).get("ACCTNO").toString());
				map.put("outCardName",((Map) outList.get(0)).get("ACCTNAME").toString());
				map.put("inCardNo",((Map) inList.get(0)).get("ACCTNO").toString());
				map.put("inCardName",((Map) inList.get(0)).get("ACCTNAME").toString());
			}
			System.out.println(map);
		    int result = orderDao.insertPaymx(map);
		    if(result == 1){
				jsonObject.put("resCode", "9999");
			}else{
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "系统异常");
				return jsonObject;
			}
		}
		return jsonObject;
	}

	
	//确认收货
	public Object confirmCardMain(Map map,String code ) throws Exception{
		 JSONObject jsonObject = new JSONObject();
		 PayUtil_YDYH ahnj = new PayUtil_YDYH();
		 if("9999".equals(code)){
			 Map<String,String> returnMap = ahnj.payMoney(map);
			 if("0000".equals(returnMap.get("flag"))){
				//如果银行支付成功修改状态
				map.put("flag", "2");
				map.put("status", "1");
				map.put("middleflow1", returnMap.get("middleflow1"));
				map.put("middledate1", returnMap.get("middledate1"));
				map.put("coreflow1", returnMap.get("coreflow1"));
				map.put("coredate1", returnMap.get("coredate1"));
				orderDao.updatePayMxStatusByCard(map);
				/*Map<String,String> ms = new HashMap<String, String>();
				ms.put("orderId", map.get("oldOrderId").toString());
				ms.put("flag", "2");*/
				orderDao.updatePayMxByCardTmall(map);
				//回调接口 
				List orderList = orderDao.queryOrder(map);
				if(orderList != null && orderList.size() > 0){
					Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
					Map notifyMap = PayNotify.notify(orderMap);
					logger.debug("notifyMap === "+notifyMap);
				}
				jsonObject.put("resCode", "0000");
				jsonObject.put("resMsg", "确认订单成功");
				jsonObject.put("orderId",map.get("orderId").toString());
				jsonObject.put("payerTime",map.get("payerTime").toString());
				jsonObject.put("seqNo","");
				jsonObject.put("totalAmt",map.get("totalAmt").toString());
				jsonObject.put("backAmt","0.00");
				return jsonObject;
			 }else if("0001".equals(returnMap.get("flag"))){
				jsonObject.put("resCode", "4006");
		    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
		    	return jsonObject;
			 }else if("9999".equals(returnMap.get("flag"))){
				jsonObject.put("resCode", "4006");
		    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
		    	return jsonObject;
			 }else{
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "系统异常");
				return jsonObject;
			 }
		 }else{
			 Map<String,String> returnMap = ahnj.queryMoney(map);
			 if("0000".equals(returnMap.get("flag"))){
				    if("0".equals(returnMap.get("status"))){
				    	//如果银行支付成功修改状态
						map.put("flag", "2");
						map.put("status", "1");
						map.put("middleflow1", returnMap.get("middleflow1"));
						map.put("middledate1", returnMap.get("middledate1"));
						map.put("coreflow1", returnMap.get("coreflow1"));
						map.put("coredate1", returnMap.get("coredate1"));
						orderDao.updatePayMxStatusByCard(map);
						/*Map<String,String> ms = new HashMap<String, String>();
						ms.put("orderId", map.get("oldOrderId").toString());
						ms.put("flag", "2");*/
						orderDao.updatePayMxByCardTmall(map);
						//回调接口 
						List orderList = orderDao.queryOrder(map);
						if(orderList != null && orderList.size() > 0){
							Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
							Map notifyMap = PayNotify.notify(orderMap);
							logger.debug("notifyMap === "+notifyMap);
						}
						jsonObject.put("resCode", "0000");
						jsonObject.put("resMsg", "确认订单成功");
						jsonObject.put("orderId",map.get("orderId").toString());
						jsonObject.put("payerTime",map.get("payerTime").toString());
						jsonObject.put("seqNo","");
						jsonObject.put("totalAmt",map.get("totalAmt").toString());
						jsonObject.put("backAmt","0.00");
						return jsonObject;
				    }else{
				    	 returnMap = ahnj.payMoney(map);
						 if("0000".equals(returnMap.get("flag"))){
							//如果银行支付成功修改状态
							map.put("flag", "2");
							map.put("status", "1");
							map.put("middleflow1", returnMap.get("middleflow1"));
							map.put("middledate1", returnMap.get("middledate1"));
							map.put("coreflow1", returnMap.get("coreflow1"));
							map.put("coredate1", returnMap.get("coredate1"));
							orderDao.updatePayMxStatusByCard(map);
							/*Map<String,String> ms = new HashMap<String, String>();
							ms.put("orderId", map.get("oldOrderId").toString());
							ms.put("flag", "2");*/
							orderDao.updatePayMxByCardTmall(map);
							//回调接口 
							List orderList = orderDao.queryOrder(map);
							if(orderList != null && orderList.size() > 0){
								Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
								Map notifyMap = PayNotify.notify(orderMap);
								logger.debug("notifyMap === "+notifyMap);
							}
							jsonObject.put("resCode", "0000");
							jsonObject.put("resMsg", "确认订单成功");
							jsonObject.put("orderId",map.get("orderId").toString());
							jsonObject.put("payerTime",map.get("payerTime").toString());
							jsonObject.put("seqNo","");
							jsonObject.put("totalAmt",map.get("totalAmt").toString());
							jsonObject.put("backAmt","0.00");
							return jsonObject;
						 }else if("0001".equals(returnMap.get("flag"))){
							jsonObject.put("resCode", "4006");
					    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
					    	return jsonObject;
						 }else if("9999".equals(returnMap.get("flag"))){
							jsonObject.put("resCode", "4006");
					    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
					    	return jsonObject;
						 }else{
							jsonObject.put("resCode", "4007");
							jsonObject.put("resMsg", "系统异常");
							return jsonObject;
						 }
				    }
					
				 }else if("0001".equals(returnMap.get("flag"))){
					jsonObject.put("resCode", "4006");
			    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
			    	return jsonObject;
				 }else if("9999".equals(returnMap.get("flag"))){
					jsonObject.put("resCode", "4006");
			    	jsonObject.put("resMsg", returnMap.get("errorMessage"));
			    	return jsonObject;
				 }else{
					jsonObject.put("resCode", "4007");
					jsonObject.put("resMsg", "系统异常");
					return jsonObject;
				 }
		 }
		 
	}
	
	/**
	 * 支付
	 */
	public Object payBicycleFee(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		map.put("id", map.get("userNo"));
		map.put("USERID", map.get("userNo"));
		// 需要校验用户支付密码是否正确
		List list = appSecurtDao.getAppSecurt(map);
		if (list == null || list.size() == 0) {
			jsonObject.put("resCode", "2001");
			jsonObject.put("resMsg", "未设置支付密码");
			return jsonObject;
		}
		Map securtMap = (Map) list.get(0);
		int cnts  = Integer.parseInt(securtMap.get("CNTS").toString());
		// 支付密码锁住
		if ( cnts >= 3) {
			jsonObject.put("resCode", "2002");
			jsonObject.put("resMsg", "支付密码次数超限，请重置支付密码【短信验证方式】");
			return jsonObject;
		}
		// 支付密码不正确
		if (!securtMap.get("SECURT").toString().equals(map.get("paySecurt").toString())) {
			appSecurtDao.updateSecurt(map);
			jsonObject.put("resCode", "2003");
			jsonObject.put("resMsg", "支付密码不正确，还有"+(2-cnts)+"次机会");
			return jsonObject;
		} else {// 密码正确更新错误次数为0
			if(cnts != 0){
				appSecurtDao.updateCnts(map);
			}
		}
		//判断订单号是否存在
		List orderList = appMoneyDao.queryMoneyMxByOrder(map);
		if(orderList != null && orderList.size() > 0){
			jsonObject.put("resCode", "3004");
			jsonObject.put("resMsg", "订单号重复");
			return jsonObject;
		}
		String check = RedisUtil.setRedis(map.get("userNo").toString(), map.get("feeType").toString()+map.get("unitNo").toString()+map.get("totalAmt").toString(), map.get("orderId").toString());
		if("0".equals(check)){
			 jsonObject.put("flag", "4007");
			 jsonObject.put("reason", "系统异常[redis]");
  		     return jsonObject;
		}else if("1".equals(check)){
			 jsonObject.put("flag", "4007");
			 jsonObject.put("reason", "系统忙,请稍后再试");
  		     return jsonObject;
		}
		if("01".equals(map.get("payType").toString())){
			 jsonObject.put("flag", "1003");
			 jsonObject.put("reason", "参数不符[payType]，暂只支持电子钱包支付");
  		     return jsonObject;
		}else if("02".equals(map.get("payType").toString())){
			/** 使用余额进行支付需要更新账户余额表和账户余额明细表 **/
			List moneyList = appMoneyDao.queryBalance(map);// 查询余额
			BigDecimal balance = new BigDecimal(0.00);
			String totalAmt = map.get("totalAmt").toString();
			
			// 更新账户余额表APP_MONEY
			BigDecimal balanceAmt = new BigDecimal(totalAmt).setScale(2, BigDecimal.ROUND_HALF_UP);//消费金额
			if (moneyList != null && moneyList.size() > 0) {
				balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
				if(balance.compareTo(balanceAmt) < 0 ){
					jsonObject.put("resCode", "4002");
					jsonObject.put("resMsg", "电子钱包金额不足");
					return jsonObject;
				}
				BigDecimal qywamt = new BigDecimal(((Map) moneyList.get(0)).get("QYWAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 趣医网返现
				BigDecimal jfamt = new BigDecimal(((Map) moneyList.get(0)).get("JFAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 积分金额
				BigDecimal qtamt = new BigDecimal(((Map) moneyList.get(0)).get("QTAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 其他金额
				BigDecimal yhkamt = new BigDecimal(((Map) moneyList.get(0)).get("YHKAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 银行卡转入金额

				if (qywamt.compareTo(balanceAmt) >= 0) {
					map.put("qywamt",qywamt.subtract(balanceAmt).setScale(2,BigDecimal.ROUND_HALF_UP));
				} else if (qywamt.add(jfamt).compareTo(balanceAmt) >= 0) {
					map.put("qywamt", new BigDecimal(0.00));
					map.put("jfamt", jfamt.subtract(balanceAmt.subtract(qywamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
				} else if (qywamt.add(jfamt).add(qtamt).compareTo(balanceAmt) >= 0) {
					map.put("qywamt", new BigDecimal(0.00));
					map.put("jfamt", new BigDecimal(0.00));
					map.put("qtamt", qtamt.subtract(balanceAmt.subtract(qywamt).subtract(jfamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
				} else if (qywamt.add(jfamt).add(qtamt).add(yhkamt).compareTo(balanceAmt) >= 0) {
					map.put("qywamt", new BigDecimal(0.00));
					map.put("jfamt", new BigDecimal(0.00));
					map.put("qtamt", new BigDecimal(0.00));
					map.put("yhkamt", yhkamt.subtract(balanceAmt.subtract(qywamt).subtract(jfamt).subtract(qtamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				map.put("totalamt", balance.subtract(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
				appMoneyDao.updateMoney(map);
			}else{
				jsonObject.put("resCode", "4002");
				jsonObject.put("resMsg", "电子钱包金额不足");
				return jsonObject;
			}

			// 更新账户余额明细表APP_MONEYMX

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			Map moneyMxMap = new HashMap();
			moneyMxMap.put("orderId", map.get("orderId").toString() );
			moneyMxMap.put("zforderId", map.get("orderId").toString());
			moneyMxMap.put("payertime", formatter.format(new Date()));
			moneyMxMap.put("userNo", map.get("id").toString());
			moneyMxMap.put("mobile", "");
			moneyMxMap.put("fantime", "");
			moneyMxMap.put("transamt1", balanceAmt);
			moneyMxMap.put("transamt2", map.get("totalAmt").toString());
			moneyMxMap.put("transamt3", balance.subtract(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
			moneyMxMap.put("type", "7");
			moneyMxMap.put("transamt5", "0.00");
			moneyMxMap.put("cardno", "");
			moneyMxMap.put("channerno", "01");
			moneyMxMap.put("transtype", "2");
			moneyMxMap.put("feetype", map.get("feeType").toString());
			moneyMxMap.put("status", "1");
			moneyMxMap.put("middleflow", "");
			moneyMxMap.put("middledate", "");
			moneyMxMap.put("coreflow", "");
			moneyMxMap.put("coredate", "");
			moneyMxMap.put("dzstatus", "1");
			moneyMxMap.put("dzdate", "");
			moneyMxMap.put("dztime", "");
			moneyMxMap.put("note1", "租车押金");
			moneyMxMap.put("note2", "");
			moneyMxMap.put("note3", "1");
			moneyMxMap.put("note4", "");
			moneyMxMap.put("note5", "");
			System.out.println(moneyMxMap);
			appMoneyDao.insertMoneyMx(moneyMxMap);
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "交易成功");
			return jsonObject;
		}
		jsonObject.put("resCode", "4007");
		jsonObject.put("resMsg", "系统异常");
		return jsonObject;
	}
	
	
	
	/**
	 * 押车退费
	 */
	public Object backBicycleFee(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		List  orderList = appMoneyDao.queryMoneyMxByOrder(map);
		if(orderList == null ||  orderList.size() <= 0){
			jsonObject.put("resCode", "3005");
			jsonObject.put("resMsg", "还车订单不存在");
			return jsonObject;
		}
		Map<String,String> bmap = new HashMap<String, String>();
		bmap.put("orderId","BACK"+map.get("orderId").toString());
		List  orderList2 = appMoneyDao.queryMoneyMxByOrder(bmap);
		if(orderList2 != null &&  orderList2.size() > 0){
			jsonObject.put("resCode", "3005");
			jsonObject.put("resMsg", "租车已还");
			return jsonObject;
		}
		Map oldOrderMap = (Map)orderList.get(0);
		String check = RedisUtil.setRedis(oldOrderMap.get("USERNO").toString(),map.get("totalAmt").toString(), map.get("orderId").toString());
		if("0".equals(check)){
			 jsonObject.put("flag", "4007");
			 jsonObject.put("reason", "系统异常[redis]");
			     return jsonObject;
		}else if("1".equals(check)){
			 jsonObject.put("flag", "4007");
			 jsonObject.put("reason", "系统忙,请稍后再试");
			     return jsonObject;
		}
		
		BigDecimal totalAmt  = new BigDecimal(map.get("totalAmt").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);;
		BigDecimal payAmt = new BigDecimal(oldOrderMap.get("TRANSAMT1").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
		String status = oldOrderMap.get("STATUS").toString();
		if(!"1".equals(status)){ 
			jsonObject.put("resCode", "3007");
			jsonObject.put("resMsg", "租车订单未成功");
			return jsonObject;
		}
		if(totalAmt.compareTo(payAmt) > 0){ 
			jsonObject.put("resCode", "3008");
			jsonObject.put("resMsg", "费用大于租车押金");
			return jsonObject;
		}
		List moneyList = appMoneyDao.queryBalance(map);// 查询余额
		BigDecimal balance   = new BigDecimal("0.00");
		BigDecimal qtbalance   = new BigDecimal("0.00");
		if(moneyList != null && moneyList.size() > 0){
			balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
			qtbalance = new BigDecimal(((Map) moneyList.get(0)).get("QTAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
		}
		BigDecimal backAmt = payAmt.subtract(totalAmt).setScale(2, BigDecimal.ROUND_HALF_UP);
		map.put("id", map.get("userNo"));
		map.put("totalamt", balance.add(backAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
		map.put("qtamt", qtbalance.add(backAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
		//更新余额表
		appMoneyDao.updateMoney(map);
		// 更新账户余额明细表APP_MONEYMX
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Map moneyMxMap = new HashMap();
		moneyMxMap.put("orderId", "BACK"+map.get("orderId").toString() );
		moneyMxMap.put("zforderId", map.get("orderId").toString());
		moneyMxMap.put("payertime", formatter.format(new Date()));
		moneyMxMap.put("userNo", map.get("userNo").toString());
		moneyMxMap.put("mobile", "");
		moneyMxMap.put("fantime", "");
		moneyMxMap.put("transamt1", backAmt);
		moneyMxMap.put("transamt2", map.get("totalAmt").toString());
		moneyMxMap.put("transamt3",  balance.add(backAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
		moneyMxMap.put("type", "8");
		moneyMxMap.put("transamt5", "0.00");
		moneyMxMap.put("cardno", "");
		moneyMxMap.put("channerno", "01");
		moneyMxMap.put("transtype", "1");
		moneyMxMap.put("feetype", oldOrderMap.get("FEETYPE").toString());
		moneyMxMap.put("status", "1");
		moneyMxMap.put("middleflow","");
		moneyMxMap.put("middledate", "");
		moneyMxMap.put("coreflow", "");
		moneyMxMap.put("coredate", "");
		moneyMxMap.put("dzstatus", "1");
		moneyMxMap.put("dzdate", "");
		moneyMxMap.put("dztime", "");
		moneyMxMap.put("note1", "还车退款");
		moneyMxMap.put("note2", "");
		moneyMxMap.put("note3", "1");
		moneyMxMap.put("note4", map.get("borrowDate"));
		moneyMxMap.put("note5",map.get("backDate"));
		appMoneyDao.insertMoneyMx(moneyMxMap);
		jsonObject.put("resCode", "0000");
		jsonObject.put("resMsg", "交易成功");
		
		//如下代码是进行返现操作
		List moneyList2 = appMoneyDao.queryBalance(map);// 查询余额
		BigDecimal balance2   = new BigDecimal("0.00");
		BigDecimal qtbalance2   = new BigDecimal("0.00");
		if(moneyList2 != null && moneyList2.size() > 0){
			balance2 = new BigDecimal(((Map) moneyList2.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
			qtbalance2 = new BigDecimal(((Map) moneyList2.get(0)).get("QTAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
		}
		map.put("id", map.get("userNo"));
		map.put("totalamt", balance2.add(totalAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
		map.put("qtamt", qtbalance2.add(totalAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
		//更新余额表
		appMoneyDao.updateMoney(map);
		// 更新账户余额明细表APP_MONEYMX
		Map moneyMxMap2 = new HashMap();
		moneyMxMap2.put("orderId", "JL"+map.get("orderId").toString() );
		moneyMxMap2.put("zforderId", map.get("orderId").toString());
		moneyMxMap2.put("payertime", formatter.format(new Date()));
		moneyMxMap2.put("userNo", map.get("userNo").toString());
		moneyMxMap2.put("mobile", "");
		moneyMxMap2.put("fantime", "");
		moneyMxMap2.put("transamt1", totalAmt);
		moneyMxMap2.put("transamt2", totalAmt);
		moneyMxMap2.put("transamt3",  balance2.add(totalAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
		moneyMxMap2.put("type", "4");
		moneyMxMap2.put("transamt5", "0.00");
		moneyMxMap2.put("cardno", "");
		moneyMxMap2.put("channerno", "01");
		moneyMxMap2.put("transtype", "1");
		moneyMxMap2.put("feetype", oldOrderMap.get("FEETYPE").toString());
		moneyMxMap2.put("status", "1");
		moneyMxMap2.put("middleflow","");
		moneyMxMap2.put("middledate", "");
		moneyMxMap2.put("coreflow", "");
		moneyMxMap2.put("coredate", "");
		moneyMxMap2.put("dzstatus", "1");
		moneyMxMap2.put("dzdate", "");
		moneyMxMap2.put("dztime", "");
		moneyMxMap2.put("note1", "租车奖励");
		moneyMxMap2.put("note2", "");
		moneyMxMap2.put("note3", "1");
		moneyMxMap2.put("note4", "");
		moneyMxMap2.put("note5","");
		appMoneyDao.insertMoneyMx(moneyMxMap2);
		
		return jsonObject;
	}
	
	
	
	/**
	 * 押车退费
	 */
	public Object queryBicycleFee(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		List  orderList = appMoneyDao.queryMoneyMxByOrder(map);
		if(orderList == null ||  orderList.size() <= 0){
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "未租车");
			jsonObject.put("status", "0");
			jsonObject.put("totalamt", "0");
			return jsonObject;
		}else{
			map.put("orderId", "BACK"+map.get("orderId")+"");
			List backOrderList = appMoneyDao.queryMoneyMxByOrder(map);
			if(backOrderList == null ||  backOrderList.size() <= 0){
				jsonObject.put("resCode", "0000");
				jsonObject.put("resMsg", "租车中");
				jsonObject.put("status", "1");
				jsonObject.put("totalamt", ((Map)orderList.get(0)).get("TRANSAMT1"));
				return jsonObject;
			}else{
				jsonObject.put("resCode", "0000");
				jsonObject.put("resMsg", "已还车");
				jsonObject.put("status", "2");
				jsonObject.put("totalamt", ((Map)backOrderList.get(0)).get("TRANSAMT2"));
				return jsonObject;
			}
		}
	}
	
	//查询对账集合
	public List queryDzAll(Map map) {
		List mxList = orderDao.queryDzAll(map);
		return mxList;
	}

	//查询对账集合  分单位编号
	public List queryDzUnit(Map map) {
		List mxList = orderDao.queryDzUnit(map);
		return mxList;
	}
	 
	
	//查询对账集合
	public List queryDzAllFeetype(Map map) {
		List mxList = orderDao.queryDzAllFeetype(map);
		return mxList;
	}

	//查询对账集合  分单位编号
	public List queryDzUnitFeetype(Map map) {
		List mxList = orderDao.queryDzUnitFeetype(map);
		return mxList;
	}
	 
	
	/**
	 * 支付payXnbFee
	 */
	public Object payXnbFee(Map map){
		JSONObject jsonObject = new JSONObject();
		map.put("id", map.get("userNo"));
		map.put("USERID", map.get("userNo"));
		// 需要校验用户支付密码是否正确
		List list = appSecurtDao.getAppSecurt(map);
		if (list == null || list.size() == 0) {
			jsonObject.put("resCode", "2001");
			jsonObject.put("resMsg", "未设置支付密码");
			return jsonObject;
		}
		Map securtMap = (Map) list.get(0);
		int cnts  = Integer.parseInt(securtMap.get("CNTS").toString());
		// 支付密码锁住
		if ( cnts >= 3) {
			jsonObject.put("resCode", "2002");
			jsonObject.put("resMsg", "支付密码次数超限，请重置支付密码【短信验证方式】");
			return jsonObject;
		}
		// 支付密码不正确
		if (!securtMap.get("SECURT").toString().equals(map.get("paySecurt").toString())) {
			appSecurtDao.updateSecurt(map);
			jsonObject.put("resCode", "2003");
			jsonObject.put("resMsg", "支付密码不正确，还有"+(2-cnts)+"次机会");
			return jsonObject;
		} else {// 密码正确更新错误次数为0
			if(cnts != 0){
				appSecurtDao.updateCnts(map);
			}
		}
		//判断订单号是否存在
		List orderList = orderDao.queryOrder(map);
		if(orderList != null && orderList.size() > 0){
			jsonObject.put("resCode", "3004");
			jsonObject.put("resMsg", "订单号重复");
			return jsonObject;
		}
		String check = RedisUtil.setRedis(map.get("userNo").toString(), map.get("feeType").toString()+map.get("inCardNo").toString()+map.get("unitNo").toString()+map.get("totalAmt").toString(), map.get("orderId").toString());
		if("0".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统异常[redis]");
 		     return jsonObject;
		}else if("1".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统忙,请稍后再试");
 		     return jsonObject;
		}
		if("01".equals(map.get("payType").toString())){
			return cardXnbPayment(map);
		}
		return null;
	}
	
	/*
	 * accountno 存放对私个人收款卡号
	 * acccountname  存放对私个人收款户名
	 * TRAFFICNO 存放身份证号码
	 * TRAFFICCODE 存放收款电话号码
	 * TRAFFICTIME 存放社保卡号
	 * */
	public Object cardXnbPayment(Map map){
		JSONObject jsonObject = new JSONObject();
		// 初始化返回数据
		jsonObject.put("orderId", map.get("orderId").toString());
		String payerTime = formatter.format(new Date()); 
		jsonObject.put("payerTime",payerTime);
		jsonObject.put("seqNo","");
		//校验卡号、手机号是否匹配
		map.put("acctNo", map.get("cardNo").toString());
		List cardList = appSignDao.querySignByCard(map);
		if(cardList != null && cardList.size()>0){
			Map cardMap = (Map) cardList.get(0);
			String phone = cardMap.get("PHONE").toString();
			if(!map.get("mobile").toString().equals(phone)){
				jsonObject.put("resCode", "4010");
				jsonObject.put("resMsg", "与银行预留手机不一致");
				return jsonObject;
			}
			map.put("outCardNo",((Map)cardList.get(0)).get("ACCTNO"));
			map.put("outCardName",((Map)cardList.get(0)).get("ACCTNAME"));
		}else{
			jsonObject.put("resCode", "4004");
			jsonObject.put("resMsg", "银行卡未签约");
			return jsonObject;
		}
		String unitNo = map.get("unitNo").toString().trim();
		String merchantId  = "";
		String unitName = "";
		if(!"".equals(unitNo)){
			List unitList = appUnitDao.queryUnitByUnitNo(map);
			if(unitList != null && unitList.size()>0){
				 merchantId  = ((Map)unitList.get(0)).get("MERCHANTID").toString();
				 unitName = ((Map)unitList.get(0)).get("UNITNAME").toString();
			}else{
				 jsonObject.put("resCode", "3002");
				 jsonObject.put("resMsg", "商户号未配置");
				 return jsonObject;
			}
		}
		
		//sb.append(map.get("inCardNo").toString() + "|"+map.get("inCardName").toString() + "|");
		
		map.put("payerTime",payerTime);
		map.put("unitName",unitName);
		map.put("flag","0");
		map.put("merchantId",merchantId);
		map.put("transType","1");
		map.put("status","1");
		map.put("oldOrderId","");
		map.put("remark", map.get("inIdNumber"));
		System.out.println("map==========="+map);
		int result = orderDao.insertXnbPaymx(map);
		System.out.println("insertXnbPaymx========="+result);
		map.put("merchantId", merchantId);
		map.put("payerTime", payerTime);
		//map.put("signNo", signNo);
		return  cardXnbMain(map);
		
	}
	
	
	//支付  银行卡扣款更新状态   走安徽农金，对私到对私接口
	/**
	 * map.put("middleflow1", returnMap.get("middleflow1"));
			map.put("middledate1", returnMap.get("middledate1"));
			map.put("coreflow1", returnMap.get("coreflow1"));
			map.put("coredate1", returnMap.get("coredate1"));
	 * */
	public Object cardXnbMain(Map map){
		 JSONObject jsonObject = new JSONObject();
		 PayUtil_YDYH ahnj = new PayUtil_YDYH();
		 Map<String,String> returnMap = ahnj.payMoney0016(map);
		 if("0000".equals(returnMap.get("flag"))){
			//如果银行支付成功修改状态
			map.put("flag", "2");
			map.put("status", "1");
			map.put("middleflow1", returnMap.get("middleflow1"));
			map.put("middledate1", returnMap.get("middledate1"));
			map.put("coreflow1", returnMap.get("coreflow1"));
			map.put("coredate1", returnMap.get("coredate1"));
			int updateStatus = orderDao.updatePayMxStatusByCard(map);
			logger.debug("updateStatus === "+updateStatus);
			//回调接口 
			List orderList = orderDao.queryOrder(map);
			if(orderList != null && orderList.size() > 0){
				Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
				Map notifyMap = PayNotify.notify(orderMap);
				logger.debug("notifyMap === "+notifyMap);
			}
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "支付成功");
			jsonObject.put("orderId",map.get("orderId").toString());
			jsonObject.put("payerTime",map.get("payerTime").toString());
			jsonObject.put("seqNo","");
			return jsonObject;
		 }else if("0001".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
	   		jsonObject.put("resMsg", returnMap.get("errorMessage"));
	   		return jsonObject;
		 }else if("9999".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
			jsonObject.put("resMsg", returnMap.get("errorMessage"));
			return jsonObject;
		 }else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		 }
	}
	
	
	/**
	 * 支付payXnbFee0017
	 */
	public Object payXnbFee0017(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		String orderId = map.get("orderId").toString();
		map.put("oldOrderId", orderId);
		map.put("orderId", orderId+"A");
		//判断订单号是否存在
		List orderList = orderDao.queryOrder(map);
		if(orderList != null && orderList.size() > 0){
			jsonObject.put("resCode", "3004");
			jsonObject.put("resMsg", "订单号重复");
			return jsonObject;
		}
		String check = RedisUtil.setRedis(map.get("userNo").toString(), map.get("feeType").toString()+map.get("unitNo").toString()+map.get("totalAmt").toString(), map.get("orderId").toString());
		if("0".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统异常[redis]");
 		     return jsonObject;
		}else if("1".equals(check)){
			 jsonObject.put("resCode", "4007");
			 jsonObject.put("resMsg", "系统忙,请稍后再试");
 		     return jsonObject;
		}
		if("01".equals(map.get("payType").toString())){
			return cardXnbPayment0017(map);
		}
		 jsonObject.put("resCode", "4007");
		 jsonObject.put("resMsg", "系统异常");
		  return jsonObject;
	}
	
	
	/*
	 * 存折到对公账户，新农保缴费
	 * */
	public Object cardXnbPayment0017(Map map) throws Exception{
		JSONObject jsonObject = new JSONObject();
		// 初始化返回数据
		jsonObject.put("orderId", map.get("orderId").toString());
		String payerTime = formatter.format(new Date()); 
		jsonObject.put("payerTime",payerTime);
		jsonObject.put("seqNo","");
		map.put("outCardNo",map.get("inCardNo"));
		map.put("outCardName",map.get("inCardName"));
		map.put("cardNo", map.get("inCardNo"));
		map.put("mobile", map.get("inCardMobile"));
		String unitNo = map.get("unitNo").toString().trim();
		String merchantId  = "";
		String unitName = "";
		if(!"".equals(unitNo)){
			List unitList = appUnitDao.queryUnitByUnitNo(map);
			if(unitList != null && unitList.size()>0){
				 merchantId  = ((Map)unitList.get(0)).get("MERCHANTID").toString();
				 unitName = ((Map)unitList.get(0)).get("UNITNAME").toString();
				 Map m = (Map)unitList.get(0);
				 // 获取转入
				 List inList = commerDao.queryCommer(merchantId);/*商户编号*/
				 if (inList == null || inList.size() == 0) {
					 jsonObject.put("resCode", "3002");
					 jsonObject.put("resMsg", "商户号未配置");
					 
					 return jsonObject;
				 }else{
					 map.put("inCardNo",((Map)inList.get(0)).get("ACCTNO"));
					 map.put("inCardName",((Map)inList.get(0)).get("ACCTNAME"));
				 }
			}else{
				 jsonObject.put("resCode", "3002");
				 jsonObject.put("resMsg", "商户号未配置");
				 return jsonObject;
			}
		}
		
		//sb.append(map.get("inCardNo").toString() + "|"+map.get("inCardName").toString() + "|");
		map.put("tmallMerchantId", "");
		map.put("payerTime",payerTime);
		map.put("unitName",unitName);
		map.put("flag","0");
		map.put("merchantId",merchantId);
		map.put("transType","1");
		map.put("status","1");
		map.put("remark", map.get("inIdNumber"));
		map.put("detail", map.get("outCardName"));
		System.out.println("map==========="+map);
		int result = orderDao.insertXnbSecondPaymx(map);
		System.out.println("cardXnbPayment0017========="+result);
		map.put("merchantId", merchantId);
		map.put("payerTime", payerTime);
		//map.put("signNo", signNo);
		return  cardXnbMain0017(map);
		
	}
	
	//支付  银行卡扣款更新状态   走安徽农金，对私到对私接口
	/**
	 * map.put("middleflow1", returnMap.get("middleflow1"));
			map.put("middledate1", returnMap.get("middledate1"));
			map.put("coreflow1", returnMap.get("coreflow1"));
			map.put("coredate1", returnMap.get("coredate1"));
	 * */
	public Object cardXnbMain0017(Map map) throws Exception{
		 JSONObject jsonObject = new JSONObject();
		 PayUtil_YDYH ahnj = new PayUtil_YDYH();
		 Map<String,String> returnMap = ahnj.payMoney0017(map);
		 if("0000".equals(returnMap.get("flag"))){
			//如果银行支付成功修改状态
			map.put("flag", "2");
			map.put("status", "1");
			map.put("middleflow1", returnMap.get("middleflow1"));
			map.put("middledate1", returnMap.get("middledate1"));
			map.put("coreflow1", returnMap.get("coreflow1"));
			map.put("coredate1", returnMap.get("coredate1"));
			int updateStatus = orderDao.updatePayMxStatusByCard(map);
			logger.debug("updateStatus === "+updateStatus);
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "支付成功");
			jsonObject.put("orderId",map.get("orderId").toString());
			jsonObject.put("payerTime",map.get("payerTime").toString());
			jsonObject.put("seqNo","");
			return jsonObject;
		 }else if("1118".equals(returnMap.get("flag"))){
			 //更新支付状态为1118
			 map.put("resCode", "1118");
			int updateStatus = orderDao.updateXnbNext(map);
			logger.debug("updateXnbNext === "+updateStatus);
			jsonObject.put("resCode", "1118");
		   	jsonObject.put("resMsg", returnMap.get("errorMessage"));
		   	return jsonObject;
		 }else if("0001".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
	   		jsonObject.put("resMsg", returnMap.get("errorMessage"));
	   		return jsonObject;
		 }else if("9999".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
			jsonObject.put("resMsg", returnMap.get("errorMessage"));
			return jsonObject;
		 }else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		 }
	}

	public List queryXnbBatch(Map map) {
		return orderDao.queryXnbBatch(map);
	}

	public List queryUnitByUnitNo(Map map) {
		return appUnitDao.queryUnitByUnitNo(map);
	}

	public List queryCommer(String merchantId){
		return commerDao.queryCommer(merchantId);
	}
	//查询余额教育缴费、余额新农合缴费等记录
	public List queryEduPayMxByOrderId(Map map) {
		//退款的直接跳过判断，查询的没有oldOrderId这一项，map.put("oldOrderId", map.get("orderId"))
		if(map.get("oldOrderId") == null || map.get("oldOrderId").toString().isEmpty()) {
			map.put("oldOrderId", map.get("orderId"));//查询专用转换
		}
		List list = appPaymxDao.queryEduPayMxByOrderId(map);
		return list;
	}
	
	//出租车余额支付
	public Object balanceMainForTaxi(Map map,String flag)throws Exception{
		
		BigDecimal balanceAmt =  new BigDecimal(map.get("balanceAmt").toString()); //余额支付的金额
		JSONObject jsonObject = new JSONObject();
		map.put("flag",flag);
		map.put("status", "1");
		map.put("middleflow1", "");
		map.put("middledate1", "");
		map.put("coreflow1", "");
		map.put("coredate1", "");
		System.out.println("支付参数＝＝＝＝＝＝》" + map);
		int updateStatus = orderDao.updatePayMxStatusByMoney(map);
		logger.debug("updateStatus === "+updateStatus);
		List moneyList = appMoneyDao.queryBalance(map);// 查询余额
		BigDecimal balance = new BigDecimal(0.00);
		//余额扣款
		if (moneyList != null && moneyList.size() > 0) {
			
		    balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
			BigDecimal qywamt = new BigDecimal(((Map) moneyList.get(0)).get("QYWAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 趣医网返现
			BigDecimal jfamt = new BigDecimal(((Map) moneyList.get(0)).get("JFAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 积分金额
			BigDecimal qtamt = new BigDecimal(((Map) moneyList.get(0)).get("QTAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 其他金额
			BigDecimal yhkamt = new BigDecimal(((Map) moneyList.get(0)).get("YHKAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 银行卡转入金额
			
			if (qywamt.compareTo(balanceAmt) >= 0) {
				map.put("qywamt",qywamt.subtract(balanceAmt).setScale(2,BigDecimal.ROUND_HALF_UP));
			} else if (qywamt.add(jfamt).compareTo(balanceAmt) >= 0) {
				map.put("qywamt", new BigDecimal(0.00));
				map.put("jfamt", jfamt.subtract(balanceAmt.subtract(qywamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
			} else if (qywamt.add(jfamt).add(qtamt).compareTo(balanceAmt) >= 0) {
				map.put("qywamt", new BigDecimal(0.00));
				map.put("jfamt", new BigDecimal(0.00));
				map.put("qtamt", qtamt.subtract(balanceAmt.subtract(qywamt).subtract(jfamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
			} else if (qywamt.add(jfamt).add(qtamt).add(yhkamt).compareTo(balanceAmt) >= 0) {
				map.put("qywamt", new BigDecimal(0.00));
				map.put("jfamt", new BigDecimal(0.00));
				map.put("qtamt", new BigDecimal(0.00));
				map.put("yhkamt", yhkamt.subtract(balanceAmt.subtract(qywamt).subtract(jfamt).subtract(qtamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			map.put("totalamt", balance.subtract(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
			updateStatus = appMoneyDao.updateMoney(map);
			logger.debug("updateStatus === "+updateStatus);
		}

		// 更新账户余额明细表APP_MONEYMX
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Map moneyMxMap = new HashMap();
		moneyMxMap.put("orderId", map.get("orderId").toString() + "0");
		moneyMxMap.put("zforderId", map.get("orderId").toString());
		moneyMxMap.put("payertime", formatter.format(new Date()));
		moneyMxMap.put("userNo", map.get("userNo").toString());
		moneyMxMap.put("mobile", "");
		moneyMxMap.put("fantime", "");
		moneyMxMap.put("transamt1", balanceAmt);
		moneyMxMap.put("transamt2", map.get("totalAmt").toString());
		moneyMxMap.put("transamt3", balance.subtract(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
		moneyMxMap.put("type", "7");
		moneyMxMap.put("transamt5", "0.00");
		moneyMxMap.put("cardno", "");
		moneyMxMap.put("channerno", "01");
		moneyMxMap.put("transtype", "2");
		moneyMxMap.put("feetype", map.get("feeType").toString());
		moneyMxMap.put("status", "1");
		moneyMxMap.put("middleflow", "");
		moneyMxMap.put("middledate", "");
		moneyMxMap.put("coreflow", "");
		moneyMxMap.put("coredate", "");
		moneyMxMap.put("dzstatus", "0");
		moneyMxMap.put("dzdate", "");
		moneyMxMap.put("dztime", "");
		moneyMxMap.put("note1", "出租车余额支付");
		moneyMxMap.put("note2", "");
		moneyMxMap.put("note3", "1");
		moneyMxMap.put("note4", "");
		moneyMxMap.put("note5", "");
		updateStatus = appMoneyDao.insertMoneyMx(moneyMxMap);
		logger.debug("updateStatus === "+updateStatus);
		
		jsonObject.put("resCode", "0000");
		jsonObject.put("resMsg", "支付成功");
		jsonObject.put("orderId",map.get("orderId").toString());
		jsonObject.put("payerTime",map.get("payerTime").toString());
		jsonObject.put("seqNo","");
		return jsonObject;
	}


	public Object addMoneyForTaxi(Map map) throws Exception {
		//验证账户余额是否正确
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("orderId",map.get("orderId").toString());
		jsonObject.put("payerTime",map.get("payerTime").toString());
		jsonObject.put("seqNo","");
		//----map.put("userNo", map.get("taxiUserNo"));
		List moneyList = appMoneyDao.queryBalanceForTaxi(map);// 查询余额
		
		//如果司机账户为空的话需要新建一个账户
		if (moneyList == null || moneyList.size() <= 0) {
			//如果司机余额账户不存在的话我们这边需要新增一个用户并且将信息保存到最终的moneyList中以形成一个类似查询到余额的效果
			Map<String, Object> taxiMap = new HashMap<String, Object>();
			taxiMap.put("USERNO", map.get("taxiUserNo"));
			taxiMap.put("PHONE", "");
			taxiMap.put("TOTALAMT", new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
			taxiMap.put("QYWAMT", new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
			taxiMap.put("JFAMT", new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
			taxiMap.put("QTAMT", new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
			taxiMap.put("YHKAMT", new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
			taxiMap.put("NOTE1", "");
			taxiMap.put("NOTE2", "");
			taxiMap.put("NOTE3", "");
			taxiMap.put("NOTE4", "");
			taxiMap.put("NOTE5", "");
			moneyList.add(taxiMap);
			appMoneyDao.insertTaxiMoney(taxiMap);
		}
		
		if(moneyList != null && moneyList.size()  > 0){
			Map moneyMap = (Map)moneyList.get(0);
			BigDecimal totalAmt = new BigDecimal(moneyMap.get("TOTALAMT").toString());
			BigDecimal qyAmt = new BigDecimal(moneyMap.get("QYWAMT").toString());
			BigDecimal qtAmt = new BigDecimal(moneyMap.get("QTAMT").toString());
			BigDecimal jfAmt = new BigDecimal(moneyMap.get("JFAMT").toString());
			BigDecimal yhkAmt = new BigDecimal(moneyMap.get("YHKAMT").toString());
			if(totalAmt.compareTo(qyAmt.add(qtAmt).add(jfAmt).add(yhkAmt)) != 0){
				//司机账户有误，请联系管理员
				jsonObject.put("resCode", "4006");
				jsonObject.put("resMsg", "司机账户有误，请联系管理员");
				return jsonObject;
			}
			
			BigDecimal balanceAmt =  new BigDecimal(map.get("balanceAmt").toString())
				.add(new BigDecimal(map.get("cardAmt").toString()));	
			System.out.println("balanceAmt.toString() == " + balanceAmt.toString());
			System.out.println("cardAmt.toString() == " + map.get("cardAmt").toString());
			BigDecimal balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
			
			//taxi司机保存moneymx
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			Map moneyMxMap = new HashMap();
			moneyMxMap.put("orderId", map.get("orderId").toString() + "0" + "taxi");
			moneyMxMap.put("zforderId", map.get("orderId").toString() + "taxi");
			moneyMxMap.put("payertime", formatter.format(new Date()));
			moneyMxMap.put("userNo", map.get("taxiUserNo").toString());
			moneyMxMap.put("mobile", "");
			moneyMxMap.put("fantime", "");
			moneyMxMap.put("transamt1", balanceAmt);//交易金额
			moneyMxMap.put("transamt2", map.get("totalAmt").toString());//原用户余额
			moneyMxMap.put("transamt3", balance.add(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));//交易后金额
			moneyMxMap.put("type", "3");   //TODO 这个需要参考水费返现的type
			moneyMxMap.put("transamt5", "0.00");
			moneyMxMap.put("cardno", "");
			moneyMxMap.put("channerno", "01");
			moneyMxMap.put("transtype", "1");  //TODO 这个需要参考水费返现的type
			moneyMxMap.put("feetype", map.get("feeType").toString());
			moneyMxMap.put("status", "1");
			moneyMxMap.put("middleflow", "");
			moneyMxMap.put("middledate", "");
			moneyMxMap.put("coreflow", "");
			moneyMxMap.put("coredate", "");
			moneyMxMap.put("dzstatus", "0");
			moneyMxMap.put("dzdate", "");
			moneyMxMap.put("dztime", "");
			moneyMxMap.put("note1", "出租车收款");
			moneyMxMap.put("note2", "");
			moneyMxMap.put("note3", "1");
			moneyMxMap.put("note4", "");
			moneyMxMap.put("note5", "");
			int updateStatus = appMoneyDao.insertMoneyMx(moneyMxMap);
			logger.debug("updateStatus === "+updateStatus);
			
			//正确增加余额
			
		    balance = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
			BigDecimal yhkamt = new BigDecimal(((Map) moneyList.get(0)).get("YHKAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 其他金额

			map.put("totalamt", balance.add(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
			map.put("yhkamt", yhkamt.add(balanceAmt).setScale(2, BigDecimal.ROUND_HALF_UP));
			updateStatus = appMoneyDao.updateMoneyForTaxi(map);
			logger.debug("updateStatus === "+updateStatus);
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "收款成功");
			return jsonObject;
		 } else {
			 jsonObject.put("resCode", "4006");
				jsonObject.put("resMsg", "司机余额账户不存在");
				return jsonObject;
		 }
	}
	
	/**
	 * 根据手机号码到讯飞查询userno
	 */
	public String getTaxiUserNoByPhone(String phone) {
		
		String userno = "";
		String url = "http://192.168.10.112:8080/wjbz-service/services/rest/user/queryUserIdByPhone/" + phone;
		
		RestUtil restUtil = new RestUtil();
		String resultString = restUtil.load(url, "");
		System.out.println("司机手机号 =====> "  + phone);
		System.out.println("获取到的司机userno =====> "  + resultString);
		
		if ("".equals(resultString)) {
			System.out.println("根据电话号码查询userno异常");
			return userno;
		}
		
		JSONObject json = JSONObject.fromObject(resultString);
		userno = json.getString("data");
		return userno;
	}
	
	/**
	 * 出租车缴费回调
	 * 
	 */
	public void callBack(Map map) {
		List orderList = orderDao.queryOrder(map);  //根据orderId 获取到
		if(orderList != null && orderList.size() > 0){
			Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
			Map notifyMap = PayNotify.taxiNotify(orderMap);
			logger.debug("notifyMap === "+notifyMap);
		}
	}
	

	/** 
	 *出租打车银行支付
	 */
	public Object cardMainForTaxi(Map map,String flag) throws Exception{
		 JSONObject jsonObject = new JSONObject();
		 PayUtil_YDYH ahnj = new PayUtil_YDYH();
		 Map<String,String> returnMap = ahnj.payMoney(map);
		 if("0000".equals(returnMap.get("flag"))){
			//如果银行支付成功修改状态
			map.put("flag", flag);
			map.put("status", "1");
			map.put("middleflow1", returnMap.get("middleflow1"));
			map.put("middledate1", returnMap.get("middledate1"));
			map.put("coreflow1", returnMap.get("coreflow1"));
			map.put("coredate1", returnMap.get("coredate1"));
			int updateStatus = orderDao.updatePayMxStatusByCard(map);
			logger.debug("updateStatus === "+updateStatus);
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "支付成功");
			jsonObject.put("orderId",map.get("orderId").toString());
			jsonObject.put("payerTime",map.get("payerTime").toString());
			jsonObject.put("seqNo","");
			return jsonObject;
		 }else if("0001".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
	   		jsonObject.put("resMsg", returnMap.get("errorMessage"));
	   		return jsonObject;
		 }else if("9999".equals(returnMap.get("flag"))){
			jsonObject.put("resCode", "4006");
			jsonObject.put("resMsg", returnMap.get("errorMessage"));
			return jsonObject;
		 }else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		 }
	}
	
	/**
	 * taxi返现操作
	 */
	public void cashBackTaxi(Map map){
		logger.debug("进入返现环节 =======》"+map);
		Date date = new Date();
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMM");
		//设置一个查询交易记录表(app_paymx)查询条件。
		Map<String , Object> maps =new HashMap<String, Object>();
		maps.put("userNo", map.get("userNo").toString()); //用户编号
		maps.put("feetype", "99"); //出租车打车字典类型
		maps.put("note4", "出租车返现"); //备注———参数为出租车返现。
		maps.put("payTimes",sim.format(date)); //该用户本月交易数量
		//根据上述条件查询数据库
		List lists = orderDao.queryTaxiCashBack(maps);
		//若本月不存在已返现的出租车打车记录，则进行返现操作。
		if( lists.isEmpty() || lists == null){
			List moneyList = appMoneyDao.queryBalance(map);// 查询用户的余额帐户数据
			if(moneyList != null && moneyList.size()>0){
				Map moneyMap = (Map)moneyList.get(0);
				//产生返现金额 0.5到5元随机产生一个数据
				Double xx= (0.5+(Double)Math.random()*4.5);
				DecimalFormat df = new DecimalFormat("0.00");
				BigDecimal transamt1 =new BigDecimal(Double.parseDouble(df.format(xx))).setScale(2,BigDecimal.ROUND_HALF_UP);
				BigDecimal transamt2 = new BigDecimal(moneyMap.get("TOTALAMT").toString());
				BigDecimal qyAmt = new BigDecimal(moneyMap.get("QYWAMT").toString());
				BigDecimal qtAmt = new BigDecimal(moneyMap.get("QTAMT").toString());
				BigDecimal jfAmt = new BigDecimal(moneyMap.get("JFAMT").toString());
				BigDecimal yhkAmt = new BigDecimal(moneyMap.get("YHKAMT").toString());
				if(transamt2.compareTo(qyAmt.add(qtAmt).add(jfAmt).add(yhkAmt)) != 0){
					logger.debug("用户帐户有误");
					return ;
				}
				//产生一条APP_MONEYMX
				Map moneyMxMap = new HashMap();
				moneyMxMap.put("orderId", map.get("orderId").toString() + "2" + "taxiCash");
				moneyMxMap.put("zforderId", map.get("orderId").toString()); //支付orderId
				moneyMxMap.put("payertime", formatter.format(new Date()));
				moneyMxMap.put("userNo", map.get("userNo").toString());
				moneyMxMap.put("mobile", "");
				moneyMxMap.put("fantime", "");
				moneyMxMap.put("transamt1", transamt1);//交易金额
				moneyMxMap.put("transamt2", transamt1);//原用户余额
				moneyMxMap.put("transamt3", transamt2.add(transamt1).setScale(2, BigDecimal.ROUND_HALF_UP));//反现之后金额
				moneyMxMap.put("type", "4");   //TODO 这个需要参考水费返现的type
				moneyMxMap.put("transamt5", "0.00");
				moneyMxMap.put("cardno", "");
				moneyMxMap.put("channerno", "01");
				moneyMxMap.put("transtype", "1"); 
				moneyMxMap.put("feetype", map.get("feeType").toString());
				moneyMxMap.put("status", "1");
				moneyMxMap.put("middleflow", "");
				moneyMxMap.put("middledate", "");
				moneyMxMap.put("coreflow", "");
				moneyMxMap.put("coredate", "");
				moneyMxMap.put("dzstatus", "0");
				moneyMxMap.put("dzdate", "");
				moneyMxMap.put("dztime", "");
				moneyMxMap.put("note1", "");
				moneyMxMap.put("note2", "");
				moneyMxMap.put("note3", "1");
				moneyMxMap.put("note4", "出租车返现");
				moneyMxMap.put("note5", "");
				int moneyMxResult = appMoneyDao.insertMoneyMx(moneyMxMap);
				logger.debug("返现插入金额明细结果   " + moneyMxResult);
				//更新APP_MONEY
				Map money = new HashMap();
				money.put("id", map.get("userNo"));
				money.put("qtamt", qtAmt.add(transamt1).setScale(2, BigDecimal.ROUND_HALF_UP));
				money.put("totalamt", transamt2.add(transamt1).setScale(2, BigDecimal.ROUND_HALF_UP) );
				int moneyResult = appMoneyDao.updateMoney(money);
				logger.debug("更新余额表，状况" + moneyResult);
			} else {
				//如果用户余额账户不存在的话我们这边需要新增一个用户并且将信息保存到最终的moneyList中以形成一个类似查询到余额的效果
				Map<String, Object> taxiMap = new HashMap<String, Object>();
				taxiMap.put("USERNO", map.get("userNo"));
				taxiMap.put("PHONE", "");
				taxiMap.put("TOTALAMT", new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
				taxiMap.put("QYWAMT", new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
				taxiMap.put("JFAMT", new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
				taxiMap.put("QTAMT", new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
				taxiMap.put("YHKAMT", new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
				taxiMap.put("NOTE1", "");
				taxiMap.put("NOTE2", "");
				taxiMap.put("NOTE3", "");
				taxiMap.put("NOTE4", "");
				taxiMap.put("NOTE5", "");
				moneyList.add(taxiMap);
				int number = appMoneyDao.insertTaxiMoney(taxiMap);
				
				if(number !=0 ){
					//获取到moneyList
					Map moneyMap = (Map)moneyList.get(0);
					//产生返现金额 0.5到5元随机产生一个数据
					Double xx= (0.5+(Double)Math.random()*4.5);
					DecimalFormat df = new DecimalFormat("0.00");
					BigDecimal transamt1 =new BigDecimal(Double.parseDouble(df.format(xx)));
					BigDecimal transamt2 = new BigDecimal(moneyMap.get("TOTALAMT").toString());
					BigDecimal qyAmt = new BigDecimal(moneyMap.get("QYWAMT").toString());
					BigDecimal qtAmt = new BigDecimal(moneyMap.get("QTAMT").toString());
					BigDecimal jfAmt = new BigDecimal(moneyMap.get("JFAMT").toString());
					BigDecimal yhkAmt = new BigDecimal(moneyMap.get("YHKAMT").toString());
					if(transamt2.compareTo(qyAmt.add(qtAmt).add(jfAmt).add(yhkAmt)) != 0){
						logger.debug("用户帐户有误");
						return;
					}
					//产生一条APP_MONEYMX
					Map moneyMxMap = new HashMap();
					moneyMxMap.put("orderId", map.get("orderId").toString() + "2" + "taxiCash");
					moneyMxMap.put("zforderId", map.get("orderId").toString()); //支付orderId
					moneyMxMap.put("payertime", formatter.format(new Date()));
					moneyMxMap.put("userNo", map.get("userNo").toString());
					moneyMxMap.put("mobile", "");
					moneyMxMap.put("fantime", "");
					moneyMxMap.put("transamt1", transamt1);//交易金额
					moneyMxMap.put("transamt2", transamt1);//原用户余额
					moneyMxMap.put("transamt3", transamt2.add(transamt1).setScale(2, BigDecimal.ROUND_HALF_UP));//反现之后金额
					moneyMxMap.put("type", "4");   //TODO 这个需要参考水费返现的type
					moneyMxMap.put("transamt5", "0.00");
					moneyMxMap.put("cardno", "");
					moneyMxMap.put("channerno", "01");
					moneyMxMap.put("transtype", "1"); 
					moneyMxMap.put("feetype", map.get("feeType").toString());
					moneyMxMap.put("status", "1");
					moneyMxMap.put("middleflow", "");
					moneyMxMap.put("middledate", "");
					moneyMxMap.put("coreflow", "");
					moneyMxMap.put("coredate", "");
					moneyMxMap.put("dzstatus", "0");
					moneyMxMap.put("dzdate", "");
					moneyMxMap.put("dztime", "");
					moneyMxMap.put("note1", "");
					moneyMxMap.put("note2", "");
					moneyMxMap.put("note3", "1");
					moneyMxMap.put("note4", "出租车返现");
					moneyMxMap.put("note5", "");
					int moneyMxResult = appMoneyDao.insertMoneyMx(moneyMxMap);
					logger.debug("返现插入金额明细结果   " + moneyMxResult);
					//更新APP_MONEY
					Map money = new HashMap();
					money.put("id", map.get("userNo"));
					money.put("qtamt", qtAmt.add(transamt1).setScale(2, BigDecimal.ROUND_HALF_UP));
					money.put("totalamt", transamt2.add(transamt1).setScale(2, BigDecimal.ROUND_HALF_UP) );
					int moneyResult = appMoneyDao.updateMoney(money);
					logger.debug("更新余额表，状况" + moneyResult);
				} else {
					logger.debug("钱包创建失败");
				}
			}
		} else{
		}
	}

}
