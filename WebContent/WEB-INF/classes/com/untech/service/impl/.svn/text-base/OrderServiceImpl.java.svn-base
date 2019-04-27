package com.untech.service.impl;

import java.math.BigDecimal;
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
import com.untech.dao.AppSecurtDao;
import com.untech.dao.AppSignDao;
import com.untech.dao.CommerDao;
import com.untech.dao.MachineDao;
import com.untech.dao.OrderDao;
import com.untech.jpush.JPushObject;
import com.untech.service.OrderService;
import com.untech.util.DateUtils;
import com.untech.util.IdSequence;
import com.untech.util.PayUtil_AHNJ;
import com.untech.util.PayUtil_YDYH;
import com.untech.util.ResponseModel;
import com.untech.util.SignAndVertyUtil;
import com.untech.util.SocketClient;
import com.untech.util.XmlUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {
	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private MachineDao machineDao;

	@Autowired
	private AppSecurtDao appSecurtDao;

	@Autowired
	private CommerDao commerDao;
	
	@Autowired
	private AppSignDao appSignDao;
	
	@Autowired
	private AppMoneyDao appMoneyDao;

	/**
	 * 保存交易号，确立交易号、用户、手机ID三者关系
	 */
	public boolean insertTransactionNo(String id, String sid,
			String transactionNo) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("id", id);
		map.put("sid", sid);
		map.put("transactionNo", transactionNo);
		map.put("addTime", DateUtils.getCurrentTime());
		if (orderDao.insertTransactionNo(map) != 0) {
			return true;
		}

		return false;
	}

	/**
	 * 保存订单
	 */
	public boolean insertOrder(Map map) {
		try {
			// TODO Auto-generated method stub
			/*List transList = orderDao.queryTransaction(map.get("orderId")
					.toString());
			if (transList == null || transList.size() == 0) {
				return false;
			}*/
			List list = machineDao.queryById(map);
			if (list.size() == 0) {// 查询不到自动售货机
				return false;
			}
			
			map.put("payerTime", formatter.format(new Date()));
			//map.put("userId", ((Map) transList.get(0)).get("ID").toString());
			map.put("unitNo", ((Map) list.get(0)).get("UNITNO").toString());
			map.put("unitName", ((Map) list.get(0)).get("UNITNAME").toString());
			List unitList = machineDao.queryUnitByUnitNo(map);
			if(unitList != null && unitList.size()>0){
				// 获取转入商户
				map.put("note1",((Map)unitList.get(0)).get("MERCHANTID"));/*商户编号*/
			}
			if (orderDao.insertOrder(map) == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 保存订单 二维码
	 */
	public boolean insertQrCodeOrder(Map map) {
		try {
			List list = machineDao.queryById(map);
			if (list.size() == 0) {// 查询不到自动售货机
				return false;
			}
			map.put("payerTime", formatter.format(new Date()));
			map.put("unitNo", ((Map) list.get(0)).get("UNITNO").toString());
			map.put("unitName", ((Map) list.get(0)).get("UNITNAME").toString());
			List unitList = machineDao.queryUnitByUnitNo(map);
			if(unitList != null && unitList.size()>0){
				// 获取转入商户
				map.put("note1",((Map)unitList.get(0)).get("MERCHANTID"));/*商户编号*/
			}
			if (orderDao.insertQrCodeOrder(map) == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	 

	/**
	 * 查询订单详情
	 */
	public Map queryOrderDetail(Map map) {
		// TODO Auto-generated method stub
		List list = orderDao.queryOrderDetail(map);
		String money = "0.00";
		if (list != null && list.size() > 0) {
			// 查询默认支付方式(最近一次支付方式)
			map = (Map) list.get(0);
			List payTypeList = orderDao.queryLastOrder(map);
			List payList = orderDao.queryBalance(map);// 查询余额
			if(payList != null && payList.size() > 0){
				money  = ((Map)payList.get(0)).get("TOTALAMT").toString();
			}
			map.put("id", map.get("USERID").toString());
			if (payTypeList != null && payTypeList.size() > 0) {// 有最近消费
				if (((Map) payTypeList.get(0)).get("MONEYFLAG").toString().equals("02")) {// 最近消费是余额支付
					// 判断余额是否够,如果够返回余额02 否则返回银行卡支付01 (01银行卡 02余额支付 03积分支付 04银行卡+积分 05银行卡+余额 06余额+积分 07银行卡+余额+积分)
					if (payList != null && payList.size() > 0) {
						BigDecimal total = new BigDecimal(map.get("TOTAL").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						BigDecimal totalamt = new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP);
						if (totalamt.compareTo(total) >= 0 ) {
							map.put("PAYTYPE", "02");
							map.put("balance", money);
							map.put("cardNo","");
							map.put("mobile","");
						} else {//余额不够
							//查询是否有银行卡签约 返回银行卡和手机号
							List signList = orderDao.querySignByUserNo(map);
							if(signList != null && signList.size()>0){
								map.put("mobile", ((Map)signList.get(0)).get("PHONE").toString());
								map.put("cardNo", ((Map)signList.get(0)).get("ACCTNO").toString());
								map.put("PAYTYPE", "01");
								map.put("balance",money);
							}else{//无银行卡签约 返回余额
								map.put("balance", money);
								map.put("PAYTYPE", "02");
								map.put("cardNo","");
								map.put("mobile","");
							}
						}
					}else{
						map.put("balance", money);
						map.put("PAYTYPE", "02");
						map.put("cardNo","");
						map.put("mobile","");
					}
				} else {//最近消费是银行卡支付
					//查询是否有银行卡签约 返回银行卡和手机号
					String acctNo = ((Map)payTypeList.get(0)).get("ACCTNO").toString();
					map.put("acctNo", acctNo);
					List signList2 = orderDao.querySignByCardno(map);
					map.remove("acctNo");
					if(signList2 != null && signList2.size() > 0){
						map.put("cardNo", ((Map)signList2.get(0)).get("ACCTNO").toString());
						map.put("mobile", ((Map)signList2.get(0)).get("PHONE").toString());
						map.put("PAYTYPE", "01");
						map.put("balance", money);
					}else{
						List signList = orderDao.querySignByUserNo(map);
						if(signList != null && signList.size()>0){
							map.put("cardNo", ((Map)payTypeList.get(0)).get("ACCTNO").toString());
							map.put("mobile", ((Map)payTypeList.get(0)).get("PHONE").toString());
							map.put("PAYTYPE", "01");
							map.put("balance", money);
						}else{
							map.put("balance", money);
							map.put("PAYTYPE", "02");
							map.put("cardNo","");
							map.put("mobile","");
						}
					}
					
				}
			} else {// 没查询到消费
				if (payList != null && payList.size() > 0) {
					BigDecimal total = new BigDecimal(map.get("TOTAL").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					BigDecimal totalamt = new BigDecimal(((Map) payList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					if (totalamt.compareTo(total) >= 0) {//余额足够返回余额
						map.put("balance", money);
						map.put("PAYTYPE", "02");
						map.put("cardNo","");
						map.put("mobile","");
					} else {//余额不够
						//查询是否有银行卡签约 返回银行卡和手机号
						List signList = orderDao.querySignByUserNo(map);
						if(signList != null && signList.size()>0){
							map.put("cardNo", ((Map)payTypeList.get(0)).get("ACCTNO").toString());
							map.put("mobile", ((Map)payTypeList.get(0)).get("PHONE").toString());
							map.put("PAYTYPE", "01");
							map.put("balance", money);
						}else{
							map.put("balance", money);
							map.put("PAYTYPE", "02");
							map.put("cardNo","");
							map.put("mobile","");
						}
					}
				} else { 
					//查询是否有银行卡签约 返回银行卡和手机号
					List signList = orderDao.querySignByUserNo(map);
					if(signList.size()>0){
						map.put("cardNo", ((Map)payTypeList.get(0)).get("ACCTNO").toString());
						map.put("mobile", ((Map)payTypeList.get(0)).get("PHONE").toString());
						map.put("PAYTYPE", "01");
						map.put("balance", money);
					}else{
						map.put("balance", money);
						map.put("PAYTYPE", "02");
						map.put("cardNo","");
						map.put("mobile","");
					}
				}
			}
			return map;
		} 
		return null;
	}

	public Map queryBankcardList(Map map) {
		// TODO Auto-generated method stub
		Map returnMap = new HashMap();
		map.put("USERID", map.get("id").toString());
		List balanceList = orderDao.queryBalance(map);// 查询余额
		List signList = orderDao.queryBankcardList(map);// 查询签约银行卡
		List payTypeList = orderDao.queryLastOrder(map);// 最近消费
		if (balanceList.size() > 0) {
			returnMap.put("balance", ((Map) balanceList.get(0)).get("TOTALAMT").toString());
		}else{
			returnMap.put("balance","0.00");
		}
		if (payTypeList.size() > 0) {// 有最近消费
			if (((Map) payTypeList.get(0)).get("MONEYFLAG").toString().equals("02")) {// 最近消费是余额支付
				// (01银行卡 02余额支付 03积分支付 04银行卡+积分 05银行卡+余额 06余额+积分 07银行卡+余额+积分)
				returnMap.put("last", "02");
			} else {//最近消费是银行卡支付
				returnMap.put("last", "01");
			}
		} else {// 没有最近消费,默认返回银行卡支付
			if (balanceList.size() > 0) {
				BigDecimal total = new BigDecimal(map.get("TOTAL").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal totalamt = new BigDecimal(((Map) balanceList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				if (totalamt.compareTo(total) != -1) {
					returnMap.put("last", "02");
				} else {
					returnMap.put("last", "01");
				}
			} else {// 没有账户(正常系统下不存在)
				map.put("last", "01");
			}
		}
		//没有银行卡没有余额
		
		returnMap.put("cardCount", signList.size());

		List cardList = new ArrayList();

		Map cardMap = new HashMap();
		if(payTypeList.size()>0){
			for (int i = 0; i < signList.size(); i++) {
				if(((Map)signList.get(i)).get("ACCTNO").toString().equals(((Map)payTypeList.get(0)).get("ACCTNO").toString())){
					cardMap.put("cardNo", ((Map) signList.get(i)).get("ACCTNO").toString());
					cardMap.put("bankNo", ((Map) signList.get(i)).get("BANK").toString());
					cardMap.put("mobile", ((Map) signList.get(i)).get("PHONE").toString());
					cardMap.put("status", ((Map) signList.get(i)).get("STATUS").toString());
					cardList.add(cardMap);
				}
			}
			for (int i = 0; i < signList.size(); i++) {
				if(!((Map)signList.get(i)).get("ACCTNO").toString().equals(((Map)payTypeList.get(0)).get("ACCTNO").toString())){
					cardMap.put("cardNo", ((Map) signList.get(i)).get("ACCTNO").toString());
					cardMap.put("bankNo", ((Map) signList.get(i)).get("BANK").toString());
					cardMap.put("mobile", ((Map) signList.get(i)).get("PHONE").toString());
					cardMap.put("status", ((Map) signList.get(i)).get("STATUS").toString());
					cardList.add(cardMap);
				}
				
			}
		}else{
			for (int i = 0; i < signList.size(); i++) {
				cardMap.put("cardNo", ((Map) signList.get(i)).get("ACCTNO").toString());
				cardMap.put("bankNo", ((Map) signList.get(i)).get("BANK").toString());
				cardMap.put("mobile", ((Map) signList.get(i)).get("PHONE").toString());
				cardMap.put("status", ((Map) signList.get(i)).get("STATUS").toString());
				cardList.add(cardMap);
			}
		}
		returnMap.put("cardList", cardList);

		return returnMap;
	}

	public boolean pushOrderResult(Map map) {
		// TODO Auto-generated method stub
		if (orderDao.pushOrderResult(map) == 1) {
			return true;
		}
		return false;
	}

	/**
	 * 余额支付
	 */
	public Object balancePayment(Map map) {
		// TODO Auto-generated method stub
		
		JSONObject jsonObject = new JSONObject();
		// 初始化返回数据
		jsonObject.put("status", "2003");// 默认支付失败
		jsonObject.put("orderId", map.get("orderId").toString());
		jsonObject.put("payMoney", "0.00");
		try{

			// 需要校验用户支付密码是否正确
			List list = appSecurtDao.getAppSecurt(map);
			if (list == null || list.size() == 0) {
				logger.info("用户名:" + map.get("id").toString() + ",未设置支付密码!");
				jsonObject.put("resCode", "4001");
				jsonObject.put("resMsg", "未设置支付密码");
				return jsonObject;
			}
			Map securtMap = (Map) list.get(0);
			int cnts  = Integer.parseInt(securtMap.get("CNTS").toString());
			// 支付密码锁住
			if ( cnts >= 3) {
				jsonObject.put("resCode", "4002");
				jsonObject.put("resMsg", "支付密码次数超限，请重置支付密码【短信验证方式】");
				return jsonObject;
			}
			// 支付密码不正确
			if (!securtMap.get("SECURT").toString().equals(map.get("payEncrypt").toString())) {
				appSecurtDao.updateSecurt(map);
				logger.info("用户名:" + map.get("id").toString() + "支付密码不正确!");
				jsonObject.put("resCode", "4003");
				jsonObject.put("resMsg", "支付密码不正确，还有"+(2-cnts)+"次机会");
				return jsonObject;
			} else {// 密码正确更新错误次数为0
				appSecurtDao.updateCnts(map);
			}
			// 判断支付金额和订单金额是否一致
			List orderList = orderDao.queryOrderDetail(map);
			if(orderList == null || orderList.size() == 0){
				jsonObject.put("resCode", "4011");
				jsonObject.put("resMsg", "订单号不存在");
				return jsonObject;
			}else{
				Map sMap = (Map)orderList.get(0);
				String TRAFFICNO = sMap.get("TRAFFICNO")==null ?"":sMap.get("TRAFFICNO").toString();
				if("1".equals(TRAFFICNO)){
					jsonObject.put("resCode", "4013");
					jsonObject.put("resMsg", "订单已取消，请重新下单");
					return jsonObject;
				}
			}
			BigDecimal b1 = new BigDecimal(map.get("payMoney").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal b2 = new BigDecimal(((Map)orderList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
			if(b1.compareTo(b2)!=0){
				logger.info("订单"+map.get("orderId").toString()+"支付金额与订单金额不一致");
				jsonObject.put("resCode", "4004");
				jsonObject.put("resMsg", "订单"+map.get("orderId").toString()+"支付金额与订单金额不一致");
				return jsonObject;
			}
			
			map.put("USERID", map.get("id").toString());
			List moneyList = orderDao.queryBalance(map);// 查询余额
			// 判断余额是否够
			if (moneyList != null && moneyList.size() > 0) {
				BigDecimal total = new BigDecimal(map.get("payMoney").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal totalamt = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
				if (totalamt.compareTo(total) != -1) {// 余额足够
					/**
					 * 转账报文,调用中台(中间账号转账至商户账号)
					 * 上送报文：交易类型|系统名称|转账流水号（唯一）|转出账号|转出户名|转入账号|转入户名|转账金额|备注|加密串|
					 * 下送报文：返回码|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|中间业务日期|
					 * 中间业务流水号|核心日期|核心流水号|备注|加密串|
					 * 
					 */
					StringBuffer sb = new StringBuffer();
					sb.append("0002|WJBZ|" + map.get("orderId").toString() + "|");
					
					List paymxList = orderDao.queryOrderDetail(map);
					if(paymxList.size()>0){
						map.put("unitNo", ((Map)paymxList.get(0)).get("UNITNO").toString());
					}
					List unitList = machineDao.queryUnitByUnitNo(map);
					List inList = null;
					if(unitList != null && unitList.size()>0){
						// 获取转入
						inList = commerDao.queryCommer(((Map)unitList.get(0)).get("MERCHANTID").toString());/*商户编号*/
					}
					// 获取转出账号,从市政府对公账号
					List outList = commerDao.queryCommerByType("99");
	
					if (null == inList || inList.size() <= 0) {
						jsonObject.put("resCode", "4005");
						jsonObject.put("resMsg", "商户号未配置");
						return jsonObject;
					} else {
						sb.append(((Map) outList.get(0)).get("ACCTNO").toString() + "|" + ((Map) outList.get(0)).get("ACCTNAME").toString() + "|" + ((Map) inList.get(0)).get("ACCTNO").toString() + "|" + ((Map) inList.get(0)).get("ACCTNAME").toString() + "|");
						// 中台转账金额
						sb.append(new BigDecimal(map.get("payMoney").toString()).setScale(2, BigDecimal.ROUND_HALF_UP) + "|" + "|");
						// 发送给银行端
						SocketClient client = new SocketClient();
	//					String returnMsg = client.sendMessage(sb.toString());
						/** returnMsg测试 **/
						String returnMsg = "0000|返回信息|交易类型|系统名称|转账流水号|转出账号|转出户名|转入账号|转入户名|转账金额|20150101|05341234|20150102|05351235|备注|加密串|";
						// 校验报文
						if (returnMsg == null || "".equals(returnMsg.trim()) || returnMsg.length() == 4) {
							if ("2190".equals(returnMsg)) { // 超时调用冲正
								StringBuffer centBuffcz = new StringBuffer();
								centBuffcz.append("0008|WJBZ|" + map.get("orderId").toString() + "|");
								centBuffcz.append(((Map) outList.get(0)).get("ACCTNO").toString() + "|" + ((Map) outList.get(0)).get("ACCTNAME").toString() + "|" + ((Map) inList.get(0)).get("ACCTNO").toString() + "|" + ((Map) outList.get(0)).get("ACCTNAME").toString() + "|");
								centBuffcz.append(new BigDecimal(map.get("payMoney").toString()).setScale(2, BigDecimal.ROUND_HALF_UP) + "|" + "|");
								String czStr = client.sendMessage(centBuffcz.toString());
								logger.info("冲正返回报文：" + czStr);
							}
							
							jsonObject.put("resCode", "4010");
							jsonObject.put("resMsg", "银行扣款返回错误，请联系管理员");
							return jsonObject;
						} else {
							String[] response = returnMsg.split("\\|");
							if ("0000".equals(response[0])) {
								//推送支付成功
								List l = orderDao.queryOrderDetail(map);
								String pushMsg = "{resCode:2002,resMsg:'支付成功'}";
								boolean flag = true;
									//JPushObject.sendCustomerByAlias("", "android", JSONObject.fromObject(pushMsg).toString(), ((Map)l.get(0)).get("ANDROIDID").toString());
								if(flag){
									jsonObject.put("resCode", "2002");
									jsonObject.put("resMsg", "支付成功");
									jsonObject.put("status", "2002");
								}else{
									jsonObject.put("resCode", "1002");
									jsonObject.put("resMsg", "推送订单失败");
								}
								
								// 如果银行支付成功修改状态
								// 更新APP_PAYMX
								map.put("flag", "1");
								map.put("status", "1");
								map.put("middleflow1", response[11]);
								map.put("middledate1", response[10]);
								map.put("coreflow1", response[13]);
								map.put("coredate1", response[12]);
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								map.put("payerTime", sdf.format(new Date()));
								map.put("channerNo", "01");
								map.put("Moneyflag", "02");
								map.put("order_status", "2002");//修改订单状态
								System.out.println("支付参数＝＝＝＝＝＝》" + map);
								orderDao.balancePayment4PayMx(map);
	
								/** 使用余额进行支付需要更新账户余额表和账户余额明细表 **/
								// 更新账户余额表APP_MONEY
								if (moneyList.size() > 0) {
									totalamt = new BigDecimal(((Map) moneyList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 总金额
									BigDecimal qywamt = new BigDecimal(((Map) moneyList.get(0)).get("QYWAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 趣医网返现
									BigDecimal jfamt = new BigDecimal(((Map) moneyList.get(0)).get("JFAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 积分金额
									BigDecimal qtamt = new BigDecimal(((Map) moneyList.get(0)).get("QTAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 其他金额
									BigDecimal yhkamt = new BigDecimal(((Map) moneyList.get(0)).get("YHKAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);// 银行卡转入金额
	
									if (qywamt.compareTo(total) >= 0) {
										map.put("qywamt",qywamt.subtract(total).setScale(2,BigDecimal.ROUND_HALF_UP));
									} else if (qywamt.add(jfamt).compareTo(total) >= 0) {
										map.put("qywamt", new BigDecimal(0.00));
										map.put("jfamt", jfamt.subtract(total.subtract(qywamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
									} else if (qywamt.add(jfamt).add(qtamt).compareTo(total) >= 0) {
										map.put("qywamt", new BigDecimal(0.00));
										map.put("jfamt", new BigDecimal(0.00));
										map.put("qtamt", qtamt.subtract(total.subtract(qywamt).subtract(jfamt)).setScale(2, BigDecimal.ROUND_HALF_UP));
									} else if (qywamt.add(jfamt).add(qtamt).add(yhkamt).compareTo(total) >= 0) {
										map.put("qywamt", new BigDecimal(0.00));
										map.put("jfamt", new BigDecimal(0.00));
										map.put("qtamt", new BigDecimal(0.00));
										map.put("yhkamt", yhkamt.subtract(total.subtract(qywamt).subtract(jfamt).subtract(qtamt)));
									}
									map.put("totalamt", totalamt.subtract(total).setScale(2, BigDecimal.ROUND_HALF_UP));
									orderDao.updateMoney(map);
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
								moneyMxMap.put("transamt1", total);
								moneyMxMap.put("transamt2", map.get("payMoney").toString());
								moneyMxMap.put("transamt3", totalamt.subtract(total).setScale(2, BigDecimal.ROUND_HALF_UP));
								moneyMxMap.put("type", "7");
								moneyMxMap.put("transamt5", "0.00");
								moneyMxMap.put("cardno", "");
								moneyMxMap.put("channerno", "01");
								moneyMxMap.put("transtype", "2");
								moneyMxMap.put("feetype", map.get("paytype").toString());
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
								orderDao.insertMoneyMx(moneyMxMap);
								
								jsonObject.put("resCode", "2002");
								jsonObject.put("resMsg", "支付成功");
								return jsonObject;
							} else if ("8888".equals(response[0])) {
								jsonObject.put("resCode", "4008");
								jsonObject.put("resMsg", "银行扣款信息被篡改");
								return jsonObject;
							} else {
								jsonObject.put("resCode", "4010");
								jsonObject.put("resMsg", response[1]);
								return jsonObject;
							}
						}
					}
				} else {// 余额不够
					jsonObject.put("resCode", "4007");
					jsonObject.put("resMsg", "电子钱包金额不足");
					return jsonObject;
				}
			}else{
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "电子钱包金额不足");
				return jsonObject;
			}
		}catch(Exception e){
			e.printStackTrace();
			jsonObject.put("resCode", "4012");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		}
	}

	public Object cardPayment(Map map) {
		JSONObject jsonObject = new JSONObject();
		// 初始化返回数据
		jsonObject.put("status", "2003");// 默认支付失败
		jsonObject.put("orderId", map.get("orderId").toString());
		jsonObject.put("payMoney", "0.00");
		//校验卡号、手机号是否匹配
		List cardList = orderDao.querySignByCard(map);
		if(cardList != null && cardList.size()>0){
			Map cardMap = (Map) cardList.get(0);
			String phone = cardMap.get("PHONE").toString();
			if(!map.get("mobile").toString().equals(phone)){
				jsonObject.put("resCode", "4009");
				jsonObject.put("resMsg", "银行预留手机号不正确");
				return jsonObject;
			}
		}else{
			jsonObject.put("resCode", "4009");
			jsonObject.put("resMsg", "银行卡未签约");
			return jsonObject;
		}

		// 需要校验用户支付密码是否正确
		List list = appSecurtDao.getAppSecurt(map);
		if (list == null || list.size() == 0) {
			logger.info("用户名:" + map.get("id").toString() + ",未设置支付密码!");
			jsonObject.put("resCode", "4001");
			jsonObject.put("resMsg", "未设置支付密码");
			return jsonObject;
		}

		Map securtMap = (Map) list.get(0);
		int cnts  = Integer.parseInt(securtMap.get("CNTS").toString());
		// 支付密码锁住
		if ( cnts >= 3) {
			jsonObject.put("resCode", "4002");
			jsonObject.put("resMsg", "支付密码次数超限，请重置支付密码【短信验证方式】");
			return jsonObject;
		}
		// 支付密码不正确
		if (!securtMap.get("SECURT").toString().equals(map.get("payEncrypt").toString())) {
			appSecurtDao.updateSecurt(map);
			logger.info("用户名:" + map.get("id").toString() + "支付密码不正确!");
			jsonObject.put("resCode", "4003");
			jsonObject.put("resMsg", "支付密码不正确，还有"+(2-cnts)+"次机会");
			return jsonObject;
		} else {// 密码正确更新错误次数为0
			appSecurtDao.updateCnts(map);
		}
		// 判断支付金额和订单金额是否一致
		List orderList = orderDao.queryOrderDetail(map);
		if(orderList == null || orderList.size() == 0){
			jsonObject.put("resCode", "4011");
			jsonObject.put("resMsg", "订单号不存在");
			return jsonObject;
		}else{
			Map sMap = (Map)orderList.get(0);
			String TRAFFICNO = sMap.get("TRAFFICNO")==null ?"":sMap.get("TRAFFICNO").toString();
			if("1".equals(TRAFFICNO)){
				jsonObject.put("resCode", "4013");
				jsonObject.put("resMsg", "订单已取消，请重新下单");
				return jsonObject;
			}
		}
		BigDecimal b1 = new BigDecimal(map.get("cardMoney").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal b2 = new BigDecimal(((Map)orderList.get(0)).get("TOTALAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
		if(b1.compareTo(b2)!=0){
			logger.info("订单"+map.get("orderId").toString()+"支付金额与订单金额不一致");
			jsonObject.put("resCode", "4004");
			jsonObject.put("resMsg", "订单"+map.get("orderId").toString()+"支付金额与订单金额不一致");
			return jsonObject;
		}
		
		//根据用户编号获取用户协议号
		map.put("channerNo", "01");
		List paymxList = orderDao.queryOrderDetail(map);
		if(paymxList.size()>0){
			map.put("unitNo", ((Map)paymxList.get(0)).get("UNITNO").toString());
		}
		map.put("proname", "08"); /*!!!!!收费类型(暂定为08)!!!!*/
		List signList = orderDao.querySign(map);
		String signNo="";
		if(signList != null && signList.size() > 0){
			Map m = (Map) signList.get(0);
			signNo = m.get("SIGNNO") == null ? "" : m.get("SIGNNO").toString();
			String status = m.get("STATUS") == null ? "" : m.get("STATUS").toString();
			if(!"0".equals(status)){
				jsonObject.put("resCode", "4009");
				jsonObject.put("resMsg", "银行卡未签约");
				return jsonObject;
			}
		 }else{
			 jsonObject.put("resCode", "4010");
			 jsonObject.put("resMsg", "银行卡签约协议号不存在");
			 return jsonObject;
		 } 
		
		 BigDecimal paynumber = new BigDecimal(map.get("cardMoney").toString()); 
		 BigDecimal basenumber = new BigDecimal("100"); 
		 
		 List unitList = machineDao.queryUnitByUnitNo(map);
		 if(unitList.size()>0){
			 // 获取转入
			 List inList = commerDao.queryCommer(((Map)unitList.get(0)).get("MERCHANTID").toString());/*商户编号*/
			 if (inList == null || inList.size() == 0) {
				 jsonObject.put("resCode", "4005");
				 jsonObject.put("resMsg", "商户号未配置");
				 return jsonObject;
			 }
		 }
			
		 //拼装前往银联支付的报文
		 Map<String,Object>  paymap = new LinkedHashMap<String,Object>();
		 String mId = IdSequence.getMessgeId(map.get("id").toString(), "APReq");
		 paymap.put("version", "1.0.1");//版本号
		 paymap.put("merchantId",((Map)unitList.get(0)).get("MERCHANTID").toString());//商户编号/*********************/
		 paymap.put("certId","0001");//数字签名
		 paymap.put("serialNo", map.get("orderId").toString());//商户的订单号
		 
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		 String date  = sdf.format(new Date());
		 paymap.put("date", date);//交易日期和时间
		 paymap.put("signNo", signNo);//协议号
		 paymap.put("charge", "0");//手续费
		 paymap.put("amount", ((paynumber.multiply(basenumber)).intValue())+""); //交易金额，银行卡的交易金额
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
				jsonObject.put("resCode", "4010");
				jsonObject.put("resMsg", "银行扣款系统异常");
				return jsonObject;
				
			} else{
				//如果银行支付成功修改状态
				map.put("flag", "1");
				map.put("channerNo", "01");
				map.put("Moneyflag", "02");
				map.put("order_status", "2002");//修改订单状态
				map.put("status", "1");
				orderDao.updatePayMxByCard(map);
				//推送支付成功
				List l = orderDao.queryOrderDetail(map);
				String pushMsg = "{resCode:2002,resMsg:'支付成功'}";
				boolean flag = true;
					//JPushObject.sendCustomerByAlias("", "android", JSONObject.fromObject(pushMsg).toString(), ((Map)l.get(0)).get("ANDROIDID").toString());
				if(flag){
					jsonObject.put("resCode", "2002");
					jsonObject.put("resMsg", "支付成功");
					jsonObject.put("status", "2002");
				}else{
					jsonObject.put("resCode", "1002");
					jsonObject.put("resMsg", "推送订单失败");
				}
				
				
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

	public List queryTransaction(String transactionNo) {
		return orderDao.queryTransaction(transactionNo);
	}
	
	public int  cancelOrder(Map map) {
		return orderDao.cancelOrder(map);
	}
	
	public static void main(String[] args) {
		System.out.println(new BigDecimal(10.00).compareTo(new BigDecimal(10.00)));
		System.out.println(new BigDecimal(0.00).compareTo(new BigDecimal(10.00)));
		System.out.println(new BigDecimal(10.00).compareTo(new BigDecimal(0.00)));
		String pushMsg = "{resCode:2002,resMsg:'支付成功'}";
		System.out.println(JSONObject.fromObject(pushMsg));
	}

	public Object backFee(Map map) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		//判断订单号是否存在
		List orderList = orderDao.queryOrder(map);
		if(orderList == null ||  orderList.size() <= 0){
			jsonObject.put("resCode", "3005");
			jsonObject.put("resMsg", "退费原订单不存在");
			return jsonObject;
		}
		//判断订单是否已经退费
		List returnFeeList = orderDao.queryReturnFeeOrder(map);
		if(returnFeeList == null ||  returnFeeList.size() <= 0){
			jsonObject.put("resCode", "3005");
			jsonObject.put("resMsg", "该订单已经退费");
			return jsonObject;
		}
		
		Map oldOrderMap = (Map)orderList.get(0);
		BigDecimal backAmt = new BigDecimal(oldOrderMap.get("PAYAMT").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);;
		
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
		if("01".equals(moneyFlag)){
			//查询银行协议号等信息
			map.put("balanceAmt","0.00");
			map.put("cardAmt",oldOrderMap.get("PAYAMT").toString());
			map.put("totalAmt",oldOrderMap.get("PAYAMT").toString());
			map.put("payType",moneyFlag);
			map.put("status","1");
			List signList = appSignDao.querySign(map);
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
			} 
		}else if("02".equals(moneyFlag)){
			map.put("balanceAmt",oldOrderMap.get("PAYAMT").toString());
			map.put("cardAmt","0.00");
			map.put("totalAmt",oldOrderMap.get("PAYAMT").toString());
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
		}
		map.put("oldOrderId", map.get("orderId").toString());
		map.put("orderId", map.get("orderId").toString()+"TF");
	    map.put("detail", oldOrderMap.get("DETAIL").toString());
		int result = orderDao.returnFee(map);
	    if(result == 1){
			jsonObject.put("resCode", "9999");
		}else{
			jsonObject.put("resCode", "4007");
			jsonObject.put("resMsg", "系统异常");
			return jsonObject;
		}
		return jsonObject;
	}

	public Object backCardMain(Map map) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		 PayUtil_AHNJ ahnj = new PayUtil_AHNJ();
		 Map<String,String> returnMap = ahnj.backMoney(map);
		 if("0000".equals(returnMap.get("flag"))){
			//如果银行支付成功修改状态
			map.put("flag", "2");
			map.put("orderId", map.get("ORDERID").toString());
			
			orderDao.updatePayMxStatusByCard(map);
			orderDao.updatePayMxByCardBack(map);
			jsonObject.put("resCode", "0000");
			jsonObject.put("resMsg", "退费成功");
			jsonObject.put("orderId",map.get("orderId").toString());
			jsonObject.put("payerTime",map.get("PAYERTIME").toString());
			jsonObject.put("seqNo","");
			jsonObject.put("totalAmt",map.get("totalAmt").toString());
			jsonObject.put("backAmt",new BigDecimal(map.get("BACKAMT").toString()).add(new BigDecimal(map.get("totalAmt").toString())));
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
	public Object backBalanceMain(Map map){
		 
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

	public List queryOrderInfo(Map map){
		return orderDao.queryOrderDetail(map);
	}
	
}
