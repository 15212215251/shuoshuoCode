package com.untech.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.untech.util.Common_Const;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * MerchantController
 * @author cheny
 *  主要实现扫码支付对接
 * 
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Scope("prototype")
@Controller
@RequestMapping(value = "/api/download")
public class DownloadController {

	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(DownloadController.class.getName());
	
	/**
	 * dzfile 下载对账文件
	 * @return
	 * http://www.cnblogs.com/Scott007/p/3817285.html
	 * @throws Exception 
	 */
	@RequestMapping(value = "/dzfile")
	public void dzfile(HttpServletRequest request,HttpServletResponse response) throws Exception {
		System.out.println("Come on, baby .......");
		int BUFFER_SIZE = 4096;
		InputStream in = null;
		OutputStream out = null;
		try{
		    request.setCharacterEncoding("utf-8");  
		    ((ServletRequest) response).setCharacterEncoding("utf-8");  
		    response.setContentType("application/octet-stream");
		    String userName = request.getHeader("userName");
		    String passwd = request.getHeader("passwd");
		    String fileName = request.getHeader("fileName");
		    System.out.println("userName:" + userName);
		    System.out.println("passwd:" + passwd);
		    System.out.println("fileName:" + fileName);
		    ///home/appweb/dz/ 
		    //根据文件类型校验用户名和密码
//		    String[] fileSplit  = fileName.split("_");
//		    String feetype = fileSplit[2];
		    
		    //可以根据传递来的userName和passwd做进一步处理，比如验证请求是否合法等             
		    File file = new File(Common_Const.DZPATH+ "/" + fileName);
		    response.setContentLength((int) file.length());
		    response.setHeader("Accept-Ranges", "bytes");
		    int readLength = 0;
		    in = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
		    out = new BufferedOutputStream(response.getOutputStream());
		    
		    byte[] buffer = new byte[BUFFER_SIZE];
		    while ((readLength=in.read(buffer)) > 0) {
		        byte[] bytes = new byte[readLength];
		        System.arraycopy(buffer, 0, bytes, 0, readLength);
		        out.write(bytes);
		    }
		    out.flush();
		    response.addHeader("token", "success");
		}catch(Exception e){
		    e.printStackTrace();
		    response.addHeader("token", "fail");
		}finally {
		    if (in != null) {
		        try {
		            in.close();
		        } catch (IOException e) {
		        }
		    }
		    if (out != null) {
		        try {
		            out.close();
		        } catch (IOException e) {
		        }
		    }
		}

	}
	 
	
}
