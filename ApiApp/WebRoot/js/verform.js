
 //弹出信息样式设置
Vanadium.config = {
    valid_class: 'rightformcss',//验证正确时表单样式
    invalid_class: 'failformcss',//验证失败时该表单样式
    message_value_class: 'msgvaluecss',//这个样式是弹出信息中调用值的样式
    advice_class: 'failmsg',//验证失败时文字信息的样式
    prefix: ':',
    separator: ';',
    reset_defer_timeout: 100
}
//验证类型及弹出信息设置
Vanadium.Type = function(className, validationFunction, error_message, message, init) {
  this.initialize(className, validationFunction, error_message, message, init);
};
Vanadium.Type.prototype = {
  initialize: function(className, validationFunction, error_message, message, init) {
    this.className = className;
    this.message = message;
    this.error_message = error_message;
    this.validationFunction = validationFunction;
    this.init = init;
  },
  test: function(value) {
    return this.validationFunction.call(this, value);
  },
  validMessage: function() {
    return this.message;
  },
  invalidMessage: function() {
    return this.error_message;
  },
  toString: function() {
    return "className:" + this.className + " message:" + this.message + " error_message:" + this.error_message
  },
  init: function(parameter) {
    if (this.init) {
      this.init(parameter);
    }
  }
};
Vanadium.setupValidatorTypes = function() {
Vanadium.addValidatorType('empty', function(v) {
    return  ((v == null) || (v.length == 0));
  });
//***************************************以下为验证方法,使用时可仅保留用到的判断
Vanadium.addValidatorTypes([
	 
    //是否为空
    ['required', function(v) {
      return !Vanadium.validators_types['empty'].test(v);
    }, '此项不可为空!'],
    //强制选中 
    ['accept', function(v, _p, e) {
      return e.element.checked;
    }, '为了权益你请接受!'],
    //
    ['integer', function(v) {
      if (Vanadium.validators_types['empty'].test(v)) return true;
      var f = parseFloat(v);
      return (!isNaN(f) && f.toString() == v && Math.round(f) == f);
    }, '请输入一个正确的整数值.'],
    //数字
    ['number', function(v) {
      return Vanadium.validators_types['empty'].test(v) || (!isNaN(v) && !/^\s+$/.test(v));
    }, '请输入一个正确的数字.'],
    
	//邮箱验证
    ['email', function (v) {
      return (Vanadium.validators_types['empty'].test(v) || /\w{1,}[@][\w\-]{1,}([.]([\w\-]{1,})){1,3}$/.test(v))
    }, '请输入正确邮箱'],
    
     
    //
    ['min_length',
      function (v, p) {
        if (p === undefined) {
          return true
        } else {
          return v.length >= parseInt(p)
        }
        ;
      },
      function (_v, p) {
      
        return '输入字符长度不能低于' + p + '个.'
      }
    ],
    ['max_length',
      function (v, p) {
        if (p === undefined) {
          return true
        } else {
          return v.length <= parseInt(p)
        }
        ;
      },
      function (_v, p) {
        return '输入字符长度不大于' + p + '个'
      }
    ],
	//判断密码是相同
    ['same_as',
      function (v, p) {
        if (p === undefined) {
          return true
        } else {
          var exemplar = document.getElementById(p);
          if (exemplar)
            return v == exemplar.value;
          else
            return false;
        };
      },
      function (_v, p) {
        var exemplar = document.getElementById(p);
        if (exemplar)
          return '两次密码输入不相同.';
        else
          return '没有可参考值!'
      },
      "",
      function(validation_instance) {
        var exemplar = document.getElementById(validation_instance.param);
        if (exemplar){
          jQuery(exemplar).bind('validate', function(){
            jQuery(validation_instance.element).trigger('validate');
          });
        }
      }
    ],
  //ajax判断是否存在值
   ['ajax',
      function (v, p, validation_instance, decoration_context, decoration_callback) {
        if (Vanadium.validators_types['empty'].test(v)) return true;
        if (decoration_context && decoration_callback) {
          jQuery.post(p, {value: v, id: validation_instance.element.id}, function(data) {
          
           
           var resultObj = $("#resultName");
   			//动态改变节点的内容
           resultObj.html(data.info);
           
            decoration_callback.apply(decoration_context, [[data], true]);
          });
        }
        return true;
      }]
    ,
    ['emailajax',
      function (v, p, validation_instance, decoration_context, decoration_callback) {
        if (Vanadium.validators_types['empty'].test(v)) return true;
        if (decoration_context && decoration_callback) {
          jQuery.post(p, {value: v, id: validation_instance.element.id}, function(data) {
          
           
           var resultObj = $("#resultEmail");
   			//动态改变节点的内容
           resultObj.html(data.info);
           
            decoration_callback.apply(decoration_context, [[data], true]);
          });
        }
        return true;
      }]
    ,
    //apk包验证
    ['apkajax',
      function (v, p, validation_instance, decoration_context, decoration_callback) {
        if (Vanadium.validators_types['empty'].test(v)) return true;
        if (decoration_context && decoration_callback) {
          jQuery.post(p, {value: v, id: validation_instance.element.id}, function(data) {
          
           
           var resultObj = $("#appPackageTip");
   			//动态改变节点的内容
           resultObj.html(data.info);
           
            decoration_callback.apply(decoration_context, [[data], true]);
          });
        }
        return true;
      }]
    ,
    	//正则匹配,此用法不甚理解
    ['format',
      function(v,p) {
     
        var params = p.match(/^\/(((\\\/)|[^\/])*)\/(((\\\/)|[^\/])*)$/);    
        if(v.length==0){
          return true;
        }       
        if (params.length == 7) {
          var pattern = params[1];
          var attributes = params[4];
          try
          {
            var exp = new RegExp(pattern, attributes);
            return exp.test(v);
          }
          catch(err)
          {
            return false
          }
        } else {
          return false
        }
      },
      function (_v, p) {
       
        var params = p.split('/');
        if (params.length == 3 && params[0] == "") {
          return '请输入正确的手机';
        } else {
          return '请输入正确的手机';
        }
      }
    ]
    ,
     ['qq_format',
      function(v,p) {
       if(v.length==0){
          return true;
        }    
        var params = p.match(/^\/(((\\\/)|[^\/])*)\/(((\\\/)|[^\/])*)$/);           
        if (params.length == 7) {
          var pattern = params[1];
          var attributes = params[4];
          try
          {
            var exp = new RegExp(pattern, attributes);
            return exp.test(v);
          }
          catch(err)
          {
            return false
          }
        } else {
          return false
        }
      },
      function (_v, p) {
        var params = p.split('/');
        if (params.length == 3 && params[0] == "") {
          return '请输入正确的QQ';
        } else {
          return '请输入正确的QQ';
        }
      }
    ]
    
  ])
  if (typeof(VanadiumCustomValidationTypes) !== "undefined" && VanadiumCustomValidationTypes) Vanadium.addValidatorTypes(VanadiumCustomValidationTypes);
};

