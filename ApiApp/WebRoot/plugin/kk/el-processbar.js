/*
 * jQuery ELProcessBar plugin
 * Version 1.0.0 (06/05/2012)
 * @requires jQuery v1.7.0 or later
 *
 * Copyright (c) 2012 Eddie Lin
 
*/
(function($){
    $.fn.ELProcessBar = function(){
        if ( typeof this!='object'){ new Exception('not a object'); }        
        if (arguments.length==0) { new Exception('arguments incorrect.'); }
        var options = arguments[0];
        function __initProcessBar(pb, opts){
            pb.addClass('processBarOuter');
            var w = opts.width;
            var h = opts.height;
            if (!!w){
                pb.width(w);
            }
            if (!!h){
                pb.height(h);
            }
            pb.css('display','none');
            var pbInner = [];
            pbInner.push('<div class="processTitle" style="display:' + (opts.showText?'':'none') + '">' + opts.title + '</div>');
            pbInner.push('<div class="processBarInner">');
            pbInner.push('<div class="finish"></div>');
            pbInner.push('<div class="notfinish"></div>');
            pbInner.push('</div>')
            pb.append(pbInner.join(''));
            var p = pb.parent();
            pw = p.width();
            ph = p.height();
            var po = p.offset();
            pb.offset({left:po.left + (pw-pb.width())/2, top:po.top +(ph-pb.height())/2});
            var pbbg = $('<div class="processBg"></div>');
            if (opts.mask){
                pbbg.insertBefore(pb);
            }
            pbbg.offset(p.offset());
            pbbg.width(pw);
            pbbg.height(ph);
        }
        function __start(){
            var $this = this;
            _pbBg.show(); 
            _pb.show();
            _processHandler = setInterval(function(){
                __processing.call($this);
            }, _opts.interval)
        }
        function __processing(){
            var opts = _opts;
            var step = opts.step;
            var cur = _current + step;
            if (cur>100){ cur = 0; }
            _current = cur;
            _finishBar.width(cur+'%');
            _notFinishBar.width((100-cur) + '%')
        }
        function __stop(){
            clearInterval(_processHandler);
            _current = 0;
            _pb.hide();
            _pbBg.hide();
        }

        var _methods = {
            'start' : __start,
            'stop' : __stop
        };

        var _defaults = {
            step:5,
            interval:100,
            showText:false,
            title:'loading',
            loading:'',
            mask:true,
            width:250,
            height:80,
            params:{}
        };
        var _opts = $.extend({}, _defaults, options);
        var _pb = $(this);
        var _pbId = _pb.attr('id');
        var _processHandler = null;
        var _currentIndex = 1;

        __initProcessBar(_pb, _opts);
        var _finishBar = _pb.find('div.finish');
        var _notFinishBar = _pb.find('div.notfinish');
        var _pbBg = _pb.parent().find('div.processBg');
        var _current = 0;

        var returnObj = $.extend({}, {pb: _pb}, _methods );
        return returnObj;
    };
})(jQuery);