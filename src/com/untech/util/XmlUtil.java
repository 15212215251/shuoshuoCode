package com.untech.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;




/**
 * @author xuhui
 * @time 20150505
 * @desc 将xml转为Map对象
 * @see from app to Payserver 
 * @ method Dom2Map
 * @ DataFormat {RequestBody={controlData={certId=44444, userNo=ZT12345}}}
 * */
public class XmlUtil  {
	private static Logger logger = Logger.getLogger(XmlUtil.class.getName()); 
	

	public static Map<String, Object> Dom2MapBank(String xml) throws DocumentException{
		logger.info("xml=="+xml);
		Document doc=  DocumentHelper.parseText(xml);
		Map<String, Object> map = new HashMap<String, Object>();
		if(doc == null){
			throw new DocumentException() ;
		}
		Element root = doc.getRootElement();
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			//System.out.println(e.getName());
			List list = e.elements();
			if(list.size() > 0){
				map.put(e.getName(), Dom2Map(e));
			}else{
				map.put(e.getName(), e.getText()== null ?"":e.getText().trim());
			}
		}
		return map;
	}
	

	public static Map Dom2Map(Element e){
		Map	map = new HashMap();
		List list = e.elements();
		if(list.size() > 0){
			for (int i = 0;i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List mapList = new ArrayList();
				
				if(iter.elements().size() > 0){
					Map m = Dom2Map(iter);
					if(map.get(iter.getName()) != null){
						Object obj = map.get(iter.getName());
						if(!obj.getClass().getName().equals("java.util.ArrayList")){
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if(obj.getClass().getName().equals("java.util.ArrayList")){
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					}else
						map.put(iter.getName(), m);
				}
				else{
					if(map.get(iter.getName()) != null){
						Object obj = map.get(iter.getName());
						if(!obj.getClass().getName().equals("java.util.ArrayList")){
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if(obj.getClass().getName().equals("java.util.ArrayList")){
							mapList = (List) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
					}else
						map.put(iter.getName(), iter.getText() == null?"":iter.getText().trim());
				}
			}
		}else
			map.put(e.getName(), e.getText()== null?"":e.getText().trim());
		return map;
	}
    
	
	/**
	 * 安徽农金快捷支付组装xml
	 * @param map
	 * @param methodName
	 * @param mId
	 * @param id
	 * @return
	 */
	public  static String Map2xmlQuick(Map<String,Object> map,String methodName,String mId,String id){
		map.put("certId","");
		StringBuffer xml= new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><MessageSuit>" +
				"<Message id=\""+mId+"\"><"+methodName+" id=\""+id+"\">");
		for(Map.Entry<String, Object> entry: map.entrySet()) {
			xml.append("<"+entry.getKey()+">"+entry.getValue()+"</"+entry.getKey()+">");
		}
		xml.append("</"+methodName+"></Message></MessageSuit>");
		return xml.toString();
		/*String xml2  = xml.toString().replaceAll("<certId></certId>", "");
		EncryPt pt  = new EncryPt();
		String sign  = pt.sign(xml2);
		logger.info(xml +"====="+sign);
		return  xml.toString().replaceAll("<certId></certId>","<certId>"+sign+"</certId>");*/
	}
	
	
	public static String MapInstance(String xml,String key,Object obj){
		 if(obj instanceof Map){ 
			Map<String,Object> mp=(Map)obj;
			for(Map.Entry<String, Object> entry: mp.entrySet()) {
				 System.out.print(entry.getKey() + ":" + entry.getValue()+ "\t");
				 xml=MapInstance(xml,entry.getKey(),entry.getValue());
				}
		  }else{
			  xml+="<"+key+">"+obj.toString()+"</"+key+">";
			  System.err.println("这不是个MAP对象:"+obj);
		  }
		return xml;
	}
	

}

