package com.untech.dao;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface WjbzMoneyDzDao {
	 
	public List queryMoneyDzMx(String dzDate);
	
	public List queryDzResultByDate(String dzDate);
	
	
}
