package com.kkpush.api.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kkpush.account.domain.Developer;
import com.kkpush.api.domain.ApiCallBack;
import com.kkpush.api.service.ApiCallBackService;
import com.kkpush.systembridge.SessionBridge;
import com.kkpush.util.CommonConstants;
import com.kkpush.util.HttpUtil;
import com.kkpush.util.StrUtil;
import com.kkpush.web.controller.PublicController;
/**
 *  网站认证Controle
 * @author zengzhiwu
 *
 */
@Controller
@RequestMapping("/v1")
public class ApiCallBackController extends PublicController{
	private static final Log logger = LogFactory.getLog(ApiCallBackController.class);
	@Autowired
	public ApiCallBackService callBackService;
	private final static String VALIDATE_CONTENT = "Welcome Jpush";
	
	@RequestMapping(value = "/api", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendUser(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> requestParams = requestToMap(request);
		Map<String, Object> responseMap = new HashMap<String, Object>();
		  if("w".equals("c")){
			  responseMap.put("adimage", "http://www.airpush.com/images/adsthumbnail/48.png");
	    	  responseMap.put("title", "应用列表");
	    	  responseMap.put("text", "获取应用列表");
	    	  responseMap.put("textColor", "#FCDFFF");
	    	  responseMap.put("number", "");
	    	  responseMap.put("sms", "");
	    	  responseMap.put("countrycode", "");
	    	  responseMap.put("creativeid", "32132");
	    	  responseMap.put("adtype", "W");
	    	  responseMap.put("current_time", "2013-01-11 09:04:33");
	    	  responseMap.put("delivery_time", "2013-01-11 09:04:33");
	    	  responseMap.put("nextmessagecheck", 120);
	    	  responseMap.put("expirytime", 86400);
	    	  responseMap.put("superad", 1);
		  }else{
			  responseMap.put("title", "更多就爱");
			  responseMap.put("url", "http:\\api.airpush.com\redirect.php?market=http:\\\\partners.handango.com\\smartphone\\home.jsp?siteId=2897&apcd=eyJhcHBpZCI6Ijk1ODczIiwid2lmaSI6IjEiLCJ2ZXJzaW9uIjoiNS4wIiwicHVzaF9ndWlkIjoiMWNmNDE2YzhlZjAxNTQ5M2Y2MjU3NTM4OWFmNTY0MWIiLCJjb3VudHJ5Ijo0MSwiY2FycmllciI6NTI3MSwiZGV2aWNlIjoiOTgxMyIsInBob25lTW9kZWwiOjEwMDQ0MSwibWFudWZhY3R1cmVyIjozMjg5OCwiY2l0eSI6MCwic3RhdGUiOjEwMDIxMCwiaW1laSI6ImE3ZTFjYzk1NDIwZGRlMDk5MThiYmUwMmNhZTQzODI2In0=");
			  responseMap.put("number", "");
			  responseMap.put("sms", "");
			  responseMap.put("countrycode", "");
			  responseMap.put("creativeid", "155597");
			  responseMap.put("campaignid", "61035");
			  responseMap.put("expirytime", 86399);
			  responseMap.put("adtype", "BPW");
			  responseMap.put("ip1", "http:\\\\api1.airpush.com");
			  responseMap.put("ip2", "http:\\\\api2.airpush.com");
			  responseMap.put("text", "更多就爱");
			  responseMap.put("current_time", "2012-01-15 19:15:18");
			  responseMap.put("delivery_time", "2012-01-15 19:15:18");
			  responseMap.put("nextmessagecheck", 7200);
			  responseMap.put("header", "Browser");
			  
			  
		  }
    	  
    	  /***
    	   * 
    	   * 
    	   * 
    	   * {"title":"Click to Learn More",
"url":"http:\/\/api.airpush.com\/redirect.php?market=http:\/\/partners.handango.com\/smartphone\/home.jsp?siteId=2897&apcd=eyJhcHBpZCI6Ijk1ODczIiwid2lmaSI6IjEiLCJ2ZXJzaW9uIjoiNS4wIiwicHVzaF9ndWlkIjoiMWNmNDE2YzhlZjAxNTQ5M2Y2MjU3NTM4OWFmNTY0MWIiLCJjb3VudHJ5Ijo0MSwiY2FycmllciI6NTI3MSwiZGV2aWNlIjoiOTgxMyIsInBob25lTW9kZWwiOjEwMDQ0MSwibWFudWZhY3R1cmVyIjozMjg5OCwiY2l0eSI6MCwic3RhdGUiOjEwMDIxMCwiaW1laSI6ImE3ZTFjYzk1NDIwZGRlMDk5MThiYmUwMmNhZTQzODI2In0=",
"number":null,
"sms":null,
"countrycode":null,
"creativeid":"155597",
"campaignid":"61035",
"expirytime":86399,
"adtype":"BPW",
"adimage":"http:\/\/s3.amazonaws.com\/creative-adtype-images\/155597-320.png",
"ip1":"http:\/\/api1.airpush.com\/","ip2":"http:\/\/api2.airpush.com\/",
"text":" Optout: xapush.com, Ad by: AirTest",
"current_time":"2012-01-15 19:15:18",
"delivery_time":"2012-01-15 19:15:18",
"nextmessagecheck":7200,
"header":"Browser"}

    	   */
		if(requestParams != null){
			 String model = requestParams.get("model")+"";
			 if(!StrUtil.isEmpty(model)){
				
	             if("user".equals(model)){
	            	 if("setuserinfo".equals(requestParams.get("action"))){
	            	 }
	              }else if("message".equals(model)){
                     /***
                      * {"adimage":"http://www.airpush.com/images/adsthumbnail/48.png",
						"title":"Top DROID Apps",
						"text":"Get the Latest and Greatest Apps Today!!, Optout: xapush.com, Ad by: Airpushdemo",
						"url":"http://www.airpush.com/testapp/",
						"textColor":"#FCDFFF",
						"number":null,
						"sms":null,
						"countrycode":null,
						"creativeid":"0",
						"adtype":"W",
						"campaignid":"0",
						"current_time":"2013-01-11 09:04:33",
						"delivery_time":"2013-01-11 09:04:33",
						"nextmessagecheck":120,
						"expirytime":86400,
						"superad":1} 
                      */
	            	  
	            	
				      
	              }
			 }
		}
		
		return responseMap;
	}
	
	/**
	 * 
	 * @author 
	 * @date 2012-12-17
	 * @desc  下载认证key
	 * @param request
	 * @param response
	 */
	@RequestMapping("/download_key")
	public void downValidateKey(HttpServletRequest request,HttpServletResponse response){
		SessionBridge session = SessionBridge.getInstance();
		Developer dev = session.lookup(request);
		ApiCallBack apiCallBack = new ApiCallBack();
		Map<String, Object> params = new HashMap<String,Object>();
		if(dev == null) return;

		String apiSecretKey = getValidateKey(dev.getDevId());
		OutputStream outputStream = null;

		try {
			String baseUrl = getRootUrl(request.getParameter("baseurl"),true);
			String saveOrUpdate = request.getParameter("saveorupdate"); //修改/添加
			logger.info("uid = "+dev.getDevId()+" .download validate key:"+apiSecretKey+" $saveOrUpdate="+saveOrUpdate);

			if(saveOrUpdate != null && !saveOrUpdate.equals("") && !saveOrUpdate.equals("0")){

				params.put("devId", dev.getDevId());
				List<ApiCallBack> apiCallBackList = callBackService.selectApiCallBackList(params);
				for(ApiCallBack apicall : apiCallBackList){
					if(apicall.getBackUrl().equals("") || apicall.getBackUrl() == null){
						params.put("callbackUrl", "");
					}else{
						params.put("callbackUrl", baseUrl+apicall.getBackUrl());
					}
					params.put("baseUrl", baseUrl);
					params.put("apiWebsiteValidate", 0);
					params.put("apiSecretKey", apiSecretKey);
					callBackService.updateApiCallBack(params);
				}

			}else{
				apiCallBack.setBaseUrl(baseUrl);
				apiCallBack.setApiSecretKey(apiSecretKey);
				apiCallBack.setDevId(dev.getDevId());
				apiCallBack.setDevName(dev.getDevName());
				apiCallBack.setApiWebsiteValidate(0);
				apiCallBack.setType(CommonConstants.API.SEND_MESSAGE);
				callBackService.insertApiCallBack(apiCallBack);
			}

			response.reset();
			response.setCharacterEncoding("utf-8");  
			response.setContentType("multipart/form-data");  
			response.setHeader("Content-Disposition", "attachment;fileName="+apiSecretKey+".key");  
			outputStream = response.getOutputStream();
			outputStream.write(VALIDATE_CONTENT.getBytes());
			outputStream.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(outputStream != null)
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}


	/**
	 * 
	 * @author
	 * @date 2012-12-17
	 * @desc  获取apicallback信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getApiCallBackUrl")
	public ModelAndView getApiCallBackUrl(HttpServletRequest request,HttpServletResponse response){

		SessionBridge session = SessionBridge.getInstance();
		Developer dev = session.lookup(request);
		Map<String, Object> params = new HashMap<String,Object>();
		ModelAndView modelAndView = new ModelAndView("api/account_validate_website");
		if(dev == null)return modelAndView;

		try {
			params.put("devId", dev.getDevId());
			List<ApiCallBack> apiCallBackResults = callBackService.selectApiCallBackList(params);
			if(apiCallBackResults != null && apiCallBackResults.size() != 0){
				ApiCallBack apiCallBack = apiCallBackResults.get(0);
				modelAndView.addObject("api", apiCallBack);
			}
			modelAndView.addObject("callbackUrls", apiCallBackResults);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;

	}

	/**
	 * 
	 * @author
	 * @date 2012-12-17
	 * @desc  认证key是否存在/正确
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/validate_key", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> validateWebSiteKey(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		SessionBridge session = SessionBridge.getInstance();

		Developer dev = null;
		if(session.isSessionOut(request).containsKey(CommonConstants.SESSION_USER)){
			dev = (Developer)session.isSessionOut(request).get("user");
		}else{
			return session.isSessionOut(request);
		}
		String url = request.getParameter("base_url");
		logger.info("call validateWebSiteKey url = "+url);

		try{

			if(url == null || url.equals("")){
				responseMap.put("success", false);
				responseMap.put("info","请填写正确的顶级域名！");
				return responseMap;
			}
			params.put("devId", dev.getDevId());
			params.put("type",CommonConstants.API.SEND_MESSAGE);
			ApiCallBack apiCallBack = callBackService.selectApiCallBackByCond(params);

			String secretKey = apiCallBack.getApiSecretKey()+".key";
			String targetUrl = url.trim()+ secretKey;
			String[] results = HttpUtil.doGet(targetUrl);
			logger.info(String.format("call validateWebSiteKey targetUrl: %s,execute result code :%s,conten:%s",targetUrl,results[0],results[1]));

			if(Integer.parseInt(results[0]) != 200 
					|| !results[1].equals(VALIDATE_CONTENT))
			{
				responseMap.put("success", false);
				responseMap.put("info","认证失败，请确保您下载的最新文件上传至网站根目录！");
				return responseMap;
			}
			else
			{ 	
				//暂时不需要验证内容，内容跟key名称一样
				params.put("apiWebsiteValidate", 1);
				callBackService.updateApiCallBack(params);

				responseMap.put("success", true);
				responseMap.put("info","认证成功！");
				return responseMap;
			}
		}catch (Exception e) {
			e.printStackTrace();

			responseMap.put("success", false);
			responseMap.put("info","系统繁忙，稍后再试......");
			return responseMap;

		}

	}

	/**
	 * 
	 * @author 
	 * @date 2012-12-17
	 * @desc  保存api_callbackurl 信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/updateCallbakUrl")
	public @ResponseBody Map<String, Object>  updateCallbakUrl(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> params = new HashMap<String,Object>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		SessionBridge session = SessionBridge.getInstance();
		Developer dev = null;
		if(session.isSessionOut(request).containsKey(CommonConstants.SESSION_USER)){
			dev = (Developer)session.isSessionOut(request).get("user");
		}else{
			return session.isSessionOut(request);
		}

		String callbackUrl = request.getParameter("callbackUrl"); 
		String baseUrl = request.getParameter("baseUrl"); 
		String reqType = request.getParameter("type");  
		logger.info("call updateCallbakUrl,callbackUrl = "+callbackUrl+" &baseUrl="+baseUrl+" $regType="+reqType);

		try {

			if(baseUrl == null ||  baseUrl.equals("")){
				responseMap.put("success", false);
				responseMap.put("info", "请先验证顶级域名后再设置回调地址!");
				return responseMap;
			}

			//添加回调地址时，如果回调接口类型不存在的话，插入，否则修改
			params.put("devId", dev.getDevId());
			params.put("type", reqType);
			ApiCallBack apiCallBack = callBackService.selectApiCallBackByCond(params);
			String targetUrl = (callbackUrl.equals("") || callbackUrl == null) ? "" :getRootUrl(apiCallBack.getBaseUrl(),true)+callbackUrl;

			if(apiCallBack == null){
				apiCallBack = callBackService.selectApiCallBackOne(params);
				apiCallBack.setType(reqType);
				apiCallBack.setCallbackUrl(targetUrl);
				apiCallBack.setBackUrl(callbackUrl);
				callBackService.insertApiCallBack(apiCallBack);
			}else{
				params.put("callbackUrl", targetUrl);
				params.put("backUrl", callbackUrl);
				params.put("type", reqType);
				callBackService.updateApiCallBack(params);
			}
			responseMap.put("success", true);
			responseMap.put("info", "回调地址修改成功！");

		}catch (Exception e) {
			responseMap.put("success", false);
			responseMap.put("info", "服务器忙，稍后再试.....");
			e.printStackTrace();
		}
		return responseMap;


	}




}
