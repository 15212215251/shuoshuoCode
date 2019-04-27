package com.untech.dao;

import java.util.List;
import java.util.Map;

public interface AppPaymxDao {
	//获取paymx记录
	public List getPaymxForEdu(Map map);
	//更新paymx 表的flag
	public int updatePayMxFlagByOrderId(Map map);
	//更新paymx 表的NOTE2
	public int updatePaymxFlag1ByOrderId(Map map);
	//获取定时任务上次执行执行失败的记录 **
	public List getFailedPaymx();
	//查询余额教育缴费记录
	public List queryEduPayMxByOrderId(Map map);
	//获取节假日信息
	public List getHolidayListByTimeStr(String hdate);
}
