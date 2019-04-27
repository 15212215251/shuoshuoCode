package com.untech.dao;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface OnlinePayDao {
	 
	
	public List queryCardType(String cardNo);
	
	public List querySignCard(String cardNo);
	
	public List queryAllCommer();

	public  void updateSignCard(Map map);
	
	public  void insertSignCard(Map map);
	
	public  void updateSignCardStatus(Map map);
	
    public List querySignCardMx(Map map);
	
	public  void updateSignCardMx(Map map);
	
	public  void insertSignCardMx(Map map);
	
	public List querySignCardMxStatus(Map map);
		
	public List querySecurt(Map map);
	
	public List validSecurt(Map map);
	
	public  void updateSecurt(Map map);
	
	public  void insertSecurt(Map map);
	
	public  void updateSecurtByCnts(Map map);
	
	
	
	
}
