package com.untech.util;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

/**
 *
 * 这个例子用来测试报文的发送。
 * 报文发送事例代码，这里使用了MultiThreadedHttpConnectionManager 在发送大量数据时，可以保证资源的回收利用。
 *
 *

 */
public class HttpClientMessageSender {

    private static HttpConnectionManager connectionManager;

   
    public static String send(String reqXml, String postUrl) {

        // 发送报文
        HttpClient httpClient = new HttpClient(connectionManager);

        PostMethod method = new PostMethod(postUrl);

        method.addRequestHeader("Content-Type", "text/xml; charset=utf-8");
        try {
            method.setRequestEntity(new StringRequestEntity(reqXml, null, "utf-8"));

            httpClient.executeMethod(method);


            String requestcharset = method.getRequestCharSet();

            int statuscode  = method.getStatusCode();
            System.err.print("statuscode========/n"+statuscode);
            System.err.print("requestcharset========"+requestcharset);
            // 获得返回报文
            String resXml = method.getResponseBodyAsString();

            return resXml;
        } catch (Exception e) {
            // TODO 根据需要自行处理日志
        	System.out.println("connect error");
            e.printStackTrace();
        } finally {
            method.releaseConnection();
            if(httpClient != null && httpClient.getHttpConnectionManager() != null ){
            	 httpClient.getHttpConnectionManager().closeIdleConnections(0);  
            }
           
        }
        return null;
    }

    public HttpClientMessageSender() {
        super();

        // 创建一个线程安全的HTTP连接池
        connectionManager = new MultiThreadedHttpConnectionManager();

        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        // 连接建立超时
        params.setConnectionTimeout(10000);
        // 数据等待超时
        params.setSoTimeout(60000);
        // 默认每个Host最多10个连接
        params.setDefaultMaxConnectionsPerHost(20);
        // 最大连接数（所有Host加起来）
        params.setMaxTotalConnections(400);

        connectionManager.setParams(params);
    }
    
    public static void main(String[] args) {
    	JSONObject jsonObj = new JSONObject();
		jsonObj.put("userNo", "949494674dc2704f014dc27197130000");
		jsonObj.put("feeType","01");
		jsonObj.put("startDate","20140909");
		jsonObj.put("endDate","20150809");
		send(jsonObj.toString(), "http://218.23.173.110:8998/UntechPay/bornOrder");
	}

}