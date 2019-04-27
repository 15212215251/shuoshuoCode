package com.untech.util;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileDzUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(FileDzUtil.class);
    public static String QYWReconLx(List list ,String unitNo,String date) {
		
		//获取文件名
	    String fileName = "QYW_WJBZ_"+date+"_"+unitNo+".txt";
		if(unitNo == null || "".equals(unitNo.trim())){
			fileName = "QYW_WJBZ_" + date + ".txt";
		}
		String filePath=Common_Const.DZPATH;
		File file=new File(filePath+"/"+fileName);
		logger.info(filePath+"/"+fileName);
		try{
			if(!file.exists()){
				file.createNewFile();
			}
			//写入文件
			FileWriter writer = new FileWriter(filePath+"/"+fileName);
			//循环集合将集合内数据以|为分割符进行分割
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String Line = map.get("ORDERID").toString()+"|"+map.get("UNITNO").toString()+"|"+map.get("UNITNAME").toString()+"|"
				             +map.get("PAYERTIME").toString()+"|"+map.get("FEETYPE").toString()+"|"+map.get("MONEYFLAG").toString()+"|"
				             +map.get("PAYAMT").toString()+"|"+map.get("CARDAMT").toString()+"|"+map.get("MONEY").toString()+"|"
				             +map.get("CARDNO").toString()+"|"+map.get("TRANSTYPE").toString()+"|"+map.get("BACKAMT").toString()+"|"
				             +map.get("FLAG").toString()+"|"
				             +"\n";
				logger.info("本次写入数据为:"+Line); 
		        writer.write(Line);
		        writer.flush();
			}
			writer.close();
		}catch (Exception e) {
			return "";
		}
		return fileName;
	}


public static String CommonReconLx(List list ,String unitNo,String feetype,String date) {
	
	//获取文件名
    String fileName = "WJBZ_"+date+"_"+feetype+"_"+unitNo+".txt";
	if(unitNo == null || "".equals(unitNo.trim())){
		fileName = "WJBZ_" +feetype +"_"+date + ".txt";
	}
	String filePath=Common_Const.DZPATH;
	File file=new File(filePath+"/"+fileName);
	logger.info(filePath+"/"+fileName);
	try{
		if(!file.exists()){
			file.createNewFile();
		}
		//写入文件
		FileWriter writer = new FileWriter(filePath+"/"+fileName);
		//循环集合将集合内数据以|为分割符进行分割
		for(int i=0;i<list.size();i++){
			Map map = (Map)list.get(i);
			String Line = map.get("ORDERID").toString()+"|"+map.get("UNITNO").toString()+"|"+map.get("UNITNAME").toString()+"|"
			             +map.get("PAYERTIME").toString()+"|"+map.get("FEETYPE").toString()+"|"+map.get("MONEYFLAG").toString()+"|"
			             +map.get("PAYAMT").toString()+"|"+map.get("CARDAMT").toString()+"|"+map.get("MONEY").toString()+"|"
			             +map.get("CARDNO").toString()+"|"+map.get("TRANSTYPE").toString()+"|"+map.get("BACKAMT").toString()+"|"
			             +map.get("FLAG").toString()+"|"
			             +"\n";
			logger.info("本次写入数据为:"+Line); 
	        writer.write(Line);
	        writer.flush();
		}
		writer.close();
	}catch (Exception e) {
		return "";
	}
	return fileName;
}

}
