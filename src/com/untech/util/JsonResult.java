package com.untech.util;

/**
 * 结果
 * 
 * @author wuxw
 * 
 */
public class JsonResult {
	private String resCode;
	private String resMsg;
	private Object data;

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}


}