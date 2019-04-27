package test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.untech.util.AESUtil;
import com.untech.util.Common_Const;
import com.untech.util.RandomUtil;
import com.untech.util.ReturnUtil;

import net.sf.json.JSONObject;


public class onlinePayClient {

	// private static final String targetURL =
	// "http://localhost:8080/AppThirdServer/person2";
	private static final String targetURL1 = "http://127.0.0.1:8080/UntechPayTest/cardType";
	private static final String targetURL2 = "http://127.0.0.1:8080/UntechPayTest/cardSign";
	private static final String targetURL3 = "http://127.0.0.1:8080/UntechPayTest/cardSignStatus";
	private static final String targetURL4 = "http://127.0.0.1:8080/UntechPayTest/securtManager";
	private static final String targetURL5 = "http://127.0.0.1:8080/UntechPay/queryBankcardList";
	private static final String targetURL9 = "http://127.0.0.1:8090/UntechPay/pushOrderResult";
	private static final String targetURL10 = "http://218.23.173.110:8090/UntechPay/pushShipmentResult";
	private static final String targetURL7 = "http://218.23.173.110:8099/UntechPayTest/payFee";
	
	private static final String targetURL11 = "http://127.0.0.1:8080/UntechPay/cancelOrderNo";
	
	private static final String targetURL12 = "http://127.0.0.1:8080/UntechPay/OrderMxs";
	
	private static final String targetURL13 = "http://127.0.0.1:8080/UntechPay/backFee";
	
	private static final String targetURL14 = "http://127.0.0.1:8080/UntechPay/OrderStatus";
	
	public static void main(String[] args) {
		//cardType();
		cardSign();
		//cardSignStatus();
		
		//securtManager();
		//payment();
		//testOrderMxs();
		
		//back();
    	//OrderStatus();
	}
	public static void cardType(){
		try {
			URL targetUrl = new URL(targetURL1);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl
					.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("userNo", "949494674dc2704f014dc27197130000");
			jsonObj.put("cardNo","6217780123456789");
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
	
	public static void cardSign(){
		try {
			URL targetUrl = new URL(targetURL2);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl
					.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("userNo", "1111111111111111112222");
			jsonObj.put("cardNo","62177883001004444444");
			jsonObj.put("cardName","陈勇");
			jsonObj.put("cardType","D");
			jsonObj.put("certType","1");
			jsonObj.put("certNo","3424271987122307654");
			jsonObj.put("mobilePhone","18667935431");
			jsonObj.put("mobileMsg","123456");
			jsonObj.put("channerNo","01");
			jsonObj.put("signType","1");
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
	
	public static void cardSignStatus(){
		try {
			URL targetUrl = new URL(targetURL3);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl
					.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("userNo", "949494674dc2704f014dc27197130000");
			jsonObj.put("cardNo","621778830010099999");
			jsonObj.put("cardName","陈勇");
			jsonObj.put("cardType","D");
			jsonObj.put("certType","1");
			jsonObj.put("certNo","3424271987122307654");
			jsonObj.put("mobilePhone","18667935431");
			jsonObj.put("mobileMsg","123456");
			jsonObj.put("channerNo","01");
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
	
	
	public static void securtManager(){
		try {
			URL targetUrl = new URL(targetURL4);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl
					.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("userNo", "949494674dc2704f014dc27197130000");
			jsonObj.put("paySecurt","CA7121AA614993E5");
			jsonObj.put("flag","2");
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
