package com.untech.service;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface OrderService {

	public boolean insertTransactionNo(String id, String sid, String transactionNo);

	public boolean insertOrder(Map map);
	
	public Map queryOrderDetail(Map map);
	
	public Map queryBankcardList(Map map);
	
	public boolean pushOrderResult(Map map);
	
	/**
	 * 余额支付
	 * @param map
	 * @return
	 */
	public Object balancePayment(Map map);
	
	/**
	 * 卡支付
	 * @param map
	 * @return
	 */
	public Object cardPayment(Map map);
	
	public List queryTransaction(String transactionNo);
	
	public int cancelOrder(Map map);
	
	public Object backFee(Map map);

	public Object backCardMain(Map map);
	
	public Object backBalanceMain(Map map);
	
	public boolean insertQrCodeOrder(Map map);
	
	public List queryOrderInfo(Map map);
}
