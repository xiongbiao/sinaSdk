package com.kkpush.app.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.kkpush.account.domain.Developer;
import com.kkpush.app.domain.App;
import com.kkpush.app.service.AppService;
import com.kkpush.util.Def;
import com.kkpush.util.FileHelp;
import com.kkpush.util.IosCVException;
import com.kkpush.util.MD5;
import com.kkpush.util.SystemConfig;
import com.kkpush.util.WebInterface;
import com.kkpush.web.controller.PublicController;

@Controller
@RequestMapping("/app")
public class AppController extends PublicController{
	@Autowired
	AppService appService;
	private static Logger logger = LoggerFactory.getLogger(AppController.class);

	@RequestMapping(value = "/saveApp", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateApp(HttpServletRequest request, HttpServletResponse res) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		ModelAndView mav = new ModelAndView("app/new");

		logger.debug("saveOrUpdateApp  param : "+getMapToString(requestToMap(request)));

		//存在优化
		String appId = request.getParameter("appId") != null ? request.getParameter("appId") : "";
		String appName = request.getParameter("appName");
		Developer dev=(Developer)request.getSession().getAttribute("user");
		String devId = dev==null?"":dev.getDevId().toString();
		String appPackage = request.getParameter("appPackage");
		String appKey = request.getParameter("appKey");
		String appTypeId = request.getParameter("appTypeId");
		String appDescription = request.getParameter("appDescription");
		//		String appStage = request.getParameter("appStage")!=null?"0":"1";
		String appStage = request.getParameter("appStage") ;
		int stage =Integer.valueOf(appStage==null?"0":appStage);

		String certificatePass = request.getParameter("certificatePass");
		String platform  = "";
		int isPushAd = 0; 
		if(null!=request.getParameter("isPushAd")){
			isPushAd =1;	
		}
		int isPushMsg = 0;
		if(null!=request.getParameter("isPushMsg")){
			isPushMsg =1;	
		}	
		int  app_id = -1;
		try {
			if (!"".equals(devId)) {
				// 编辑应用信息
				if (!"".equals(appId)) {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("devId", devId);
					param.put("appId", appId);
					app_id = Integer.valueOf(appId);

					App app=  appService.getApp(param);

					if(appPackage!=null&& !"".equals(appPackage)){
						app.setAppPackage(appPackage);
					}
					if(appTypeId!=null&& !"".equals(appTypeId)){
						app.setAppTypeId(appTypeId);
					}
					if(appDescription!=null && !"".equals(appDescription)){
						app.setAppDescription(appDescription);
					}
					if(appName!=null && !"".equals(appName)){
						app.setAppName(appName);
					}
					app.setIsPushAd(isPushAd);
					app.setIsPushMsg(isPushMsg);
					app.setAppStage(Integer.valueOf( appStage));

					Map<String, Object> paramfile =new HashMap<String, Object>();
					paramfile.put("devId", devId);
					int appid=app.getAppId();
					app.setAppId(appid);
					paramfile.put("appId", appid+""); 
					paramfile.put("r_file", "appIcon");
					paramfile.put("type", "icon"); 
					//保存icon图片
					String appIcon = saveFile(paramfile, request);
					logger.info(" appIcon : "+appIcon);
					if(appIcon!=null){
						app.setAppIcon(appIcon);
					}

					//保存证书
					Map<String, Object> appleCertificate =new HashMap<String, Object>();
					appleCertificate.put("filePath","apple");
					appleCertificate.put("r_file", "appleCertificate");
					appleCertificate.put("newFileName", appKey);
					String apple = FileHelp.saveFile(appleCertificate, request);
					logger.info("save appleCertificate result = "+apple);
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					MultipartFile file = multipartRequest.getFile("appleCertificate");
					int ctype = 0 ;
					if(file!=null && file.getSize() > 0){
						if(stage==0){
							app.setAppleDataTest(file.getBytes());
							app.setCertificatePassTest(certificatePass);
							ctype = 2 ;
						}
						else{
							app.setAppleData(file.getBytes());
							app.setCertificatePass(certificatePass);
							ctype = 1 ;
						}
					}
					app.setAppleCertificate(apple);

					//接口
					Map<String, Object> newAppInterParam = new HashMap<String, Object>();
					newAppInterParam.put("id", appId);
					newAppInterParam.put("name_cn",appName );
					//应用包名不为空时
					if(app.getAppPackage()!=null && (!"".equals(app.getAppPackage().trim()))){
						newAppInterParam.put("pkg", appPackage);
						platform = "a";
					}
					//
					if(app.getCertificatePassTest()!=null||app.getCertificatePass()!=null){
						newAppInterParam.put("ios_key", "iOS");
						if (platform.length() > 0) {
							platform += ",";
						}
						platform += "i";
					}
					newAppInterParam.put("send_id", appKey);
					newAppInterParam.put("app_key", appKey);
					newAppInterParam.put("dev_id", devId);
					newAppInterParam.put("is_sync", 0);
					app_id = WebInterface.newApp(newAppInterParam);
					
					if(!(app_id+"").equals(appId)){
						responseMap = getResponseMap ("save",false,Def.getAppErrStr(app_id));
						responseMap.put("appId", appId); 

						mav.addObject("model", responseMap);
						return mav;
					}
					app.setPlatform(platform);		
					appService.updateApp(app);

					if(ctype>0){
						Map<String, Object> isoParam = new HashMap<String, Object>();
						isoParam.put("appkey", appKey);
						isoParam.put("ctype", ctype);
						boolean f =	WebInterface.iosCertificateValidation(isoParam);
						if(f){
							responseMap = getResponseMap ("save",true,"编辑成功");
							responseMap.put("appId", appId+"");
						}else{
							Map<String, Object> iosparam = new HashMap<String, Object>();
							iosparam.put("appId", appId);
							iosparam.put("devId", devId);
							iosparam.put("ctype", ctype);
							appService.deleteIosCertificate(iosparam);

							responseMap = getResponseMap ("save",true,"编辑成功");
							responseMap.put("ios", 1);
							responseMap.put("appId",appId );
							responseMap.put("info", "修改应用成功  <span>iOS证书不合法  </span> <span>请上传证书合法 </span> ");
						}
					}else{
						responseMap = getResponseMap ("save",true,"编辑成功");
						responseMap.put("appId",  appId);
					}
				} 
				//新增应用
				else {
					logger.debug("新增应用  devId ： %s "+String.format("", devId) +devId+" :-------");
					//先oracle 插入数据
					appKey = dev.getDevName()+new Date().toString()+"34FG10";  
					appKey = MD5.MD5Encode(appKey).substring(2, 26);
					String apiMasterSecret = dev.getDevId()+"rdv58"+appKey;
					apiMasterSecret = MD5.MD5Encode(apiMasterSecret).substring(2, 26);
					//
					Map<String, Object> appleCertificate =new HashMap<String, Object>();
					appleCertificate.put("filePath","apple");
					appleCertificate.put("r_file", "appleCertificate");
					appleCertificate.put("newFileName", appKey);
					String apple = FileHelp.saveFile(appleCertificate, request);
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					MultipartFile file = multipartRequest.getFile("appleCertificate");
					Map<String, Object> newAppInterParam = new HashMap<String, Object>();
					newAppInterParam.put("name_cn",appName );
					//应用包名不为空时
					if(appPackage!=null && (!"".equals(appPackage.trim()))){
						newAppInterParam.put("pkg", appPackage);
						platform = "a";
					}
					//
					if(file!=null && file.getSize()>0){
						newAppInterParam.put("ios_key", "iOS");
						if (platform.length() > 0) {
							platform += ",";
						}
						platform += "i";
					}
					newAppInterParam.put("send_id", appKey);
					newAppInterParam.put("app_key", appKey);
					newAppInterParam.put("dev_id", devId);
					newAppInterParam.put("is_sync", 0);

					app_id = WebInterface.newApp(newAppInterParam);
					if(app_id<=0){
						responseMap.put("info", Def.getAppErrStr(app_id));
						responseMap.put("method", "save");
						responseMap.put("success", "fasle");

						mav.addObject("model", responseMap);
						return mav;
					}
					
					//保存数据
					logger.debug("appid",app_id);
					App app = new App();
					app.setAppId(app_id);
					app.setAppName(appName);
					app.setAppPackage(appPackage);
					app.setAppDescription(appDescription);
					app.setAppTypeId(appTypeId);
					app.setDevId(Integer.valueOf(devId));
					app.setAppStage(Integer.valueOf(appStage));
					app.setAppKey(appKey);
					app.setIsPushAd(isPushAd);
					app.setIsPushMsg(isPushMsg);
					app.setPlatform(platform);
					Date currentTime = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					app.setLastUpdateTime( formatter.format(currentTime));
					app.setItime(formatter.format(currentTime));
					app.setApiMasterSecret(apiMasterSecret);
					Map<String, Object> paramfile = new HashMap<String, Object>();
					try { 
						//						 logger.info( String.format("新增应用 参数  devId ：%s 、appid: %s, appname: %s,appkey : %s  ", devId, app.getAppId()) +"",app.getAppName(),app.getAppKey());
						appService.insertApp(app);  
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("app insertApp err : " +e.getMessage() );
						responseMap.put("method", "save");
						responseMap.put("success", "fasle");
						responseMap.put("info", "失败!");

						mav.addObject("model", responseMap);
						return mav;
					}
					paramfile.put("devId", devId);
					app.setAppId(app_id);
					paramfile.put("appId", app_id+"");
					paramfile.put("r_file", "appIcon");
					paramfile.put("type", "icon"); 
					String appIcon = saveFile(paramfile, request); //保存logo
					app.setAppIcon(appIcon);
					int ctype = 0 ;
					if(file!=null && file.getSize()>0){
						if(stage==0){
							app.setAppleDataTest(file.getBytes());
							app.setCertificatePassTest(certificatePass);
							ctype = 2 ;
						}
						else{
							app.setAppleData(file.getBytes());
							app.setCertificatePass(certificatePass);
							ctype = 1 ;
						}
					}
					app.setAppleCertificate(apple);
					appService.updateApp(app);
					if(ctype>0){
						Map<String, Object> isoParam = new HashMap<String, Object>();
						isoParam.put("appkey", appKey);
						isoParam.put("ctype", ctype);
						boolean f =	WebInterface.iosCertificateValidation(isoParam);
						if(f){
							responseMap = getResponseMap ("save",true,"新增成功");
							responseMap.put("appId", app_id+"");
						}else{
							Map<String, Object> param = new HashMap<String, Object>();
							param.put("appId", app_id);
							param.put("devId", devId);
							param.put("ctype", ctype);

							appService.deleteIosCertificate(param);
							responseMap = getResponseMap ("save",true,"新增成功");
							responseMap.put("ios", 1);
							responseMap.put("appId", app_id+"");
							responseMap . put("info", "新增应用成功  <span>iOS证书不合法  </span> <a href="+ request.getContextPath()+"/app/edit/"+app_id+".html> <span>重新上传证书 </span></a>");
						}
					}else{
						responseMap = getResponseMap ("save",true,"新增成功");
						responseMap.put("appId", app_id+"");
					}
				}
			}else{
				responseMap = getResponseMap ("save",false,"登录已经失效， 请重新登录！");
				responseMap.put("appId",  "-1");
			}
			mav.addObject("model", responseMap);
			logger.debug("-----end-------新建应用---------"); 
		} catch (IosCVException e) {
			responseMap.put("method", "save");
			responseMap.put("success", "true");
			responseMap.put("appId", app_id+"");
			responseMap.put("info", e.getMessage());

			mav.addObject("model", responseMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("neworUpdate app err : " +e.getMessage());
			responseMap.put("info", "未知错误");
			responseMap.put("method", "save");
			responseMap.put("success", "fasle");

			mav.addObject("model", responseMap);
		}
		return mav;
	}




	/**
	 * 新增与修改应用
	 * 
	 * @param request
	 * @param response
	 * @return

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveOrUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String appId = request.getParameter("appId") != null ? request.getParameter("appId") : "";
		String appName = request.getParameter("appName");
		Developer dev=(Developer)request.getSession().getAttribute("user");
		String devId = dev==null?"":dev.getDevId().toString();
		String appPackage = request.getParameter("appPackage");
		String appKey = request.getParameter("appKey");
		String appTypeId = request.getParameter("appTypeId");
		String appDescription = request.getParameter("appDescription");
		String isPushAd = request.getParameter("isPushAd");
		String isPushMsg = request.getParameter("isPushMsg");
		try {
			logger.info(appName+" : "+appPackage+"  :  " +appDescription +" : " + appTypeId);
			logger.info("isPushAd : " +isPushAd);
			logger.info("isPushMsg : " +isPushMsg);
			if (!"".equals(devId)) {
				// 编辑应用信息
				if (!"".equals(appId)) {
					    Map<String, Object> param = new HashMap<String, Object>();
						param.put("devId", devId);
						param.put("appId", appId);
						App app=  appService.getApp(param);
						if(appPackage!=null){
							app.setAppPackage(appPackage);
						}
						if(appTypeId!=null){
							app.setAppTypeId(appTypeId);
						}
						if(appDescription!=null){
							app.setAppDescription(appDescription);
						}
						if(appName!=null){
							app.setAppName(appName);
						}
						Map<String, Object> paramfile =new HashMap<String, Object>();
						paramfile.put("devId", devId);
						int appid=app.getAppId();
						app.setAppId(appid);
						paramfile.put("appId", appid+""); 
						paramfile.put("r_file", "appIcon");
						paramfile.put("type", "icon"); 
						//保存图片
						String appIcon = saveFile(paramfile, request);
						logger.info(" appIcon : "+appIcon);
						if(appIcon!=null){
					  	  app.setAppIcon(appIcon);
					  	}
					  	for(int i=1; i<=3;i++){
					  		Map<String, Object> param1 =new HashMap<String, Object>();
					  		param1.put("devId", devId);
					  		param1.put("appId", appid+"");
					  		param1.put("r_file", "f"+i);
					  		param1.put("type", "info"+i);  
						  	if(i==1){ 
						  		String ima1=	saveFile(param1, request);
						  		if(	ima1!=null){
						  		  app.setAppImage1(ima1);
						  		}
						  	}
						  	else if(i==2){
						  		String ima2=	saveFile(param1, request);
						  		if(	ima2!=null){
						  	    	app.setAppImage2(ima2);
						  		}
						  	}
						  	else if(i==3){
						  		String ima3=saveFile(param1, request);
						  		if(	ima3!=null){
						  		   app.setAppImage3(ima3);
						  		}
						  	}
					  	}
					  	appService.updateApp(app);
					responseMap.put("appId", appId); 
					responseMap.put("method", "save");
					responseMap.put("success", "true");
					responseMap.put("info", "编辑成功！");
				} 
				else { 
					App app=new App();
					app.setAppName(appName);
					app.setAppPackage(appPackage);
					app.setAppDescription(appDescription);
					app.setAppTypeId(appTypeId);
					app.setDevId(Integer.valueOf(devId));
					appKey = dev.getDevName()+appPackage+"34FG10";  
					appKey = MD5.MD5Encode(appKey).substring(2, 26);
					app.setAppKey(appKey);
					Date currentTime = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					app.setLastUpdateTime( formatter.format(currentTime));
					app.setItime(formatter.format(currentTime));
					Map<String, Object> paramfile =new HashMap<String, Object>();
					 try { 
					     appService.insertApp(app);  
					 } catch (Exception e) {
                        e.printStackTrace();
    					responseMap.put("method", "save");
    					responseMap.put("success", "fasle");
    					responseMap.put("info", "失败!");
                        return responseMap;
					 }

					paramfile.put("devId", devId);
					int appid=appService.getAppID(paramfile);
					app.setAppId(appid);
					paramfile.put("appId", appid+"");

					paramfile.put("r_file", "appIcon");
					paramfile.put("type", "icon"); 
					String appIcon = saveFile(paramfile, request);
				  	app.setAppIcon(appIcon);
				  	for(int i=1; i<=3;i++){
				  		Map<String, Object> param =new HashMap<String, Object>();
				  		param.put("devId", devId);
				  		param.put("appId", appid+"");
				  		param.put("r_file", "f"+i);
				  		param.put("type", "info"+i); 

					  	if(i==1){
					  		app.setAppImage1(	saveFile(param, request));
					  	}
					  	else if(i==2){
					  		app.setAppImage2(saveFile(param, request));
					  	}
					  	else if(i==3){
					  		app.setAppImage3(saveFile(param, request));
					  	}
				  	}

				  	appService.updateApp(app);

					responseMap.put("method", "save");
					responseMap.put("success", "true");
					responseMap.put("appId", appid+"");
					responseMap.put("info", "新增成功！");
				}
			}  
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("info", e.getClass() + ":" + e.getMessage());
			return responseMap;
		}
	}
	 */


	@RequestMapping(value = "/getAppList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getAppList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Developer dev=(Developer)request.getSession().getAttribute("user");
		String devId = dev==null?null:dev.getDevId().toString();
		int page =request.getParameter("pageIndex")==null?1:Integer.valueOf(request.getParameter("pageIndex"));
		int rows =request.getParameter("pageSize")==null?10:Integer.valueOf(request.getParameter("pageSize"));
		String appId=request.getParameter("appId")==null?"": request.getParameter("appId") ;
		String beginTime=request.getParameter("beginTime")==null?"": request.getParameter("beginTime") ;
		String endTime=request.getParameter("endTime")==null?"": request.getParameter("endTime") ;
		String whereStr=request.getParameter("whereStr")==null?"": request.getParameter("whereStr") ;
		try {
			// 查询开发者应用
			if (devId!=null&&!devId.equals("")) {
				Map<String,Object> param =new HashMap<String, Object>();
				param.put("devId", devId);
				param.put("index", (page-1)*rows);
				param.put("size", rows);
				if(!appId.equals("")&&!appId.equals("0")){
					param.put("appId",Integer.valueOf( appId));
				}
				String endDate = getEndDate(request);
				String beginDate = getStartDate(request);
				//				param.put("beginTime", beginDate);
				//				param.put("endTime", endDate+ " 23:59:59 ");

				param.put("endDate", endDate);
				param.put("startDate", beginDate);
				if(whereStr!=null&&!whereStr.equals("")){
					param.put("whereStr", "%"+whereStr+"%");
				}
				//				if(!beginTime.equals("")){
				//					param.put("beginTime", beginTime);
				//				}else{
				//					Calendar c = Calendar.getInstance();
				//					c.setTime(new Date());
				//					c.add(Calendar.MONTH, -1); 
				//				} 
				//				if(!endTime.equals("")){
				//					param.put("endTime", endTime+ " 23:59:59 ");
				//				}  
				int count = appService.getCount(param);
				ArrayList<App> alist =(ArrayList<App>) appService.getAppList(param);	
				responseMap.put("total", count);
				responseMap.put("rows", alist);
			} else {
				logger.debug("没有开发信息");
			}


			//			ArrayList<App> alist = new ArrayList<App>();

			//			int sum=;
			//			for(int i=(page-1)*rows+1;i<=page*rows;i++){
			//				App a = new App(); 
			//				 a.setAppId(i);
			//					a.setAppName("新应用"+i);
			//					a.setAppPackage("ccom.android.as"+i);
			//					alist.add(a);
			//				 
			//			 }



			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("info", e.getClass() + ":" + e.getMessage());
			return responseMap;
		}
	}


	@RequestMapping(value = "/getDevAppTree", method = RequestMethod.POST)
	public @ResponseBody ArrayList<Tree>  getDevAppTree(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Developer dev=(Developer)request.getSession().getAttribute("user");
		String devId = dev==null?"":dev.getDevId().toString();
		try {
			if (!devId.equals("")) {
				Map<String,Object> param =new HashMap<String, Object>();
				param.put("devId", devId);
				ArrayList<App> alist =null;
				alist=(ArrayList<App> )appService.findAppNameList(param);
				if(alist!=null&&alist.size()>0){ 
					ArrayList<Tree > rList=new ArrayList<Tree>();
					for(int i=0;i<alist.size();i++){
						Tree t=new  Tree();
						App a= alist.get(i);
						t.setId(a.getAppPackage());
						t.setText(a.getAppName());
						rList.add(t);
					}
					return rList;
				}
			}  
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("info", e.getClass() + ":" + e.getMessage());
			return null;
		}
	}

	class Tree{
		String id;
		String text;
		String iconCls;

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getIconCls() {
			return iconCls;
		}
		public void setIconCls(String iconCls) {
			this.iconCls = iconCls;
		}
	}

	@RequestMapping(value = "/getDevApp", method = RequestMethod.POST)
	public @ResponseBody ArrayList<App>  getDevApp(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Developer dev=(Developer)request.getSession().getAttribute("user");
		String devId = dev==null?"":dev.getDevId().toString();
		try {
			if (!devId.equals("")) {
				//String  stage = request.getParameter("stage"); //应用状态 0 - 测试状态 1- 生成状态
				Map<String,Object> param = requestToMap(request);
				String type=param.get("isall")==null? null : param.get("isall") +"" ;
				param.put("devId", devId);
				ArrayList<App> alist =new ArrayList<App>();
				alist.clear();
				if(type==null){
					App a=new App();
					a.setAppId(0);
					a.setAppName("全部应用");
					alist.add(a);
				}
				logger.info("size :  " + alist.size());
				ArrayList<App>  alistTemp=(ArrayList<App> )appService.findAppNameList(param);
				if(alistTemp!=null&&alistTemp.size()>0){ 

					for (int i=0;i<alistTemp.size();i++){
						alist.add(alistTemp.get(i));
					} 
					return alist;
				}
			}  
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("info", e.getClass() + ":" + e.getMessage());
			return null;
		}
	}



	public String saveFile(Map<String, Object> pamer ,HttpServletRequest request){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获得文件：
		String devId=(String)pamer.get("devId");
		String appid=(String)pamer.get("appId");
		String r_file=(String)pamer.get("r_file");
		String type=(String)pamer.get("type");
		String cPath=SystemConfig.getProperty("file_path", "");
		if(cPath==null){
			cPath= request.getSession().getServletContext().getRealPath("/appfile");
		}else{
			cPath=cPath+"appfile";
		}
		MultipartFile file = multipartRequest.getFile(r_file);
		if(file==null || file.getSize()==0){
			return null;

		}
		String targetDirectory=cPath+"/"+devId+"/"+appid;
		try {
			logger.info("文件路径 : "+targetDirectory);
			File filenew = new File(targetDirectory);
			if (filenew.exists()) {
				logger.info("File exists");
			} else {
				logger.info("The file does not exist, are creating...");				
				if (filenew.mkdirs()) {
					logger.info("file Success！");
				} else {
					logger.info("File creation failed！");
				}
			}	 
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		String filename="";
		if(file!=null){
			try {
				filename=saveFileToServer(file,type,targetDirectory) ; 
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		return filename;
	}
	public String saveFileToServer(MultipartFile multifile,String type, String path)
	throws IOException {
		String filename = multifile.getOriginalFilename();
		String infoextName = filename.substring(filename.lastIndexOf("."));  
		String infofileName = type + infoextName;	
		// 创建目录
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 读取文件流并保持在指定路径
		InputStream inputStream = multifile.getInputStream();
		OutputStream outputStream = new FileOutputStream(path +"/"+infofileName);
		byte[] buffer = multifile.getBytes();
		int bytesum = 0;
		int byteread = 0;
		while ((byteread = inputStream.read(buffer)) != -1) {
			bytesum += byteread;
			outputStream.write(buffer, 0, byteread);
			outputStream.flush();
		}
		outputStream.close();
		inputStream.close();
		logger.info("文件名称 ： "+infofileName); 
		return infofileName;
	}


	@RequestMapping(value = "/deleteApp", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteApp(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> param =new HashMap<String, Object>(); 
		try {
			Developer dev=(Developer)request.getSession().getAttribute("user");
			String appId = request.getParameter("appId") != null ? request.getParameter("appId") : "";
			if(dev==null){
				responseMap.put("success", false);
				responseMap.put("info", "重新登录！");
				return responseMap; 
			} 
			if(appId.equals("")){
				responseMap.put("success", false);
				responseMap.put("info", "没有对应的操作应用！");
				return responseMap;
			} 

			param.put("devId", dev.getDevId());
			param.put("id", appId);
			param.put("appId", appId);

			int isExistsApp = appService.isExistsApp(param);
			logger.debug("delete app isExistsApp = "+isExistsApp);	
			if(isExistsApp < 1){
				responseMap.put("success", false);
				responseMap.put("info", "没有对应的操作应用！");
				return responseMap;
			}

			responseMap.put("success", false);
			responseMap.put("info", "没有对应的操作应用！");

			JSONObject json = WebInterface.deleteApp(param);
			if(json != null){
				logger.debug("delete app result json:"+json.toString());
				if(json.getBoolean("success")){
					appService.deleteApp(param); 
					responseMap.put("success", true);
					responseMap.put("info", "删除应用成功！"); 
				}else{
					responseMap.put("success", false);
					responseMap.put("info", "删除应用失败，请联系管理员！"); 
				}
			}else{
				responseMap.put("success", false);
				responseMap.put("info", "删除应用失败，请联系管理员！"); 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseMap.put("success", false);
			responseMap.put("info", "删除应用失败，请联系管理员！"); 
		}

		logger.debug("delete app responseMap:["+responseMap.toString()+"]");
		return responseMap;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> delete(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Developer dev=(Developer)request.getSession().getAttribute("user");
		String appId = request.getParameter("appId") != null ? request.getParameter("appId") : "";
		if(dev==null){
			responseMap.put("success", "false");
			responseMap.put("info", "重新登录！");
			return responseMap; 
		} 
		if(appId.equals("")){
			responseMap.put("success", "false");
			responseMap.put("info", "没有对应的操作应用！");
			return responseMap;
		} 
		try { 
			Map<String, Object> param =new HashMap<String, Object>(); 
			param.put("devId", dev.getDevId());
			param.put("appId", appId);
			appService.deleteApp(param); 
			responseMap.put("success", "true");
			responseMap.put("info", "删除成功！"); 
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("info", e.getClass() + ":" + e.getMessage());
			return responseMap;
		}
	}

	/**
	 * 检查应用包名是否存在
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/isExistsAppPackage", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> isExistsAppPackage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String id = request.getParameter("name") != null ? request.getParameter("name") : "";
		String appPackage = request.getParameter("param");
		Developer dev=(Developer)request.getSession().getAttribute("user");
		try {
			if (dev!=null&&id.equals("appPackage")&&!"".equals((appPackage+"").trim())) {
				Map<String, Object> param  = new HashMap<String, Object>();
				appPackage = new String(appPackage.getBytes("ISO-8859-1"), "utf-8");

				param.put("package", appPackage);
				String appid = request.getParameter("appid");
				boolean isAppid=appid!=null&&!"".equals(appid)&&!"null".equals(appid);
				if(isAppid){
					logger.info("appid : "+appid);
					param.put("appid", appid);
				}
				param.put("dev_id",dev.getDevId());
				//正式要 添加后面被注释的逻辑
				boolean result = WebInterface.isExistsAppPackage(param);
				if(!result){ 
					boolean isE = appService.isExistsAppPackage(param);
					if(!isE) {
						logger.info("包名是否存在---"+result);
						responseMap.put("method", "eap");
						responseMap.put("success", "true");
						responseMap.put("info", "此包名可用！");
					}else{
						responseMap.put("method", "eap");
						responseMap.put("success", "fasle");
						responseMap.put("info", "此包名不可用！");
					}
				}else{
					responseMap.put("method", "eap");
					responseMap.put("success", "false");
					responseMap.put("info", "此包名不可用！");
				} 
			}else{
				responseMap.put("method", "eap");
				responseMap.put("success", "fasle");
				if(dev==null){
					responseMap.put("info", "登录失效，请重新登录！");					
				}
				if(!id.equals("appPackage")){
					responseMap.put("info", "参数错误 请联系 管理员！");
				}
				if("".equals(appPackage)){
					responseMap.put("info", "包名不能为空！");
				}
			}
			return responseMap; 
		}catch (Exception e) {
			logger.info(e.toString());
			e.printStackTrace();
			responseMap.put("info", "服务器忙 请稍后再试!");
			logger.info("this is  isExistsAppPackage method err : " +e.toString() );
			return responseMap;
		}
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> upload(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Developer dev=(Developer)request.getSession().getAttribute("user");
		String appId = request.getParameter("appId") != null ? request.getParameter("appId") : "";
		//appId没有值，则不进行上传的操作
		if(appId.equals("")){
			responseMap.put("success", "false");
			responseMap.put("info", "没有对应的应用Id，上传失败！");
			return responseMap;
		}
		Map<String, Object> param =new HashMap<String, Object>();
		try {
			// 转型为MultipartHttpRequest：
			// File f=(File)request.getParameter("file");
			if(dev!=null){
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				// // 获得文件：
				MultipartFile file = multipartRequest.getFile("file");
				if(file==null){
					responseMap.put("filename", "");
					responseMap.put("success", "fasle");
					responseMap.put("info", "没有选择文件！");
					return responseMap;
				}
				// // 获得文件名：
				String filename = file.getOriginalFilename();
				logger.info(filename);
				param.put("devId", dev.getDevId()+"");
				param.put("appId", appId);
				param.put("r_file", "file");
				param.put("type", filename.substring(0, filename.indexOf("."))); 
				String apkName = saveFile(param, request);
				App app = new App();
				app.setAppId(Integer.valueOf(appId));
				app.setDevId(dev.getDevId());
				app.setAppApk(apkName);
				appService.updateApp(app);
				// // 获得输入流：
				// InputStream input = file.getInputStream();
				// 写入文件
				responseMap.put("filename", filename);
				responseMap.put("success", "true");
				responseMap.put("info", "上传成功！");
				// 或者：
				// File source = new File(localfileName.toString());
			} 
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("info", e.getClass() + ":" + e.getMessage());
			logger.info("this is  upload method err : " +e.toString());
			return responseMap;
		}
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void getAppDowmload(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> parme = PublicController.requestToMap(request);
		String contextPath=request.getContextPath();// 获得项目名称
		String fileName = parme.get("file")+"";
		if(parme.get("type")!=null&&!"".equals(fileName)){

			if(parme.get("type").equals("android")){
				try {

					if (!response.isCommitted()) {
						response.sendRedirect(contextPath + "/home/file/"+fileName);
						return;
					}

				}catch(IOException e) {
					e.printStackTrace();
				}
			}else if(parme.get("type").equals("ios")){
				try {
					if (!response.isCommitted()) {
						response.sendRedirect(contextPath + "/home/file/"+fileName);
						return;
					}
				}catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}

	}



	/**
	 * 下载example例子
	 * 
	 * @param request
	 * @param response
	 */

	@RequestMapping(value = "/dowmloadExample", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getAppDowmloadExample(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			Developer dev=(Developer)request.getSession().getAttribute("user");
			String devId = dev==null?"":dev.getDevId().toString();
			if (!devId.equals("")) {
				Map<String, Object> param = PublicController.requestToMap(request);
				String contextPath = request.getSession().getServletContext().getRealPath(request.getContextPath()) ;
				param.put("devId", devId);
				boolean isUpdate = "0".equals(param.get("isUpdate"))?true:false; 
				App app=appService.getApp(param);
				if(app!=null){
					if(param.get("type")!=null){
						if(param.get("type").equals("android")){

							if(app.getAppPackage()!=null && !"".equals(app.getAppPackage())){
								String examplePath = appService.getDowmloadPath(app, contextPath, isUpdate);
								logger.info(" examlePath  : " + examplePath);
								responseMap.put("method", "getAppDowmloadExample");
								responseMap.put("info", examplePath);
								responseMap.put("success", true);
							}else{
								responseMap.put("method", "getAppDowmloadExample");
								responseMap.put("info", "应用不存在Android包名,请完善应用资料。");
								responseMap.put("success", false);
							}
							//							 response.sendRedirect(contextPath + examplePath);
						}else if(param.get("type").equals("ios")){
							//iOS-example 例子
							//						 try {
							//								 response.sendRedirect(contextPath + "/home/file/"+SystemConfig.getProperty("iosFile"));
							//							}catch (IOException e) {
							//								e.printStackTrace();
							//						} 
						}
					}
				}else{
					responseMap.put("method", "getAppDowmloadExample");
					responseMap.put("info", "应用不存在");
					responseMap.put("success", false);
				}
			}  			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(" err : " +e.toString());
			responseMap.put("method", "getAppDowmloadExample");
			responseMap.put("info", "创建Example错误");
			responseMap.put("success", false);
		}
		return responseMap;
	}


	@RequestMapping(value = "/getApp", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getApp(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Developer dev=(Developer)request.getSession().getAttribute("user");
		String devId = dev==null?"":dev.getDevId().toString();
		String appId = request.getParameter("appId");
		try {
			if (!devId.equals("")) {
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("devId", devId);
				param.put("appId", appId);
				responseMap.put("method", "getApp");
				responseMap.put("success", "true");
				App app=appService.getApp(param);


				responseMap.put("info", app);
			}  			
			return responseMap;

		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("info", "err :" + e.toString());
			logger.info(" err : " +e.toString());
			return null;
		}
	}
	public static void main(String[] args) {
	/*	String masterSecret = 14+"rdv58"+"5a78e02e-1cc6-4390-8e6f-d4f73a391c70";
		masterSecret = MD5.MD5Encode(masterSecret);
		System.err.println(masterSecret); 
		System.out.println(masterSecret.substring(2,26));*/
	
	}

	

}
