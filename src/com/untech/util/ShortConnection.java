package com.untech.util;

import java.util.Map;

import org.apache.log4j.Logger;


/**
 * 短连接
 * 
 * @author wing
 */
public class ShortConnection extends Thread {
	
	Logger logger = Logger.getLogger(ShortConnection.class);
	
	private Map<String,String> notifyMap;
	
	public ShortConnection(Map<String,String> map) {
		this.notifyMap = map;
	} 
	public void run() {
		try {
			Thread.sleep(2000);
			logger.debug("notifyMap === "+notifyMap);
			Map<String,String> returnMap = PayNotify.notify(this.notifyMap);
			logger.debug("notifyMap === "+returnMap);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	 
	 
}