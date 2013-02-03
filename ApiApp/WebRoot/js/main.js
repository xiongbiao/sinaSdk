function setContent2(loc) {
	$("#cont").load("${pageContext.request.contextPath}/ggad/"+loc);
	//alert($("#cont").innerHTML);
}
var gain=function(id){
	var obj =document.getElementById(id);
	if(undefined==obj || null==obj){
		alert("id="+id+"的元素不存在！");
	}
	return obj;
}
var gainValue=function(id){
	return gain(id).value;
}
/**
 * 火狐 谷歌浏览器兼容IE的outerHTML
 */
 if(window.HTMLElement) {
    HTMLElement.prototype.__defineSetter__("outerHTML",function(sHTML){
        var r=this.ownerDocument.createRange();
        r.setStartBefore(this);
        var df=r.createContextualFragment(sHTML);
        this.parentNode.replaceChild(df,this);
        return sHTML;
        });

    HTMLElement.prototype.__defineGetter__("outerHTML",function(){
     var attr;
        var attrs=this.attributes;
        var str="<"+this.tagName.toLowerCase();
        for(var i=0;i<attrs.length;i++){
            attr=attrs[i];
            if(attr.specified)
                str+=" "+attr.name+'="'+attr.value+'"';
            }
        if(!this.canHaveChildren)
            return str+">";
        return str+">"+this.innerHTML+"</"+this.tagName.toLowerCase()+">";
        });
        
 HTMLElement.prototype.__defineGetter__("canHaveChildren",function(){
  switch(this.tagName.toLowerCase()){
            case "area":
            case "base":
         case "basefont":
            case "col":
            case "frame":
            case "hr":
            case "img":
            case "br":
            case "input":
            case "isindex":
            case "link":
            case "meta":
            case "param":
            return false;
        }
        return true;

     });
}
// 使用方法 ：onchange='fileValidating(this, "jpg;png;bmp;gif")'
function fileValidating(obj, supportFile) {
	var files = supportFile.split(";");
	var checkedPic = obj.value;
	var unSupport = true;
	for (i=0;i<files.length;i++) {
		var suffix = checkedPic.substring(checkedPic.lastIndexOf(".")+1, checkedPic.length);
		if (suffix==files[i].toUpperCase()||suffix==files[i].toLowerCase()) {
			unSupport = false;
			break;
		}
	}
	supportFile = supportFile.replace(";", "、");
	if (unSupport){
		var obj =obj
		obj.outerHTML = obj.outerHTML;  //清空<input type="file">字段的value 避免不必要的文件上传到服务器！
		alert("请选择 "+supportFile+" 格式文件");
	}
}
function loadJS(url){
var domScript=document.createElement('script');
domScript.src=url;
document.getElementsByTagName('head')[0].appendChild(domScript);
domScript=null;
}