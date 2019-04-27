package com.untech.util;

import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONObject;

import org.codehaus.xfire.client.Client;


public class SendSmsUtil {
	
	public static boolean sendMsg(String authCode,String phone,String content){
		    Client client;
			try {
				System.out.println("SendSmsUtil------------------");
				System.out.println("authCode------------------"+authCode+phone+content);
				//10.
				client = new Client(new URL("http://60.174.83.212/app-net-ws/services/ISmsService?wsdl"));
				System.out.println("1111111");
				authCode =  AESSMS.encrypt(authCode, "E9DA036F58B724C1");
				System.out.println("11111112");
			    phone =  AESSMS.encrypt(phone, "E9DA036F58B724C1");
			    System.out.println("11111113");
			    content =  AESSMS.encrypt(content, "E9DA036F58B724C1");
			    System.out.println("11111114");
			    Object[] results11 = client.invoke("sendSms", new Object[]{authCode,phone,content});  
			    System.out.println("11111115");
				String result = results11[0].toString();
				System.out.println("11111116");
				System.out.println(result);
				result  = AESSMS.decrypt(result, "E9DA036F58B724C1");
				System.out.println(result);
				JSONObject obj  = JSONObject.fromObject(result);
				System.out.println(obj);
				System.out.println(obj.get("result"));
				if(obj != null && "200".equals(obj.get("result"))){
					return true;
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}  
			return false;
	}
	
	public static void main(String[] args) {
		sendMsg("YDYH", "18667935421", "18667935421测试");
	}

}
