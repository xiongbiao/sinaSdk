/**
 * 
 */
package com.kkpush.util;

import java.util.Iterator;
import java.util.Map;

import redis.clients.jedis.Jedis;

/**
 * redis 操作
 * @author xiongbiao
 *
 */
public class RedisHelp {
      
    private static Jedis jedis = new Jedis("127.0.0.1", 6379);
	public static void  setData( Map<String, Object> pa){
	     Iterator it = pa.entrySet().iterator();   
	     while (it.hasNext())    
	     {   
	             Map.Entry pairs = (Map.Entry)it.next();   
	             jedis.set(pairs.getKey().toString(), pairs.getValue().toString());
	      }  
	     
	}
	/**
	 * 
	 */
   public static String getData(String keyWhere){
	  String value = jedis.get(keyWhere);
      return value;	  
   } 	
	
}
