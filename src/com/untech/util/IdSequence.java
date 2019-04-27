package com.untech.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IdSequence {
	
	public static String getMessgeId(String userNo,String type ){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String time = format.format(new Date());
		return userNo + time;
	}

}
