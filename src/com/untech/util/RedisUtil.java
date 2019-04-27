package com.untech.util;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class RedisUtil {
	
	public static boolean setRedis(Map<String,String> map){
		Jedis jedis =  null;
		try{
			jedis =  RedisFactory.getJedis();
			if(jedis == null ){
				return false;
			}
			String seqId = map.get("transactionNo");
			System.out.println("seqId==="+seqId);
			jedis.hmset(seqId, map);
			jedis.expire(seqId, 5*60); //设置5分钟失效
			RedisFactory.returnResource(jedis);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			RedisFactory.returnResource(jedis);
		}
	}
	
	
	//如果已经失效返回{} ，报错返回null
	public static Map<String,String> getRedis(String seqId){
		Jedis jedis = null;
		try{
			jedis =  RedisFactory.getJedis();
			if(jedis == null ){
				return null;
			}
			Map<String,String> map = jedis.hgetAll(seqId);//获取id
			System.out.println("==="+map);
			jedis.del(seqId);//用完就删除了
			return map;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}finally{
			RedisFactory.returnResource(jedis);
		}
	}
	
	//电费 01 水费02  话费05 交罚04  银行卡转入M01  积分兑换M02  奖励M03   
	public static String setRedis(String userno,String type,String payertime){
		String redisFlag  = Common_Const.REDISFLAG;
		if(!"true".equals(redisFlag)){
			return "2";
		}
		Jedis jedis =  null;
		try{
			jedis =  RedisFactory.getJedis();
			if(jedis == null ){
				return "0";
			}
			String value = jedis.get(userno+""+type);//获取id
			System.out.println(value+"===value");
			if(value != null && !"".equals(value)){
				return "1";
			}
			jedis.set(userno+""+type, payertime);
			jedis.expire(userno+""+type, 50); //设置50s失效
			return "2";
		}catch (Exception e) {
			e.printStackTrace();
			return "0";
		}finally{
			RedisFactory.returnResource(jedis);
		}
	}
	
	public static String getRedis(String userno,String type){
		Jedis jedis =  null;
		try{
			jedis =  RedisFactory.getJedis();
			if(jedis == null ){
				return null;
			}
			String value = jedis.get(userno+""+type);//获取id
			System.out.println("==="+value);
			if(value == null){
				return "";
			}else{
				return value;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}finally{
			RedisFactory.returnResource(jedis);
		}
	}
	//删除短信验证码，验证成功后删除
	public static void delRedis(String userno,String type){
		Jedis jedis =  null;
		try{
		    jedis =  RedisFactory.getJedis();
			if(jedis != null ){
				long id = jedis.del(userno+""+type);
				System.out.println("id======"+id);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			RedisFactory.returnResource(jedis);
		}
	}
	
	public static synchronized  boolean setRedisNx(String key){
		try{
			Jedis jedis =  RedisFactory.getJedis();
			if(jedis == null ){
				return false;
			}
			String value = jedis.get(key);//获取key
			System.out.println("==="+value);
			if(value == null){
				value = "0";
			} 
			value = (Integer.parseInt(value)+1)+"";
			jedis.set(key, value);
			if(Integer.parseInt(value) < 5){
				jedis.expire(key, 5*60); //设置15分钟失效
			}else{
				jedis.expire(key, 15*60); //设置15分钟失效
			}
			
			RedisFactory.returnResource(jedis);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//短信验证码设置
	public static boolean setSmsRedis(String userno,String phone,String type,String authCode){
		Jedis jedis = null;
		try{
			jedis =  RedisFactory.getJedis();
			if(jedis == null ){
				return false;
			}
			jedis.set(phone+""+type, authCode);
			jedis.expire(phone+""+type, 10*60); //设置5分钟失效
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			RedisFactory.returnResource(jedis);
		}
	}
	
	
	public static void main(String[] args) {
		
		//String authCode  = RedisUtil.getRedis("11", "333");
  	  Jedis jedis =  RedisFactory.getJedis();
  	  jedis.set("name","xinxin");//向key-->name中放入了value-->xinxin  
        System.out.println(jedis.get("name"));//执行结果：xinxin  
        RedisFactory.returnResource(jedis);
        System.out.println(jedis.get("name"));//执行结果：xinxin 
        
        Map<String,String> map = new HashMap<String, String>();
        map.put("sid", "sid111");
        map.put("userid","444444444444444444444");
        jedis.hmset("name2", map);
        jedis.expire("name2", 10);
        
        try {
			Thread.sleep(6000);
		  } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		System.out.println("boolean ==="+jedis.exists("name2"));
		Map<String,String> map2  = jedis.hgetAll("name2");
		System.out.println(map2);
		System.out.println(map2.get("userid"));
		jedis.del("name2");
		System.out.println("boole33an ==="+jedis.exists("name2"));
		Map<String,String> map3  = jedis.hgetAll("name2");
		System.out.println(map3);
		if(map3 == null || map3.get("id") == null){
			System.out.println("444");
		}
        
	}

}
