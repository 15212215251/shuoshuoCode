package com.untech.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.untech.dao.AppMerchantParamDao;
import com.untech.dao.AppMoneyDao;
import com.untech.dao.AppPaycodeInfoDao;
import com.untech.dao.AppPaycodeLimitinfoDao;
import com.untech.dao.AppSecurtDao;
import com.untech.dao.AppSignDao;
import com.untech.dao.AppUnitDao;
import com.untech.dao.CommerDao;
import com.untech.dao.OrderDao;
import com.untech.service.MerchantService;
import com.untech.util.DateUtils;
import com.untech.util.OrderId;
import com.untech.util.PayNotify;
import com.untech.util.PayUtil_YDYH;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("merchantService")
@Transactional
public class MerchantServiceImpl implements MerchantService {
	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(MerchantServiceImpl.class.getName());
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	
	SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");

	@Autowired
	private AppMerchantParamDao appMerchantParamDao;
	
	@Autowired
	private AppPaycodeInfoDao appPaycodeInfoDao;
	
	@Autowired
	private AppPaycodeLimitinfoDao appPaycodeLimitinfoDao;
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private AppUnitDao appUnitDao;
	
	@Autowired
	private AppMoneyDao appMoneyDao;
	
	@Autowired
	private AppSecurtDao appSecurtDao;

	@Autowired
	private CommerDao commerDao;
	
	@Autowired
	private AppSignDao appSignDao;

	public AppMerchantParamDao getAppMerchantParamDao() {
		return appMerchantParamDao;
	}
	public void setAppMerchantParamDao(AppMerchantParamDao appMerchantParamDao) {
		this.appMerchantParamDao = appMerchantParamDao;
	}
	public List queryMerchantParam(Map map) {
		return appMerchantParamDao.queryMerchantParam(map);
	}
	
	public OrderDao getOrderDao() {
		return orderDao;
	}
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	
	public AppPaycodeInfoDao getAppPaycodeInfoDao() {
		return appPaycodeInfoDao;
	}
	public void setAppPaycodeInfoDao(AppPaycodeInfoDao appPaycodeInfoDao) {
		this.appPaycodeInfoDao = appPaycodeInfoDao;
	}
	public AppPaycodeLimitinfoDao getAppPaycodeLimitinfoDao() {
		return appPaycodeLimitinfoDao;
	}
	public void setAppPaycodeLimitinfoDao(
			AppPaycodeLimitinfoDao appPaycodeLimitinfoDao) {
		this.appPaycodeLimitinfoDao = appPaycodeLimitinfoDao;
	}
	
	
	public AppUnitDao getAppUnitDao() {
		return appUnitDao;
	}
	public void setAppUnitDao(AppUnitDao appUnitDao) {
		this.appUnitDao = appUnitDao;
	}
	public AppMoneyDao getAppMoneyDao() {
		return appMoneyDao;
	}
	public void setAppMoneyDao(AppMoneyDao appMoneyDao) {
		this.appMoneyDao = appMoneyDao;
	}
	public AppSecurtDao getAppSecurtDao() {
		return appSecurtDao;
	}
	public void setAppSecurtDao(AppSecurtDao appSecurtDao) {
		this.appSecurtDao = appSecurtDao;
	}
	public CommerDao getCommerDao() {
		return commerDao;
	}
	public void setCommerDao(CommerDao commerDao) {
		this.commerDao = commerDao;
	}
	public AppSignDao getAppSignDao() {
		return appSignDao;
	}
	public void setAppSignDao(AppSignDao appSignDao) {
		this.appSignDao = appSignDao;
	}
	/**
	 * 1、存放本方流水号 SEQNO
	 * 2、TRAFFICNO  是否输入密码支付 01 无密支付  02 有密支付
	 * 3、TRAFFICTIME 订单完成时间，即支付成功时间
	 * */
	public Map create(Map map) {
		Map returnMap = new HashMap();
		map.put("orderId", map.get("outTradeNo"));
		map.put("payCode", map.get("payCode"));
		//查询商户的付款码是否可用
		String totalAmout = map.get("totalAmout").toString();
		String tradeNo = OrderId.getWjbzOrderNo();//唯一订单号
		map.put("tradeNo", tradeNo);
		//查询订单号是否存在
		List orderList = orderDao.queryOrderDetail(map);
		if(orderList != null && orderList.size()>0){
			returnMap.put("resCode", "3004");
			returnMap.put("resMsg", "订单号重复");
			return returnMap;
		}
		//查询付款码是否可以使用
		List payCodeList  = appPaycodeInfoDao.queryPayCodeinfo(map);
		if(payCodeList != null && payCodeList.size() > 0){
			Map tmpMap = (Map) payCodeList.get(0);
			String payerTime = tmpMap.get("payerTime").toString();
			long time  = DateUtils.getsubDate(payerTime, formatter.format(new Date()));
			if(time >60){
				returnMap.put("resCode", "5000");
				returnMap.put("resMsg", "付款码不存在或已失效");
				return returnMap;
			}
			//判断用户有没有设置小额免密
			String userNo = tmpMap.get("USERNO").toString();
			tmpMap.put("userNo", userNo);
			map.put("userNo", userNo);
			List infoList  =  appPaycodeLimitinfoDao.queryLimitinfo(tmpMap);
			if(infoList != null && infoList.size() > 0){
				Map limitInfo = (Map)infoList.get(0);
				String status = limitInfo.get("STATUS").toString();
				if("Y".equals(status)){
					//查询当日小额免密金额，判断本次小额免密金额
					
					String transamt =  limitInfo.get("TRANSAMT").toString();
					String daytransamt =  limitInfo.get("DAYTRANSAMT").toString();
					if(new BigDecimal(totalAmout).compareTo(new BigDecimal(transamt)) > 0){
						//默认是需要输入支付密码的
						//推送支付密码输入界面
						returnMap.put("resCode", "5001");
						returnMap.put("resMsg", "待用户输入支付密码");
						return returnMap;
					}else{
						//查询当日小额免密金额
						tmpMap.put("date", formatter2.format(new Date()));
						tmpMap.put("type","01");
						List amtList = orderDao.querySumOrderamt(tmpMap);
						if(amtList != null && amtList.size() >0){
							String allamt = ((Map)amtList.get(0)).get("PAYAMT").toString();
							if(new BigDecimal(totalAmout).add(new BigDecimal(allamt)).compareTo(new BigDecimal(daytransamt)) > 0){
								returnMap.put("resCode", "5001");
								returnMap.put("resMsg", "待用户输入支付密码");
								return returnMap;
							}
							//直接进行支付
							/*
							 * 默认支付顺序
							 * 1、先扣除钱包金额
							 * 2、扣除最近消费的银行卡金额
							 * 3、依次扣除银行卡，直到支付成功
							 * */
							tmpMap.put("USERID", userNo);
							List payList = orderDao.queryBalance(map);// 查询余额
							String money = "";
							if(payList != null && payList.size() > 0){
								money  = ((Map)payList.get(0)).get("TOTALAMT").toString();
							}
							//余额足够，进行余额支付
							//调用余额支付通用方法
							if(new BigDecimal(money).compareTo(new BigDecimal(totalAmout)) >= 0){
								//异步推送并回掉
								return balanceMainFirst(map);
							}else{
								//查询最近一次银行卡消费卡号，若没有则继续查询银行卡下面卡号
								Map cardMap = new HashMap();
								List payTypeList = orderDao.queryLastOrder(tmpMap);
								String cardNo = "";
								if(payTypeList != null && payTypeList.size()>0){
									cardNo = ((Map)payTypeList.get(0)).get("ACCTNO").toString();
								}
								//查询银行卡余额，然后调用支付
								List cardList = appSignDao.queryBankcardList(tmpMap);
								if(cardList != null && cardList.size() > 0){
									for(int i=0;i<cardList.size();i++){
										cardMap.put(((Map)cardList.get(i)).get("ACCTNO"), ((Map)cardList.get(i)).get("ACCTNAME")+"|"+((Map)cardList.get(i)).get("PHONE"));
									}
									if(!"".equals(cardNo) && cardMap.get(cardNo) != null ){
										tmpMap.put("outCardNo", cardNo);
										tmpMap.put("outCardName", cardMap.get(cardNo).toString().split("\\|")[0]);
										PayUtil_YDYH ydyh = new PayUtil_YDYH();
										Map ret = ydyh.queryBalance(cardMap);
										if("0000".equals(ret.get("resCode").toString())){
											String bankamt = ret.get("balance").toString();
											cardMap.remove(cardNo);
											System.out.println("cardMap===="+cardMap);
											if(  new BigDecimal(bankamt).compareTo(new BigDecimal(totalAmout).add(new BigDecimal(1.00))) >=0 ){
												//银行卡进行支付，调用银行卡支付
												map.put("outCardNo", cardNo);
												map.put("outCardName", cardMap.get(cardNo).toString().split("\\|")[0]);
												return cardPayment(map);
											}else{
												Set set = cardMap.entrySet();     
												Iterator i = set.iterator();     
												while(i.hasNext()){  
												    Map.Entry<String, String> entry=(Map.Entry<String, String>)i.next(); 
												    System.out.println(entry.getKey()+"=="+entry.getValue()); 
												    tmpMap.put("outCardNo", cardNo);
													tmpMap.put("outCardName", cardMap.get(cardNo).toString().split("\\|")[0]);
													ret = ydyh.queryBalance(cardMap);
													if("0000".equals(ret.get("resCode").toString())){
														bankamt = ret.get("balance").toString();
														if(  new BigDecimal(bankamt).compareTo(new BigDecimal(totalAmout).add(new BigDecimal(1.00))) >=0 ){
															//银行卡进行支付，调用银行卡支付
															map.put("outCardNo", cardNo);
															map.put("outCardName", cardMap.get(cardNo).toString().split("\\|")[0]);
															map.put("mobile", cardMap.get(cardNo).toString().split("\\|")[1]);
															return cardPayment(map);
														}
													}else{
														returnMap.put("resCode", "5003");
														returnMap.put("resMsg", "银行接口异常");
														return returnMap;
													}
												} 
											}
										}else{
											returnMap.put("resCode", "5003");
											returnMap.put("resMsg", "银行接口异常");
											return returnMap;
										}
									}
								}else{
									returnMap.put("resCode", "4007");
									returnMap.put("resMsg", "系统异常");
									return returnMap;
								}
							}
						}else{
							returnMap.put("resCode", "4007");
							returnMap.put("resMsg", "系统异常");
							return returnMap;
						}
					}
					
				}else if("N".equals(status)){
					//默认是需要输入支付密码的
					//推送支付密码输入界面
					returnMap.put("resCode", "5001");
					returnMap.put("resMsg", "待用户输入支付密码");
					return returnMap;
				}
			}else{
				//默认是需要输入支付密码的
				//推送支付密码输入界面
				returnMap.put("resCode", "5001");
				returnMap.put("resMsg", "待用户输入支付密码");
				return returnMap;
			}
			
		}else{
			returnMap.put("resCode", "5000");
			returnMap.put("resMsg", "付款码不存在或已失效");
			return returnMap;
		}
		return null;
	}
	
	
	
	
	//余额支付和更新
	public Map balanceMainFirst(Map map){
		Map retMap = new HashMap();
		//flag 01 不验证密码   02 验证密码
		String flag = map.get("flag").toString();
		if("02".equals(flag)){
			// 需要校验用户支付密码是否正确
			List list = appSecurtDao.getAppSecurt(map);
			if (list == null || list.size() == 0) {
				retMap.put("resCode", "2001");
				retMap.put("resMsg", "未设置支付密码");
				return retMap;
			}
			Map securtMap = (Map) list.get(0);
			int cnts  = Integer.parseInt(securtMap.get("CNTS").toString());
			// 支付密码锁住
			if ( cnts >= 3) {
				retMap.put("resCode", "2002");
				retMap.put("resMsg", "支付密码次数超限，请重置支付密码【短信验证方式】");
				return retMap;
			}
			// 支付密码不正确
			if (!securtMap.get("SECURT").toString().equals(map.get("paySecurt").toString())) {
				appSecurtDao.updateSecurt(map);
				retMap.put("resCode", "2003");
				retMap.put("resMsg", "支付密码不正确，还有"+(2-cnts)+"次机会");
				return retMap;
			} else {// 密码正确更新错误次数为0
				if(cnts != 0){
					appSecurtDao.updateCnts(map);
				}
			}
		}
		//保存支付明细，保存为余额支付渠道
		/**
		 * 转账报文,调用中台(中间账号转账至商户账号)
		 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
		 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
		 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
		 */
		StringBuffer sb = new StringBuffer();
		sb.append("0002|WJBZ|" + map.get("outTradeNo").toString() + "|");
		map.put("unitNo", map.get("merchantId"));
		List unitList = appUnitDao.queryUnitByUnitNo(map);
		List inList = null;
		String unitName = "";
		String merchantId = "";
		String feeType  =  "";
		if(unitList != null && unitList.size()>0){
			// 获取转入账号信息
			inList = commerDao.queryCommer(((Map)unitList.get(0)).get("MERCHANTID").toString());/*商户编号*/
			feeType = ((Map)unitList.get(0)).get("PRONAME").toString();
			unitName = ((Map)unitList.get(0)).get("UNITNAME").toString();
			merchantId = ((Map)unitList.get(0)).get("MERCHANTID").toString();
		}else{
			retMap.put("resCode", "3002");
			retMap.put("resMsg", "商户信息未配置");
			return retMap;
		}
		 
		// 获取转出账号,从市政府对公账号
		List outList = commerDao.queryCommerByType("99");
		if (null == outList || outList.size() <= 0) {
			retMap.put("resCode", "3002");
			retMap.put("resMsg", "商户号未配置");
			return retMap;
		} 
		if (null == inList || inList.size() <= 0) {
			retMap.put("resCode", "3002");
			retMap.put("resMsg", "商户号未配置");
			return retMap;
		} 
		sb.append(((Map) outList.get(0)).get("ACCTNO").toString() + "|" + ((Map) outList.get(0)).get("ACCTNAME").toString() + "|" + ((Map) inList.get(0)).get("ACCTNO").toString() + "|" + ((Map) inList.get(0)).get("ACCTNAME").toString() + "|");
		// 中台转账金额
		sb.append(new BigDecimal(map.get("totalAmount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP) + "|" + "|");
		System.out.println("sb-------------"+sb);
		//执行数据库保存操作
		map.put("message", sb.toString());
		map.put("payerTime",formatter.format(new Date()));
		map.put("unitName",unitName);
		map.put("orderId", map.get("outTradeNo"));
		map.put("cardNo", "");
		map.put("mobile", "");
		map.put("channelNo", "01");
		map.put("flag", "0");
		map.put("feeType", feeType);
		map.put("merchantId",merchantId);
		map.put("backUrl", map.get("notifyUrl"));
		map.put("transType","1");
		map.put("balanceAmt", map.get("totalAmount"));
		map.put("cardAmt", "0.00");
		map.put("totalAmt", map.get("totalAmount"));
		map.put("payType", "02");//moneyflag
		map.put("status","0");
		map.put("detail", "");
		map.put("seqNo", map.get("tradeNo"));//我方系统订单号
		map.put("traficNo", flag);//01 无密支付 02有密支付 
		/*
		 *  (#{orderId},#{payerTime},#{userNo},#{unitNo},#{unitName},#{totalAmt},#{cardNo},#{mobile},#{channelNo},#{flag},#{feeType},
             #{merchantId},#{backUrl},0.00,#{transType},#{balanceAmt},#{cardAmt},0.00,0.00,#{totalAmt},#{payType},
             #{status},#{detail},#{seqNo},#{traficNo} )
		 * *
		 */
		System.out.println("insertPaymx map=="+map);
	    int result = orderDao.insertPayCodePaymx(map);
	    System.out.println("result --- "+result);
	    map.put("resCode", "9999");
	    return map;
	}
	
	//余额支付和更新
	public Object balanceMain(Map map) {
		/**
		 * 转账报文,调用中台(中间账号转账至商户账号)
		 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
		 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
		 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
		 */
		String  sb = map.get("message").toString();
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
			map.put("flag","2");
			map.put("status", "1");
			map.put("middleflow1", returnMap.get("middleflow1"));
			map.put("middledate1", returnMap.get("middledate1"));
			map.put("coreflow1", returnMap.get("coreflow1"));
			map.put("coredate1", returnMap.get("coredate1"));
			map.put("successTime", formatter.format(new Date()));
			System.out.println("支付参数＝＝＝＝＝＝》" + map);
			int updateStatus = orderDao.updatePayCodeByMoney(map);
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
		 } else if("8888".equals(returnMap.get("flag"))){
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
	
	//银行卡支付
	public Map cardPayment(Map map) {
		Map retMap = new HashMap();
		//flag 01 不验证密码   02 验证密码
		String flag = map.get("flag").toString();
		if("02".equals(flag)){
			// 需要校验用户支付密码是否正确
			List list = appSecurtDao.getAppSecurt(map);
			if (list == null || list.size() == 0) {
				retMap.put("resCode", "2001");
				retMap.put("resMsg", "未设置支付密码");
				return retMap;
			}
			Map securtMap = (Map) list.get(0);
			int cnts  = Integer.parseInt(securtMap.get("CNTS").toString());
			// 支付密码锁住
			if ( cnts >= 3) {
				retMap.put("resCode", "2002");
				retMap.put("resMsg", "支付密码次数超限，请重置支付密码【短信验证方式】");
				return retMap;
			}
			// 支付密码不正确
			if (!securtMap.get("SECURT").toString().equals(map.get("paySecurt").toString())) {
				appSecurtDao.updateSecurt(map);
				retMap.put("resCode", "2003");
				retMap.put("resMsg", "支付密码不正确，还有"+(2-cnts)+"次机会");
				return retMap;
			} else {// 密码正确更新错误次数为0
				if(cnts != 0){
					appSecurtDao.updateCnts(map);
				}
			}
		}
		map.put("unitNo", map.get("merchantId"));
		List unitList = appUnitDao.queryUnitByUnitNo(map);
		String merchantId  = "";
		String unitName = "";
		String feeType="";
		if(unitList != null && unitList.size()>0){
			 merchantId  = ((Map)unitList.get(0)).get("MERCHANTID").toString();
			 unitName = ((Map)unitList.get(0)).get("UNITNAME").toString();
			 feeType = ((Map)unitList.get(0)).get("PRONAME").toString();
			 Map m = (Map)unitList.get(0);
			 // 获取转入
			 List inList = commerDao.queryCommer(merchantId);/*商户编号*/
			 if (inList == null || inList.size() == 0) {
				 retMap.put("resCode", "3002");
				 retMap.put("resMsg", "商户号未配置");
				 return retMap;
			 }else{
				 map.put("inCardNo",((Map)inList.get(0)).get("ACCTNO"));
				 map.put("inCardName",((Map)inList.get(0)).get("ACCTNAME"));
			 }
		 }else{
			 retMap.put("resCode", "3002");
			 retMap.put("resMsg", "商户号未配置");
			 return retMap;
		 }
		//执行数据库保存操作
		 /**
		  * sb.append(map.get("outCardNo").toString() + "|"+map.get("outCardName").toString() + "|");
		sb.append(map.get("inCardNo").toString() + "|"+map.get("inCardName").toString() + "|");
		  * */
		
		//执行数据库保存操作
		map.put("payerTime",formatter.format(new Date()));
		map.put("unitName",unitName);
		map.put("orderId", map.get("outTradeNo"));
		map.put("cardNo", map.get("outCardNo"));
		map.put("channelNo", "01");
		map.put("flag", "0");
		map.put("feeType", feeType);
		map.put("merchantId",merchantId);
		map.put("backUrl", map.get("notifyUrl"));
		map.put("transType","1");
		map.put("balanceAmt", "0.00");
		map.put("cardAmt", map.get("totalAmount"));
		map.put("totalAmt", map.get("totalAmount"));
		map.put("payType", "01");//moneyflag
		map.put("status","1");
		map.put("detail", "");
		map.put("seqNo", map.get("tradeNo"));//我方系统订单号
		map.put("traficNo", flag);//01 无密支付 02有密支付 
		 
		map.put("payerTime",formatter.format(new Date()));
		map.put("unitName",unitName);
		map.put("flag","0");
		map.put("merchantId",merchantId);
		map.put("transType","1");
		map.put("status","1");
		map.put("oldOrderId","");
		//不需要验证密码时候保存，需要验证密码的时候已经保存
		if("01".equals(flag)){
			int result = orderDao.insertPaymx(map);
			logger.info("保存支付明细：" + result);
		}
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
			map.put("successTime", formatter.format(new Date()));
			int updateStatus = orderDao.updatePayCodeByCard(map);
			logger.debug("updateStatus === "+updateStatus);
			//回调接口 
			List orderList = orderDao.queryOrder(map);
			if(orderList != null && orderList.size() > 0){
				Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
				Map notifyMap = PayNotify.notify(orderMap);
				logger.debug("notifyMap === "+notifyMap);
			}
			retMap.put("resCode", "0000");
			retMap.put("resMsg", "支付成功");
			retMap.put("orderId",map.get("orderId").toString());
			retMap.put("payerTime",map.get("payerTime").toString());
			retMap.put("seqNo","");
			return retMap;
		 }else if("0001".equals(returnMap.get("flag"))){
			retMap.put("resCode", "4006");
			retMap.put("resMsg", returnMap.get("errorMessage"));
	   		return retMap;
		 }else if("9999".equals(returnMap.get("flag"))){
			 retMap.put("resCode", "4006");
			 retMap.put("resMsg", returnMap.get("errorMessage"));
			return retMap;
		 }else{
			 retMap.put("resCode", "4007");
			 retMap.put("resMsg", "系统异常");
			return retMap;
		 }
		
	}

}
