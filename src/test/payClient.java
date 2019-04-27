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


public class payClient {

	// private static final String targetURL =
	// "http://localhost:8080/AppThirdServer/person2";
	private static final String targetURL1 = "http://218.23.173.110:8099/UntechPayTest/queryCards";
	private static final String targetURL2 = "http://127.0.0.1:8080/UntechPay/queryUnits";
	private static final String targetURL3 = "http://127.0.0.1:8080/UntechPay/bornOrder";
	private static final String targetURL4 = "http://127.0.0.1:8090/UntechPay/queryOrderDetail";
	private static final String targetURL5 = "http://127.0.0.1:8080/UntechPay/queryBankcardList";
	private static final String targetURL9 = "http://127.0.0.1:8090/UntechPay/pushOrderResult";
	private static final String targetURL10 = "http://218.23.173.110:8090/UntechPay/pushShipmentResult";
	private static final String targetURL7 = "http://218.23.173.110:8099/UntechPayTest/payFee";
	
	private static final String targetURL8 = "http://60.174.83.214:9899/UntechPayTestWjbz/payFee";

	private static final String targetURL11 = "http://127.0.0.1:8080/UntechPay/cancelOrderNo";
	
	private static final String targetURL12 = "http://127.0.0.1:8080/UntechPay/OrderMxs";
	
	private static final String targetURL13 = "http://127.0.0.1:8080/UntechPay/backFee";
	
	private static final String targetURL14 = "http://127.0.0.1:8080/UntechPay/OrderStatus";
	
	public static void main(String[] args) {
//		testqueryCards();
		//testqueryUnits();
		payment();
		//testOrderMxs();
		
		//back();
    	//OrderStatus();
	}
	public static void OrderStatus(){
		try {
			URL targetUrl = new URL(targetURL14);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl
					.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("userNo", "949494674dc2704f014dc27197130000");
			jsonObj.put("orderId","CHENY000000099000011");
			jsonObj.put("encryptKey","459835177B0D597C87E8EF804C67B93AA0FCCB62B5AAB593F1014BEBF5A16C79DC12F5E6C3A1869AF2F1B55C86F49AF3A6E431C69926BC5BFAAE5A42905FAD77844CCCA3A6774B7C9DA66F0022D1990D20BDB886C603FFB0960EF8D939720A5A8D684AC4C927EBA5258CF07B63B49D58F5A0C5129995C9B37C380CB0149FEDAB");
			jsonObj.put("encryptValue","E1F1E156455A5D684763F54FADA03F9A8A2A2E324E1A1C92BDC5C8EE66EA50052C839C87BF6AFF31F2BEFAF1D4F6976B");
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
	public static void testOrderMxs(){
		try {
			URL targetUrl = new URL(targetURL12);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl
					.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("userNo", "949494674dc2704f014dc27197130000");
			jsonObj.put("feeType","01");
			jsonObj.put("startDate","20140909");
			jsonObj.put("endDate","20151009");
			jsonObj.put("encryptKey","459835177B0D597C87E8EF804C67B93AA0FCCB62B5AAB593F1014BEBF5A16C79DC12F5E6C3A1869AF2F1B55C86F49AF3A6E431C69926BC5BFAAE5A42905FAD77844CCCA3A6774B7C9DA66F0022D1990D20BDB886C603FFB0960EF8D939720A5A8D684AC4C927EBA5258CF07B63B49D58F5A0C5129995C9B37C380CB0149FEDAB");
			jsonObj.put("encryptValue","E1F1E156455A5D684763F54FADA03F9A8A2A2E324E1A1C92BDC5C8EE66EA50052C839C87BF6AFF31F2BEFAF1D4F6976B");
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
	
	public static void testqueryCards(){
		try {
			URL targetUrl = new URL("http://218.23.173.110:8099/UntechPayTest/queryCards");
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl
					.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			JSONObject jsonObj = new JSONObject();
			//明文
			jsonObj.put("userNo", "949494674dc2704f014dc27197130000");
			//发送报文只包含encryptKey encryptValue
			String key = RandomUtil.generateNumber(16);
			System.out.println(jsonObj.toString());//{"userNo":"949494674dc2704f014dc27197130000"}
			Object obj = ReturnUtil.getPayReturn(jsonObj, key);
			System.out.println(obj.toString());
		    //{"encryptKey":"3E4B5FA77CC6B5AAAB96C180844930C85D99720347C67150F3B15E4CA3CE613F511EC9D145D3FEA4BD70E5BD92D7C171D25D3D4CE5AFDC4F973E1E521676DCD406ED0622DC2B2157160E03F941FC5856416679860A7E9148E473F70B6D0D5946DEB77B5AA22CE57BFCB8F1CFDEE43379DD388D7822F4D14163F2005456B98D29","encryptValue":"C906D6043BF2DEDBFE17B6AD55C5ED8A1DD0449949F9E4B7EEE7EA1828C84D510616E75FB4A401095C32342CF115025E"}
			OutputStream outputStream = httpConnection.getOutputStream();
			
			outputStream.write(obj.toString().getBytes());
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
	
 
	public static void testqueryUnits(){
		try {
			URL targetUrl = new URL(targetURL2);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type","application/json");

			JSONObject jsonObj = new JSONObject();
			OutputStream outputStream = httpConnection.getOutputStream();
			jsonObj.put("userNo", "949494674dc2704f014dc27197130000");
			jsonObj.put("feeType", "04");
			outputStream.write(jsonObj.toString().getBytes());
			outputStream.flush();
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}
			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream()),"UTF-8"));
			String output;
			StringBuffer sb = new StringBuffer();
			System.out.println("Output from Server:\n");
			while ((output = responseBuffer.readLine()) != null) {
				System.out.println(output);
				sb.append(output);
			}
			System.out.println(sb);
			//System.out.println(JSONObject.fromObject(sb.toString()).get("value").toString());
			//System.out.println(AESUtil.decrypt(JSONObject.fromObject(sb.toString()).get("value").toString()));
			httpConnection.disconnect();
		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 生成订单
	 */
	public static void testBornOrder(){
		try {
			URL targetUrl = new URL(targetURL3);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type","application/json");

			JSONObject jsonObj = new JSONObject();
			/*jsonObj.put("value", "13B2EF263CADD3F4B9607E6B51786CF800C9103907DE782C14F20815F770FBA74090AB09AA19F19B6CC3C50C498C7FD994996BED00C9EBA4DDD9AE0724896F1A0692C65D0E4EB99BAE876A4FD2B302C9A7EF7E4112DE4B31221A438B96FFDE51619B481E75AA51C07F70EB8A4FD85F1132DB1F3FC766F975EB0A0CC5DE5AA965777B7C99ADAD6D4818AA3A3F896B597727969BF40C54EAEA22FDDCFC3E4FD0325391B1CC3E370A34E932C6B07A96D4BE7D031127D54761D15805378938DA13A78676E026567C1FA3F98004C9E7CFC99C");
			jsonObj.put("key","AE9C62063C0AB4E0AA8682B17DEFDE856F8D7A8F088E33AB9D7CF290DEE1C01BC0C47A756A488914DC2F2626E6CFFC3544F825294093B5773254FE855A3763A6D98CE5D9799B7406EDA8B0D03AC06271C1862508D75495B1F6737FA117D9814212217318F4BE9C143A118F484D5D97B566629954C3773CE43176FD3057343101");
			*/
			jsonObj.put("transactionNo", "425701");
			//jsonObj.put("userId", "949494674dc2704f014dc27197130000");
			jsonObj.put("payamt", "10.50");
			jsonObj.put("flag", "0");
			jsonObj.put("feeType", "08");
			jsonObj.put("androidId", "android10001");
			jsonObj.put("mid", "100001");
			jsonObj.put("detail", "330ml可口可乐,500ml营养快线,500ml激活");
			jsonObj.put("key", "key0001");
			jsonObj.put("encrypt", "encrypt001");
			
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
	
	/**
	 * 根据（交易号）订单号获取订单信息接口
	 */
	public static void queryOrderDetail(){
		try {
			URL targetUrl = new URL(targetURL4);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type","application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("orderId", "SBZF2015082018083416601");
			jsonObj.put("key", "key0001");
			jsonObj.put("encrypt", "encrypt001");
			
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
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 根据（交易号）订单号获取订单信息接口
	 */
	public static void queryBankcardList(){
		try {
			URL targetUrl = new URL(targetURL5);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type","application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("id", "0620626AD970F055E050A8C080014054");
			jsonObj.put("key", "key0001");
			jsonObj.put("encrypt", "encrypt001");
			
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
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void pushOrderResult(){
		try {
			URL targetUrl = new URL(targetURL9);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type","application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("androidId", "123456789");
			jsonObj.put("transactionNo", "bd483351f3b4342f");
			jsonObj.put("status", "2004");
			jsonObj.put("key", "key0001");
			jsonObj.put("encrypt", "encrypt001");
			
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
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void pushShipmentResult(){
		try {
			URL targetUrl = new URL(targetURL10);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type","application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("androidId", "123456789");
			jsonObj.put("transactionNo", "bd483351f3b4342f");
			jsonObj.put("status", "2003");
			jsonObj.put("key", "key0001");
			jsonObj.put("encrypt", "encrypt001");
			
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
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void payment(){
		try {
			URL targetUrl = new URL(targetURL8);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type","application/json");

			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("payType", "02");
			jsonObj.put("totalAmt", "1");
			jsonObj.put("cardAmt", "0.00");
			jsonObj.put("orderId", "WJBZCLOCK20181129113721ANMV");
			jsonObj.put("feeType", "28");
			jsonObj.put("unitNo", "200000003");
			jsonObj.put("cardNo", "");
			jsonObj.put("mobile", "18375330973");
		    jsonObj.put("bankNo", "01");
			jsonObj.put("channelNo", "01");
			//jsonObj.put("cardNo", "");
			//jsonObj.put("mobile", "");
			jsonObj.put("paySecurt", "A1EFA966B83E2082");
			jsonObj.put("detail", "我家亳州-打卡报名支付");
//			jsonObj.put("paySecurt", "A1EFA966B83E2082");
//			jsonObj.put("detail", "嗲那电费");
			jsonObj.put("backUrl", "www.baidu.com");
			
			//jsonObj.put("key", "2428302D6D73A8EBFE8281F6461F33409FBE2EBC2A5BFA748816480BB2380BB66B6C60DEABA31D4CD216ED543F9C6E000BB8E536290A2CB3E997F0735CFC3E9356030B99C99768F2DAED6DDECC98A0869948FBC3D81A8B0902E203876C670AC328E27E4ABC8036893F11B2FA5807907FDC9A663432A7B88AF5D3AE0BA759F392");
			//jsonObj.put("encrypt", "83D2A356C46A11C62E966CD517D6AAF18E87E579A81A70E6D2A41DB53B3C7BB7");
			
			OutputStream outputStream = httpConnection.getOutputStream();
			String key = RandomUtil.generateNumber(16);
			jsonObj = (JSONObject)ReturnUtil.getPayReturn(jsonObj, key);
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
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void back(){
		try {
			URL targetUrl = new URL(targetURL13);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type","application/json");

			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put("userNo", "1");
			jsonObj.put("totalAmt", "10.00");
			jsonObj.put("orderId", "CHENY000000099000011");
			jsonObj.put("feeType", "01");
			jsonObj.put("oldOrderId", "CHENY00000009900001");
			jsonObj.put("oldPayerTime", "20150901 12:11:26");
			jsonObj.put("backUrl", "http://218.23.173.110:8099/UntechPayTest/queryCards");
			
			jsonObj.put("key", "2428302D6D73A8EBFE8281F6461F33409FBE2EBC2A5BFA748816480BB2380BB66B6C60DEABA31D4CD216ED543F9C6E000BB8E536290A2CB3E997F0735CFC3E9356030B99C99768F2DAED6DDECC98A0869948FBC3D81A8B0902E203876C670AC328E27E4ABC8036893F11B2FA5807907FDC9A663432A7B88AF5D3AE0BA759F392");
			jsonObj.put("encrypt", "83D2A356C46A11C62E966CD517D6AAF18E87E579A81A70E6D2A41DB53B3C7BB7");
			
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
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	public static void cancelOrderNo(){
		try {
			URL targetUrl = new URL(targetURL11);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type","application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("id", "949494674dc2704f014dc27197130000");
			jsonObj.put("orderId", "SBZF2015082518503770101");
			jsonObj.put("channel", "02");
			
			jsonObj.put("key", "2428302D6D73A8EBFE8281F6461F33409FBE2EBC2A5BFA748816480BB2380BB66B6C60DEABA31D4CD216ED543F9C6E000BB8E536290A2CB3E997F0735CFC3E9356030B99C99768F2DAED6DDECC98A0869948FBC3D81A8B0902E203876C670AC328E27E4ABC8036893F11B2FA5807907FDC9A663432A7B88AF5D3AE0BA759F392");
			jsonObj.put("encrypt", "83D2A356C46A11C62E966CD517D6AAF18E87E579A81A70E6D2A41DB53B3C7BB7");
			
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
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
