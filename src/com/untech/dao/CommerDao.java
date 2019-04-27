package com.untech.dao;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface CommerDao {

	public List queryCommer(String merchantId);
	
	public List queryCommerByType(String feetype);
}
