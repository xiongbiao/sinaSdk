 
 $(function() { 
   //header
    var _appid=appid;
    var _appsPath =appsPath;
    
    jQuery.post(appsPath+"/app/getAppList", {"pageSize":5}, function(data) {
     var appList ="";
       if(data!=null){
         var total = data.total;     
         var rows = data.rows;    
         if(rows!=null&&rows.length>0){
	      for(var i =0; i<rows.length;i++ ){
		    	  var index = i;//此处 为展示顺序，必须要
		    	  var appStatus ="";
			        if(rows[i].appStage==0){
			             appStatus="开发阶段";
			        }
			        if(rows[i].appStage==1){
			             appStatus="生产阶段";
			        }
		    	  if(i==0){
		    		  appList = appList+"<li  class='"+divItemSelect+"' onclick=\"window.location='/app/detail.jsp?appId="+rows[i].appId+"'\">";
			        
		    	  }else{
		    		  appList = appList+"<li onclick=\"window.location='/app/detail.jsp?appId="+rows[i].appId+"'\">";
			        
		    	  }
		    	 
		    	 if(rows[i].appIcon==null){
			        	 appList = appList+"  <div class='app_icon' style='background-image: url(/resources/images/default_app_icon_57x57.png);'></div>";
			         }else{
			        	 appList = appList+"  <div class='app_icon' style='background-image:url("+appPath+rows[i].appId+"/"+rows[i].appIcon+");'></div>";
			         }
		             appList = appList+"  <h3>"+rows[i].appName+"</h3>";
		             appList = appList+"  <p class='meta'>"+appStatus+"</p>";
		             appList = appList+"   <p class='meta'><em><span class='app_counts_ajax' data-key='"+rows[i].appKey+"' data-type='devices'>"+rows[i].activeUserPer+"</span></em> 活跃用户数</p>";
		             appList = appList+"</li>"+
		            "<input type='hidden' name='d_index' value='" +
                index +
                "' />" ;//位置，勿动
		             appList = appList+"<input type='hidden' name='h_appid' value='" +rows[i].appId +"' />"; 
	         } 
         }
       }
        $('#id_menu_app_list').html(appList);
        $('#app_total').html("("+total+")");
      //注册事件
         registerInputEvent();
    });
    if(_appid!=null){
    	jQuery.post(appsPath+"/app/getApp?appId="+_appid,  function(data) {
    	      $("#left_appName").html(data.info.appName);
    	      $("#header_appName").html(data.info.appName);
    	});
    }else{
    	//alert()
    }
     
    
     /**
      * 当鼠标 放上去 的效果 CSS  Class名称
      */
		var divItemSelect = 'li_item_select';
		/**
		 * 展示数据 的DIV
		 */
     var showDataDivId = "id_menu_app_list";
		/**
		 * 输入框
		 */
     var inputDataTextId = "appselect_search";
		/**
		 * 点击
		 * @param {Object} event 
		 */
     $(document).click(function(event){
         if (event.target.id != inputDataTextId) {
				//$("#" + showDataDivId).slideUp(200);
         }
     });
		/**
		 * 鼠标在文本框输入值
		 * @param {Object} event
		 */
     $("#" + inputDataTextId).keyup(function(event){
         if (event.keyCode == 40) {//down
             chageSelect(1);
         }
         else  if (event.keyCode == 38) {//up
             chageSelect(-1);
         }
         else  if (event.keyCode == 13) {//回车
             item_click($("#" + showDataDivId + " div[class='" + divItemSelect + "']"));
         }
         else   if (this.value.length >=0) {
        	 var appWhere=$("#appselect_search").val();
 		    jQuery.post(appsPath+"/app/getAppList", {"pageSize":5,"whereStr":appWhere}, function(data) {
 			     var appList ="";
 			       if(data!=null){
 			         var total = data.total;     
 			         var rows = data.rows;    
 			         if(rows!=null&&rows.length>0){
 				      for(var i =0; i<rows.length;i++ ){
 				    	  var index = i;//此处 为展示顺序，必须要
 				    	  var appStatus ="";
 	 				        if(rows[i].appStage==0){
 	 				             appStatus="开发阶段";
 	 				        }
 	 				        if(rows[i].appStage==1){
 	 				             appStatus="生成阶段";
 	 				        }
 				    	  if(i==0){
 				    		  appList = appList+"<li  class='"+divItemSelect+"' onclick=\"window.location='/app/detail.jsp?appId="+rows[i].appId+"'\">";
 	 				        
 				    	  }else{
 				    		  appList = appList+"<li onclick=\"window.location='/app/detail.jsp?appId="+rows[i].appId+"'\">";
 	 				        
 				    	  }
 				    	
 				    	    if(rows[i].appIcon==null){
	 				        	 appList = appList+"  <div class='app_icon' style='background-image: url(/resources/images/default_app_icon_57x57.png);'></div>";
	 				         }else{
	 				        	 appList = appList+"  <div class='app_icon' style='background-image:url("+appPath+rows[i].appId+"/"+rows[i].appIcon+");'></div>";
	 				         }
	 			             appList = appList+"  <h3>"+rows[i].appName+"</h3>";
	 			             appList = appList+"  <p class='meta'>"+appStatus+"</p>";
	 			             appList = appList+"   <p class='meta'><em><span class='app_counts_ajax' data-key='"+rows[i].appKey+"' data-type='devices'>"+rows[i].activeUserPer+"</span></em> 活跃用户数</p>";
	 			             appList = appList+"</li>"+
	 			            "<input type='hidden' name='d_index' value='" +
                            index +
                            "' />" ;//位置，勿动
	 			            appList = appList+"<input type='hidden' name='h_appid' value='" +rows[i].appId +"' />"; 
 				        } 
 			         }
 			       }
 			      //展示层，并展示数据
 		             $("#" + showDataDivId).html(appList).slideDown(200);
 		             //注册事件
 		             registerInputEvent();
 			        $('#app_total').html("("+total+")");
 			    });
            
         }
         /*else {
             $("#" + showDataDivId).slideUp(200);
         }*/
     });
		//.blur(function(){$("#" + showDataDivId).slideUp(200);});
		/**
		 * 键盘操作  向上 或向上 
		 * @param {Object} opt   向上 -1  向下 1 
		 */
     function chageSelect(opt){
         if ($("#id_menu_app_list").css('display') == 'block') {
             var obj = $("#id_menu_app_list li[class='" + divItemSelect + "']");
             
             if (obj.html() == null) {//当前还未选中。
            	 
                 if (opt == 1) {
                     $("#" + showDataDivId + " li:first").addClass(divItemSelect);
                 }
                 else {
                     $("#" + showDataDivId + " li:last").addClass(divItemSelect);
                 }
             }
             else {
                 var curr = parseInt($("#" + showDataDivId + " li[class='" + divItemSelect + "'] ~ input[name='d_index']").val()) + opt;
                 var divCount = $("#" + showDataDivId + " li").size();
               
                 $("#" + showDataDivId + " li[class='" + divItemSelect + "']").removeClass(divItemSelect);
                 $("#" + showDataDivId + " li:eq(" + ((curr < 0) ? (divCount - 1) : ((curr == divCount) ? 0 : curr) + ")")).addClass(divItemSelect);
             }
         }
     }
		/**
		 * 注册事件
		 */
     function registerInputEvent(){
         $("#" + showDataDivId + " li").click(function(){
             item_click($(this));
         }).mouseover(function(){
             $("#" + showDataDivId + " li[class='" + divItemSelect + "']").removeClass(divItemSelect);
             $(this).addClass(divItemSelect);
         }).mouseout(function(){
             $(this).removeClass(divItemSelect);
         });
     }
     /**
      * 点击每一项的操作
      * @param {Object} obj
      */
     function item_click(obj){
         if (obj.html() == null) {//如果是按回车键。。。
        	var h_appid =  $("#" + showDataDivId + " li[class='" + divItemSelect + "'] ~ input[name='h_appid']").val();
        	if(h_appid!=null){
	        	 if(parseInt(h_appid)+"" != "NaN"){
	        		 window.location='/app/detail.jsp?appId='+parseInt(h_appid);
	        	 }else{
	        	 }
        	}
         }
         else {  
         }
			$("#" + showDataDivId).hide();
     }
});