package com.untech.dao;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface AppMoneyDao {
	
	//查询电子钱包金额
	public List queryBalance(Map map);
	
	//查询出租车电子钱包金额
	public List queryBalanceForTaxi(Map map);
	
	//更新电子钱包金额
	public int updateMoney(Map map);
	
	//插入电子钱包消费明细记录表
	public int insertMoneyMx(Map map);
	
	public int updateBalance(Map map);
	
	
	public List queryMoneyMxByOrder(Map map);
	
	public List queryUserNoByCardno(Map map);
	
	public int updateMoneyMxByOrderId(Map map);
	
	//出租车司机进账
	public int updateMoneyForTaxi(Map map);
	
	//插入一条出租车司机money记录
	public int insertTaxiMoney(Map map);
}
