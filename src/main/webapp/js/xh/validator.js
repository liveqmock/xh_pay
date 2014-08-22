/**
 * @ignore
 * kissy validation 扩展封装，自定义消息提示
 * @author linxi
 */
KISSY.add('xh/validator', function(S, DOM, Validation){
	//全局缓存，消息显示数量
	var _cache = {}, _msgCount = 0;
	/**
	 * @class Validator
	 * 表单验证器
	 */
    function Validator(form, config){
		_cache.paddingleft = config.paddingleft || 10;
		_cache.paddingtop = config.paddingtop || 5;
		
		Validator.superclass.constructor.call(this, form, config);
    }
	
	S.augment(Validator, {
		getMsgCount: function(){
			return _msgCount;
		}
	});
	
	// Validation增加提示类XhWarn
	Validation.Warn.extend('XhWarn', XhWarn);
	// Validation增加提示类XhWarnTop
	Validation.Warn.extend('XhWarnTop', XhWarnTop);

	//增加特殊字符检测
	Validation.Rule.add("specialchar", "输入中包含非法字符。",function(value, text, msg, allow){
		var sc = ['@', '#', '\$', '\^', '&', '\*', '|', '\\', '\\/', '\!'];
		if(allow){
			for(var i=0; i<sc.length; i++){
				for(var j=0; j<allow.length; j++){
					if(sc[i].replace('\\', '') == allow[j]){
						sc.splice(i, 1);
					}
				}
			}
		}
		var regex = new RegExp('['+sc.join('')+']+', 'g');
		if(regex.test(value)){
			if(msg){
				return msg;
			}else{
				return '输入中包含非法字符';
			}
		}
	});
	// Validation增加11位手机号码验证规则
	Validation.Rule.add('mobileNumber', '手机号码不合法', function(value,text){
		//规则取自淘宝注册登录模块 @author:yanmu.wj@taobao.com
		/*
		var regex = {
			//中国移动
			cm:/^(?:0?1)((?:3[56789]|5[0124789]|8[278])\d|34[0-8]|47\d)\d{7}$/,
			//中国联通
			cu:/^(?:0?1)(?:3[0123]|4[5]|5[356]|8[356])\d{8}$/
		},
		
		*/
		var re = /^\d{11}$/, flag = false;
		if(value.match(re)){
			flag = true;
		}
		if(!flag){
			return text;
		}
	});
	Validation.Rule.add('unEqual','请选择一个选项',function(value,text,_number){
		var flag = false;
		if(value!=_number){
			flag =true;
		}
		if(!flag){
			return text;
		}
	});
	
	S.each([
			{ key:"number", reg: /^\d*$/, tip: "只能输入数字" },
			{key:"inputMoney", reg: /^[1-9]\d{1,9}([.]\d{1,2})?$/, tip: "必须是数字,不能小于10,至多保留两位小数" },
			{ key:"zip", reg: /^\d{6}$/, tip: "请输入正确的邮编" }
		],function(item){
			Validation.Rule.add(item.key,item.tip,function(value,text){
				if(!new RegExp(item.reg).test(value)){
					return text;
				}
			});
		});

	/**
	 * @class XhWarn
	 * 消息提示扩展类(右侧显示)
	 */
    function XhWarn(){
		//枚举常量 [error:0 错误, ok:1 正确, hint:2 提示, ignore:3忽略]
		var symbol = Validation.Define.Const.enumvalidsign;
		return {
			/**
			 * 初始化
			 */
			init: function(){
				var self = this, tg = self.target,
				panel = DOM.create('<div class="validator-side"><span class="estate"><span class="label"><span class="label-text"></span></span></span></div>'),
				estate = DOM.get('.estate',panel),
				label = DOM.get('.label-text',panel);
				
				S.ready(function(){
					if(S.one(".ks-dialog"))
					{
						S.one(".ks-dialog").append(panel);
					}
					else
					{
						document.body.appendChild(panel);
					}
					
				});
				S.mix(self,{
					panel: S.one(panel),
					estate: S.one(estate),
					label: S.one(label)
				});
			},
			/**
			 * 根据校验结果显示提示信息
			 */
			showMessage: function(result,msg,evttype,target) {
				var self = this,
					panel = self.panel, estate = self.estate, label = self.label;

				estate.removeClass('ok tip error');
				if (result == symbol.error) {
					estate.addClass('error');
				} else if (result == symbol.ok) {
					if(DOM.css(panel, 'display') != 'none'){
						_msgCount--;
						panel.hide();
					}
					return;
				} else if (result == symbol.hint) {
					estate.addClass('tip');
				} else if (result == symbol.ignore) {
					if(DOM.css(panel, 'display') != 'none'){
						_msgCount--;
						panel.hide();
					}
					return;
				}
				label.html(msg);
				panel.show();
				S.log(target);
				self._toggleError(target);
			},
			/**
			 * 显示错误
			 */
			_toggleError: function(target){
				var self = this, panel = self.panel[0],
				
					target = self.target, paneloffset, top, left;

				if(DOM.css(target, 'display') == 'none' || DOM.attr(target, 'type') == 'hidden'){
					var p = target.parentNode;
					paneloffset = DOM.offset(p);
					if(S.one(".ks-dialog"))
					{
						top = paneloffset.top+(DOM.outerHeight(p)-26)/2-S.DOM.offset(S.one(".ks-dialog")).top;
						left = paneloffset.left+DOM.outerWidth(p)+ _cache.paddingleft-S.DOM.offset(S.one(".ks-dialog")).left;
					}
					else
					{
						top = paneloffset.top+(DOM.outerHeight(p)-26)/2;
						left = paneloffset.left+DOM.outerWidth(p)+ _cache.paddingleft;
					}
				} else {
					paneloffset = DOM.offset(target);
					if(S.one(".ks-dialog"))
					{
						top = paneloffset.top+(DOM.outerHeight(target)-26)/2-S.DOM.offset(S.one(".ks-dialog")).top;
						left = paneloffset.left+DOM.outerWidth(target)+ _cache.paddingleft-S.DOM.offset(S.one(".ks-dialog")).left;
					}
					else
					{
						top = paneloffset.top+(DOM.outerHeight(target)-26)/2;
						left = paneloffset.left+DOM.outerWidth(target)+ _cache.paddingleft;
					}
					
				}
				_msgCount++;
				//还原panel的位置
				DOM.css(panel,{top:top,left:left});
			}
		};
	}
	
	/**
	 * @class XhWarnTop
	 * 消息提示扩展类(上侧显示)
	 */
    function XhWarnTop(){
		//枚举常量 [error:0 错误, ok:1 正确, hint:2 提示, ignore:3忽略]
		var symbol = Validation.Define.Const.enumvalidsign;
		return {
			/**
			 * 初始化
			 */
			init: function(){
				var self = this, tg = self.target,
				panel = DOM.create('<div class="validator-top"><span class="estate"><span class="icon"></span><span class="label"><span class="label-text"></span></span></span></div>'),
				estate = DOM.get('.estate',panel),
				icon = DOM.get('.icon', panel),
				label = DOM.get('.label-text',panel);
				
				S.ready(function(){
					if(S.one(".ks-dialog"))
					{
						S.one(".ks-dialog").append(panel);
					}
					else
					{
						document.body.appendChild(panel);
					}
				});
				S.mix(self,{
					panel: S.one(panel),
					estate: S.one(estate),
					icon: S.one(icon),
					label: S.one(label)
				});
			},
			/**
			 * 根据校验结果显示提示信息
			 */
			showMessage: function(result,msg,evttype,target) {
				var self = this,
					panel = self.panel, estate = self.estate, icon=self.icon, label = self.label;
					
				icon.removeClass('ok-icon tip-icon error-icon');
				if (result == symbol.error) {
					estate.addClass('error');
					icon.addClass('error-icon');
				} else if (result == symbol.ok) {
					if(DOM.css(panel, 'display') != 'none'){
						_msgCount--;
						panel.hide();
					}
					return;
				} else if (result == symbol.hint) {
					estate.addClass('tip');
					icon.addClass('tip-icon');
				} else if (result == symbol.ignore) {
					if(DOM.css(panel, 'display') != 'none'){
						_msgCount--;
						panel.hide();
					}
					return;
				}
				label.html(msg);
				panel.show();
				self._toggleError(target);
			},
			/**
			 * 显示错误
			 */
			_toggleError: function(target){
				var self = this, panel = self.panel[0],
					target = self.target, paneloffset, top, left;

				if(DOM.css(target, 'display') == 'none' || DOM.attr(target, 'type') == 'hidden'){
					var p = target.parentNode;
					paneloffset = DOM.offset(p);
					if(S.one(".ks-dialog"))
					{
						top = paneloffset.top-34-_cache.paddingtop-S.DOM.offset(S.one(".ks-dialog")).top;
						left = paneloffset.left-S.DOM.offset(S.one(".ks-dialog")).left;
					}
					else
					{
						top = paneloffset.top-34-_cache.paddingtop;
						left = paneloffset.left;
					}
				} else {
					paneloffset = DOM.offset(target);
					if(S.one(".ks-dialog"))
					{
						top = paneloffset.top-34-_cache.paddingtop-S.DOM.offset(S.one(".ks-dialog")).top;
						left = paneloffset.left-S.DOM.offset(S.one(".ks-dialog")).left;
					}
					else
					{
						top = paneloffset.top-34-_cache.paddingtop;
						left = paneloffset.left;
					}
				}
				_msgCount++;
				//还原panel的位置
				DOM.css(panel,{top:top,left:left});
				//S.Anim(panel, 'color:#f00', 1, 'bounceOut').run();
			}
		};
	}
		
	//Validation类prototype继承
	S.extend(Validator, Validation);
	//Validation属性方法(静态)复制
	S.mix(Validator, Validation);

    //返回Validator
    return Validator;
},{
    //新建的模块所需要的其他模块或资源
    requires:['dom', 'gallery/validation/1.0/', '../../../css/validator.css']
});