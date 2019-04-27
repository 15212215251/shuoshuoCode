package com.untech.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.untech.jpush.JPushObject;
import com.untech.service.OrderService;
import com.untech.service.WjbzCommenDzService;
import com.untech.service.impl.WjbzCommenDzServiceImpl;
import com.untech.util.Common_Const;
import com.untech.util.EncodeDecodeUtil;
import com.untech.util.GenerateQrCodeUtil;
import com.untech.util.OrderId;
import com.untech.util.RandomId;
import com.untech.util.RandomUtil;
import com.untech.util.RedisUtil;
import com.untech.util.ReturnUtil;
import com.untech.util.SecurtUtil;
import com.untech.util.StringUtil;

/**
 * 订单Controller
 * 
 * @author wuxw
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Scope("prototype")
@Controller
public class WjbzCommonDzController {
	 

	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(WjbzCommonDzController.class
			.getName());
	private JSONObject jsonObject = new JSONObject();

	@Autowired(required=true)
	private WjbzCommenDzServiceImpl wjbzCommenDzService;

	@RequestMapping(value = "/WjbzCommonDz")
	@ResponseBody
	public Object bornTransactionNo(@RequestBody Map<String, Object> map) {
		logger.info("WjbzCommonDz start :" + map);
		String key = RandomUtil.generateNumber(16);
		// 加解密处理
		map = SecurtUtil.decrypt(map);
		logger.info("解密 :" + map);
		if (null == map) {
			jsonObject.put("resCode", "0001");
			jsonObject.put("resMsg", "解密失败");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		logger.info("请求交易号报文:" + map);
		System.out.println("请求交易号报文=>" + map);
		
		/**
		 * dzDate 对账日期
		 * feeType 费用类型
		 */
		if (StringUtil.isEmpty(map.get("dzDate"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "对账日期为空");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("feeType"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "费用类型为空");
			return ReturnUtil.getPayReturn(jsonObject, key);
		}
		
		String dzDate = map.get("dzDate").toString();
		String feeType = map.get("feeType").toString();
		System.out.println("dzDate = " + dzDate);
		System.out.println("feeType = " + feeType);
		//根据对账日期,查询对账结果表app_dzresult,判断该日期是否已完成对账
		List dzReslist = wjbzCommenDzService.queryDzResultByDate(dzDate);
		System.out.println(dzReslist);
		System.out.println(dzReslist.size());
		if(dzReslist == null || dzReslist.size() < 1){
			jsonObject.put("resCode", "1005");
			jsonObject.put("resMsg", "该日期暂未对账,请稍后重试!");
		}else{
			//根据对账日期与费用类型查询已对账数据
			try {
				List list = wjbzCommenDzService.queryCommenDzMx(dzDate, feeType);
				if(list != null && list.size() > 0){
					//该日期存在已对账数据,根据日期生成对账文件
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
					String formatStr =formatter.format(new Date());
					String fileName = "WjbzCommenDz_" + formatStr + ".txt";
					///home/appweb/dz/
					String filePath = "/home/appweb/dz/";
//					String filePath = "D:\\Users\\6103\\Desktop\\WjbzCommenDz\\";
					
					boolean flag = writeFileContent(fileName,filePath,list);
					if(!flag){
						jsonObject.put("resCode", "1004");
						jsonObject.put("resMsg", "对账文件创建异常");
					}
					jsonObject.put("resCode", "0000");
					jsonObject.put("resMsg", "对账文件创建成功");
					jsonObject.put("fileName", fileName);
					
					
				}else{
					jsonObject.put("resCode", "1003");
					jsonObject.put("resMsg", "该日期无已对账数据");
				}
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.put("resCode", "4007");
				jsonObject.put("resMsg", "系统异常");
			}
		}
		
		System.out.println("响应请求交易号报文=>" + jsonObject);
		logger.info("响应请求交易号报文=>" + jsonObject);
		return ReturnUtil.getPayReturn(jsonObject, key);

	}
	
	/**
     * 向文件中写入内容
     * @param fileName 文件名
     * @param filepath 文件路径
     * @param list 写入内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String fileName,String filePath,List list) throws IOException{
        Boolean bool = false;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filePath + fileName);//文件路径(包括文件名称)
            if(!file.exists()){
                file.createNewFile();
            }else{
            	file.delete();
            	file.createNewFile();
            }
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String content = new String();
            
            for(int i=0;i < list.size();i++){
            	if(i==0){
            		content += "订单号|收费商户编号|收费商户名称|支付时间|费用类型|支付类型|支付总金额|卡付款金额|电子钱包付款金额|卡号|费用操作类型|退款金额|支付状态|钱包支付对应订单编号"+"\r\n";
            	}
            	Map map = new HashMap();
            	map = (Map) list.get(i);
                String ORDERID = map.get("ORDERID") == null ? "" : map.get("ORDERID")+"";
                String UNITNO = map.get("UNITNO") == null ? "" : map.get("UNITNO")+"";
                String UNITNAME = map.get("UNITNAME") == null ? "" : map.get("UNITNAME")+"";
                String PAYERTIME = map.get("PAYERTIME") == null ? "" : map.get("PAYERTIME")+"";
                String FEETYPE = map.get("FEETYPE") == null ? "" : map.get("FEETYPE")+"";
                String MONEYFLAG = map.get("MONEYFLAG") == null ? "" : map.get("MONEYFLAG")+"";
                String TOTALAMT = map.get("TOTALAMT") == null ? "" : map.get("TOTALAMT")+"";
                String CARDAMT = map.get("CARDAMT") == null ? "" : map.get("CARDAMT")+"";
                String MONEY = map.get("MONEY") == null ? "" : map.get("MONEY")+"";
                String CARDNO = map.get("CARDNO") == null ? "" : map.get("CARDNO")+"";
                String TRANSTYPE = map.get("TRANSTYPE") == null ? "" : map.get("TRANSTYPE")+"";
                String BACKAMT = map.get("BACKAMT") == null ? "" : map.get("BACKAMT")+"";
                String FLAG = map.get("FLAG") == null ? "" : map.get("FLAG")+"";
                String ANDROIDID = map.get("ANDROIDID") == null ? "" : map.get("ANDROIDID")+"";
                content += ORDERID+"|"+UNITNO+"|"+UNITNAME+"|"+PAYERTIME+"|"+FEETYPE+"|"+MONEYFLAG+"|"+TOTALAMT+"|"+CARDAMT+"|"+MONEY+"|"+CARDNO+"|"+TRANSTYPE+"|"+BACKAMT+"|"+FLAG+"|"+ANDROIDID+"\r\n";
            }
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(content);
            pw.flush();
            bool = true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }
	
	public static void main(String[] args) {
		
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		String formatStr =formatter.format(new Date());
//		System.out.println(formatStr);
		
		String ss = null;
		System.out.println((ss+"")  == null);
		
		String s = ss == null ? "" : ss;
		System.out.println("11111");
		System.out.println(s);
		System.out.println("22222");
		
	}
	
		
		
}
