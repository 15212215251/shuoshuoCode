package com.untech.util;

import java.net.*;
import java.io.*;
import org.apache.log4j.Logger;

public class SocketClientToo {
	static Logger log = Logger.getLogger(SocketClientToo.class.getName()); // 日志记录信息
	private String hostName;
	private int portNum;
	private int delaySecond; // 发文接收返回报文延时

	public SocketClientToo() {
		this.hostName = "59.203.96.27";//生产
		//this.hostName = "59.203.196.297";
		this.portNum = 8099;
		this.delaySecond = 60000;
	}

	private Socket getSocket() {
		Socket socket = null;
		try {
			socket = new Socket(hostName, portNum);
		} catch (UnknownHostException e) {
			System.out.println("-->未知的主机名:" + hostName + " 异常");
		} catch (IOException e) {
			System.out.println("-hostName=" + hostName + " portNum=" + portNum
					+ "---->IO异常错误" + e.getMessage());
		}
		return socket;
	}

	public String sendMessage(String strMessage) {
		log.info("strMessage=="+strMessage);
		//将发送字符串进行加密处理
		//strMessage = strMessage + MD5.encode(strMessage+"ydyh2015|")+"|";
		log.info("strMessage=md5 ="+strMessage);
		String str = "";
		String serverString = "";
		Socket socket;
		try {
			socket = getSocket();
			// socket.setKeepAlive(true);
			if (socket == null) { // 未能得到指定的Socket对象,Socket通讯为空
				return "0001";
			}
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			// log.info("---->发送报文="+strMessage);
			out.println(strMessage);
			out.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			long sendTime = System.currentTimeMillis();
			long receiveTime = System.currentTimeMillis();
			boolean received = false; // 成功接收报文
			boolean delayTooLong = false;
			serverString = null;
			while (!received && !delayTooLong) {
				if (socket.getInputStream().available() > 0) {
					// serverString = in.readLine();
					char tagChar[];
					tagChar = new char[20480];
					int len;
					String temp;
					String rev = "";
					if ((len = in.read(tagChar)) != -1) {
						temp = new String(tagChar, 0, len);
						rev += temp;
						temp = null;
					}
					serverString = rev;
				}
				receiveTime = System.currentTimeMillis();
				if (serverString != null)
					received = true; // 字符串不为空，接收成功
				if ((receiveTime - sendTime) > delaySecond)
					delayTooLong = true; // 接收等待时间过长，超时
			}
			in.close();
			out.close();
			str = serverString;
			if (delayTooLong)
				str = "2190"; // 超时标志为真，返回超时码
			if (!received)
				str = "2190";
			socket.close();
		} catch (UnknownHostException e) {
			log.error("---->出现未知主机错误! 主机信息=" + this.hostName + " 端口号="
					+ this.portNum + " 出错信息=" + e.getMessage());
			str = "2191";
			// System.exit(1);
		} catch (IOException e) {
			log.error("---->出现IO异常! 主机信息=" + this.hostName + " 端口号="
					+ this.portNum + " 出错信息=" + e.getMessage());
			e.printStackTrace();
			str = "2191";
		} catch (Exception e) {
			str = "2177";
			log.error("---->出现未知异常" + e.getMessage());
		} finally {
			socket = null;
			str.trim();
			log.info("--->返回的socket通讯字符串="+str);
			return str;
		}
	}
}