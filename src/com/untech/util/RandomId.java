package com.untech.util;
import java.text.SimpleDateFormat;
import java.util.Date;


	public class RandomId extends Thread{  
		  
	    private static long orderNum = 0l;  
	    private static String date ;  
	      
	    public static void main(String[] args) throws InterruptedException {  
	    	System.out.println(new SimpleDateFormat("yyMMddHHmmssS").format(new Date()));
	        for (int i = 0; i < 50; i++) {  
	           System.out.println(RandomId.getOrderNo());  
	        }  
	        System.out.println(new SimpleDateFormat("yyMMddHHmmssS").format(new Date()));
	    }  
	  
	    /** 
	     * 生成订单编号 
	     * @return 
	     */  
	    public static synchronized String getOrderNo() {  
	        String str = new SimpleDateFormat("mmss").format(new Date());  
	        if(date==null||!date.equals(str)){  
	            date = str;  
	            orderNum  = 0l;  
	        }  
	        orderNum ++;  
	        long orderNo = Long.parseLong((date)) * 100;  
	        orderNo += orderNum;;  
	        return orderNo+"";  
	    }  
	  
}