package com.untech.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.untech.dao.WjbzMoneyDzDao;
import com.untech.service.WjbzMoneyDzService;
 

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("WjbzMoneyDzService")
@Transactional
public class WjbzMoneyDzServiceImpl implements WjbzMoneyDzService {
	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(WjbzMoneyDzServiceImpl.class.getName());
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

	@Autowired
	private WjbzMoneyDzDao wjbzMoneyDzDao;
	
	public List queryMoneyDzMx(String dzDate) {
		return wjbzMoneyDzDao.queryMoneyDzMx(dzDate);
	}
	
	public List queryDzResultByDate(String dzDate){
		return wjbzMoneyDzDao.queryDzResultByDate(dzDate);
	};


}
