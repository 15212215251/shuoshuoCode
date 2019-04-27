package com.untech.util;

import java.util.Map;

import org.apache.log4j.Logger;

import com.untech.service.PayService;


/**
 * 短连接
 * 
 * @author wing
 */
public class XnbShortConnection extends Thread {
	
	Logger logger = Logger.getLogger(XnbShortConnection.class);
	
	private Map notifyMap;
	
	private PayService payService;
	
	public XnbShortConnection(Map map,PayService service) {
		this.notifyMap = map;
		this.payService = service;
	} 
	public void run() {
		try {
			System.out.println("payService======================================="+payService);
			System.out.println("notifyMap==="+notifyMap);
		    payService.payXnbFee0017(notifyMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	 
	 
}