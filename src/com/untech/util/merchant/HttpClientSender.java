package com.untech.util.merchant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;


/**
 *
 * 这个例子用来测试报文的发送。
 * 报文发送事例代码，这里使用了MultiThreadedHttpConnectionManager 在发送大量数据时，可以保证资源的回收利用。
 *
 *

 */
public class HttpClientSender {

    private static HttpConnectionManager connectionManager;
    private Logger log = LoggerFactory.getLogger(HttpClientSender.class);
   
    public  String send(Map<String,String> requestMap, String postUrl) {
    	requestMap.remove("private_key");
    	requestMap.remove("sign_type");
    	String partnerId = requestMap.get("partnerId");
        // 发送报文
        HttpClient httpClient = new HttpClient(connectionManager);
        PostMethod method = new PostMethod(postUrl);
        method.addRequestHeader("Content-Type", "application/json; charset=utf-8");
        method.addRequestHeader("Referer","");
        try {
        	//公共参数添加至Header
        	Iterator<Entry<String, String>> it = requestMap.entrySet().iterator();  
    		while(it.hasNext()){  
    			Entry<String, String> entry=  it.next();  
    			if("partnerId".equals(entry.getKey()) ||
    					"mAuthToken".equals(entry.getKey()) ||
    					"timestamp".equals(entry.getKey()) ||
    					"format".equals(entry.getKey()) ||
    					"charset".equals(entry.getKey()) ||
    					"version".equals(entry.getKey()) ||
    					"signType".equals(entry.getKey()) ||
    					"sign".equals(entry.getKey()) ||
    					"notifyUrl".equals(entry.getKey())
    					){
    				System.out.println(entry.getKey() + " = " + entry.getValue());
    				if("sign".equals(entry.getKey())){
    					System.out.println("--------------------------------");
    					System.out.println(entry.getValue());
    					System.out.println(Base64Utils.encodeToString(entry.getValue().getBytes("UTF-8")));
    					method.addRequestHeader(entry.getKey(), Base64Utils.encodeToString(entry.getValue().getBytes("UTF-8")));
    				}else{
    					method.addRequestHeader(entry.getKey(), entry.getValue());
    				}
    				
    				it.remove();
    			}
    		}
        	
        	String requestJson = JsonTools.mapToJson(requestMap);
        	log.info("发送报文："+requestJson);
            method.setRequestEntity(new StringRequestEntity(requestJson, null, "utf-8"));
            //method.addParameter("KEY", reqXml);
            httpClient.executeMethod(method);
            String requestcharset = method.getRequestCharSet();
            int statuscode  = method.getStatusCode();
            System.err.print("statuscode========/n"+statuscode);
            System.err.print("requestcharset========"+requestcharset);
            // 获得返回报文
            String resXml = method.getResponseBodyAsString();
            if(requestMap.containsKey("billDate")){   //区分处理对账交易
            	log.info("下载对账文件");
            	if(statuscode==200){  //对账文件下载成功
            		log.info("对账文件下载成功");
            		InputStream in = method.getResponseBodyAsStream(); 
            		String filePath = ""+File.separator+partnerId+"_"+requestMap.get("billDate")+".xls";
            		File dzFile = new File(filePath);
            		if(dzFile.exists()) {
            			dzFile.deleteOnExit();
    				}
            		dzFile.createNewFile();
        			FileOutputStream out = new FileOutputStream(dzFile);  
        			byte[] b = new byte[1024];  
        			int len = 0;  
        			while((len=in.read(b))!= -1){  
        				out.write(b,0,len);  
        			}  
        			out.close();
        			in.close();  
        			return dzFile.getPath();
            	}else{
            		log.info("对账文件下载失败");
            		return null;
            	}
            }
            log.info("返回报文："+resXml);
            return resXml;
        } catch (Exception e) {
        	System.out.println("connect error");
            e.printStackTrace();
        } finally {
            method.releaseConnection();
            if(httpClient != null && httpClient.getHttpConnectionManager() != null ){
            	 httpClient.getHttpConnectionManager().closeIdleConnections(0);  
            }
           
        }
        return "";
    }

    public HttpClientSender() {
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
		//send(jsonObj.toString(), "http://218.23.173.110:8090/UntechPay/bornOrder");
	}

}