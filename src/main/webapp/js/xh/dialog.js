KISSY.add('xh/dialog',function(S, Global,Node, O, DragPlugin, ResizePlugin, ConstrainPlugin){
    function Dialog(){
        var _t = this;
        _t.g = new Global();
    }
    S.augment(Dialog,{
    	init:function(_o){
    		_o=_o?_o:{};
    	    var _t = this;
    	    _t.btn=_o.btn?_o.btn:null;
    	    _t.node=_o.node?_o.node:null;
    	    _t.width=_o.width?_o.width:'480';
    	    _t.height=_o.height?_o.height:'auto';
    	    _t.headcont=_o.headcont?_o.headcont:null;
    	    _t.bodycont=_o.bodycont?_o.bodycont:null;
    	    _t.footcont=_o.footcont?_o.footcont:null;
    	    _t.callBack=_o.callBack?_o.callBack:_t.callBack?_t.callBack:function(){};
    	    _t.stat=_o.stat==false?_o.stat:true;
    	    _t.sh=_o.sh==false?_o.sh:true;
    	    _t.mask=_o.mask==false?_o.mask:true;
    	    _t.closable=_o.closable==false?_o.closable:true;
            _t.closeAction  = _o.closeAction == true? _o.closeAction:'destroy';
            _t.render  = _o.render ? _o.render:'body' ;
    	    _t.d = new O.Dialog({
    	    	stat:_t.stat,
    	    	sh:_t.sh,
                srcNode:_t.node ,//指向哪个窗体
                width: _t.width,
                height: _t.height,
                headerContent:_t.headcont,
                bodyContent: _t.bodycont,
                footerContent:_t.footcont,
                 closeAction :_t.closeAction,
                render : _t.render ,
                mask: _t.mask,//是否需要遮罩
                closable: _t.closable,//是否需要关闭
                align: {
                    node:window,
                    points: ['cc', 'cc'],
                    offset:[0,0]
                },
                effect: {//动画效果
                    effect: "slide", //"fade",
                    duration: 0.5
                }
            });
    	    _t.d.plug(new DragPlugin({
                handlers: ['.ks-stdmod-header']
            }).plug(new ConstrainPlugin({
                constrain:window
            }))).plug(new ResizePlugin({
                    minWidth: 150,
                    minHeight: 70,
                    handlers: ["b", "t", "r", "l", "tr", "tl", "br", "bl"]
                }));
                    return _t;
    	},
    	//创建
    	create:function(_o){
    		_o=_o?_o:{};
    	    var _t = this;
    	    _t.node=_o.node?_o.node:null;
			_t.width=_o.width?_o.width:'480';
    	    _t.height=_o.height?_o.height:'auto';
    	    _t.headcont=_o.headcont?_o.headcont:null;
    	    _t.bodycont=_o.bodycont?_o.bodycont:null;
    	    _t.footcont=_o.footcont?_o.footcont:null;
			_t.closable=_o.closable==false?_o.closable:true;
            _t.mask=_o.mask==false?_o.mask:true;
    	    _t.callBack=_o.callBack?_o.callBack:_t.callBack?_t.callBack:function(){};
			_t.closeAction  = _o.closeAction == true? _o.closeAction:'destroy';
            _t.render  = _o.render ? _o.render:'' ;
     	    if(_t.node && _t.bodycont != '' && !_t.d)
	    	{
				(function(n, h, b,c,ca, m,cl,el){
					n.on("click", function (e) {
						_t.init({width:_t.width,height:_t.height,headcont: h,bodycont: b,closable:c,callBack:ca,mask:m,closeAction:cl,render:el }).run();
				   });
				})(_t.node, _t.headcont, _t.bodycont,_t.closable ,_t.callBack, _t.mask,_t.closeAction, _t.render);

	    	}
    	    return _t;
    	},
     	//显示一弹出
    	showDialog:function(_o){
    		_o=_o?_o:{};
    	    var _t = this;
    	    _t.node=_o.node?_o.node:null;
			_t.width=_o.width?_o.width:'480';
    	    _t.height=_o.height?_o.height:'auto';
    	    _t.headcont=_o.headcont?_o.headcont:null;
    	    _t.bodycont=_o.bodycont?_o.bodycont:null;
    	    _t.footcont=_o.footcont?_o.footcont:null;
			_t.closable=_o.closable==false?_o.closable:true;
			_t.selfMark = _o.selfMark;
			_t.mask=_o.mask==true?_o.mask:false;
    	    _t.callBack=_o.callBack?_o.callBack:_t.callBack?_t.callBack:function(){};
			 _t.render  = _o.render ? _o.render:'';
			 _t.closeAction  = _o.closeAction == true? _o.closeAction:'destroy';
			_t.init({width:_t.width,height:_t.height,headcont:_t.headcont,bodycont:_t.bodycont,closable:_t.closable,callBack:_t.callBack,mask:_t.mask,closeAction:_t.closeAction,render:_t.render}).run();
    	    return _t;
    	},
		destroy:function(){
    	    var _t = this;
			_t.d.destroy();
			return _t;
			},

    	run:function(_o){
    		_o=_o?_o:{};
    	    var _t = this;
    	    _t.callBack=_o.callBack?_o.callBack:_t.callBack?_t.callBack:function(){};
    	    _t.d.show();
            _t.callBack();
    	    return _t;
    	},
    	//提示初始化
    	tip:function(_o){
    		_o=_o?_o:{};
    	    var _t = this;
    	    _t.width=_o.width?_o.width:'';
    	    _t.height=_o.height?_o.height:'';
    	    _t.content=_o.content?_o.content:null;
    	    _t.node=_o.node?_o.node:null;
    	     _t.triggerType=_o.triggerType == 'mouse'?_o.triggerType:'click';
    	     _t.render=_o.render?_o.render:_t.node;
    	     _t.popup = new O.Popup({
    	    	trigger:_t.node,
                width: _t.width,
                height: _t.height,
                content:_t.content,
                 render:_t.render,
				triggerType:_t.triggerType,
                align:{
                    node:_t.node,
                    points:['bc', 'tc'],
                    offset:[0, 0]
                }
            });
    	    return _t;
    	},
    	// 鼠标划过提示信息
    	tipInfo:function(_o){
    		_o=_o?_o:{};
    	    var _t = this;
    	    _t.node=_o.node?_o.node:null;
            _t.content=_o.content?_o.content:'';
            _t.fun=_o.fun?_o.fun:function(){};
    	    if(_t.node && _t.content != '' && !_t.popup)
	    	{
    	        _t.tip({node:_t.node,content:'<div class="delete-dialog"><a class="delete-dialog-bg-control"></a><p class="delete-dialog-alert clearfix">' + _t.content + '</p></div>', triggerType:'mouse',fun:function(){
    	        	 _t.node.on('click',function(){
   	    	    	  _t.popup.destroy();
   	                    _t.popup=null;

   	    	 })
    	        }});

	    	}
    	    return _t;
    	},
    	// 张开详细内容
    	openMore:function(_o){
    		_o=_o?_o:{};
    	    var _t = this;
    	    _t.node=_o.node?_o.node:null;
            _t.content=_o.content?_o.content:'';

    	    if(_t.node && _t.content != '' && !_t.popup)
	    	{
    	        _t.tip({node:_t.node,width:'320',height:'260',content:'<div class="delete-dialog" style="width:300px; height:250px;"><a class="delete-dialog-bg-control"></a><p class="delete-dialog-alert" style="width:300px;margin-top:10px;height:250px;line-height:22px;text-align:left;overflow-y:scroll">' + _t.content + '</p></div>', triggerType:'mouse'});
	    	}
    	    return _t;
    	},
        openMoreLeft:function(_o){
            _o=_o?_o:{};
            var _t = this;
            _t.node=_o.node?_o.node:null;
            _t.content=_o.content?_o.content:'';
            if(_t.node && _t.content != '' && !_t.popup)
            {
                _t.tip({node:_t.node,width:'320',height:'260',content:'<div class="delete-dialog" style="width:300px; height:250px;left:130px"><a class="delete-dialog-bg-control" style="margin:-23px 0 0 10px"></a><p class="delete-dialog-alert" style="width:300px;margin-top:10px;height:250px;line-height:22px;text-align:left;overflow-y:scroll">' + _t.content + '</p></div>', triggerType:'mouse'});
            }
            return _t;
        },
    	//确认提示
    	conf:function(_o){
    		_o=_o?_o:{};
    	    var _t = this;
    	    _t.node=_o.node?_o.node:null;
            _t.content=_o.content?_o.content:'';
            _t.render=_o.render?_o.render:'';
    	    if(_t.node && _t.content != '' )
	    	{
    	    	(function(n,c,t){
    	    		n.on("click", function (e) {
    	    			if( !_t.popup){
    	    	    	_t.tip({content:c,render:t}).tipRun(e,_o);
    	    			}
    	    		});
    	    	})( _t.node,_t.content, _t.render);
	    	}
    	    return _t;
    	},
    	tipRun:function(e,_o){
    		_o=_o?_o:{};
    		 var _t = this;
    		 var t = Node.one(e.target);
    		 _t.fun=_o.fun?_o.fun:function(){};
    			 _t.popup.set('content', '<div class="delete-dialog"><a class="delete-dialog-bg-control"></a><p class="delete-dialog-alert">' + _t.content + '</p><p class="confbtn clearfix"><a class="delete-confirm-bid"></a><a class="delete-cancel-bid"></a></p></div>');
                 _t.popup.set('align', {
                     node:t,
                     points:['bc', 'tc']
                 });
                 _t.popup.show();
                 //确定按钮
                 Node.one('.delete-confirm-bid').on('click',function(){
             		_t.fun();
                    _t.popup.destroy();
                    _t.popup=null;
                    });
                 Node.one('.delete-cancel-bid').on('click',function(){
                     _t.popup.destroy();
                     _t.popup=null;
                 });
             return _t;
    	},

    	//确认提示框(有确定取消按钮)
    	alert:function(_o,content){
    		_o=_o?_o:{};
    		var _t = this,thisEl,confirm,cancel;
    	    _t.msg=_o.msg?_o.msg:'发送成功，请等待审核';
    	    _t.fun = _o.fun?_o.fun:function(){};
    	    _t.confirmFun=_o.confirmFun?_o.confirmFun:function(){};
    	    _t.cancelFun=_o.confirmFun?_o.cancelFun:function(){};
    	    if(_t.msg!=''&& !_t.popup)
    	    {
    	    	_t.tip({content:_t.msg});
    	    	_t.popup.set('content', _o.content ? _o.content : '<div class="stips"><div class="btips-top"><div class="btips-top-r"><div class="btips-top-c"></div></div></div><div class="btips-center"><div class="btips-center-r"><div class="btips-center-c"><span class="btips-suc"></span><span class="btips-detail">'+_t.msg+'</span></div><div class="bbtn-box"><span class="bbtn-unuse"></span><div class="bbtn-box1"><a class="btn success">确定</a><a class="btn fail">取消</a></div></div></div></div><div class="btips-bottom"><div class="btips-bottom-r"><div class="btips-bottom-c"></div></div></div></div>');
                _t.popup.set('align', {
                    node:window,
                    points:['cc', 'bc']
                });
                _t.popup.show().render();
                thisEl = _t.popup.get('el');
                confirm = KISSY.DOM.get('.success',thisEl);
                cancel = KISSY.DOM.get('.fail',thisEl);
                KISSY.Event.on(confirm,'click',function(){
                	_t.fun();
                	_t.confirmFun();
        	    	_t.popup.destroy();
                    _t.popup=null;
                });
                KISSY.Event.on(cancel,'click',function(){
                	_t.popup.destroy();
                	_t.cancelFun();
                    _t.popup=null;
                });
                _t.fun();
    	    }


    		return _t;
    	},
    	//操作成功提示
    	successTip:function(_o){
    		_o=_o?_o:{};
    	    var _t = this;
    	    _t.msg=_o.msg?_o.msg:'成功';
    	    _t.fun = _o.fun?_o.fun:function(){};
    	    _t.width = _o.width?_o.width:'';
            _t.height = _o.height?_o.height:'300';
            _t.time = _o.time?_o.time:1500;
    	    if(_t.msg!='' && !_t.popup)
    	    {
    	    	_t.tip({content:_t.msg});
    	    	_t.popup.set('content', '<div class="tips"><span class="stips-suc"></span><span class="tips-detail">'+ _t.msg +'</span></div>');
                _t.popup.set('align', {
                    node:window,
                    width:_t.width,
                    height:_t.height,
                    points:['cc', 'cc']
                });
                _t.popup.show();
                setTimeout(function () {
                	 _t.fun();
           			_t.popup.destroy();
                    _t.popup=null;
           	        }, _t.time);

    	    }
    		return _t;
    	},
    	//操作失败提示
    	failureTip:function(_o){
    		_o=_o?_o:{};
    	    var _t = this;
    	    _t.msg=_o.msg?_o.msg:'发送失败';
    	    _t.fun = _o.fun?_o.fun:function(){};
    	    _t.width = _o.width?_o.width:'350';
    	    if(_t.msg!=''&& !_t.popup)
    	    {
    	    	_t.tip({content:_t.msg});
    	    	_t.popup.set('content', '<div class="tips"><span class="stips-fail"></span><span class="tips-detail">'+ _t.msg +'</span></div>');
                _t.popup.set('align', {
                    node:window,
                    points:['cc', 'cc']
                });
                _t.popup.show();
                setTimeout(function () {
                    _t.fun();
           			_t.popup.destroy();
                    _t.popup=null;
           	        }, 1500);
    	    }
    		return _t;
    	}
  });
    return Dialog;
},{
	requires:['xh/global','node','overlay','component/plugin/drag','component/plugin/resize','dd/plugin/constrain','../../../css/dialog.css']
});
