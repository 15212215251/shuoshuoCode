package com.untech.util;



public class Common_Const {
	//RSA 文件存放路径
	public final static String RSAFILEPATH=PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "certPath");
	
	public final static String DZPATH=PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "dzPath");
	
	public final static String URL=PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "url");
	
	public final static String URL1=PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "url1");
	
	// mobile phone
	public final static String  MOBILEAGENTID=PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "mobileagentid");
	
	public final static String  MOBILESOURCE=PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "mobilesource");
	
	public final static String  MOBILEKEY=PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "mobilekey");
	
	public final static String  MOBILEACCSEGMENT=PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "accsegment");
	
	public final static String  MOBILEDIRECTPRODUCT=PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "directProduct");
	
	public final static String  MOBILEDIRECTFILL=PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "directFill");
	
	public final static String  MOBILEBACKURL=PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "backurl");
	
	public final static String  MOBILEDIRECTSEARCH=PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "directSearch");

	public final static String  IP =PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "ip");
	//hosptialip= hosptialport= hosptialuser= hosptialpasswd= hosptialdir=
	
	public final static String  HOSPTIALIP =PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "hosptialip");
	
	public final static String  HOSPTIALPORT =PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "hosptialport");
	
	public final static String  HOSPTIALUSER =PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "hosptialuser");
	
	public final static String  HOSPTIALPASSWD =PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "hosptialpasswd");
	
	public final static String  HOSPTIALDIR =PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "hosptialdir");
	
	public final static String  POSTURL =PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "posturl");
	
	public final static String isSecurt = PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "isSecurt");
 
	
	public final static String  REDISFLAG =PropertiesUtil.getInstance().getPropertyValue("conf/application.properties", "redisFlag");

}
