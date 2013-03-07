package com.jpush.tools.util;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.jpush.tools.model.Mailinfo;
 

public class SendMail {
	private MimeMessage mimeMsg; // MIME邮件对象
	private Session session; // 邮件会话对象
	private Properties props; // 系统属性
	private String username = ""; // smtp认证用户名和密码
	private String password = "";
	private int port = 25;
	private Multipart mp; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象
//	private static Logger logger = LoggerFactory.getLogger(SendMail.class);
	public SendMail(String smtp) {
		setSmtpHost(smtp);
		createMimeMessage();
	}
	public void setSmtpHost(String hostName) {
		if (props == null)
			props = System.getProperties(); // 获得系统属性对象
		props.put("mail.smtp.host", hostName); // 设置SMTP主机
		props.put("mail.smtp.port", port);
	}

	// 创建MIME邮件对象
	public boolean createMimeMessage() {
		try {
			session = Session.getDefaultInstance(props, null); // 获得邮件会话对象
			mimeMsg = new MimeMessage(session); // 创建MIME邮件对象
			mp = new MimeMultipart();
			return true;
		} catch (Exception e) {
			System.out.println("创建MIME邮件对象失败！" + e);
			return false;
		}
	}

	// 是否需要验证
	public void setNeedAuth(boolean need) {
		if (props == null)
			props = System.getProperties();
		if (need) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
	}

	// 传递发送者邮件用户名与密码
	public void setNamePass(String name, String pass) {
		username = name;
		password = pass;
	}

	// 设置主题
	public boolean setSubject(String mailSubject) {
		try {
			mimeMsg.setSubject(mailSubject);
			return true;
		} catch (Exception e) {
			System.out.println("设置邮件主题发生错误！");
			return false;
		}
	}

	// 设置邮件主体
	public boolean setBody(String mailBody) {
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent(
					"<meta http-equiv=Content-Type content=text/html; charset=gb2312>"
							+ mailBody, "text/html;charset=GB2312");
			mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
			System.out.println("设置邮件正文时发生错误！" + e);
			return false;
		}
	}

	// // 添加附件
	// public boolean addFileAffix(String filename) {
	// try {
	// BodyPart bp = new MimeBodyPart();
	// FileDataSource fileds = new FileDataSource(filename);
	// DataHandler dh = new DataHandler(fileds);
	// bp.setDataHandler(dh);
	// bp.setFileName(fileds.getName());
	// mp.addBodyPart(bp);
	// return true;
	// } catch (Exception e) {
	// System.out.println("增加邮件附件：" + filename + "发生错误！" + e);
	// return false;
	// }
	// }

	// 设置发信人
	public boolean setFrom(String from) {
		try {
			mimeMsg.setFrom(new InternetAddress(from));
			System.out.println("发信人 is " + from);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 设置收信人
	public boolean setTo(String to) {
		if (to == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress
					.parse(to));
			System.out.print("收信人 is " + to);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	// 复制邮件到收件人
	public boolean setCopyTo(String copyto) {
		if (copyto == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.CC,
					(Address[]) InternetAddress.parse(copyto));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 发送邮件
	public boolean sendout() throws MessagingException {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			System.out.println("正在发送邮件....");
			FileUtil.writeErLogtoFile("正在发送邮件....", true);
			Session mailSession = Session.getInstance(props, null);
			mailSession.setDebug(true);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"), username,
					password);
			transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
			System.out.println("发送邮件成功！");
			FileUtil.writeErLogtoFile("发送邮件成功！", true);
			transport.close();
			return true;
		} catch (Exception e) {
			System.out.println("邮件发送失败！" + e);
			FileUtil.writeErLogtoFile("邮件发送失败！" + e.getMessage(), true);
			return false;
		}
	}

	// 这是这个类的测试，建议先做这个测试再搭建其它的环境，所需要的包有：
	// activation.jar dsn.jar imap.jar mail.jar mailapi.jar pop3.jar
	// smtp.jar

	// public static void main(String[] args) throws MessagingException {
	//   
	// String mailbody = "测试测试";
	//   
	// SendMail themail = new SendMail("smtp.163.com");//发送者邮箱的类型
	// themail.setNeedAuth(true);
	//  
	// if (themail.setSubject("标题") == false) return; //主题
	// if(themail.setBody(mailbody) == false) return;//邮件内容
	// if(themail.setTo("uci**@163.com") == false) return; 接收者邮箱
	// if(themail.setFrom("fxf***@163.com") == false) return;//发送者邮箱
	// 注意邮箱不要使用你新注册的邮箱，因为pop功能还没开通
	// // * if(themail.addFileAffix("c:\\附件.text") == false) return;//先不发附件
	// themail.setNamePass("fxf3***", "hik***");//发送者邮箱用户，密码
	//  
	// if (themail.sendout() == false) return;
	// }
	//  
	
	public static void fromMailTable() throws  Exception{
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	
	public static void fromMail(Mailinfo mailInfo) throws Exception {
		try {
			
			  String smtp = SystemConfig.getProperty("mail.smtp", "smtp.qiye.163.com");//"smtp.qiye.163.com";
			  String username = SystemConfig.getProperty("mail.username", "service@jpush.cn");//"service@jpush.cn";
			  String pwd = SystemConfig.getProperty("mail.passWord", "1234qwer");//"1234qwer";
			
			  SendMail themail = new SendMail(smtp);
			  themail.setNamePass(username, pwd);//发送者的邮箱名，密码
			  themail.setNeedAuth(true);//是否要验证

			  System.out.println(smtp+"--"+username+"---"+pwd);
			  themail.setSubject(mailInfo.getSubject());//主题
			  String mailbody = mailInfo.getMailbody();//邮件内容也是激活用户的链接
			  themail.setBody(mailbody);
			  themail.setFrom(username);//发送者
			  themail.setTo(mailInfo.getToMail());//接收者
			if(themail.sendout()){
				System.out.println("成功");
				FileUtil.writeLogtoFile("[ "+mailInfo.getToMail()+" ]"+ "--发送成功", true);
			}else{
				System.out.println("失败");
				FileUtil.writeLogtoFile("[ "+mailInfo.getToMail()+" ]"+ "--发送失败", true);
			}
			System.out.println("进入发邮件");
			System.out.println("发已经结束");
		} catch (Exception e) {
//			e.printStackTrace();
			FileUtil.writeErLogtoFile("发送邮件失败 错误  ： "+e.getMessage(), true);
			throw e;
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		
		 try {
			 Mailinfo mailInfo =new Mailinfo();
				System.out.println("2--------");
				String toMail = "xiongbiao@oa.kktalk.cn";
				mailInfo.setToMail(toMail);
//				mailInfo.setMailbody("广告ID :   广告json详情 :  <br/>" );
				 String  con =	"";
				 mailInfo.setMailbody(con);
					mailInfo.setSubject("重置密码邮件－极光推送");
				System.out.println("begin--------");
				SendMail.fromMail(mailInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
}
