$(function(){
	$('button').not('[class="btn"]').mouseover(function(){ $(this).addClass('hover'); });
	$('button').not('[class="btn"]').mouseout(function(){ $(this).removeClass('hover'); });
//	$('input').mouseover(function(){ $(this).addClass('focus'); });
//	$('input').focus(function(){ $(this).attr('focus', true); $(this).addClass('focus'); });
//	$('input').mouseout(function(){
//		if (!$(this).attr('focus')){
//			$(this).removeClass('focus');
//		}
//	});
//	$('input').blur(function(){ $(this).attr('focus', false); $(this).removeClass('focus'); });
	$('a.checkbox').click(function(){
		if (!$(this).attr('checked')){
			$(this).attr('checked', true);
			$(this).addClass('checkbox-check');
		}else{
			$(this).attr('checked', false);
			$(this).removeClass('checkbox-check');			
		}
	});
	
	
	$('#nav>li.item').mouseover(function(){
		$(this).addClass("current");
	});
	$('#nav>li.item').mouseout(function(){
//		alert($(this).attr('current'));
		if ($(this).attr("current")=="false") {
			$(this).removeClass("current");
		}
	});
});

//该函数解决iE下路径问题。兼容ie6，7，firefox add by exceljava 2010-1-6
function getPath(obj){
	if(obj){
		if (window.navigator.userAgent.indexOf("MSIE")>=1){
			obj.select();
			return document.selection.createRange().text;
		}else if(window.navigator.userAgent.indexOf("Firefox")>=1){
			if(obj.files){
				return obj.files.item(0).mozFullPath;
			}
			return obj.mozFullPath;
		}
		return obj.value;
	}
}