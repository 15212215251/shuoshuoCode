package com.untech.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.untech.dao.AppPaycodeInfoDao;
import com.untech.dao.AppPaycodeLimitinfoDao;
import com.untech.dao.AppPaycodeLimitlistDao;
import com.untech.service.PayCodeService;
import com.untech.util.PayCodeUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("payCodeService")
@Transactional
public class PayCodeServiceImpl implements PayCodeService {
	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(PayCodeServiceImpl.class.getName());
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

	@Autowired
	private AppPaycodeLimitlistDao appPaycodeLimitlistDao;
	
	@Autowired
	private AppPaycodeLimitinfoDao appPaycodeLimitinfoDao;
	 
	@Autowired
	private AppPaycodeInfoDao appPaycodeInfoDao;
	 
	// 查询用户
	public Map querryLimit(Map map) throws Exception {
		Map returnMap = new HashMap();
		map.put("userNo", map.get("userNo"));
		map.put("isValid","Y");
		List infoList = appPaycodeLimitinfoDao.queryLimitinfo(map);// 查询个人设置信息
		List limitListQuery = appPaycodeLimitlistDao.queryLimitlist(map);// 查询限额设置列表
		List<Map> limitList = new ArrayList<Map>();
		if(limitListQuery != null && limitListQuery.size() >0){
			for(int i=0;i<limitListQuery.size();i++){
				Map tmpMap = (Map) limitListQuery.get(i);
				Map<String,String> newMap = new HashMap<String,String>();
				newMap.put("limitAmt", tmpMap.get("TRANSAMT").toString());
				newMap.put("daylimitAmt", tmpMap.get("DAYTRANSAMT").toString());
				limitList.add(newMap);
			}
			returnMap.put("limitCount", limitListQuery.size());
		}else{
			returnMap.put("limitCount", 0);
			returnMap.put("cardList", null);
		}
		if(infoList != null && infoList.size() > 0){
			Map tmpMap = (Map) infoList.get(0);
			String status = tmpMap.get("STATUS").toString();
			if("Y".equals(status)){
				returnMap.put("status", "0");
				returnMap.put("limit", tmpMap.get("TRANSAMT").toString());
			}else{
				returnMap.put("status", "1");
				returnMap.put("limit", "");
			}
		}else{
			returnMap.put("status", "1");
			returnMap.put("limit", "");
		}
		return returnMap;
	}


	public AppPaycodeLimitlistDao getAppPaycodeLimitlistDao() {
		return appPaycodeLimitlistDao;
	}
	public void setAppPaycodeLimitlistDao(
			AppPaycodeLimitlistDao appPaycodeLimitlistDao) {
		this.appPaycodeLimitlistDao = appPaycodeLimitlistDao;
	}


	public Map setLimit(Map map) throws Exception {
		Map<String,String> returnMap = new HashMap<String,String>();
		//查询是否设置过限额
		List infoList = appPaycodeLimitinfoDao.queryLimitinfo(map);// 查询个人设置信息
		String flag = map.get("flag").toString().trim();
		String limit = map.get("limit").toString().trim();
		if("1".equals(flag)){//设置限额
			map.put("isValid", "Y");
			List limitListQuery = appPaycodeLimitlistDao.queryLimitlist(map);// 查询限额设置列表
			map.put("status", "Y");
			if(limitListQuery != null && limitListQuery.size() > 0){
				for(int i = 0;i<limitListQuery.size();i++){
					Map tmpMap = (Map) limitListQuery.get(i);
					if(new BigDecimal(limit).compareTo(new BigDecimal(tmpMap.get("TRANSAMT").toString())) == 0){
						map.put("daytransamt", tmpMap.get("DAYTRANSAMT").toString());
						map.put("transamt", tmpMap.get("TRANSAMT").toString());
					}
				}
			}
			if(map.get("transamt") == null){
				returnMap.put("resCode", "4007");
				returnMap.put("resMsg", "系统异常");
			}
			if(infoList != null && infoList.size() > 0){//新增
				 appPaycodeLimitinfoDao.insertLimitinfo(map);
				 returnMap.put("resCode", "0000");
				 returnMap.put("resMsg", "限额设置成功");
			}else{//更新
				 appPaycodeLimitinfoDao.updateLimitinfo(map);
				 returnMap.put("resCode", "0000");
				 returnMap.put("resMsg", "限额设置成功");
			}
		}else {//取消限额
			if(infoList != null && infoList.size() > 0){
				 map.put("daytransamt", "0.00");
				 map.put("transamt", "0.00");
				 map.put("status", "N");
				 appPaycodeLimitinfoDao.updateLimitinfo(map);
				 returnMap.put("resCode", "0000");
				 returnMap.put("resMsg", "限额设置成功");
			}else{ 
				 returnMap.put("resCode", "4007");
				 returnMap.put("resMsg", "用户未设置限额");
			}
		}
		return returnMap;
	}


	public Map codemanager(Map map) throws Exception {
		Map returnMap = new HashMap();
		String payCode = PayCodeUtil.getOrderNo(map);
		map.put("payCode", payCode);
		appPaycodeInfoDao.insertPayCodeinfo(map);
		returnMap.put("payCode", payCode);
		return map;
	}
	
	
}
