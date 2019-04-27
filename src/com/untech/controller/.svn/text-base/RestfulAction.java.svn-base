package com.untech.controller;

 

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.untech.model.User;
import com.untech.service.RestfulService;
import com.untech.util.UUIDGenerator;

/** 
 * 基于Restful风格架构测试 
 *  
 * @author dekota 
 * @since JDK1.5 
 * @version V1.0 
 * @history 2014-2-15 下午3:00:12 dekota 新建 
 */  
@Scope("prototype")
@Controller  
public class RestfulAction {  
	
	@Autowired
	private RestfulService restfulService;

	public RestfulService getRestfulService() {
		return restfulService;
	}
	public void setRestfulService(RestfulService restfulService) {
		this.restfulService = restfulService;
	}

	/** 日志实例 */  
    private static final Logger logger = Logger.getLogger(RestfulAction.class);  
  
    @RequestMapping(value = "/hello", produces = "text/plain;charset=UTF-8")  
    public @ResponseBody  
    String hello() {  
        return "你好！hello";  
    }  
  
    @RequestMapping(value = "/person", method = RequestMethod.POST)  
    @ResponseBody
    public  Object addPerson2(@RequestBody Map<String,Object> person) {  
        logger.info("注册人员信息成功i333333333d=" + person.get("id"));  
        logger.info("注册人员信息成功i333333333d=" + person.get("name")); 
        logger.info("注册人员信息成功i333333333d=" + person.get("age")); 
        JSONObject jsonObject = new JSONObject();  
        jsonObject.put("msg", "注册人员信息成功");  
        User p = new User();
        p.setId(Integer.parseInt(person.get("id")+""));
        p.setAge(Integer.parseInt(person.get("age")+""));
        p.setName(person.get("name")+"");
        int j = restfulService.insertUser(p);
        logger.info("j===="+j);
        return jsonObject;  
    } 
    
    
    @RequestMapping(value = "/listUser")  
    @ResponseBody
    public  Object  listUser(@ModelAttribute("user") User user, HttpServletRequest request){
//    	List<User> list = restfulService.queryAllUser();
//    	Enumeration headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String key = (String) headerNames.nextElement();
//            String value = request.getHeader(key);
//            System.out.println(key+":"+value);
//        }
    	System.out.println("传过来的参数为："+user);
    	List<User> list  = new ArrayList<User>();
    	for(int i=0;i<3;i++){
    		user = new User();
    		user.setId(i+10000);
    		user.setAge(i+19);
    		user.setName("jack"+i);
    		list.add(user);
    	}
    	 JSONObject jsonObject = new JSONObject();  
         jsonObject.put("userList", list);  
         System.out.println(jsonObject);
         return jsonObject;
    }
    
}  