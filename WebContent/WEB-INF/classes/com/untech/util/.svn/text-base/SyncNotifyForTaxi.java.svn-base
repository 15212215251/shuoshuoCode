package com.untech.util;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import net.sf.json.JSONObject;

/**
 * 出租车异步通知
 * @author hanShiwen
 *
 */
public class SyncNotifyForTaxi implements Runnable{
	
	Logger logger = Logger.getLogger(this.getClass());
	private String json;
	private String url;
	
	public SyncNotifyForTaxi (String json, String url) {
		this.json = json;
		this.url = url;
	}

	public void run() {
		for (int i=0;i<5;i++) {
			logger.info("异步回调开始-URL　＝＝＝》" + url);
			String flag = "success";
			JSONObject respJson = notifyReq (json, url);
			String respcode = "";
			if (respJson != null) {
				respcode = respJson.getString("respcode");
				if (flag.equals(respcode)) {
					System.out.println("异步通知成功-------------------1");
					break;
				}
			}
			System.out.println("异步回调成功标志  ===》 " + flag.equals(respcode));
			//如果回调失败间隔5s再次调用
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("异步通知-------------------2");
		}
	}
	
	
	public JSONObject notifyReq(String json, String url) {
		HttpClientMessageSenderByJson send = new HttpClientMessageSenderByJson();
		JSONObject respJson = null;
		try {
			String resp = send.send(json,url);
			respJson = JSONObject.fromObject(resp);
		} catch (Exception e) {
			return null;
		}
		return respJson;
	}
}
