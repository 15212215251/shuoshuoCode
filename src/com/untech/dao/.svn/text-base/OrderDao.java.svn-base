package com.untech.dao;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface OrderDao {

	public int insertTransactionNo(Map map);
	
	public int insertOrder(Map map);
	
	public int insertQrCodeOrder(Map map);
	
	public int returnFee(Map map);
	
	public List queryTransaction(String transactionNo);
	
	public List queryOrderDetail(Map map);
	
	public List queryLastOrder(Map map);
	
	public List queryBalance(Map map);
	
	public List queryBankcardList(Map map);
	
	public int pushOrderResult(Map map);
	
	public int balancePayment4PayMx(Map map);
	
	public int updateMoney(Map map);
	
	public List querySign(Map map);
	
	public int insertMoneyMx(Map map);
	
	public int updatePayMxByCard(Map map);
	
	public List querySignByUserNo(Map map);
	
	public List querySignByCardno(Map map);
	
	public List querySignByCard(Map map);
	
	public int cancelOrder(Map map);
	
	
	public List queryOrder(Map map);
	
	public int insertPaymx(Map map);
	//学生缴费系统插入app_paymx记录
	public int insertPaymxForEdu(Map map);
	
	public int updatePayMxStatusByMoney(Map map);
	
	public int updatePayMxStatusByCard(Map map);
	
	public int updateXnbNext(Map map);
	
	public List queryPaymxInfo(Map map);
	
	public int updatePayMxByCardBack(Map map);
	
	public List queryReturnFeeOrder(Map map);
	
	public List queryDzAll(Map map);
	
	public List queryDzUnit(Map map);
	
	public int updatePayMxByCardTmall(Map map);
	
    public List queryDzAllFeetype(Map map);
	
	public List queryDzUnitFeetype(Map map);
	
	public List querySumOrderamt(Map map);
	
	public int insertPayCodePaymx(Map map);
	
	public List queryLastOrderCard(Map map);
	
	public int updatePayCodeByMoney(Map map);
	
	public int updatePayCodeByCard(Map map);
	
	public int insertXnbPaymx(Map map);
	
	public List queryXnbBatch(Map map);
	
	public int insertXnbSecondPaymx(Map map);
	
	public int  updateBackPayMxRefuse(Map map);
	
	/**
	 * 查询打车用户当月的打车记录
	 * @param map
	 * @return
	 */
	public List queryTaxiCashBack(Map map);
}
