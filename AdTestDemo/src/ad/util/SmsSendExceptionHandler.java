package ad.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ad.model.Mailinfo;

/**
 * 线程执行错误后，发送一份内部邮件给管理员，要求线程在创建之后必须设置 setUncaughtExceptionHandler
 * @author HAIQING
 *
 */
public class SmsSendExceptionHandler implements UncaughtExceptionHandler {
	
	
	public void uncaughtException(Thread t, Throwable e) {
		String threadname=t.getClass().getName();//获得线程名
		String subject="线程："+t.getName()+"抛出异常，请检查";
		StringWriter sw=new StringWriter();
		PrintWriter pw=new PrintWriter(sw);
		e.printStackTrace(pw);
		String msg=sw.toString();//拿到线程报错的详细信息
		String body="线程状态："+t.getState()+"。类路径："+t.getClass().getName()+"。异常详细信息："+sw.toString(); 
		System.err.println(sw.toString());
		FileUtil.writeLogtoFile("[ 错误日志 ]--"+sw.toString(), false);
		
		Mailinfo mailInfo = new Mailinfo();
		String toMail = "xiongbiao@jpush.cn";
		mailInfo.setToMail(toMail);
		mailInfo.setMailbody(sw.toString());
		mailInfo.setSubject(body);
		try {
			SendMail.fromMail(mailInfo);
		} catch (Exception e2) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		//以下代码是使用 反射机制 重新创建线程
//		try {
//			Class tempclass=Class.forName(t.getClass().getName());
//			t=(Thread)tempclass.newInstance();
//			SmsSendExceptionHandler smsHandler=new SmsSendExceptionHandler(); 
//			t.setUncaughtExceptionHandler(smsHandler);
//			t.start();
//			log.info(threadname+"。重新创建实例并执行！");
//		} catch (ClassNotFoundException e1) {
//			e1.printStackTrace();
//			log.error("线程类路径："+threadname+"未发现", e1);
//		} catch (InstantiationException e1) {
//			e1.printStackTrace();
//			log.error("线程类："+threadname+"创建失败", e1);
//		} catch (IllegalAccessException e1) {
//			e1.printStackTrace();
//			log.error("线程类："+threadname+"创建失败", e1);
//		}
	}
}