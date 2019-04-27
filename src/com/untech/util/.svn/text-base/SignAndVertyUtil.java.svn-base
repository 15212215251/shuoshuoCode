package com.untech.util;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


public class SignAndVertyUtil {
	/*
	 * 	<CSRes id="CSRes">
		<version>1.0.1</version>
		<merchantId>800000000004</merchantId>
		<certId>0001</certId>
		<date>20090722 15:49:43</date>
		<accountName>陈勇</accountName>
		<bankCardNo>6217788300100338066</bankCardNo>
		<bankCardType>D</bankCardType>
		<certificateType>1</certificateType>
		<certificateNo>342427198712230532</certificateNo>
		<mobilePhone>18667935421</mobilePhone>
		<signNo>8000000000046217788300100338066</signNo>
		</CSRes>
	 * 
	 * 
	 *  <Error id="Error">
		<version>1.0.1</version>
		<merchantId>800000000004</merchantId>
		<certId>0001</certId>
		<errorCode>1820</errorCode>
		<errorMessage>该银行卡号已经成功签约</errorMessage>
		<errorDetail>客户已签约</errorDetail>
		</Error>
	
	 * 
	 * */
	private static Logger logger = Logger.getLogger(SignAndVertyUtil.class.getName()); //单例
	////网上支付 APReq
	public static  Map<String,Object > SignXml(String xml,String msgType){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			PrivateKey privateKey  = KeyUtil.getMOCKPriKey();
			String resultXml = SignUtil.sign(xml, privateKey, msgType);
			HttpClientMessageSender sender = new HttpClientMessageSender();
			resultXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +resultXml ;
			logger.info("resultXml--\r\n"+resultXml);
			String url = Common_Const.URL;
			if(msgType.equals("PFQReq") || msgType.equals("TTQReq")  || msgType.equals("PFPReq")  ||msgType.equals("TTPReq") ||msgType.equals("PFHQReq") ){
				url = Common_Const.URL1;
			}
			logger.info( url);
			String respesXml =  sender.send(resultXml, url);
			logger.info("respesXml===" + respesXml);
			if(respesXml == null || "null".equals(respesXml)){
				respesXml = "";
				map.put("isChecked", false);
				ResponseModel model = new ResponseModel();
				model.setErrorCode("9000");
				model.setErrorMessage("银行方异常");
				model.setErrorDetail("银行方异常");
				map.put("Error", model);
				return map;
			}
			PublicKey pubKey = KeyUtil.getMOCKPubKey();
			boolean isChecked = SignUtil.check(respesXml, pubKey);
			logger.info("对xml签名后的结果进行验证签名是:" + isChecked);
			map.put("isChecked", isChecked);
			/*map.put("respesXml", respesXml);*/
			Map<String,Object> returnMap = new HashMap<String, Object>();
			returnMap = XmlUtil.Dom2MapBank(respesXml);
			//通用错误信息,需要判断签约成功和返回已签约的状态返回   
			Map<String, Object>  map_2 = (Map<String, Object>) returnMap.get("Message");
			Map<String, Object>  map_3 = (Map<String, Object>)map_2.get("Error");
			logger.info("map_3==="+map_3);
			if(map_3 != null){
				ResponseModel model = new ResponseModel();
				model.setMerchantId(map_3.get("merchantId").toString());
				model.setVersion(map_3.get("version").toString());
				model.setErrorCode(map_3.get("errorCode").toString());
				model.setCertId(map_3.get("certId").toString());
				model.setErrorDetail(map_3.get("errorDetail").toString());
				model.setErrorMessage(map_3.get("errorMessage").toString());
				map.put("Error", model);
			}else{//将处理成功的信息存到map中
				String resType = msgType.substring(0,msgType.length()-1)+"s";
				map_3 = (Map<String, Object>)map_2.get(resType);
				System.out.println("map_3==="+map_3);
				map.put(resType, map_3);
				if("CCQFReq".endsWith(msgType) || "SCQFReq".endsWith(msgType)) {
					String filepath=Common_Const.DZPATH;
					logger.info("文件的签名结果为：" + SignUtil.checkFileSignature(respesXml, pubKey,filepath));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("isChecked", false);
		}
		return map;
		
	}
	
	public static void main(String[] args) {
		SignXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?><MessageSuit><Message id=\"40288acc4db02ebe014db229e8ac00f020150603085413556\"><PFQReq id=\"PFQReq\"><version>1.0.1</version><merchantId>800000000007</merchantId><certId>0001</certId><date>20150603 20:54:13</date><userNo>r</userNo><gridId>34401</gridId><busiNo>34010100790002</busiNo></PFQReq>", "PFQReq");
	}
}
