package com.kkpush.account.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kkpush.account.domain.Developer;
import com.kkpush.account.service.DeveloperService;
import com.kkpush.model.Mailinfo;
import com.kkpush.systembridge.SessionBridge;
import com.kkpush.util.CommonConstants;
import com.kkpush.util.HttpUtil;
import com.kkpush.util.MD5;
import com.kkpush.util.SendMail;
import com.kkpush.util.SystemConfig;
import com.kkpush.util.TokenUtil;
import com.kkpush.util.VerificationCode;
import com.kkpush.util.WebInterface;
import com.kkpush.web.controller.PublicController;

@Controller
@RequestMapping("/developer")
public class DeveloperController extends PublicController{
	@Autowired
	DeveloperService developerService;
	private static Logger logger = LoggerFactory.getLogger(DeveloperController.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/***
	 * 获得验证码
	 * 
	 * @return
	 */
	@RequestMapping(value = "/random", method = RequestMethod.GET)
	public void getRandom(HttpServletRequest request, HttpServletResponse response){
		try {
			//禁止缓存
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "No-cache");
			response.setDateHeader("Expires", 0);
			// 指定生成的响应是图片
			response.setContentType("image/jpeg");
			int width = 100;
			int height = 30;
			String sRand = "" ;
			Map<String ,Object> reMap = VerificationCode.getRandom(width, height);
			sRand = reMap.get("sRand").toString(); 	
			BufferedImage image =  (BufferedImage)reMap.get("image");   //创建BufferedImage类的对象
			//将生成的验证码保存到Session中
			HttpSession session=request.getSession(true);
			session.setAttribute("randCheckCode",sRand);
			ImageIO.write(image,"JPEG",response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("验证码 random  err :" + e.toString()); 

		} 
	}



	@RequestMapping(value = "/loginout", method = RequestMethod.GET)
	public void loginOut(HttpServletRequest request, HttpServletResponse response){
		try {
			//禁止缓存
			Cookie[] cookies=request.getCookies();  
			try  
			{  
				for(int i=0;i<cookies.length;i++)    
				{  
					Cookie cookie = cookies[i];  
					cookie.setMaxAge(0);  
					cookie.setPath("/");//根据你创建cookie的路径进行填写      
					response.addCookie(cookie);  
				}  
			}catch(Exception ex)  
			{  
				logger.debug("清空Cookies发生异常！");  
			}   
			response.sendRedirect(request.getContextPath()+"/login.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("err :" + e.toString()); 
		} 
	}
	/**
	 * 新增
	 * @author shidong（这里之前存在bug:用户没登出，进行注册动作，将不会创建用户，而是更新了当前用户的信息。解决：将原来的saveOrUpdate方法分离，原来逻辑不变）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> save(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();

		String devName = request.getParameter("devName")==null?"":request.getParameter("devName");
		String password = request.getParameter("password")==null?"":request.getParameter("password");
		String contact = request.getParameter("contact");
		String email = request.getParameter("email");
		String qq = request.getParameter("QQ");
		String mobilePhone = request.getParameter("mobilePhone");
		String otherContact = request.getParameter("otherContact");
		String companyName = request.getParameter("company_name");

		//第三方跳转过来注册。
		String referer = request.getParameter("referer");
		// 新增要判断用户存在不 和 邮件
		Developer developer = new Developer(); 
		developer.setContact(contact);
		developer.setQq(qq);
		developer.setMobilePhone(mobilePhone);
		developer.setEmail(email);
		developer.setOtherContact(otherContact);
		developer.setCompanyName(companyName);
		try {
			if(devName.equals("")||password.equals("")){
				responseMap.put("method", "Create");
				responseMap.put("success", "false");
				responseMap.put("info", "消息填写不完整！");
				return responseMap;
			}
			password = MD5.MD5Encode(password).toUpperCase();
			developer.setDevName(devName);
			developer.setPassword(password);
			Date nowtime = new Date();
			String nowtimeStr = sdf.format(nowtime);

			developer.setRegTime(nowtimeStr);
			developer.setLastUpdateTime(nowtimeStr);
			String acCode = UUID.randomUUID().toString();
			developer.setActivationCode(acCode);
			if (developerService != null) {
				developerService.insertDeveloper(developer);
			}

			//获取最新用户
			Developer dev = developerService.isExists(developer);

			/**发送欢迎邮件**/
			logger.info("send email for register start>>>>>>>>");
			Mailinfo mailInfo = new Mailinfo();
			mailInfo.setToMail(dev.getEmail());
			String rmString = RandomStringUtils.randomAlphanumeric(10);
			String  con = getSuccessRegistEmail(rmString, dev);
			mailInfo.setMailbody(con);
			mailInfo.setSubject("注册开发者帐户邮件");
			SendMail.fromMail(mailInfo);

			params.put("devId", dev.getDevId());
			params.put("emailCode", rmString);
			params.put("emailFlag", 2);
			developerService.updateEmailCode(params);
			dev.setEmailFlag(2);
			logger.info("send email for register end>>>>>>>>");
			/*********** SendEmail END **************************/

			request.getSession().setAttribute("user", dev);
			responseMap.put("method", "Create");
			responseMap.put("success", "true");
			responseMap.put("info", "新增成功！");

			String tokenPass = UUID.randomUUID().toString().replace("-", "");
			dev.setLoginToken(tokenPass);
			dev.setTokenTime(nowtimeStr);
			//创建登录token
			developerService.updateUserToken(dev);

			if (null != referer && !"".equals(referer)) {
				Map<String, Object> _params = new HashMap<String, Object>();
				if (referer.length() > 200) {
					referer = referer.substring(0, 200);
				}
				_params.put("uid", dev.getDevId());
				_params.put("referer", referer);
				try {
					developerService.insertThirdSiteRegister(_params);
				} catch (Exception e) {
					e.printStackTrace();
					//数据收集不影响主业务
				}
			}
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("success", "false");
			responseMap.put("info",  e.getMessage());
			return responseMap;
		}
	}

	/**
	 * 修改用户信息
	 * @author shidong（这里之前存在bug:用户没登出，进行注册动作，将不会创建用户，而是更新了当前用户的信息。解决：将原来的saveOrUpdate方法分离，原来逻辑不变）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> updateProfile(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		if ((null == session) || (null == session.getAttribute("user"))) {
			responseMap.put("success", "false");
			responseMap.put("info",  "您还没有登陆！");
			return responseMap;
		}
		String devId = ((Developer)session.getAttribute("user")).getDevId().toString();
		String contact = request.getParameter("contact");
		String email = request.getParameter("email");
		String qq = request.getParameter("QQ");
		String mobilePhone = request.getParameter("mobilePhone");
		String otherContact = request.getParameter("otherContact");
		String companyName = request.getParameter("company_name");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("devId", devId);
		Developer developer = developerService.getDev(param);
		developer.setContact(contact);
		developer.setQq(qq);
		developer.setMobilePhone(mobilePhone);
		developer.setEmail(email);
		developer.setOtherContact(otherContact);
		developer.setCompanyName(companyName);
		try {
			developer.setDevId(Integer.valueOf(devId));
			developerService.updateDeveloper(developer);
			request.getSession().setAttribute("user", developer);
			responseMap.put("method", "Create");
			responseMap.put("success", "true");
			responseMap.put("info", "修改成功！");
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("success", "false");
			responseMap.put("info",  e.getMessage());
			return responseMap;
		}
	}


	/**
	 * 获得用户应用数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/userApp", method = RequestMethod.POST)
	public  @ResponseBody Map<String, Object> getUserApp(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Developer dev=(Developer)request.getSession().getAttribute("user");
		String devId = dev==null?"":dev.getDevId().toString();
		try { 
			if(!"".equals(devId)){
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("devId", devId);
				Map<String, Object> result = developerService.getDevAppCount(param);
				responseMap = getResponseMap("getUserApp",true,"获取应用数成功");
				responseMap.put("user_app", result);

			}else{
				responseMap = getResponseMap("getUserApp",false,"请登录");
			}
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			responseMap = getResponseMap("getUserApp",false,"服务器忙稍后再试……");
			return responseMap;
		}
	}



	/**
	 * 登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public  @ResponseBody Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String devName = request.getParameter("devName") != null ? request.getParameter("devName") : "";
		String password = request.getParameter("password");
		String r_randCheckCode = request.getParameter("randCheckCode");
		String isRemember = request.getParameter("isRemember");
		String loginToken = request.getParameter("loginToken");
		String saveCookie = request.getParameter("saveCookie");
		HttpSession session = request.getSession();

		try {
			if(isRemember==null || !"1".equals( isRemember)){
				String randCheckCode = session.getAttribute("randCheckCode")==null?null:session.getAttribute("randCheckCode").toString();
				//无验证码
				if(randCheckCode == null){
					responseMap = getResponseMap("login",false,"验证码过期 刷新验证码！");
					responseMap.put("input", "randCheckCode");
					return responseMap; 
				}
				//验证 验证码是否对
				if(r_randCheckCode == null ||  randCheckCode == null || !r_randCheckCode.toLowerCase().equals(randCheckCode.toLowerCase())){
					responseMap = getResponseMap("login",false,"输入验证码错误！");
					responseMap.put("input", "randCheckCode");
					return responseMap;
				} 
			}
			if (!devName.equals("") && !password.equals("")) {
				password = MD5.MD5Encode(password).toUpperCase();
				Developer developer = new Developer();
				developer.setDevName(devName);
				developer.setPassword(password);
				if(loginToken!=null)
					developer.setLoginToken(loginToken);

				Developer dev = developerService.isExists(developer);
				if (dev != null) {
					if( dev.getIsEnabled() == 1){
						responseMap = getResponseMap("login",false,"账户异常 联系客服！");
						return responseMap;						
					} 
					// 未激活 提示他激活用户
					//					if (dev.getIsActivation() == 0) {						
					//						responseMap = getResponseMap("login",false,"noActivation");
					//						return responseMap;	 
					//					}
					// 跳转到开发者应用管理中心 保存开发者到session里面

					session.setAttribute("user", dev); 

					Map<String, Object> param = new HashMap<String, Object>();

					param.put("contact", dev.getContact());
					param.put("username", dev.getDevName());
					param.put("devid", dev.getDevId());
					responseMap = getResponseMap("login",true,"success");
					//记住密码
					if(saveCookie!=null&&"on".equals(saveCookie)){
						String tokenPass = UUID.randomUUID().toString().replace("-", "");
						dev.setLoginToken(tokenPass);
						String tokenTime = sdf.format(new Date());
						dev.setTokenTime(tokenTime);
						//创建登录token
						developerService.updateUserToken(dev);

						responseMap.put("token", tokenPass); 
					}
				} else {
					responseMap = getResponseMap("login",false,"用户名或者密码错误！");
				}
				return responseMap;
			} else {
				responseMap = getResponseMap("login",false,"用户名或者密码不能为空！");
				return responseMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			responseMap = getResponseMap("login",false,"服务器忙稍后再试……");
			return responseMap;
		}
	}
	@RequestMapping(value = "/login_ser", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object>  login(Developer developer,HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> responseMap = null;

		if(developer==null) return null;
		Developer dev = developerService.isExists(developer);

		if (dev != null) {
			if(dev.getIsEnabled() == 1){
				responseMap = getResponseMap("login",false,"用户名或者密码不能为空！");
			}else{
				responseMap = getResponseMap("login",true,"success");
				responseMap.put("dev", dev);
			} 
		} else{
			responseMap = getResponseMap("login",false,"用户信息已经不存在，重新登陆！");
		}

		return responseMap;
	}



	@RequestMapping(value = "/upass_r", method = RequestMethod.POST)
	public  @ResponseBody Map<String, Object> upass_r(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();

		try {
			Map<String, Object> param = requestToMap(request);
			//获得临时密码
			String password=developerService.getTempPass(param);
			logger.debug(" password : " + password);
			if(password!=null&&!password.equals("")){
				String token=param.get("token")+"";
				String email =  param.get("email").toString();
				logger.debug("email : "+email);
				//验证tocak
				boolean  isoktoken = TokenUtil.validToken( password,email, token.toString());
				//选择跳转页面
				if(isoktoken){
					Developer dev = new Developer();
					dev.setEmail(email);
					dev.setPassword(password);
					dev = developerService.isExistsR(dev);  
					String devId = dev==null?"":dev.getDevId().toString();
					String r_randCheckCode = param.get("randCheckCode")+"";
					logger.debug("devId: " +devId +"r_randCheckCode :" + r_randCheckCode);
					String newPass = param.get("newPass").toString();
					newPass = MD5.MD5Encode(newPass).toUpperCase();
					if(!devId.equals("")){
						String randCheckCode =request.getSession().getAttribute("randCheckCode")==null?null:request.getSession().getAttribute("randCheckCode").toString();
						//是否开启 验证码 功能的验证
						boolean isCode = false;

						if(isCode == true){
							//无验证码
							if(randCheckCode == null){
								responseMap.put("method", "upass_r");
								responseMap.put("input", "randCheckCode");
								responseMap.put("success", "false");
								responseMap.put("info", "验证码过期 刷新验证码！"); 
								return responseMap; 
							}
							//验证 验证码是否对
							if(r_randCheckCode == null ||  randCheckCode == null || !r_randCheckCode.toLowerCase().equals(randCheckCode.toLowerCase())){
								responseMap.put("method", "upass_r");
								responseMap.put("input", "randCheckCode");
								responseMap.put("success", "false");
								responseMap.put("info", "输入验证码错误！"); 
								return responseMap;
							} 
						}
						logger.debug("newPass: " +newPass +"r_randCheckCode :" + r_randCheckCode);
						if (!"".equals(newPass.trim())) {
							param.put("devId", devId);
							param.put("newPass", newPass);
							developerService.updatePass(param);
							//把临时密码删除
							//								param.put("tempPass", "");
							//								developerService.updateTempPass(param);
							responseMap.put("method", "upass_r");
							responseMap.put("success", "true");
							responseMap.put("info", "修改成功！");
							request.getSession().setAttribute("user", dev); 
							return responseMap;
						}else{
							responseMap.put("method", "upass_r");
							responseMap.put("success", "false");
							responseMap.put("info", "请认真填写！");
							return responseMap;
						}
					}else{
						responseMap.put("method", "upass_r");
						responseMap.put("success", "false");
						responseMap.put("info", "err:101 访问出错 请求失败！");
						logger.debug("upass_r return err:101   不存在用户");
					}
					return null;
				}else{
					//response.sendRedirect(contextPath+"/login_r.jsp");
					responseMap.put("method", "upass_r");
					responseMap.put("success", "false");
					responseMap.put("info", "err:100 访问出错 请求失败！");
					logger.debug("upass_r return  err：100 token 校验失败");
				} 
			}else{
				responseMap.put("method", "upass_r");
				responseMap.put("success", "false");
				responseMap.put("info", " 此链接失效 ！");
			}
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("method", "upass_r");
			responseMap.put("success", "false");
			responseMap.put("info",  "系统繁忙 稍后再试……");
			return responseMap;
		}
	}



	/**
	 * 找回密码登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */ 
	@RequestMapping(value = "/login_r", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> loginR(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			Map<String, Object> param =  requestToMap(request);
			//获得临时密码
			String password=developerService.getTempPass(param);
			logger.debug("password : "+password);
			if(password!=null&&!password.equals("")){
				String token=param.get("token")+"";
				String email =  param.get("em").toString();
				logger.debug("email : "+email+" password: "+password);
				//验证tocak
				boolean  isoktoken = TokenUtil.validToken( password,email, token.toString());
				logger.debug(""+isoktoken);
				//选择跳转页面
				String contextPath=request.getContextPath();// 获得项目名称
				if(isoktoken){
					Developer dev = new Developer();
					dev.setEmail(email);
					dev.setPassword(password);
					dev = developerService.isExistsR(dev);  
					//账户被禁用
					if(  dev.getIsEnabled() == 1){
						//						responseMap.put("method", "login");
						//						responseMap.put("success", "false");
						//						responseMap.put("info", "账户异常 联系客服！");
					} 
					// 未激活 提示他激活用户
					if (dev.getIsActivation() == 0) {						
						//						responseMap.put("method", "login");
						//						responseMap.put("success", "false");
						//						responseMap.put("info", "noActivation");
					}
					// 跳转到开发者应用管理中心 保存开发者到session里面
					else {
						logger.debug("devid" + dev.getDevId() );
					}
					request.getSession().setAttribute("user", dev); 
					logger.debug("devid" + dev.getDevId() );
					response.sendRedirect(contextPath+"/login_r.jsp?");
					return null;
				}else{
					responseMap.put("method", "login");
					responseMap.put("success", "false");
					responseMap.put("info", "token er ！");
				} 
			}else{
				responseMap.put("method", "login");
				responseMap.put("success", "false");
				responseMap.put("info", "Connection failure ！");
			}
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			responseMap.put("info", " 服务器忙 稍后再试……");
			return responseMap;
		}
	}
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setAttribute("message", "测试");


		return new ModelAndView("dev/test");
	}


	/**
	 * 检查用户名是否存在
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/isExistsName", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> isExistsName(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String id = request.getParameter("clientid") != null ? request.getParameter("clientid") : "";
		String devName = request.getParameter("devName");
		try {
			if (id.equals("devName")) {
				responseMap.put("devName", devName);
				if (developerService.isExistsName(responseMap)) {
					responseMap.put("method", "isExistsName");
					responseMap.put("success", "fasle");
					responseMap.put("info", "用户名存在！");
					return responseMap;
				}else{
					responseMap.put("method", "isExistsName");
					responseMap.put("success", "true");
					responseMap.put("info", "用户名可用");
				}
			}else{
				responseMap.put("method", "isExistsName");
				responseMap.put("success", "false");
				responseMap.put("info", "参数错误");
			}
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("method", "isExistsName");
			responseMap.put("success", "false");
			responseMap.put("info", "服务器忙 请稍后再试!");
			return responseMap;
		}
	}

	/**
	 * 检查邮件是否存在
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/isExistsEmail", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> isExistsEmail(HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, Object> responseMap = new HashMap<String, Object>();
		String id = request.getParameter("clientid") != null ? request.getParameter("clientid") : "";
		String email = request.getParameter("email");

		try {
			if (id.equals("email")) {

				String devId = request.getSession().getAttribute("user")==null ? null : ((Developer)request.getSession().getAttribute("user")).getDevId()+"";

				String isUpdate = request.getParameter("isUpdate") ;
				Map<String, Object> param =new HashMap<String, Object>();
				param.put("email", email);

				if (developerService.isExistsEmail(param)) {
					logger.debug("----------------------存在------");
					boolean b_update=(devId!=null&&isUpdate!=null&&"1".equals(isUpdate));
					if(b_update){
						param.put("devId", devId);
						if(!developerService.isExistsEmail(param)){
							responseMap.put("method", "isExistsEmail");
							responseMap.put("success", "false");
							responseMap.put("info", "邮箱已经被注册了！");
							return responseMap;
						}
					}else{
						responseMap.put("method", "isExistsEmail");
						responseMap.put("success", "false");
						responseMap.put("info", "邮箱已经被注册了！");
						return responseMap;
					}
				}else{
					logger.debug("2-------"+"isExistsEmail 不存在 可以 ");
				}
			}
			responseMap.put("method", "isExistsEmail");
			responseMap.put("success", "true");
			responseMap.put("info", "邮箱可用！");
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("info", e.getClass() + ":" + e.getMessage());
			return responseMap;
		}
	}

	/**
	 * 用户激活
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/activate", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> Activate(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		String name = request.getParameter("name") != null ? request.getParameter("name") : "";
		String code = request.getParameter("code");
		try {
			if (!name.equals("")) {
				param.put("name", name);
				param.put("code", code);
				if (developerService.Activate(param) >0) {
					developerService.updateActivate(param);
					responseMap.put("method", "activate");
					responseMap.put("success", "true");
					responseMap.put("info", "激活成功！");
					String contextPath=request.getContextPath();// 获得项目名称
					response.sendRedirect(contextPath+"/activate.jsp"); 
					return responseMap;
				}else{
					responseMap.put("method", "activate");
					responseMap.put("success", "false");
					responseMap.put("info", "激活失败！");
				}
			}else{
				responseMap.put("method", "activate");
				responseMap.put("success", "false");
				responseMap.put("info", "激活失败！");
			}
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("method", "activate");
			responseMap.put("success", "false");
			responseMap.put("info","系统繁忙 稍后再试……");
			return responseMap;
		}
	}

	/**
	 *  修改密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/UpdatePass", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> UpdatePass(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String oldPass = request.getParameter("oldPass") != null ? request.getParameter("oldPass") : "";
		String newPass = request.getParameter("newPass");
		oldPass = MD5.MD5Encode(oldPass).toUpperCase();
		newPass = MD5.MD5Encode(newPass).toUpperCase();
		try {
			Developer dev=(Developer)request.getSession().getAttribute("user");
			String devId = dev==null?"":dev.getDevId().toString();
			if(!devId.equals("")){
				if (!oldPass.equals("")&&!"".equals(newPass.trim())) {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("oldPass", oldPass);
					param.put("devId", devId);
					if(!developerService.isExistsPass(param)){
						responseMap.put("method", "UpdatePass");
						responseMap.put("success", "false");
						responseMap.put("info", "原密码不正确！");
						return responseMap;
					}else{
						param.put("newPass", newPass);
						developerService.updatePass(param);
						responseMap.put("method", "UpdatePass");
						responseMap.put("success", "true");
						responseMap.put("info", "修改成功！");
						return responseMap;
					}
				}else{
					responseMap.put("method", "UpdatePass");
					responseMap.put("success", "false");
					responseMap.put("info", "请认真填写！");
					return responseMap;
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("method", "UpdatePass");
			responseMap.put("success", "false");
			responseMap.put("info","系统繁忙 稍后再试……");
			return responseMap;
		}
	}


	/**
	 *  修改邮箱
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/UpdateEmail", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> UpdateEmail(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			Developer dev=(Developer)request.getSession().getAttribute("user");
			String devId = dev==null?"":dev.getDevId().toString();

			if(!devId.equals("")){
				Map<String , Object> param =  requestToMap(request);
				String email = param.get("email") != null ? param.get("email").toString() : "";
				boolean isEmail= developerService.isExistsEmail(param);
				if(isEmail){
					responseMap.put("method", "UpdateEmail");
					responseMap.put("success", "false");
					responseMap.put("info","该邮箱已被占用，请更换邮箱！");
					return responseMap;
				}

				if (!"".equals(email.trim())) {

					//修改email，修改邮箱认证状态
					param.put("devId", devId);
					param.put("emailFlag", 0);
					developerService.updateEmailFlag(param);
					developerService.updateEmail(param);

					//从新设置session信息，为了跟QA系统信息同步
					dev.setEmailFlag(0);
					dev.setEmail(email);
					request.getSession().setAttribute("user", dev);

					responseMap.put("method", "UpdateEmail");
					responseMap.put("success", "true");
					responseMap.put("info", "修改成功！");
					return responseMap;
				}else{
					responseMap.put("method", "UpdateEmail");
					responseMap.put("success", "false");
					responseMap.put("info", "请认真填写！");
					return responseMap;
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("method", "UpdateEmail");
			responseMap.put("success", "false");
			responseMap.put("info","系统繁忙 稍后再试……");
			return responseMap;
		}
	}

	/**
	 * 修改提现密码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/UpdateTxPass", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> UpdateTxPass(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String oldPass = request.getParameter("oldPass") != null ? request.getParameter("oldPass") : "";
		String newPass = request.getParameter("newPass");
		try {
			Developer dev=(Developer)request.getSession().getAttribute("user");
			String devId = dev==null?"":dev.getDevId().toString();
			if(!devId.equals("")){
				if (!oldPass.equals("")&&!"".equals(newPass.trim())) {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("oldPass", oldPass);
					param.put("devId", devId);
					if(!developerService.isExistsPass(param)){
						responseMap.put("method", "UpdatePass");
						responseMap.put("success", "false");
						responseMap.put("info", "密码不正确！");
						return responseMap;
					}else{
						param.put("newPass", newPass);
						developerService.updateTxPass(param);
						responseMap.put("method", "UpdatePass");
						responseMap.put("success", "true");
						responseMap.put("info", "修改成功！");
						return responseMap;
					}
				}else{
					responseMap.put("method", "UpdatePass");
					responseMap.put("success", "false");
					responseMap.put("info", "请认真填写！");
					return responseMap;
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("method", "UpdatePass");
			responseMap.put("success", "false");
			responseMap.put("info","系统繁忙 稍后再试……");
			return responseMap;
		}
	}


	/**
	 * 找回密码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/fpass", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> forgotpassword(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			Map<String, Object> param=  requestToMap(request);
			boolean isEmail= developerService.isExistsEmail(param);
			//存在邮箱 发邮件 发邮件
			if(isEmail){
				Random random = new Random();      //实例化一个Random对象
				String newPass="";
				int itmp=0;
				for(int i=0;i<8;i++){
					if(random.nextInt(2)==1){
						itmp=random.nextInt(26)+65; //生成A~Z的字母
					}else{
						itmp=random.nextInt(10)+48; //生成0~9的数字
					}
					char ctmp=(char)itmp;
					newPass+=String.valueOf(ctmp);
				}
				//打印临时密码
				logger.debug("newPass :  " +newPass);
				//插入临时密码
				param.put("tempPass", newPass);
				developerService.updateTempPass(param);
				//发邮件到用户
				Mailinfo mailInfo = new Mailinfo();
				mailInfo.setToMail(param.get("email")+"");
				String email=param.get("email").toString();
				String token = TokenUtil.generateToken(newPass, email);


				String  con =	getEmail(SystemConfig.getProperty("httpUrl", ""),token,email);

				mailInfo.setMailbody(con);
				mailInfo.setSubject("重置密码邮件－极光推送");
				SendMail.fromMail(mailInfo);
				responseMap.put("info", "重置密码邮件已经发您邮箱 请查收邮件");
				responseMap.put("success", "true");
			}//不存在 
			else{
				responseMap.put("info", "邮箱不正确");
				responseMap.put("success", "false");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			responseMap.put("method", "fpass");
			responseMap.put("success", "false");
			responseMap.put("info","系统繁忙 稍后再试……");
			responseMap.put("success", "false");
		} 
		return responseMap;
	}




	/**
	 *   
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getdev", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getDev(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			Developer dev=(Developer)request.getSession().getAttribute("user");
			String devId = dev==null?"":dev.getDevId().toString();
			if(!devId.equals("")){ 
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("devId", devId);
				responseMap.put("method", "getdev");
				responseMap.put("success", "true");
				responseMap.put("info", developerService.getDev(param));
			} 
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("e : " +  e.toString());
			responseMap.put("info", "系统繁忙……");
			return responseMap;
		}
	}


	/**
	 * 
	 * @desc  发送邮箱认证
	 * @param request
	 * @param response
	 * @return
	 * @author zengzhiwu
	 * @date 2012-11-15
	 */
	@RequestMapping(value = "/sendValidatEmail", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> sendValidateEmail(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		final Map<String, Object> params = new HashMap<String, Object>();
		final HttpSession session = request.getSession();


		try {
			final Developer dev=(Developer)request.getSession().getAttribute("user");
			if(dev == null){
				responseMap.put("info", "登陆信息已过期，请从系登陆后再操作！");
				responseMap.put("success", "false");
				responseMap.put("loginout", "true");
				return responseMap;
			}
			String devId = dev.getDevId().toString();
			params.put("devId", Integer.parseInt(devId));

			logger.info(devId+"  call sendValidatEmail email ("+dev.getEmail()+") send..............");
			if(dev.getEmailFlag() == 0 || dev.getEmailFlag() == 2){

				new Thread(){
					public void run(){
						//发邮件到用户
						Mailinfo mailInfo = new Mailinfo();
						mailInfo.setToMail(dev.getEmail());
						String rmString = RandomStringUtils.randomAlphanumeric(30);
						String  con = validateEmail(rmString, dev.getEmail());
						mailInfo.setMailbody(con);
						mailInfo.setSubject("极光推送开发者帐户邮箱验证");
						try {
							SendMail.fromMail(mailInfo);
						} catch (Exception e) {
							logger.info(" send validate email error......");
							e.printStackTrace();
						}

						//插入邮箱认证码
						params.put("emailCode", rmString);
						params.put("emailFlag", 2);
						developerService.updateEmailCode(params);
						dev.setEmailFlag(2);
						session.setAttribute("user",dev);
					}
				}.start();

				responseMap.put("info", "认证信息已发到您的邮箱， 请查收邮件！");
				responseMap.put("success", "true");
				logger.info(devId+" call sendValidatEmail email ("+dev.getEmail()+") sucess end!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("e : " +  e.toString());
			responseMap.put("info", "系统繁忙,稍后再试……");
			responseMap.put("success", "false");
			return responseMap;
		}

		return responseMap;
	}


	/**
	 * 
	 * @desc  发送邮箱认证
	 * @param request
	 * @param response
	 * @return
	 * @author zengzhiwu
	 * @date 2012-11-15
	 */
	@RequestMapping(value = "/validateEmail", method = RequestMethod.GET)
	public ModelAndView validateEmail(HttpServletRequest request,HttpServletResponse response) {

		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		ModelAndView mView = new ModelAndView("/dev/sus_email");

		try {

			String rms = request.getParameter("rms");//ramdom code
			String em = request.getParameter("em");  //email
			params.put("email", em);
			Developer dev = developerService.getAllDevById(params);

			logger.debug(rms +"_"+em+" "+dev.getEmailCode()+"_"+dev.getEmail());
			if(dev.getEmailFlag() == 2){
				if(rms.equals(dev.getEmailCode()) && em.equals(dev.getEmail())){
					params.put("emailFlag",1);
					params.put("devId", dev.getDevId());
					developerService.updateEmailFlag(params);

					dev.setEmailFlag(1);
					request.getSession().setAttribute("user", dev);

					responseMap.put("info", "邮箱认证成功！");
					responseMap.put("success", "true");
				}else{
					responseMap.put("info", "认证出错，请从新认证");
					responseMap.put("success", "true");
				}
			}else if(dev.getEmailFlag() == 1){
				responseMap.put("info", "您的邮箱已经被认证过！");
				responseMap.put("success", "true");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("e : " +  e.toString());
			responseMap.put("info", "系统繁忙,稍后再试……");
			responseMap.put("success", "false");
			mView.addAllObjects(responseMap);
			return mView;
		}

		return mView.addAllObjects(responseMap);
	}



	/**
	 * 
	 * @desc  修改用户基本信息
	 * @param request
	 * @param response
	 * @return
	 * @author zengzhiwu
	 * @date 2012-11-19
	 */
	@RequestMapping(value = "/updateBase", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> updateBaseInfo(HttpServletRequest request,HttpServletResponse response) {

		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		String contact = request.getParameter("contant");
		String qq = request.getParameter("qq");
		String mobilePhone = request.getParameter("mobilePhone");
		String companyName = request.getParameter("companyName");

		try {
			Developer dev=(Developer)request.getSession().getAttribute("user");
			String devId = dev==null?"":dev.getDevId().toString();
			logger.info("call updateBaseInfo devId = "+devId );

			if(!devId.equals("")){
				if((contact == null || contact.trim().equals("")) || (mobilePhone == null || mobilePhone.trim().equals(""))){
					responseMap.put("method", "updateBase");
					responseMap.put("success", "false");
					responseMap.put("info","填写信息有误");
					return responseMap;
				}

				if(contact.equals(dev.getContact()) && qq.equals(dev.getQq())
						&& mobilePhone.equals(dev.getMobilePhone()) && companyName.equals(dev.getCompanyName())){
					responseMap.put("method", "updateBase");
					responseMap.put("success", "true");
					responseMap.put("info","没有修改任何信息");
					return responseMap;
				}

				params.put("devId", devId);
				params.put("contact", contact);
				params.put("qq", qq);
				params.put("mobilePhone", mobilePhone);
				params.put("companyName", companyName);
				developerService.updateDevBase(params);

				dev.setContact(contact);
				dev.setQq(qq);
				dev.setMobilePhone(mobilePhone);
				dev.setCompanyName(companyName);
				request.getSession().setAttribute("user",dev);

				responseMap.put("method", "updateBase");
				responseMap.put("success", "true");
				responseMap.put("info","修改信息成功！");
				return responseMap;

			}
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("method", "UpdatePass");
			responseMap.put("success", "false");
			responseMap.put("info","系统繁忙 稍后再试……");
			return responseMap;
		}
	}



	/**
	 * 
	 * @desc  q2a自动登录
	 * @param request
	 * @param response
	 * @return
	 * @author zengzhiwu
	 * @date 2012-11-19
	 */
	@RequestMapping(value = "/q2alogin", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> q2aValidateLogin(HttpServletRequest request,HttpServletResponse response) {

		Map<String, Object> responseMap = new HashMap<String, Object>();
		String devName = request.getParameter("devName");
		String loginToken = request.getParameter("loginToken");
		String autoStatus = request.getParameter("autoStatus");

		logger.debug("autoStatus : "  + autoStatus);

		if (autoStatus!=null&&autoStatus.equals("1")) {
			if (devName != null && loginToken != null && devName.length() > 0 && loginToken.length() > 0) {
				logger.debug("auto login...............");
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("devName", devName);
				param.put("loginToken", loginToken);
				param.put("isRemember", 1);

				Developer developer = new Developer();
				developer.setDevName(devName);
				developer.setLoginToken(loginToken);
				developer = WebInterface.PushLogin(param);

				if (developer!=null) {
					// 将该user放入到session中  
					request.getSession().setAttribute( CommonConstants.SESSION_USER, developer); 
					responseMap.put("data","success");
					logger.info(">>>>>> user validate success.auto login.");

				} else{ 
					responseMap.put("data","error");
					logger.info(">>>>>> user info error.");
				}
			}

		}else{
			responseMap.put("data","error");
			logger.info(">>>>>> user info so little,not auto logon.");
		}

		return responseMap;

	}



	/**
	 * 
	 * @desc 预览资料，从数据库查，从新设置到session中，保证最新信息
	 * @param request
	 * @param response
	 * @return
	 * @author zengzhiwu
	 * @date 2012-11-29
	 */
	@RequestMapping("/view_account")
	public ModelAndView getUserInfo(HttpServletRequest request,HttpServletResponse response) {

		Map<String, Object> params = new HashMap<String, Object>();
		HttpSession session = request.getSession();

		Developer developer = (Developer)session.getAttribute("user");
		params.put("devId",developer.getDevId());
		developer = developerService.getAllDevById(params);
		session.setAttribute("user",developer);

		ModelAndView mv = new ModelAndView("dev/account","dev",developer);
		return mv;
	}
	
	public static void main(String[] args) {
		/*	//截取url 
		Pattern pattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:]+"); 
		Matcher matcher = pattern.matcher("http://kktalk.cn/vel/callback"); 
		StringBuffer buffer = new StringBuffer(); 
		while(matcher.find()){              
		    buffer.append(matcher.group());        
		    buffer.append("\r\n");              
		System.out.println(buffer.toString()); 
		} 
		 */




		String url = "http://www.kktalk.net/ap/callback?uri=dsdf/sdf";
		//System.out.println(url.substring(5,5+2));
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i<url.length(); i++){
			if(i > 2) break;
			int index = url.indexOf("/");
			System.out.println(index);
			if(index == -1 && i == 2){
				buffer.append(url).append("/");
			}else{
				buffer.append(url.substring(0, index+1));
			}
			String netxUrl = url.substring(index+1);
			url = netxUrl;
			System.err.println(url);
		}
		System.out.println("over url :"+buffer.toString());

		/*Pattern p = Pattern.compile("?<=(?:://\\w+\\.)?)(?:\\w+\\.)(?:com\\.cn|net\\.cn|org\\.cn|com|net|org|cn|biz|info|cc|tv)",Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(url);
		if(matcher.find()){

			System.out.println(matcher.group());
		}*/


	}


}
