package com.untech.util;

import org.apache.commons.dbcp.BasicDataSource;

public class MyBasicDataSource extends BasicDataSource {



 @Override
 public synchronized void setPassword(String password) {
  
  System.out.println("password-->"+password);
  //将密码解密
  DesEncrypt des=new DesEncrypt();
  des.getKey("0123456789ABCDEF");//生成密匙
  password= des.getDesString(password);// 把String 类型的密文解密
 // System.out.println("明文=" + password);
  super.setPassword(password);
 }
 
 /**
  * @param args
  */
 public static void main(String[] args) {
  // TODO Auto-generated method stub
  MyBasicDataSource mbds=new MyBasicDataSource();
  mbds.setPassword("ahrz@2014&*()");
  System.out.println(mbds.getPassword());
 }

}