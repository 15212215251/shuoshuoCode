package com.untech.util;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


	public class PayCodeUtil extends Thread{  
		  
	    private static long orderNum = 0l;  
	    private static String date ;  
	      
	   
	    /** 
	     * 生成订单编号 
	     * @return 
	     */  
	    public static synchronized String getOrderNo(Map<String,String> map) { 
	    	String userno = map.get("userNo");
	    	String payerTime = map.get("payerTime");
	    	String deviceId = map.get("deviceId");
	    	String md5 = MD5Util.encode(userno+payerTime+deviceId);
	    	System.out.println(md5);
	    	String  md510 = MyArrayUtil.hexToBit(md5);
	    	BigInteger bi = new BigInteger(md510, 2);    //转换为BigInteger类型  
	        System.out.println( bi.toString());     //转换成十进制
	        
	        return "51"+getStringRandomNum(16);  
	    }  
	  
	    
	    //生成随机数字和字母,  
	    public static String getStringRandom(int length) {  
	          
	        String val = "";  
	        Random random = new Random();  
	          
	        //参数length，表示生成几位随机数  
	        for(int i = 0; i < length; i++) {  
	              
	            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
	            //输出字母还是数字  
	            if( "char".equalsIgnoreCase(charOrNum) ) {  
	                //输出是大写字母还是小写字母  
	                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
	                val += (char)(random.nextInt(26) + temp);  
	            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
	                val += String.valueOf(random.nextInt(10));  
	            }  
	        }  
	        return val;  
	    }  
	    
	    
	    //生成随机数字  
	    public static String getStringRandomNum(int length) {  
	        String val = "";  
	        Random random = new Random();  
	        //参数length，表示生成几位随机数  
	        for(int i = 0; i < length; i++) {  
	             val += String.valueOf(random.nextInt(10));  
	        }  
	        System.out.println(val);
	        return val;  
	    }  
	      
	    public static void  main(String[] args) {  
	         Map<String,String> map = new HashMap<String, String>();
	          map.put("userNo","c0437f894dc44be1bdeab784f2c653f6");
		      map.put("payerTime","20161212103456786");
		      map.put("deviceId","1234md56789");
		      getOrderNo(map);
		      getStringRandomNum(16);
	    } 
	  
}