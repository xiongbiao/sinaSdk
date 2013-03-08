package examples;

import java.util.List;

import org.jredis.ClientRuntimeException;
import org.jredis.JRedis;
import org.jredis.ProviderException;
import org.jredis.RedisException;
import org.jredis.protocol.Command;
import org.jredis.ri.alphazero.JRedisClient;
import org.jredis.ri.alphazero.support.DefaultCodec;

import examples.bean.Classes;
import examples.bean.User;

public class FirstDemo {

	public static void main(String[] args) {
		String password = "";
		if (args.length > 0)
			password = args[0];
		new FirstDemo().run(password);
	}

	
	
	/**
	 *  
	 * @param password
	 */
	private void run(String password) {
		try {
			// JRedis jredis = new JRedisClient("192.168.182.129", 6379, "jredis", 0);
			// 创建jredis客户端。基本上的操作都是这个接口提供的
			JRedis jredis = new JRedisClient("127.0.0.1", 6379);
			jredis.ping();//
			int objcnt = 50000;
			System.out.format("Creating and saving %d Java objects to redis ...", objcnt);
			Classes c = new Classes("一班", 1);
			long time=System.currentTimeMillis();
			for (int i = 1; i < objcnt; i++) {
				// instance it
				User user = new User("我" + i, i);
				user.setClazz(c);
				// get the next available object id from our Redis counter using INCR command
				long id = jredis.incr("User::next_id");
				// we can bind it a unique key using map (Redis "String") semantics now
				String key = "objects::User::" + id;
				// voila: java object db
				jredis.set(key, user);
//				jredis.set
				// and lets add it to this set too since this is so much fun
				jredis.sadd("users", user);
			}
			long time1 =System.currentTimeMillis();
			System.out.format(" and done.\n" +" 时间 ： " + (time1-time) );
//		    String str = new String(jredis.get("report_active"));
//			System.out.println(str);
			
			List<User> members = DefaultCodec.decode(jredis.smembers("users"));
			for (User user : members) {
				if(user.getId()==1){
    				System.out.println("\nid:" + user.getId());
    				System.out.println("name:" + user.getName());
    				System.out.println("calss:" + user.getClazz().getName());
				}
			}
			long time2 =System.currentTimeMillis();
			System.out.format("读取数据 done.\n" +" 时间 ： " + (time2-time1) );
			
			jredis.quit();
		} catch (RedisException e) {
			e.printStackTrace();
			if (e.getCommand() == Command.PING) {
				System.out .format("I'll need that password!  Try again with password as command line arg for this program.\n");
			}
		} catch (ProviderException e) {
			e.printStackTrace();
			System.out.format("Oh no, an 'un-documented feature':  %s\nKindly report it.", e.getMessage());
		} catch (ClientRuntimeException e) {
			e.printStackTrace();
			System.out.format("%s\n", e.getMessage());
		}
		
		
	}
}
