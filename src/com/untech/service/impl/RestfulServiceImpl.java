package com.untech.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.untech.dao.RestfulDao;
import com.untech.model.Log;
import com.untech.model.User;
import com.untech.service.RestfulService;


@Service
@Transactional
public class RestfulServiceImpl implements RestfulService {
	
    @Autowired
    private RestfulDao restfulDao;
    
    

	public RestfulDao getRestfulDao() {
		return restfulDao;
	}



	public void setRestfulDao(RestfulDao restfulDao) {
		this.restfulDao = restfulDao;
	}



	public int insertUser(User user) {
		
		int i = restfulDao.insertUser(user);
		System.out.println("iu================="+i);
		Log log = new Log();
		log.setId(user.getId());
		log.setLogtime(new Date());
		log.setName("在红汶川");
		int j  = restfulDao.insertLog(log);
		System.out.println("j================="+j);
		return 0;
	}



	public List<User> queryAllUser() {
		
		return  restfulDao.queryAllUser();
	}

}
