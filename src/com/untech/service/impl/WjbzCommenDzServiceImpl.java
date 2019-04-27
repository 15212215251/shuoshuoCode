package com.untech.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.untech.dao.WjbzCommenDzDao;
import com.untech.service.WjbzCommenDzService;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("WjbzCommenDzService")
@Transactional
public class WjbzCommenDzServiceImpl implements WjbzCommenDzService {
	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(WjbzCommenDzServiceImpl.class.getName());
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

	@Autowired
	private WjbzCommenDzDao wjbzCommenDzDao;
	
	

	public List queryCommenDzMx(String dzDate,String feeType1){
		Map map = new HashMap();
		String[] feeType = feeType1.split(",");
		map.put("dzDate", dzDate);
		map.put("feeType", feeType);
		return wjbzCommenDzDao.queryCommenDzMx(map);
	}
	
	public List queryDzResultByDate(String dzDate){
		return wjbzCommenDzDao.queryDzResultByDate(dzDate);
	};


}
