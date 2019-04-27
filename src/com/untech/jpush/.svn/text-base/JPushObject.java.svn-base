package com.untech.jpush;

import java.io.UnsupportedEncodingException;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;


public class JPushObject {
	private static final String appKey = "add8e965436e2bf4fb8f05db";
	private static final String masterSecret = "c5de8db38f644aa0550622d9";


   /**
    *  所有平台，所有设备，内容为 【成佩涛发送过来的!】 的通知
    *  
    *  */
    public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll("成佩涛发送过来的!");
    }
    public static boolean sendAllMsg(String alias,String content){
    	 JPushClient jpushClient = new JPushClient(masterSecret, appKey, 0);
  	   
         PushPayload payload =  buildPushObject_all_alias_alert(alias,content);
         try {
             PushResult result = jpushClient.sendPush(payload);
             return true;
         } catch (APIConnectionException e) {
           e.printStackTrace();         
         } catch (APIRequestException e) {
             System.out.println("Should review the error, and fix the request"+ e);
             System.out.println("HTTP Status: " + e.getStatus());
             System.out.println("Error Code: " + e.getErrorCode());
             System.out.println("Error Message: " + e.getErrorMessage());
         }
		return false;
    }
    /*
     * title 标题可为空
     * type  android  ios 
     * content 内容
     * alias  别名
     * */
    public static boolean sendCustomerByAlias(String title,String type,String content,String alias ) {
    	
    	JPushClient jpushClient = new JPushClient(masterSecret, appKey, 0);
    	try {
	    	if("android".equals(type)){
				PushResult result = jpushClient.sendAndroidMessageWithAlias(title, content, alias);
				System.out.println(result.getOriginalContent());
	    		System.out.println(result.getRateLimitQuota());
	    		System.out.println(result.getRateLimitRemaining());
	    		System.out.println(result.getRateLimitReset());
				return true;
	    	}else if("ios".equals(type)){
	    		PushResult result = jpushClient.sendIosMessageWithAlias(title, content, alias);
	    		System.out.println(result.getOriginalContent());
	    		System.out.println(result.getRateLimitQuota());
	    		System.out.println(result.getRateLimitRemaining());
	    		System.out.println(result.getRateLimitReset());
	    		return true;
	    	}
    	} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIRequestException e) {
			e.printStackTrace();
		}
    	return false;
    }

    
    /**
     *  所有平台，推送目标是别名为 alias，通知内容为  centent
     *  
     *  */
    public static PushPayload buildPushObject_all_alias_alert(String alias,String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(content))
                .build();
    }

    public static boolean sendMoneyChange(String alias,String content,String type) {
    	JPushClient jpushClient = new JPushClient(masterSecret, appKey, 0);
    	PushPayload load = null ;
    	if("android".equals(type)){
    		load = PushPayload.newBuilder()
            .setPlatform(Platform.android())
            .setAudience(Audience.alias(alias))
            .setNotification(Notification.alert(content))
            .build();
    	}else if("ios".equals(type)){
    		load = PushPayload.newBuilder()
            .setPlatform(Platform.ios())
            .setAudience(Audience.alias(alias))
            .setNotification(Notification.alert(content))
            .build();
    	}
        
        try {
            PushResult result = jpushClient.sendPush(load);
            return true;
        } catch (APIConnectionException e) {
          e.printStackTrace();         
        } catch (APIRequestException e) {
            System.out.println("Should review the error, and fix the request"+ e);
            System.out.println("HTTP Status: " + e.getStatus());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Error Message: " + e.getErrorMessage());
        }
        return false;
}

    
    /**
     *  平台是 Android，目标是 tag 为 "tag1" 的设备，内容是 【这是内容】，并且标题为 【这是标题】。
     *  
     *  */
    public static PushPayload buildPushObject_android_tag_alertWithTitle() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag("tag1"))
                .setNotification(Notification.android("content", "title", null))
                .build();
    }

        
    
    //测试主方法
    public static void main(String arg[]) throws UnsupportedEncodingException{
    	  JPushClient jpushClient = new JPushClient(masterSecret, appKey, 0);
    	   
          // For push, all you need do is to build PushPayload object.
//    	  String strOut = new String(BASE64.encode("fbf92ca87ea46b0bafa1b2d3:6ab20b6e4b05dceca3fc9d33".getBytes("GB18030")));
//    	  System.out.println("strOut:"+strOut);
         /* PushPayload payload =  buildPushObject_android_tag_alertWithTitle();
          try {
              PushResult result = jpushClient.sendPush(payload);
              System.out.println(result);
          } catch (APIConnectionException e) {
            e.printStackTrace();         
          } catch (APIRequestException e) {
              System.out.println("Should review the error, and fix the request"+ e);
              System.out.println("HTTP Status: " + e.getStatus());
              System.out.println("Error Code: " + e.getErrorCode());
              System.out.println("Error Message: " + e.getErrorMessage());
          }*/
    	  try {
			//jpushClient.sendMessageAll("888");
    		
    		  PushResult result = jpushClient.sendAndroidMessageWithAlias("test", "111111111111", "08136331");
    		  System.out.println(result.getOriginalContent());
    		  System.out.println(result.getRateLimitQuota());
    		  System.out.println(result.getRateLimitRemaining());
    		  System.out.println(result.getRateLimitReset());
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


}
