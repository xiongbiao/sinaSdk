(function($){
    $.fn.ELGrid = function(){
        if ( typeof this!='object'){ new Exception('not a object'); }        
        if (arguments.length==0) { new Exception('arguments incorrect.'); }

        var options = arguments[0];
        var params = $(arguments).slice(1);

        function __initGrid(grid, opts){
            var grid_id = grid.attr('id');
            var layout = [];
            layout.push('<div id="' + grid_id + 'Mask"></div>');
            layout.push('<div id="' + grid_id + 'Body" class="el-gridBody">');
            layout.push('<div id="' + grid_id + 'FixedLayout" class="el-gridFixed"></div>');
            layout.push('<div id="' + grid_id + 'FixedHeader" class="el-gridHeader"></div>');
            layout.push('<div id="' + grid_id + 'FixedCol" class="el-gridFixCol"></div>');
            layout.push('<div id="' + grid_id + 'DataLayout" class="el-gridDataLayout"></div>');
            layout.push('</div>');
            layout.push('<div id="' + grid_id + 'Footer" class="el-gridFooter">');
            layout.push('<a href="#" class="page-link" id="page-prev"></a>');
            layout.push('<span id="pageInfo">当前:&nbsp;&nbsp;0 / 0&nbsp;&nbsp;&nbsp;&nbsp;总记录数:&nbsp;&nbsp;0&nbsp;&nbsp;0~0  </span>');
            layout.push('<a href="#" class="page-link" id="page-next"></a>');
            layout.push('<a href="#" class="page-link" id="page-refresh"></a>');
            layout.push('</div>');
            grid.append(layout.join(''));
            grid.addClass('el-grid');
            var width = opts.width;
            var height = opts.height;

            if ( width && $.isNumeric(width) ){
                grid.width(width);
                grid.find('#'+grid_id+'Body').width(width-2);
                grid.find('#'+grid_id+'DataLayout').width(width-2);
            }
            if ( height && $.isNumeric(height) ){
                grid.height(height);
                gridHeight = height - grid.find('#'+grid_id+'Footer').height();
                grid.find('#'+grid_id+'DataLayout').height(gridHeight);
            }

            grid.find('a.page-link').click(__changePage);

            var cols = opts.colModel;
            var table = __createTable(cols);
            grid.find('.el-gridDataLayout').append(table.join(''));
            
            _pb = grid.find('#' + grid_id + 'Mask').ELProcessBar({width:250, height:50});
        }

        function __createTable(cms){
            var headCols = ['<thead><tr>'];
            var width=0;
            $.each(cms, function(i, o){
                var field = o.field;
                var id = o.id || field || ('col'+i);
                var text = o.label;
                var w = o.width;
                if ($.isNumeric(w)){
                    width += w*1;
                }
                headCols.push('<th id="' + id + '" style="' + ($.isNumeric(w)?('width:' + w + 'px'):'')+'">' + text + '</th>');
            });

            headCols.push('</tr></thead>');
            var table = ['<table id="' + _gridId + 'DataGrid" style="width:'+width+'px">'];
            $.merge(table, headCols);
            $.merge(table, __createBody([], _opts));
            table.push('</table>');
            return table;
        }

        function __createBody(datas, opts){
            var records = datas || [];
            var body = ['<tbody>'];
            var pagesize = opts.pageSize;
            var fields = opts.colModel;
            var colsize = fields.length;
            var recordsCount = records.length;
            var spaceCount = pagesize - recordsCount;

            for(var i=recordsCount; i<pagesize; i++){
                var fs = fields;
                var record = {};
                $.each(fs, function(i, f){
                    record[f.field] = '-';
                });
                records.push(record);
            }
            var list = __createTr(fields, records);
            $.merge(body, list);
            body.push('</tbody>');

            return body; 
        } 

        function __createFooter(opts){
            var foot = [''];

            foot.push('');
            return foot;
        }

        function __createTr(fields, records){
            var trList = [];

            if (!(records instanceof Array)){
                records = [records];
            }

            $.each(records, function(i, v){
                var line = trList;
                var fs = fields;
                
                line.push('<tr class="' + (i%2==0?'even':'odd') + '">');
                $.each(fs, function(j, f){
                    var val = v[f.field];
                    if (val=='-'){
                        val = '&nbsp;';
                    }else if (typeof f.renderer == 'function'){
                        val = f.renderer(val);
                    }
                    val = (typeof val=='undefined' || val==null) ? '' : val;
                    line.push('<td class="' + (f.lock?'fixCol':'') + '">' + val + '</td>')
                }) 
                line.push('</tr>');
            });
            return trList;
        }

        function __sendRequest(url, params, callback){
            var handler = _ajaxHandler;
            if (!!_ajaxHandler){ return false; }
            if (!params || !url){
                new Exception('no parameters')
                return false;
            }
            var $this = this;
            __setLoadingStatus('loading');
            _ajaxHandler = $.ajax({
                url: url,
                data: params,
                type: 'post',
                dataType: 'json',
                complete:function(){
                    _ajaxHandler = null;
                    __setLoadingStatus('complete');
                },
                success: function (retData) {
                    _currentIndex = params.pageIndex;
                    _pageSize = params.pageSize;
                    $.extend(_lastParams, params);
                    callback.call($this, retData, params, _opts);
                    _ajaxHandler = null;
                }
            });
        }

        function __setLoadingStatus(s){
            switch(s){
                case 'loading':
                    _pb.start();
                    break;
                case 'complete':
                    _pb.stop();
                    break;
            }
        }

        function __reDrawGrid(retData, params, opts){
            var footer = _fixFooter;
            var dataLayout = _dataLayout;
            var total = retData.total;
            var pageSize = params.pageSize;
            var pageIndex = params.pageIndex;
            var start = pageSize*(pageIndex-1)+1;
            var end = Math.min(pageSize*pageIndex, total);
            var totalPage = Math.floor(total / pageSize);
            if (totalPage*pageSize < total){ totalPage+=1; }
            _totalPage = totalPage;

            dataLayout.find('tbody').remove();
            dataLayout.find('table').append(__createBody(retData.rows, opts).join(''));

            var btnPrev = footer.find('a#page-prev');
            var btnNext = footer.find('a#page-next');
            if (pageIndex > 1){
                btnPrev.removeClass('link-disabled');
            }else{
                btnPrev.addClass('link-disabled');
            }
            if (pageIndex < totalPage){
                btnNext.removeClass('link-disabled');
            }else{
                btnNext.addClass('link-disabled');
            }

            var pageInfo = ['当前:&nbsp;&nbsp;'];
            pageInfo.push(pageIndex + ' / ' + totalPage);
            pageInfo.push('&nbsp;&nbsp;&nbsp;&nbsp;');
            pageInfo.push('总记录数:&nbsp;&nbsp;');
            pageInfo.push(total);
            pageInfo.push('&nbsp;&nbsp;&nbsp;&nbsp;');
            pageInfo.push(start + '~' + end);
            footer.find('span#pageInfo').html(pageInfo.join(''));

            //var grid = dataLayout.
            //__fixTable();
        }

        function __changePage(){
            var id=this.id;
            var index = _currentIndex;
            var pageSize = _pageSize;

            switch(id){
            case 'page-prev':
                if (index<=1){ return false; }
                index--;
                __goToPage(index, pageSize);
                break;
            case 'page-next':
                if (index>=_totalPage){ return false; }
                index++;
                __goToPage(index, pageSize);
                break;
            case 'page-refresh':
            	__reload();
            	break;
            }
            return false;
        }

        function __goToPage(pageIndex, pageSize){
            var params = $.extend({}, _baseParams, { 'pageSize':pageSize, 'pageIndex':pageIndex });
            __sendRequest(_url, params, __reDrawGrid);
        }

        function __fixTable(){
            var opts = _opts;
            var fields = opts.colModel;
            var isFixHead = opts.fixHead;
            var fixColCount = opts.fixColumns;
            var isFixCol = ($.isNumeric(fixColCount) && fixColCount>0);

            var divGrid=_dataLayout, divHead=_fixHeader, divFix=_fixLayout, divCol=_fixCol, divBody=_fixBody;
            var tblGrid=null, tblHead=null, tblFix=null, tblFix=null;

            tblGrid = divGrid.find('table');
            tblHead = tblGrid.clone(true);
            tblHead.attr('id', tblGrid.attr('id')+'_Head');
            divHead.append(tblHead);
            tblFix = tblGrid.clone(true)
            tblFix.attr('id', tblGrid.attr('id')+'_Fix');
            divFix.append(tblFix);
            tblCol = tblGrid.clone(true)
            tblCol.attr('id', tblGrid.attr('id')+'_Col');
            divCol.append(tblCol);

            var headHeight = tblGrid.find('thead').height() + 2;
            divHead.height(headHeight);
            divFix.height(headHeight);
            var fixColWidth = 0;
            $.each(fields.slice(0, fixColCount), function(i, f){
                fixColWidth += f.width;
            });
            fixColWidth += 2;
            if ($.browser.msie){
                switch ($.browser.version){
                    case "7.0":
                        if (fixColCount >= 3) fixColWidth--;
                        break;
                    case "8.0":
                        if (fixColCount >= 2) fixColWidth--;
                        break;
                }
            }

            divFix.width(fixColWidth);
            divCol.width(fixColWidth);
            divGrid.scroll(function(){
                divHead.scrollLeft($(this).scrollLeft());
                divCol.scrollTop($(this).scrollTop());
            });


            var width = divBody.width();
            var height = divBody.height();
            divFix.css({ "overflow": "hidden", "position": "relative", "z-index": "50", "background-color": "Silver" });  
            divHead.css({ "overflow": "hidden", "width": width - 17, "position": "relative", "z-index": "45", "background-color": "Silver" });  
            divCol.css({ "overflow": "hidden", "height": height - 17, "position": "relative", "z-index": "40", "background-color": "Silver" });  
            divGrid.css({ "overflow": "scroll", "width": width, "height": height, "position": "relative", "z-index": "35" });  
            
            if (divHead.width() > divFix.find('table').width()) {  
                divHead.css("width", divFix.find('table').width());  
                divGrid.css("width", divFix.find('table').width() + 20);  
            }
            // if (divCol.height() > divCol.find('table').height()) {
            if (divGrid.height() > divGrid.find('table').height()) {
                divCol.css("height", divGrid.find('table').height());  
                divGrid.css("height", divGrid.find('table').height() + 20);  
            }
            divFix.offset({left:0, top:0});//divBody.offset());  
            divHead.offset({left:0, top:divFix.height()*(-1)});//divBody.offset());  
            divCol.offset({left:0, top:divFix.height()*(-2)});//divBody.offset());  
            var offset = divBody.offset();
            divGrid.offset({left:offset.left, top:offset.top*(-1)+17});//divBody.offset()); 

            divFix.show();
            divCol.show();
            divHead.show();
            divGrid.show();
        }

        function __loadData(options){
            var params = $.extend(_baseParams, { 'pageIndex':1 }, options);
            __sendRequest(_url, params, __reDrawGrid);
        }

        function __loadPage(options){
        }

        function __reload(){
            __goToPage(_currentIndex, _pageSize);
        }

        var _methods = {
            'load' : __loadData,
            'loadpage' : __loadPage,
            'reload' : __reload
        };

        var _defaults = {
            pageSize:10,
            loading:'loading...',
            params:{}
        };
        var _opts = $.extend({}, _defaults, options);
        var _cols = _opts.colModel;
        var _g = $(this);
        var _pb = null;
        var _gridId = _g.attr('id');
        var _ajaxHandler = null;
        var _pageSize = _opts.pageSize;
        var _total = 0;
        var _totalPage =0;
        var _currentIndex = 1;

        __initGrid(_g, _opts);
        var _fixLayout = _g.find('.el-gridFixed');
        var _fixHeader = _g.find('.el-gridHeader');
        var _fixCol = _g.find('.el-gridFixCol');
        var _fixFooter = _g.find('.el-gridFooter');
        var _dataLayout = _g.find('.el-gridDataLayout');
        var _fixBody = _g.find('.el-gridBody');

        var _baseParams = $.extend({}, { 'pageIndex':_currentIndex, 'pageSize':_pageSize },  _opts.params);
        var _lastParams = {};
        $.extend(_lastParams, _baseParams);
        var _url = _opts.url; 
        __sendRequest.call(this, _url, _lastParams, __reDrawGrid);

        var returnObj = $.extend({}, { grid:_g }, _methods);
        return returnObj;
    };
    
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
            var pb = _pb;
            var pbbg = _pbBg;

             
            pbbg.show(); 
            pb.show();
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
