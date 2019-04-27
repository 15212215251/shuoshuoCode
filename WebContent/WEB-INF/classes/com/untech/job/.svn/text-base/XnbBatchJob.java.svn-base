package com.untech.job;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.untech.service.PayService;
import com.untech.util.SpringApplicationContextHolder;
 
/**
 * 签约对账
 * 1、自动签约商户表为自动签约标志的商户
 * 2、先调用主机对账进行签约对账
 * 3、更新数据库的签约状态为省联社一致
 * 4、检查数据库签约表状态和签约明细表不一致的明细，进行自动发起签约和解约
 * 5、再次发起签约对账。
 */
public class XnbBatchJob{
	
	 
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdf1= new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    private final static Logger logger = LoggerFactory.getLogger(XnbBatchJob.class);
	/**
	 * 定时对账job
	 */
	public void tradingJob(){
		logger.info("====================签约对账开始===================");
		//签约
		//this.SignCard();
	}
	
	//签约
	/*************************************************
	**************************************************/
	public void SignCard(){
		 PayService payService = (PayService) SpringApplicationContextHolder.getBean(PayService.class);
		 System.out.println("PayService=========="+payService);
		 //查询所有新农保1118返回码的错误信息
		Map map = new HashMap();
		List list = payService.queryXnbBatch(map);
		if(list != null && list.size()>0){
			for(int i=0;i<list.size();i++){
				System.out.println("----------------------------"+i);
				Map tmpMap = (Map) list.get(i);
				String unitNo = tmpMap.get("UNITNO").toString();
				map = new HashMap();
				map.put("unitNo", unitNo);
				List unitList = payService.queryUnitByUnitNo(map);
				if(unitList != null && unitList.size()>0){
					 String merchantId  = ((Map)unitList.get(0)).get("MERCHANTID").toString();
					 String unitName = ((Map)unitList.get(0)).get("UNITNAME").toString();
					 Map m = (Map)unitList.get(0);
					 // 获取转入
					 List inList = payService.queryCommer(merchantId);/*商户编号*/
					 if (inList == null || inList.size() == 0) {
						 break;
					 }else{
						 map.put("inCardNo",((Map)inList.get(0)).get("ACCTNO"));
						 map.put("inCardName",((Map)inList.get(0)).get("ACCTNAME"));
					 }
				}else{
					 break;
				}
				map.put("outCardNo", tmpMap.get("CARDNO"));
				map.put("outCardName", tmpMap.get("DETAIL"));
				map.put("orderId", tmpMap.get("ORDERID"));
				map.put("totalAmt", tmpMap.get("PAYAMT")+"");
				map.put("remark", tmpMap.get("TRAFFICNO"));
				map.put("payerTime", tmpMap.get("PAYERTIME"));
				try {
					System.out.println(map);
					JSONObject jsonObject = (JSONObject) payService.cardXnbMain0017(map);
					if("1118".equals(jsonObject.get("resCode"))){
						System.out.println("111111111111111111111");
                        break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
	/**
	 * 将金额转换成两位小数的字符串
	 * @param dccAmt
	 * @return
	 */
	private String toDouble(String dccAmt){
		double amt = Double.parseDouble(dccAmt);
		return String.format("%.2f",amt);
	}
	
	public String getYesDay(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return sdf.format(cal.getTime());
	}
	
	public static String subDay(String day ){
		
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		Date beginDate = null;
		try {
			beginDate = dft.parse(day);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
		String endDate = dft.format(date.getTime());
		return endDate;
	}
	
	public static String getDecimal(String amt){
		String n = new BigDecimal(amt).multiply(new BigDecimal(100)).setScale(0)+"";
		System.out.println(n);
		int length = n.length();
		for(int i=0;i<12-length;i++){
			n="0"+n;
		}
		System.out.println(n);
		return n;
	}
	
	public static void main(String[] args){
		  
		
		System.out.println(subDay("20160727"));
		
		/* //组包和解包
		 SocketClientSecurt securt = new SocketClientSecurt();
		 //发送给银联
		 SocketClientQuick quick = new SocketClientQuick();
		 //调用 签到进行组包
		 String resposne = securt.sendMessage("05~01~2~2~2~2~2~2~2~");
		 System.out.println("resposne===="+resposne);
		 String[] res = resposne.split("~");
		 System.out.println("res[1]========="+res[1]);
		 byte[] b = MyArrayUtil.hexString2byte(res[1]);
		 System.out.println(b.length+"=-----------b.length");
		 byte[] contHeader = MyArrayUtil.int2hexByte(b.length, 2);
		 System.out.println("contHeader-------"+ MyArrayUtil.byte2hexString(contHeader));
		 byte[] request = new byte[b.length + 2];
		 System.arraycopy(contHeader, 0, request, 0, 2);
		 System.arraycopy(b, 0, request, 2, b.length);
		 System.out.println(request.length+"request");
		 String hex = MyArrayUtil.byte2hexString(request);
		 System.out.println("hex=========="+hex);
		 //调用银联进行签到
		 byte[] resposne2 = quick.sendMessage(request);
		 System.out.println("resposne===="+ MyArrayUtil.byte2hexString(resposne2));
		
		 byte[] cont  = new byte[resposne2.length-2];
		 System.arraycopy(resposne2, 2, cont, 0, cont.length);
		 System.out.println("cont===="+ MyArrayUtil.byte2hexString(cont));
		 String contHex = MyArrayUtil.byte2hexString(cont);
		 //调用签到解包
		 resposne = securt.sendMessage("06~"+contHex+"~01~");
		 System.out.println("resposne===="+resposne);
		 String[] str = resposne.split("~");
		 if("00".equals(str[1])){
			 
		 }*/
		
		//System.out.println("flag============="+PosSignUtil.sign());
		/* String contHex = "60000000036031000000000810003800010AC000140000861500510513080009360031353030353134363033343430303137383136303034393437333431363438343130303130001300000001003000004051161DF4125B4CED02956F998FA3E7746A25273BE3D2D6AD664FCAC5000000000000000072C71E7F";
		 SocketClientSecurt securt = new SocketClientSecurt();
		 String resposne = securt.sendMessage("06~"+contHex+"~01~");
		 System.out.println("resposne===="+resposne);
		 String[] str = resposne.split("~");*/
	}
}
