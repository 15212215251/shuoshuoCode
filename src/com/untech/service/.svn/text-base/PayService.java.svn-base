package com.untech.service;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface PayService {

	/*查询银行卡列表和电子钱包金额*/
	public Map queryBankcardList(Map map)  throws Exception;
	/*查询收费单位列表信息**/
	public Map queryUnits(Map map)  throws Exception;
	
	public Object payFee(Map map) throws Exception;
	
	public Map OrderMxs(Map map) throws Exception;
	
	public Object balanceMain(Map map,String flag) throws Exception;
	
	public Object cardMain(Map map,String flag) throws Exception;
	
	public Object backFee(Map map) throws Exception;
	
    public Object backBalanceMain(Map map) throws Exception;
	
	public Object backCardMain(Map map) throws Exception;
	
	public Object OrderStatus(Map map) throws Exception;
	
	public List queryDzAll(Map map);
	
	public List queryDzUnit(Map map);
	
	public Object payTmallFee(Map map) throws Exception;
	
	public Object confirmFee(Map map) throws Exception;
	
	public Object confirmCardMain(Map map,String code ) throws Exception;
	
	public Object payBicycleFee(Map map) throws Exception;
	
	public Object backBicycleFee(Map map) throws Exception;
	
	public Object queryBicycleFee(Map map) throws Exception;

	public List queryDzAllFeetype(Map map);
	
	public List queryDzUnitFeetype(Map map);
	
	public Object payXnbFee(Map map) throws Exception;
	
	public Object payXnbFee0017(Map map) throws Exception;
	
	public List queryXnbBatch(Map map);
	
	public List queryUnitByUnitNo(Map map);
	
	public List queryCommer(String merchantId);
	
	public Object cardXnbMain0017(Map map) throws Exception;
	
	public Map queryUserNoByCardno(Map map);
	//教育缴费记录查询
	public List queryEduPayMxByOrderId (Map map);
	//教育缴费支付
	public Object payFeeEdu(Map map) throws Exception;
	
	public Object balanceMainEdu(Map map,String flag) throws Exception;
	//taxi余额支付
	public Object balanceMainForTaxi(Map map,String flag) throws Exception;
	//taxi司机收款
	public Object addMoneyForTaxi(Map map) throws Exception;
	
	//根据手机号码查询司机的userno
	public String getTaxiUserNoByPhone(String phone);
	
	/**
	 * 出租车缴费回调
	 * @param map
	 */
	public void callBack(Map map);
	
	/**
	 * 出租车银行卡缴费
	 * @param map
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public Object cardMainForTaxi(Map map, String string) throws Exception;
	
	/**
	 * 出租车用户反现
	 * @param map
	 * @return
	 */
	public void cashBackTaxi(Map map);
}
