/*
 * jQuery Progress Bar plugin
 * Version 1.1.0 (06/20/2008)
 * @requires jQuery v1.2.1 or later
 *
 * Copyright (c) 2012 Gary Teo
 * http://sin180.com
 */
(function($) {
	$.fn.grid =function(options){
		var t = this;
		return $.addGrid(t, options);
	};


	$.addGrid = function(t,options) { 
      
		var size = !!options.size?10:options.size;				
		var index =1;						 
		var pageS;
		var msg= options.msg;
		var datafile=[];		
		var widths=[];
		var data,total; 	
		var url = options.url;	     //链接地址
		var headfile =options.colModel;//标题
		var width = options.width ;
		var params=options.params ;//参数
        //头部创建
        _showhead = function(_this){
            var $this = _this; 
           
            var hf=headfile;
            var hfStr="";
				if(hf.length>0){ 
				    hfStr="<thead><tr class='head'>"; 
					for(var i = 0; i < hf.length; i++){
                        var width= hf[i].width==null?90:hf[i].width; 
						hfStr=hfStr+"<td width='"+width+"'>" +
                        hf[i].headfile
                                + "</td>";
                           datafile[i]=hf[i].dataflie;
                           widths[i]=hf[i].width;
						}
						hfStr=hfStr+"</tr></thead>"  ;
					  }
			   $this.append(hfStr);
       }
       
      function recurJson(json,key)
	  {  
		  for(var i in json){
			  if(i==key){ 
			   return json[i];
			  } 
			  if(typeof json[i]=="object"){
			    recurJson(json[i],key);
			  }
		  }
		}    
       //内容创建
       _showContent= function(_this){
    	   $(_this).find('tbody').remove();
    	   var $this = _this;

//    	   var conStr=" <tbody id='content'> ";
    	   var tbodyStr = ['<tbody id="content">'];
           if(data!=null && data.length>0){
        	   for(var i = 0; i < data.length; i++){
//                    conStr += "<tr " + + ">";
                   tbodyStr.push('<tr class="');
                   tbodyStr.push(i%2==0?'eve':'odd');
                   tbodyStr.push('">');

                     for(var j=0 ; j<datafile.length;j++ ){
                       var val= recurJson(data[i],datafile[j]); 
                        var renderer =  options.colModel[j].renderer;
                       // alert(renderer)
	                    if(renderer!=null&&renderer!='undefined'){
	                      val=renderer (val); 
	                    }
                       var width= widths[j]==null?90:widths[j];
                       tbodyStr.push('<td width="');
                       tbodyStr.push(width);
                       tbodyStr.push('" class="grid_td">');
                       tbodyStr.push(val);
                       tbodyStr.push('</td>');
//                       conStr=conStr+"<td width='"+width+"' class='grid_td'>" +
//                                val +
//                                "</td>";
                      }
                     tbodyStr.push('</tr>');
//			    conStr=conStr+"</tr>"; 
			}
          } 
           tbodyStr.push('</tbody>');
//           conStr=conStr+" </tbody> ";
//            $this.append(conStr);
           
           $this.append(tbodyStr.join(''));
       }
       //没有数据
       _showNoContent= function(_this){
          var $this = _this; 
          //清楚表格中内容
          $('#content').remove();    
          var conStr=" <tbody id='content'> <tr> <td colspan="+datafile.length+" class='grid_notd'><span style='color: #4975B7;'>没有任何可显示的结果</span> </td> </tr>  </tbody> ";
            $this.append(conStr);	  
       }
       
       
       _showPager = function (_this){
         var $this = _this;            
         $this.append('<tfoot id="foot"> <tr> <td colspan="'+datafile.length+'"><div id="pager" class="yahoo2" style="width:'+width+'px;"></div></td> </tr></tfoot>'); 
         
       }
       //创建分页
       _showPage = function(_this ){
    	   var $this = _this;
           var $pager = $("#pager");
                         
               //清楚分页div中的内容
            $("#pager span").remove();
            $("#pager a").remove();  
            
            //添加第一页
            if (index == 1){
               $pager.append("<span class='disabled'>首页</span>");
              }else {
               var first = $("<a href='javascript:void(0)' first='" + 1 + "'>首页</a>").click(function () {
                               index = 1;
                                $('#content').hide();
                               _ajax(params);
                               return false;
                  });
                  $pager.append(first);
            }
            
            //添加上一页
            if (index == 1){
                $pager.append("<span class='disabled'>上一页</span>");
              }else {
                var pre = $("<a href='javascript:void(0)' pre='" + (index - 1) + "'>上一页</a>").click(function () {
                          index = index-1;
                           $('#content').hide();
                          _ajax(params);     
                        return false;
                    });
                 $pager.append(pre);
            }
             //设置分页的格式  这里可以根据需求完成自己想要的结果
               var interval = 0; //设置间隔
               var start = Math.max(1, index - interval); //设置起始页
               var end = Math.min(index + interval, pageS)//设置末页

               if (index < interval + 1) {
                    end = (2 * interval + 1) > pageS ? pageS : (2 * interval + 1);
                  }

               if ((index + interval) > pageS) {
                   start = (pageS - 2 * interval) < 1 ? 1 : (pageS - 2 * interval);
                 }
                //生成页码
                 for (var j = start; j < end + 1; j++) {
                   if (j == index) {
                    var spanSelectd = $("<span class='current'>" + j + "</span>");
                        $pager.append(spanSelectd);
                            } //if 
                            else {
                                var a = $("<a href='javascript:void(0)'>" + j + "</a>").click(function () {
                                  $('#content').hide();
                                     alert(j)
                                    return false;
                                });
                         $pager.append(a);
                    } //else
                 } //for

  	            //下一页
  	           if (index == pageS) {
                    $pager.append("<span class='disabled'>下一页</span>");

                } 
                else {
                    var next = $("<a href='javascript:void(0)' next='" + (index + 1) + "'>下一页</a>").click(function () {
                        index = index+1;
                        $('#content').hide();
                         _ajax(params);  
                        return false;
                    });
                    $pager.append(next);
                } 
                //最后一页
                if (index == pageS) {
                    $pager.append("<span class='disabled'>末页</span>"); 
                }
                else {
                    var last = $("<a href='javascript:void(0)' last='" + pageS + "'>末页</a>").click(function () {
                        index = pageS;
                         $('#content').hide();
                         _ajax(params);
                        return false;
                    });
                    $pager.append(last);
                }

        } 
        
        
        
        _show=function(_this){
             _showhead(_this);
             _loading(_this);
             this._ajax(params); 
             _showPager(_this);
           
        }
        //创建loading数据
        _loading = function(_this){
           var $this = _this;  
		   var  loadingStr=' <tbody id="loading" style="display: none;" ><tr><td  colspan="'+datafile.length+'"><div id="msg"  class="fakewindowcontain" style=" width:'+width+'px; text-align: center;" >'+							 
							'<div class="ui-widget-shadow ui-my-shadow" ></div>'+
							'<div class="ui-widget ui-widget-content ui-my ">'+
							'<div class="ui-dialog-content ui-widget-content ui--my-dialog-content" >'+
							'<div id="progressbar"></div><br /><span id="msginfo"> '+msg+' </span>'+
							'</div></div> </div></td></tr> </tbody>'; 
		   $this.append(loadingStr); 
        }
        
        _loadingShow = function(){
    	   $("#loading").show();
       } 
       
       _loadingHide = function(){
         $("#loading").hide();
       }
      
      addJson =  function (json,newJson)
	  {  
		  for(var i in json){ 
			  newJson[i]=json[i];
			  if(typeof json[i]=="object"){
			    recurJson(json[i],key);
			  }
		  }
		  return newJson;
		}    
       
       var obj=t;
       _ajax = function(myParams){
           params=myParams;
           $(t).find('tbody').remove();
           _loadingShow();
           //构造参数
            var mParams={ "pageIndex": index,"pageSize":size  };
            if(myParams!=null){
               addJson(myParams,mParams);
            }else{ 
            }
           $.ajax({
		     url: url,
		     data: mParams,
		     type: "post",
		     dataType: "json",
		     success: function (retData) {
				//清楚表格中内容
					_loadingHide(); 
		    	 try{
				if(retData!=null){
					if(retData.rows!=null&&retData.total!=null){ 
						data=retData.rows;
						total=retData.total;
				        
						if (total % size == 0) {
							pageS = total / size;
						}else{
							pageS = parseInt(total / size) + 1;
						}
						if(total<=0){
							_showNoContent(obj);
						}else{
							_showContent(obj);
							_showPage(obj); 
						}
					}else{
						_showNoContent(obj);				          
					} 
				}else{
					_showNoContent(obj);
				}
		    	 }catch(e){}
		     }
			});    
       
       }  
       _show(t);
        t.aa=50;
        t.bb=90;
        t.ajax =function(params){   
          index=1;
          _ajax(params); 
        }
       return t; 
    } 
 
$.fn.re =function(t,params){
    t.ajax(params);
}
  
})(jQuery);