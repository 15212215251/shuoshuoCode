package com.untech.util;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


	public class OrderId extends Thread{  
		  
	    private static long orderNum = 0l;  
	    private static String date ;  
	      
	  /*  public static void main(String[] args) throws InterruptedException {  
	    	System.out.println(new SimpleDateFormat("yyMMddHHmmssS").format(new Date()));
	        for (int i = 0; i < 50; i++) {  
	           System.out.println(OrderId.getOrderNo());  
	        }  
	        System.out.println(new SimpleDateFormat("yyMMddHHmmssS").format(new Date()));
	    }  */
	  
	    /** 
	     * 生成订单编号 
	     * @return 
	     */  
	    public static synchronized String getOrderNo() {  
	        String str = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());  
	        if(date==null||!date.equals(str)){  
	            date = str;  
	            orderNum  = 0l;  
	        }  
	        orderNum ++;  
	        long orderNo = Long.parseLong((date)) * 100;  
	        orderNo += orderNum;;  
	        return "SBZF"+orderNo+"";  
	    }  
	    
	    /** 
	     * 生成订单编号 
	     * @return 
	     */  
	    public static synchronized String getWjbzOrderNo() {  
	        String str = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());  
	        if(date==null||!date.equals(str)){  
	            date = str;  
	            orderNum  = 0l;  
	        }  
	        orderNum ++;  
	        long orderNo = Long.parseLong((date)) * 1000;  
	        orderNo += orderNum;;  
	        return "WJBZ"+orderNo+"";  
	    }  
	    
	    
	    /** 
	     * 生成url
	     * @return 
	     */  
	    public static synchronized Map<String,String> getQrCodeUrl() {  
	    	Map<String,String>  map = new HashMap<String, String>();
	        String str = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());  
	        String url  = "http://218.23.173.110:9899/UntechPayTest/QrCodepay.do?";
	        String orderId =  "qrcode"+str+getStringRandom(7);
	        url += "orderid="+orderId;
	        map.put("orderid", orderId);
	        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	        url += "&time="+ timeStamp;
	        url += "&sign="+MD5Util.encode(AESUtil.encrypt(orderId+timeStamp, "001122334567890A"));
	        map.put("qrcodeurl", url);
	        return  map ;
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
	      
	    public static void  main(String[] args) {  
	        //测试  
	        System.out.println(getStringRandom(8));  
	    } 
	  
}