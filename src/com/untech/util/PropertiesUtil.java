package com.untech.util;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

/**
 * 读取配置文件
 * @author David
 *
 */
public class PropertiesUtil {
	static PropertiesUtil pu;//创建对象pu
	private static Hashtable<String,Properties>	register=new Hashtable<String,Properties>();
	private PropertiesUtil(){
		super();
	}
	/**
	 * 取得PropertiesUtil的一个实例
	 */
	public static PropertiesUtil getInstance(){
		if(pu==null)
			pu=new PropertiesUtil();
		return pu;
	}
	/**
	 * 读取配置文件
	 */
	public Properties getProperties(String fileName){
		InputStream is=null;
		Properties p=null;
		try{
		p=(Properties)register.get(fileName);
		if(p==null){
			try{
				is=new FileInputStream(fileName);
			}catch(Exception e){
				if(fileName.startsWith("/"))
					is=PropertiesUtil.class.getResourceAsStream(fileName);
				else
					is=PropertiesUtil.class.getResourceAsStream("/"+fileName);
			}
			if(is==null){
				System.out.println("未找到名称为"+fileName+"的资源！");
			}
			p=new Properties();
			p.load(is);
			register.put(fileName, p);
			if(null!=is){
				is.close();
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return p;
	}
	public String getPropertyValue(String fileName,String strKey){
		Properties p=getProperties(fileName);
		try{
			return (String)p.getProperty(strKey);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
