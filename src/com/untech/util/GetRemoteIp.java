package com.untech.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


public class GetRemoteIp {
	
	private static final Logger logger = Logger.getLogger(GetRemoteIp.class.getName());
	//客户端请求ip
	private static String ips =  Common_Const.IP;
	
	public static boolean checkIp(HttpServletRequest request){
		String ip = getRemortIP(request);
		logger.info("ip=========================="+ip);
		if(ip == null || "".equals(ip.trim())){
			if(ips.indexOf(ip) == -1 ){
				return false;
			}
		}
		return true;
	}
	public static String getRemortIP(HttpServletRequest request) {  
	    if (request.getHeader("x-forwarded-for") == null) {  
	        return request.getRemoteAddr();  
	    }  
	    return request.getHeader("x-forwarded-for");  
	}  
	
	public static String getIpAddr(HttpServletRequest request) {  
	    String ip = request.getHeader("x-forwarded-for");  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("PRoxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getRemoteAddr();  
	    }  
	    return ip;  
	}  
}
