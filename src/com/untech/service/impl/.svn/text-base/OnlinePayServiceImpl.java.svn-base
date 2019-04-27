package com.untech.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.untech.dao.AppMoneyDao;
import com.untech.dao.AppSecurtDao;
import com.untech.dao.AppSignDao;
import com.untech.dao.AppUnitDao;
import com.untech.dao.CommerDao;
import com.untech.dao.OnlinePayDao;
import com.untech.dao.OrderDao;
import com.untech.service.OnlinePayService;
import com.untech.util.PayUtil_AHNJ;
 

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("onlinePayService")
@Transactional
public class OnlinePayServiceImpl implements OnlinePayService {
	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(OnlinePayServiceImpl.class.getName());
	
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
	private OnlinePayDao onlinePayDao;
	

	public List queryCardType(String cardNo) throws Exception {
		return onlinePayDao.queryCardType(cardNo);
	}


	//签约逻辑
	public Map cardSign(Map map) throws Exception {
		Map<String,String> returnMap = new HashMap<String, String>();
		//查询银行卡是否签约
		String cardNo = map.get("cardNo").toString();
		List list  = onlinePayDao.querySignCard(cardNo);
		if(list != null && list.size() >0 ){
			Map<String,String> signMap = (Map<String, String>) list.get(0);
			//0 签约  1解约  2 初始
			String status  = signMap.get("STATUS");
			if("0".equals(status)){
				if(!map.get("userNo").toString().equals(signMap.get("USERNO"))){
					returnMap.put("resCode", "4006");
					returnMap.put("resMsg", "该银行卡已被其他用户签约");
				}else{
					returnMap.put("resCode", "4006");
					returnMap.put("resMsg", "该银行卡已签约");
				}
				return returnMap;
			} 
		}
		//走签约逻辑
		List commerList = onlinePayDao.queryAllCommer();
		map.put("bank", "01");
		map.put("note1", "1");
		map.put("status", "2");
		map.put("signNo", "");
		if(list != null && list.size() >0 ){
			onlinePayDao.updateSignCard(map);
		}else{
			onlinePayDao.insertSignCard(map);
		}
		if(commerList != null && commerList.size() > 0){
			for(int i=0;i<commerList.size();i++){
				String merchantId = ((Map<String,String>)commerList.get(i)).get("MERCHANTID");
				map.put("merchantId", merchantId);
				List list1 = onlinePayDao.querySignCardMx(map);
				if(list1 != null && list1.size() >0){
					this.onlinePayDao.updateSignCardMx(map);
				}else{
					this.onlinePayDao.insertSignCardMx(map);
				}
			}
		}
		String msg = "";
		if(commerList != null && commerList.size() > 0){
			for(int i=0;i<commerList.size();i++){
				String merchantId = ((Map<String,String>)commerList.get(i)).get("MERCHANTID");
				map.put("merchantId", merchantId);
				PayUtil_AHNJ ahnj = new PayUtil_AHNJ();
				Map<String,String> signMap = ahnj.signCard(map);
				if("0000".equals(signMap.get("flag"))){
					map.put("status", "0");
					map.put("signNo", signMap.get("signNo"));
					this.onlinePayDao.updateSignCardMx(map);
				}else{
					msg = signMap.get("errorMessage");
					break;
				}
			}
		}
		//更新签约信息表
		Map<String,Object> map2 = new HashMap<String, Object>();
		map.put("status", "0");
		List list2 = onlinePayDao.querySignCardMxStatus(map);
		map.put("status", "");
		List listAll = onlinePayDao.querySignCardMxStatus(map);
	    if(list2 != null && listAll != null && list2.size() == listAll.size() ){
	    	map.put("status", "0");
	    	onlinePayDao.updateSignCardStatus(map);
	    	returnMap.put("resCode", "0000");
	    	returnMap.put("resMsg", "签约成功");
	    }else{
	    	returnMap.put("resCode", "9999");
	    	returnMap.put("resMsg", "签约失败!"+msg);
	    }
		return returnMap;
	}
	
	
	//解约逻辑
	public Map cardSignDis(Map map) throws Exception {
		Map<String,String> returnMap = new HashMap<String, String>();
		//查询银行卡是否签约
		String cardNo = map.get("cardNo").toString();
		List list  = onlinePayDao.querySignCard(cardNo);
		map.put("status", "1");
		List list1 = onlinePayDao.querySignCardMxStatus(map);
		map.put("status", "");
		List list2 = onlinePayDao.querySignCardMxStatus(map);
		Map<String,Object> map2 = new HashMap<String, Object>();
		if(list.size() >0){
			Map<String,Object> m = (Map<String, Object>) list.get(0);
			if(m.get("STATUS") != null && "1".equals(m.get("STATUS").toString()) && list1!= null && list2 != null && list1.size() == list2.size()){
				returnMap.put("resCode", "0000");
		    	returnMap.put("resMsg", "解约成功");
				return returnMap;
			}else{
				//先解约设置状态
				map.put("status", "1");
				onlinePayDao.updateSignCardStatus(map); 
				map.put("status", "");
				List mxList = onlinePayDao.querySignCardMxStatus(map);
				if(mxList != null && mxList.size() > 0 ){
					for(int i=0;i<mxList.size();i++){
						Map ms = (Map) mxList.get(i);
						if(!"0".equals(ms.get("STATUS"))){
							continue;
						}
						map.put("merchantId", ms.get("MERCHANTID"));
						map.put("signNo", ms.get("SIGNNO"));
						PayUtil_AHNJ ahnj = new PayUtil_AHNJ();
						Map<String,String> ahnjMap = ahnj.signCardDis(map);
						if("0000".equals(ahnjMap.get("flag"))){
							map.put("status", "1");
							map.put("signNo", "");
							map.put("note1", "1");
							onlinePayDao.updateSignCardMx(map);
						}
				    }
				}
				
			}
		}
    	returnMap.put("resCode", "0000");
    	returnMap.put("resMsg", "解约成功");
		return returnMap;
	}

//status	String	0 未签约 1 已签约 2 锁定  3 签约已撤销 4 待确认
	public Map cardSignStatus(Map map) throws Exception {
		Map<String,String> returnMap = new HashMap<String, String>();
		//查询银行卡是否签约
		String cardNo = map.get("cardNo").toString();
		List list  = onlinePayDao.querySignCard(cardNo);
		if(list != null && list.size()>0){
			Map<String,String> queryMap = (Map<String, String>) list.get(0);
			if("0".equals(queryMap.get("STATUS"))){
				returnMap.put("resCode", "0000");
		    	returnMap.put("resMsg", "签约查询成功");
		    	returnMap.put("status", "1");
			}else if("1".equals(queryMap.get("STATUS"))){
				returnMap.put("resCode", "0000");
		    	returnMap.put("resMsg", "签约查询成功");
		    	returnMap.put("status", "3");
			}else{
				returnMap.put("resCode", "0000");
		    	returnMap.put("resMsg", "签约查询成功");
		    	returnMap.put("status", "4");
			}
		}else{
			returnMap.put("resCode", "0000");
	    	returnMap.put("resMsg", "签约查询成功");
	    	returnMap.put("status", "0");
		}
		return returnMap;
	}
	
	public Map securtManager(Map map) throws Exception {
		Map<String,String> returnMap = new HashMap<String, String>();
		//查询银行卡是否签约
		String flag = map.get("flag").toString();
		if("1".equals(flag)){
			List list = onlinePayDao.querySecurt(map);
			if(list != null && list.size() > 0){
				returnMap.put("status", "0");
			}else{
				returnMap.put("status", "1");
			}
			returnMap.put("resCode", "0000");
	    	returnMap.put("resMsg", "支付密码查询成功");
	    	
		}else if("2".equals(flag)){
			List list = onlinePayDao.querySecurt(map);
			if(list == null || list.size() <= 0){
				//bao cun mima
				onlinePayDao.insertSecurt(map);
			}else{
				onlinePayDao.updateSecurt(map);
			}
			
			returnMap.put("resCode", "0000");
	    	returnMap.put("resMsg", "支付密码设置成功");
		}else{
			List list = onlinePayDao.querySecurt(map);
			if(list == null || list.size() <= 0){
				returnMap.put("resCode", "2001");
		    	returnMap.put("resMsg", "未设置支付密码");
		    	return returnMap;
			}
			int cnts = Integer.parseInt(((Map)list.get(0)).get("CNTS").toString());
			if(cnts >= 3 ){
				returnMap.put("resCode", "2002");
		    	returnMap.put("resMsg", "支付密码次数超限，请重置支付密码【短信验证方式】");
		    	return returnMap;
			}
			list = onlinePayDao.validSecurt(map);
			if(list != null && list.size() > 0){
				returnMap.put("resCode", "0000");
		    	returnMap.put("resMsg", "支付密码验证成功");
			}else{
				onlinePayDao.updateSecurtByCnts(map);
				returnMap.put("resCode", "2003");
		    	returnMap.put("resMsg", "支付密码不正确");
			}
		}
		
		return returnMap;
	}

	
}
