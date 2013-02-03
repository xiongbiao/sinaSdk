/**
 * 
 */
package com.kkpush.util;



import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
/**
 * @author xiongbiao
 *将实例对象转 map
 */
public class ObjAnalysis {

	public static Map ConvertObjToMap(Object obj){
		Map<String,Object> reMap = new HashMap<String,Object>();
		if (obj == null)
			return null;
		Field[] fields = obj.getClass().getDeclaredFields();
		try {
			for(int i=0;i<fields.length;i++){
				System.out.println(fields[i]);
				try {
					Field f = obj.getClass().getDeclaredField(fields[i].getName());
					f.setAccessible(true);
					Object o = f.get(obj);
					reMap.put(fields[i].getName(), o);
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reMap;
	}

	static class Admin {
		String cPassword;
		String cUsername;
		public String getCPassword() {
			return cPassword;
		}
		public void setCPassword(String password) {
			cPassword = password;
		}
		public String getCUsername() {
			return cUsername;
		}
		public void setCUsername(String username) {
			cUsername = username;
		}
	}

	public static void main(String[] args) {
		Admin a = new Admin();
		a.setCPassword("123456");
		a.setCUsername("熊彪");
		Map m = ConvertObjToMap(a);
		System.out.println(m.get("cUsername"));
		System.out.println(m);
	}
}