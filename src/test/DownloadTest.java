package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class DownloadTest {
	
	public static void main(String[] args) {
		String remoteFileName  ="miyao";
		String localFileName = "d:/miyao123.txt";
		DownloadTest test = new DownloadTest();
		test.download3(remoteFileName, localFileName);
	}
	
	
	public void download3(String remoteFileName, String localFileName) {
		String URL_STR="http://127.0.0.1:8080/UntechPayTest/api/download/dzfile";
        FileOutputStream out = null;
        InputStream in = null;
        
        try{
            URL url = new URL(URL_STR);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            
            // true -- will setting parameters
            httpURLConnection.setDoOutput(true);
            // true--will allow read in from
            httpURLConnection.setDoInput(true);
            // will not use caches
            httpURLConnection.setUseCaches(false);
            // setting serialized
            httpURLConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
            // default is GET                        
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charsert", "UTF-8");
            // 1 min
            httpURLConnection.setConnectTimeout(60000);
            // 1 min
            httpURLConnection.setReadTimeout(60000);
       
            httpURLConnection.addRequestProperty("userName", "admin");
            httpURLConnection.addRequestProperty("passwd", "chenyong");
            httpURLConnection.addRequestProperty("fileName", "miyao");
       
            // connect to server (tcp)
            httpURLConnection.connect();
       
            in = httpURLConnection.getInputStream();// send request to
                                                                // server
            File file = new File("d:/miyao122553.txt");
            if(!file.exists()){
                file.createNewFile();
            }
       
            out = new FileOutputStream(file);  
            byte[] buffer = new byte[4096];
            int readLength = 0;
            while ((readLength=in.read(buffer)) > 0) {
                byte[] bytes = new byte[readLength];
                System.arraycopy(buffer, 0, bytes, 0, readLength);
                out.write(bytes);
            }
            
            out.flush();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
	}
	
	 
	  
    public void downLoad(String remoteFileName, String localFileName) {
    DefaultHttpClient httpClient = new DefaultHttpClient();
    OutputStream out = null;
    InputStream in = null;
    String URL_STR="http://127.0.0.1:8080/UntechPayTest/api/download/dzfile";
    try {
        HttpGet httpGet = new HttpGet(URL_STR);

        httpGet.addHeader("userName", "");
        httpGet.addHeader("passwd", "");
        httpGet.addHeader("fileName", remoteFileName);

        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        in = entity.getContent();

        long length = entity.getContentLength();
        if (length <= 0) {
            System.out.println("下载文件不存在！");
            return;
        }

        System.out.println("The response value of token:" + httpResponse.getFirstHeader("token"));

        File file = new File(localFileName);
        if(!file.exists()){
            file.createNewFile();
        }
        
        out = new FileOutputStream(file);  
        byte[] buffer = new byte[4096];
        int readLength = 0;
        while ((readLength=in.read(buffer)) > 0) {
            byte[] bytes = new byte[readLength];
            System.arraycopy(buffer, 0, bytes, 0, readLength);
            out.write(bytes);
        }
        
        out.flush();
        
    } catch (IOException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }finally{
        try {
            if(in != null){
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            if(out != null){
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
	
	 

}
