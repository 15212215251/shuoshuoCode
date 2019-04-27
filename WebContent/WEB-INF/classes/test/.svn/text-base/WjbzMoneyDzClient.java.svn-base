package test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONObject;


public class WjbzMoneyDzClient {

	// private static final String targetURL =
	private static final String targetURL1 = "http://127.0.0.1:8080/UntechPayTest/WjbzMoneyDz";
	
	public static void main(String[] args) {
		
		commenDz();
		
	}
	public static void commenDz(){
		try {
			URL targetUrl = new URL(targetURL1);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl
					.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("dzDate", "20151120");
			OutputStream outputStream = httpConnection.getOutputStream();
			
			outputStream.write(jsonObj.toString().getBytes());
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
