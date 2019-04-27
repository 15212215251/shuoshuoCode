package test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.untech.util.EncodeDecodeUtil;
import com.untech.util.RandomUtil;
import com.untech.util.ReturnUtil;

import net.sf.json.JSONObject;


public class WjbzCommenDzClient {

	// private static final String targetURL =
	private static final String targetURL1 = "http://127.0.0.1:8080/UntechPayTest/WjbzCommonDz";
	
	private static final String targetURL2 = "http://172.16.1.2:8099/UntechPayTest/WjbzCommonDz";
	
	private static final String targetURL3 = "http://60.174.83.214:8099/UntechPayTest/WjbzCommonDz";
	
	public static void main(String[] args) {
		
		commenDz();
		
	}
	public static void commenDz(){
		try {
			URL targetUrl = new URL(targetURL3);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl
					.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("dzDate", "20170112");
			jsonObj.put("feeType","13");
			
			String key = RandomUtil.generateNumber(16);
			JSONObject jsonObject = new JSONObject();
			jsonObject = (JSONObject) ReturnUtil.getPayReturn(jsonObj, key);
			
			OutputStream outputStream = httpConnection.getOutputStream();
			
			outputStream.write(jsonObject.toString().getBytes());
			outputStream.flush();
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}
			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
			String output;
			System.out.println("Output from Server:\n");
			while ((output = responseBuffer.readLine()) != null) {
				System.out.println(output);
			}
			httpConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}
	
}
