package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.untech.util.HttpClientMessageSenderByJson;
import com.untech.util.MD5;

public class PostClient {

	// private static final String targetURL =
	// "http://localhost:8080/AppThirdServer/person2";
	private static final String targetURL = "http://127.0.0.1:8080/UntechPay/listUser";
	private static final String targetURL2 = "http://218.23.173.110:8090/UntechPay/bornTransactionNo";
	private static final String targetURL3 = "http://127.0.0.1:8080/UntechPay/bornOrder";
	private static final String targetURL4 = "http://127.0.0.1:8080/UntechPay/queryOrderDetail";
	private static final String targetURL5 = "http://127.0.0.1:8080/UntechPay/queryBankcardList";
	private static final String targetURL9 = "http://127.0.0.1:8080/UntechPay/pushOrderResult";
	private static final String targetURL10 = "http://127.0.0.1:8080/UntechPay/pushShipmentResult";
	private static final String targetURL7 = "http://127.0.0.1:8080/UntechPay/payment";
	private static final String targetURL8 = "http://127.0.0.1:8080/UntechPay/returnFee";
	
	public static void main(String[] args) {
//		test();
//		testBornTransactionNo();
//		testBornOrder();
//		queryOrderDetail();
//		queryBankcardList();
//		pushOrderResult();
//		pushShipmentResult();
//		payment();
		//returnFee();
		//unionPay();
	}
	
	/*public static void main(String[] args) {
		
		Map<String,String> map = new HashMap<String, String>();
		String orderno="WJBZ000000000000006";
		String md5Source =orderno+ "100142123.45"+"FSETFD1851AD6713";
		String md5 = MD5.encode(md5Source.getBytes());
		
		map.put("sign", md5);
		map.put("orderNo", orderno);
		map.put("count", "1");
		map.put("asynUrl", "http://123.34.12.34:8080");
		map.put("reserve", "");
		map.put("mercId", "100142");
		map.put("goodsName", "1234test");
		map.put("totalPrice", "123.45");
		map.put("detail", "33");

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://36.33.24.12:8083/upl/thirdpays/payThirdCommOrders4Mobile.json");
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, Consts.UTF_8);
		httppost.setEntity(entity);

		try {
			CloseableHttpResponse response = client.execute(httppost);

			if (response.getStatusLine().getStatusCode() == 200) {
				String str = EntityUtils.toString(response.getEntity());
				System.out.println("IndexController.doPay()" + str);
				JSONObject obj  = JSONObject.fromObject(str);
				System.out.println(obj);
				System.out.println(obj.get("status"));
				Map<String,Object>  m = (Map<String,Object> )obj.get("result");
				System.out.println(m);
				System.out.println(m.get("tn"));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
*/
	
	public static void test(){
		try {
			URL targetUrl = new URL(targetURL);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl
					.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("id", "2");
			jsonObj.put("name", "233");
			jsonObj.put("age", "3");
			
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
	 * 测试生成交易号
	 */
	public static void testBornTransactionNo(){
		try {
			URL targetUrl = new URL(targetURL2);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type","application/json");

			JSONObject jsonObj = new JSONObject();
//			jsonObj.put("value", "8DE21C7AD8899A69388E9E7B3C973827DFAE732218D57B3EB4AE9E74FF5F3BF94CCEEE521F0E31926A502B5FCCB7B6991B48626B0DC4FB4ED57A875F425B399150B7F3463D3DA39C5C455032FED35BE9");
//			jsonObj.put("key", "AE9C62063C0AB4E0AA8682B17DEFDE856F8D7A8F088E33AB9D7CF290DEE1C01BC0C47A756A488914DC2F2626E6CFFC3544F825294093B5773254FE855A3763A6D98CE5D9799B7406EDA8B0D03AC06271C1862508D75495B1F6737FA117D9814212217318F4BE9C143A118F484D5D97B566629954C3773CE43176FD3057343101");
			jsonObj.put("id", "949494674dc2704f014dc27197130000");
			jsonObj.put("sid", "20001");
			jsonObj.put("type", "android");
			jsonObj.put("encrypt", "83D2A356C46A11C62E966CD517D6AAF18E87E579A81A70E6D2A41DB53B3C7BB7");
			OutputStream outputStream = httpConnection.getOutputStream();
			
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
				//sb.append(output);
			}
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
//			jsonObj.put("value", "13B2EF263CADD3F4B9607E6B51786CF800C9103907DE782C14F20815F770FBA74090AB09AA19F19B6CC3C50C498C7FD994996BED00C9EBA4DDD9AE0724896F1A0692C65D0E4EB99BAE876A4FD2B302C9A7EF7E4112DE4B31221A438B96FFDE51619B481E75AA51C07F70EB8A4FD85F1132DB1F3FC766F975EB0A0CC5DE5AA965777B7C99ADAD6D4818AA3A3F896B597727969BF40C54EAEA22FDDCFC3E4FD0325391B1CC3E370A34E932C6B07A96D4BE7D031127D54761D15805378938DA13A78676E026567C1FA3F98004C9E7CFC99C");
//			jsonObj.put("key","AE9C62063C0AB4E0AA8682B17DEFDE856F8D7A8F088E33AB9D7CF290DEE1C01BC0C47A756A488914DC2F2626E6CFFC3544F825294093B5773254FE855A3763A6D98CE5D9799B7406EDA8B0D03AC06271C1862508D75495B1F6737FA117D9814212217318F4BE9C143A118F484D5D97B566629954C3773CE43176FD3057343101");
			jsonObj.put("transactionNo", "523801");
			jsonObj.put("userId", "949494674dc2704f014dc27197130000");
			jsonObj.put("unitNo", "34120000000000");
			jsonObj.put("unitName", "亳州市东鑫商贸有限公司");
			jsonObj.put("payamt", "10.50");
			jsonObj.put("flag", "0");
			jsonObj.put("feeType", "08");
			jsonObj.put("androidId", "1c8cd53730242b35");
			jsonObj.put("mid", "2d5d9e79ccc0722754e20eb5935ee6e1");
			jsonObj.put("detail", "330ml可口可乐,500ml营养快线,500ml激活");
			jsonObj.put("key", "12345678");
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
			jsonObj.put("orderId", "SBZF201508311034166901");
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
			jsonObj.put("id", "10001");
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
			URL targetUrl = new URL(targetURL7);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type","application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("cardNo", "6217780000121111111");
			jsonObj.put("id", "949494674dc2704f014dc27197130000");
			jsonObj.put("totalamt", "10.50");
			jsonObj.put("payMoney", "10.50");
			jsonObj.put("cardMoney", "0");
			jsonObj.put("payEncrypt", "A1EFA966B83E2082");
			
			jsonObj.put("paytype", "02");
			jsonObj.put("orderId", "SBZF2015090619452628001");
			jsonObj.put("mobile", "18255140399");
			
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
	
	public static void returnFee(){
		try {
			URL targetUrl = new URL(targetURL8);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type","application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("orderId", "SBZF2015090619452628001");
			jsonObj.put("feeType", "11");
			
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
	
	
	public static void unionPay(){
		try {
			URL targetUrl = new URL("http://36.33.24.12:8083/upl/thirdpays/payThirdCommOrders4Mobile.json");
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			//httpConnection.setRequestProperty("Content-Type","application/json");

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("orderNo", "WJBZ000000000000004");
			jsonObj.put("mercId", "100142");
			jsonObj.put("goodsName", "1234test");
			jsonObj.put("detail", "test1234");
			jsonObj.put("count", "1");
			jsonObj.put("totalPrice", "123.45");
			jsonObj.put("asynUrl", "http://123.34.12.34:8080");
			jsonObj.put("reserve", "");
			String md5Source = "WJBZ000000000000004100142123.45"+"FSETFD1851AD6713";
			System.out.println(jsonObj.toString());
			String md5 = MD5.encode(md5Source.getBytes());
			jsonObj.put("sign", md5);
			OutputStream outputStream = httpConnection.getOutputStream();
			
			outputStream.write(jsonObj.toString().getBytes());
			outputStream.flush();
			System.out.println(httpConnection.getResponseCode() +"httpConnection.getResponseCode() ");
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
			
			
			HttpClientMessageSenderByJson json = new HttpClientMessageSenderByJson();
			System.out.println("--------"+jsonObj.toString());
			String returnStr  = json.send(jsonObj.toString(), "http://36.33.24.12:8083/upl/thirdpays/payThirdCommOrders4Mobile.json");
			System.out.println("returnStr====="+returnStr);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}
