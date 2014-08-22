KISSY.add('xh/global',function(S, XTemplate){
	function Global(){
	}

	S.augment(Global,{
		/*
		**作用：设置或获取XhNow
		**参数：
		**	_n：XhNow结构的Json数据
		**返回：XhNow
		*/
		xhNow:function(_n)
		{
			var _t=this;
			if(_n && !_n.dateNowText)
			{
				window.XhNow = null;
				_n.time = _n.time?_n.time:window.XhNowTime;
				_n.date = _t.getDate(_n.time,false);
				_n.date0 = _t.getDate(_n.time);
				_n.time0 = _n.date0.getTime();
				_n.timeNow = _n.time0;
				_n.dateNow = _n.date0;
				_n.action = _n.action?_n.action:'list';
				_n.type = _n.type?_n.type:'all';
				_n.templet = _n.templet?_n.templet:'common';
				_n.page = _n.page?_n.page:1;
				_n = _t.nowDate(_n);
				window.XhNow = _n;
			}
			return window.XhNow;
		},
		/*
		**作用：获取指定的XhConfig数据
		**参数：
		**	_n：XhNow结构的Json数据
		**返回：XhConfig数据
		*/
		getXhConfig:function(_n)
		{
			if(_n)
			{
				return window.XhConfig[_n.channel][_n.action];
			}
			return window.XhConfig.global;
		},
		/*
		**作用：将数据转换成数字
		**参数：
		**	_v：需要转换的数据
		**	_r：进制类型（默认十进制）
		**返回：数字
		*/
		toInt:function(_v, _r)
		{
			_r = _r?_r:10;
			return parseInt(_v, _r);
		},
		/*
		**作用：将时间戳转换成Data对象
		**参数：
		**	_v：需要转换的时间戳
		**	_z：是否转换成零点（默认true）
		**返回：Data对象
		*/
		getDate:function(_v,_z)
		{
			_z=_z==false?false:true;
			var _d = null;
			if(_v)
			{
				_d = new Date();
				_d.setTime(_v);
				if(_z)
				{
					_d.setHours(0);
					_d.setMinutes(0);
					_d.setSeconds(0);
					_d.setMilliseconds(0);
				}
			}
			return _d;
		},
		/*
		**作用：根据时间戳计算时间信息
		**参数：
		**	_n：XhNow
		**	_v：时间戳（默认XhNow.time）
		**返回：XhNow
		*/
		nowDate:function(_n, _v)
		{
			_v=_v?_v:_n.time;
			var _t = this;
			var _d = new Date();
			var _text = null;
			var _cg = _t.getXhConfig();
			_d.setTime(_v);
			for (var _i=0; _text == null && _i<_cg.dateInfo.length; _i++)
			{
				if( _v >= _n.time0 - _cg.dateInfo[_i].value)
				{
					_text = _cg.dateInfo[_i].text;
				}
			}
			if(_text == null)
			{
				_text = _d.getFullYear()+'-'+(_d.getMonth()+1)+'-'+_d.getDate();
			}
			if(_n.dateNowText != _text)
			{
				_n.dateNow = _t.getDate(_v);
				_n.timeNow = _n.dateNow.getTime();
				_n.dateNowText = _text;
			}
			return _n;
		},
		/*
		**作用：为元素绑定滚动事件
		**参数：
		**	_f：绑定的方法
		**	_e：绑定的元素（默认window）
		**返回：无
		*/
		scroll:function(_f,_e)
		{
			_e=_e?_e:window;
			S.Event.on(_e, 'scroll',_f);
		},
		/*
		**作用：为元素取消滚动事件
		**参数：
		**	_f：取消的方法
		**	_e：取消的元素（默认window）
		**返回：无
		*/
		unScroll:function(_f,_e)
		{
			_e=_e?_e:window;
			S.Event.detach(_e, 'scroll',_f);
		},
		/*
		**作用：设置垂直滚动条位置
		**参数：
		**	_f：要显示的元素（默认为XhConfig.global.contentId）
		**	_e：要设置垂直滚动条的元素（默认为window）
		**返回：无
		*/
		scrollTo:function(_d,_n)
		{
			var _t=this;
			_d=_d?_d:S.one('#' + _t.getXhConfig().contentId);
			_n=_n?_n:window;
			S.Anim(_n,{scrollTop:(S.DOM.offset(_d).top-10)},.2,'easeOutQuart').run();
		},
		/*
		**作用：模板标签替换
		**参数：
		**	_s：要替换的字符串
		**	_d：要替换的数据
		**	_t：编码类型
		**	_r：正则表达式（默认为/\\?\{{2,3}([^{}]+)\}{2,3}/g）
		**返回：替换后的字符串
		*/
		substitute: function (_s, _d, _t, _r)
		{
			_t = _t?_t:'templet';
			var _str = '';
			if(_t=='templet')
			{
				_str = new XTemplate(_s).render(_d);
			}
			else
			{
				_r=_r?_r:/\\?\{{2,3}([^{}]+)\}{2,3}/g;
				if (typeof _s != 'string' || !_d)
				{
					return _s;
				}
				_str = _s.replace(_r, function(_m, _n){
					if (_m.charAt(0) === '\\')
					{
						return _m.slice(1);
					}
					var _a=_n.split('.');
					var _v=_d;
					for(var _i=0; _i<_a.length; _i++)
					{
						_v=_v[_a[_i]];
						if(_v === undefined || _v === null)
						{
							_v = '';
							break;
						}
					}
					if(_t == "url")
					{
						_v = encodeURIComponent(_v);
					}
					return _v;
				});
			}
			return _str;
		},
		/*
		**作用：通过模板返回tag的HTML
		**参数：
		**	_c：window.XhConfig[channel][action]信息
		**	_s：tag字符串
		**返回：替换后tag的HTML
		*/
		getTag:function(_c,_s)
		{
			var _t = this;
			var _h = '';
			if(_s != undefined && _s != null)
			{
				var _a = _s.split(_c.tagBreak);
				for (var _i=0; _i<_a.length; _i++)
				{
					_h += _t.substitute(_c.templet.tag,{tagText:_a[_i],tagUrl:encodeURIComponent(_a[_i])})
				}
			}
			return _h;
		},
		/*
		**作用：验证ajax请求是否成功，并返回数据集
		**参数：
		**	_r：ajax返回的Json对象
		**	_t：指定属性
		**返回：如果正确返回数据信息，否则返回false。
		*/
		getResult:function(_r,_t)
		{
			var _d = false;
			if(_r.code==200)
			{
				if(_t)
				{
					_d = _r.content[_t];
				}
				else
				{
					_d = _r.content;
				}
			}
			return _d;
		},
		/*
		**作用：生成分享HTML
		**参数：
		**	_opt：bShare平台用的到数据
		**返回：返回分享HTML
		*/
		getShareHtml:function(_opt)
		{
			var _t = this;
			var _config = _t.getXhConfig();
			var _html = '';
			if(_opt)
			{
				for(var _i=0; _i < _config.shareList.length; _i++)
				{
					_html += _t.substitute(_config.shareHtml,S.merge(_config.shareList[_i],_opt));
				}
				_html += _t.substitute(_config.shareMore,_opt);
			}
			return _html;
		},
		/*
		**作用：设置文本框焦点到结尾
		**参数：
		**	_opt：文本框
		**	_len：焦点的位置（默认为结尾）
		**返回：没有返回
		*/
		rangeMoveEnd:function(_obj,_len)
		{
			_obj.focus();
			_len = _len>-1?_len:_obj.value.length;
			if(document.selection) {
				var _range = _obj.createTextRange();
				_range.moveStart('character',_len);
				_range.collapse();
				_range.select();
			}
			else if(typeof _obj.selectionStart == 'number' && typeof _obj.selectionEnd == 'number')
			{
				_obj.selectionStart = _obj.selectionEnd = _len;
			}
		},
		/*
		**作用：获取随机数
		**参数：
		**	_num：乱数（默认为客户端时间）
		**返回：随机数
		*/
		getRandom:function(_num)
		{
			_num=_num?_num:new Date().getTime();
			return Math.floor(Math.random()*_num+1);
		},
		/*
		**作用：获取字符串长度，汉字算两个字符
		**参数：
		**	_str：获取长度的字符串
		**返回：字符串长度
		*/
		getStrLen:function(_str)
		{
			var _len = 0;
			for (var _i = 0; _i<_str.length; _i++)
			{
				if (_str.charCodeAt(_i) > 255)
				{
					_len += 2;
				}
				else
				{
					_len++;
				}
			}
			return _len;
		}
	});

	return Global;
},{
	requires:['xtemplate']
});