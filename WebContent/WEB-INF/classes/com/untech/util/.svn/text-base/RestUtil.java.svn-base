package com.untech.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 访问rest接口服务
 * @author hanShiwen
 */
public class RestUtil {
    /**
     * 根据url + phone获取用户userno
     * @param url
     * @param query
     * @return
     */
    public String load(String url,String query){
    	String resultStr=""; //定义返回的报文
    	URL restURL;
    	HttpURLConnection conn;
    	BufferedReader bReader = null;
		try {
			restURL = new URL(url);
			conn = (HttpURLConnection) restURL.openConnection();
			conn.setRequestMethod("GET");//get请求
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setAllowUserInteraction(false);
	        bReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String line;
	        while(null != (line=bReader.readLine())){
	        	resultStr +=line;
	        }
		} catch (MalformedURLException e) {
			System.out.println("URL异常 ===》" + e.getMessage());
			e.printStackTrace();
			return resultStr;
		} catch (IOException e) {
			System.out.println("IO异常 ===》" + e.getMessage());
			e.printStackTrace();
			return resultStr;
		} catch (Exception e) {
			System.out.println("未知异常 ===》" + e.getMessage());
			e.printStackTrace();
			return resultStr;
		} finally {
			try {
				if (bReader != null) {
					bReader.close();
				}
			} catch (IOException e) {
				System.out.println("关闭流异常 ===》" + e.getMessage());
				e.printStackTrace();
			}
		}
        
        return resultStr;
    }
}
