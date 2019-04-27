package com.untech.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author wuxw
 * 
 */
public class DateUtils {

	/**
	 * 把一个Timestamp转换为字符串日期
	 * @param time
	 * @return
	 */
	public static String getDate(Timestamp time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(time);
	}
	
	/**
	 * 返回当前时间
	 * 格式 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrentTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	/**返回时间秒*/
	public static long getsubDate(String startdate,String endDate) {
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			Date start = sdf.parse(startdate);
			Date end = sdf.parse(endDate);
			long time = (end.getTime()-start.getTime())/1000;
			System.out.println(time/1000);
			return time/1000;
		}catch (Exception e) {
			e.printStackTrace();
			return 181;
		}
	}
	
	public static void main(String[] args) {
		try {
			getsubDate("20161223 16:00:01", "20161223 16:00:58");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
