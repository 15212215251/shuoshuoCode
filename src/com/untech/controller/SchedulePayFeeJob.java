package com.untech.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.untech.dao.AppMoneyDao;
import com.untech.dao.AppPaymxDao;
import com.untech.dao.AppUnitDao;
import com.untech.dao.CommerDao;
import com.untech.dao.OrderDao;
import com.untech.util.PayNotify;
import com.untech.util.PayUtil_YDYH;

/**
 * 教育缴费，新农合缴费等定时对账任务
 * @author hanShiwen
 * 20170915
 */
@Component
public class SchedulePayFeeJob {
	
private static final Logger logger = Logger.getLogger(SchedulePayFeeJob.class.getName());
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private AppMoneyDao appMoneyDao;
	
	@Autowired
	private AppPaymxDao appPaymxDao;
	
	@Autowired
	private AppUnitDao appUnitDao;
	
	@Autowired
	private CommerDao commerDao;
	/**
	 * 定时任务：工作日00:30--07:00执行任务
	 */
	@SuppressWarnings("unchecked")
	public void payFeeScheduleTask (){
		//定时任务执行之前先查询并处理flag=3订单
		QueryFailedOrder();
		Date date = new Date();
		//查询今天是否工作日
		boolean isHoliday = isHoliday(new SimpleDateFormat("yyyyMMdd").format(date));
		logger.info("isHoliday====" + isHoliday);
		if (isHoliday) {
			logger.info("非工作日定时任务不运行===========");
			return;
		}
		logger.info("定时任务开启===========");
		Map map = new HashMap();
		String dateStr = getValidDate(date); //T-3工作日的yyyyMMdd格式的时间字符串
		String endTimeStr = dateStr + " 23:59:59";
		logger.info("endTimeStr====" + endTimeStr);
		map.put("endTimeStr", endTimeStr);
		List list = appPaymxDao.getPaymxForEdu(map);
		if (list != null && !list.isEmpty()) {
			int count = 0;//标记执行记录的次数，用户判断是否执行了20条就的整数倍
			for (int i=0;i<list.size();i++) {
				if (count % 20==0 && count !=0) {
					if (!judgeTime(new Date())){
						break;
					}
				}
				logger.info("执行订单参数========" + (Map)list.get(i) );
				Map unitMap = (Map)list.get(i);
				//支付之前更新paymx--flag=3--对应的数据库中状态未知，需要查询操作
				int updateResult = appPaymxDao.updatePayMxFlagByOrderId(unitMap);
				logger.info("更新App_paymx======" + updateResult);
				payFee(unitMap);
				count++;
			}
		} else {
			logger.info("appPaymxDao查询结果" + "查询结果集为空");
		}
	}
	@SuppressWarnings("unchecked")
	private void payFee(Map map){
		//获取缴费金额
		BigDecimal balanceAmt =  new BigDecimal(map.get("PAYAMT").toString());
		//此处是为了防止orderId 和 ORDERID不同
		map.put("orderId", map.get("ORDERID").toString());
		//根据unitno 查询收款单位记录
		map.put("unitNo", map.get("UNITNO").toString());
		List unitList = appUnitDao.queryUnitByUnitNo(map);
		
		logger.info("appUnitDao查询结果" + unitList.get(0));
		// 获取转入账号信息
		List inList = commerDao.queryCommer(((Map)unitList.get(0)).get("MERCHANTID").toString());/*商户编号*/
		
		logger.info("commerDao查询结果" + inList.get(0));
		
		String inCardNo = ((Map) inList.get(0)).get("ACCTNO").toString();
		String inCardName = ((Map) inList.get(0)).get("ACCTNAME").toString();
		// 获取转出账号,从市政府对公账号
		List outList = commerDao.queryCommerByType("99");
		String outCardName = ((Map) outList.get(0)).get("ACCTNAME").toString();
		String outCardNo = ((Map) outList.get(0)).get("ACCTNO").toString();
		map.put("outCardNo", outCardNo);
		map.put("outCardName", outCardName);
		map.put("inCardNo", inCardNo);
		map.put("inCardName", inCardName);
		map.put("totalAmt", balanceAmt.toString());
		map.put("remark", "");
		StringBuilder sb = new StringBuilder();
		sb.append("0002|WJBZ|" + map.get("orderId").toString() + "|");
		sb.append(map.get("outCardNo").toString() + "|"+map.get("outCardName").toString() + "|");
		sb.append(map.get("inCardNo").toString() + "|"+map.get("inCardName").toString() + "|");
		sb.append(map.get("totalAmt").toString()+"|");
		sb.append((map.get("remark")==null?"":map.get("remark").toString())+"|");
		logger.info("转账方法请求==="+sb.toString());
		//缴费
		PayUtil_YDYH ahnj = new PayUtil_YDYH();
		Map<String,String> returnMap = ahnj.payMoney(map);
		logger.info("缴费返回的值flag"+returnMap.get("flag"));
		if("0000".equals(returnMap.get("flag"))){
			// 更新APP_PAYMX
			map.put("flag","2");
			map.put("status", "1");
			map.put("middleflow1", returnMap.get("middleflow1"));
			map.put("middledate1", returnMap.get("middledate1"));
			map.put("coreflow1", returnMap.get("coreflow1"));
			map.put("coredate1", returnMap.get("coredate1"));
			logger.info("支付参数＝＝＝＝＝＝》" + map);
			
			int updateStatus = orderDao.updatePayMxStatusByMoney(map);
			logger.debug("updateStatus === "+updateStatus);
			
			// 更新账户余额明细表APP_MONEYMX
			int updateMoneyMX = appMoneyDao.updateMoneyMxByOrderId(map);
			logger.debug("updateMoneyMX === "+updateMoneyMX);
			
			//回调此处需要搞清楚到底是哪边需要回调的
			//取消回调接口
			/*List orderList = orderDao.queryOrder(map);
			if(orderList != null && orderList.size() > 0){
				Map<String,String> orderMap = (Map<String,String>)orderList.get(0);
				Map notifyMap = PayNotify.notify(orderMap);
				logger.debug("notifyMap === "+notifyMap);
			}*/
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void getParamMap(Map map) {
		map.put("orderId", map.get("ORDERID").toString());
		//根据unitno 查询收款单位记录
		map.put("unitNo", map.get("UNITNO").toString());
		List unitList = appUnitDao.queryUnitByUnitNo(map);
		
		logger.info("appUnitDao查询结果" + unitList.get(0));
		// 获取转入账号信息
		List inList = commerDao.queryCommer(((Map)unitList.get(0)).get("MERCHANTID").toString());/*商户编号*/
		
		logger.info("commerDao查询结果" + inList.get(0));
		
		String inCardNo = ((Map) inList.get(0)).get("ACCTNO").toString();
		String inCardName = ((Map) inList.get(0)).get("ACCTNAME").toString();
		// 获取转出账号,从市政府对公账号
		List outList = commerDao.queryCommerByType("99");
		String outCardName = ((Map) outList.get(0)).get("ACCTNAME").toString();
		String outCardNo = ((Map) outList.get(0)).get("ACCTNO").toString();
		map.put("outCardNo", outCardNo);
		map.put("outCardName", outCardName);
		map.put("inCardNo", inCardNo);
		map.put("inCardName", inCardName);
		map.put("totalAmt", map.get("PAYAMT").toString());
		map.put("remark", "");
	}
	/**
	 * 判断时间是否在07:30之前，在之前返回true
	 * @param date
	 * @return
	 */
	private boolean judgeTime(Date date) {
		long time1 = date.getTime();
		String timeFmt = new SimpleDateFormat("yyyyMMdd").format(date); 
		Date date2 = null;
		try {
			date2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(timeFmt + " 07:30:05" );
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long time2 = date2.getTime();
		if(time1 < time2) {
			return true;
		}
		return false;
	}
	/**
	 * 获取T+2模式下合法的工作日
	 * @param date
	 * @return
	 */
	private String getValidDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int count = 0;
		while(true) {
			cal.add(Calendar.DATE, -1);
			String dateFormat = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
			boolean isHoliday = isHoliday(dateFormat);
			if (!isHoliday) {
				count++;
			} 
			if (count == 3) {
				break;
			}
		}
		String validDateStr =  new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		return validDateStr;
	}
	
	/**
	 * 判断是否是节假日
	 * @param fmt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isHoliday(String hdate) {
		List holiday = appPaymxDao.getHolidayListByTimeStr(hdate);
		if (null != holiday && !holiday.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 查询并处理flag=3的记录
	 */
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void QueryFailedOrder() {
		int count = 0;//标记执行记录的次数，用户判断是否执行了20条就的整数倍
		List list = appPaymxDao.getFailedPaymx();
		if (list == null || list.isEmpty()) {
			logger.info("没有flag=3支付未成功的记录=======");
			return;
		}
		for (int i=0;i<list.size();i++) {
			if (count != 0 && count % 20==0) {
				if (!judgeTime(new Date())){
					return;
				}
			}
			Map map = (Map)list.get(i);
			getParamMap(map);
			PayUtil_YDYH pay =  new PayUtil_YDYH();
			Map returnFailedMap = pay.queryMoney(map);
			if("0000".equals(returnFailedMap.get("flag"))){
				logger.info("查询成功=========");
				//查询成功，但银行未扣款
				if(!"0".equals(returnFailedMap.get("status"))){
					logger.info("查询成功银行未扣款=========");
					//对于不成功的先手工核对，待稳定后处理
					//payFee(map);
				}else{
					//查询结果已扣款，此处需要更新数据库；
					logger.info("银行扣款已经成功--->更新数据库");
					map.put("orderId", map.get("ORDERID").toString());
					map.put("flag","2");
					map.put("status", "1");
					map.put("middleflow1", returnFailedMap.get("middleflow1"));
					map.put("middledate1", returnFailedMap.get("middledate1"));
					map.put("coreflow1", returnFailedMap.get("coreflow1"));
					map.put("coredate1", returnFailedMap.get("coredate1"));
					logger.info("支付参数＝＝＝＝＝＝》" + map);
					int updateStatus = orderDao.updatePayMxStatusByMoney(map);
					logger.debug("updateStatus === "+updateStatus);
					// 更新账户余额明细表APP_MONEYMX
					int updateMoneyMX = appMoneyDao.updateMoneyMxByOrderId(map);
					logger.debug("updateMoneyMX === "+updateMoneyMX);
				}
			}else{
				logger.info("查询失败=========");
			}
			count ++;
		}
		
	}
}
