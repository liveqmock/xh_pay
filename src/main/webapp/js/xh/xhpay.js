KISSY.add('../xh/xhpay',function(S,Node,Calendar){
	var Xhpay = function(){
		if (S.Config.debug) {
			var srcPath = "../../";
			S.config({
				packages:[
					{
						name:"gallery",
						path:srcPath,
						charset:"utf-8",
						ignorePackageNameInUri:true
					}
				]
			});
		}
	};
	var _datetag="";
	var _fundsflow="";
	var _fundsrange="";
	var _min="";
	var _max="";
	var _sd=S.one('#s_date');
	var _ed=S.one('#e_date');
	S.augment(Xhpay,{
		/**
		 * 初始化
		 */
		init:function(_opt){
			var _t = this;
  			var dc = new Date();
			var one_week=_t.dateFormat(_t.getNewDate(-7,dc));
			var one_month=_t.dateFormat(_t.getNewMonth(-1,dc));
			var three_month = _t.dateFormat(_t.getNewMonth(-3,dc));
			
			_t.url = _opt.url?_opt.url:"#";
			if(S.DOM.val(_sd)==""){
				S.DOM.val(_sd, one_week );
				S.DOM.val(_ed, _t.dateFormat(dc) );
			}

			var calendar =  new Calendar({
				maxDate: new Date,
				count:1,
				isSameDate:true,
				isHoliday:false,
				triggerNode: '#s_date',
				finalTriggerNode:'#e_date'
			});


			calendar.on('show', function(e) {
				switch(e.node.attr('id')) {
					case 's_date':
						this.render();
						break;
					case 'e_date':
						this.render();
						break;
				}
			});
			S.Event.on("#internal_srh_btn","click",function(e){
				_t.postFunc();
			});
			S.Event.on(S.all("li",S.one("#funds_date_ul")),"click",function(e){
				_datetag=S.DOM.attr(this,"datetag");

				S.DOM.removeAttr(S.all("li",S.one("#funds_date_ul")),"class");
				S.DOM.addClass(this,"funds_currentli");
				if(_datetag=="0")
				{
					S.DOM.val(_sd, _t.dateFormat(dc) );
				}
				if(_datetag=="1")
				{
					S.DOM.val(_sd, one_week );
				}
				if(_datetag=="2")
				{
					S.DOM.val(_sd, one_month );
				}
				if(_datetag=="3")
				{
					S.DOM.val(_sd, three_month );
				}

				_t.postFunc();

				
			});
			S.Event.on(S.all("li",S.one("#funds_flow_ul")),"click",function(e){
				S.DOM.removeAttr(S.all("li",S.one("#funds_flow_ul")),"class");
				S.DOM.addClass(this,"funds_currentli");
				_t.postFunc();
			});
			S.Event.on(S.all("li",S.one("#funds_range_ul")),"click",function(e){
				S.DOM.removeAttr(S.all("li",S.one("#funds_range_ul")),"class");
				S.DOM.addClass(this,"funds_currentli");
				_t.postFunc();
			});
			        


		},
		getNewDate:function(day,zdate){
            var sdate=zdate.getTime()+(1*24*60*60*1000);
            var rdate = new Date(sdate+(day*24*60*60*1000));
            return rdate;
		},
		getNewMonth:function(month,zdate){
			var rdate=new Date();
			rdate.setFullYear(zdate.getFullYear(),zdate.getMonth()+month,zdate.getDate());
            return rdate;
		},
		dateFormat:function(zdate){
			var rdate = zdate.getFullYear() + "-" + (zdate.getMonth()+1) + "-" +zdate.getDate();
            return rdate;
		},
		postFunc:function(){
			var _t = this;
			S.all("li",S.one("#funds_date_ul")).each(function(item){
				if(S.DOM.hasClass(item,"funds_currentli"))
				{
					_datetag=item.attr("datetag");
				}
			});
			S.all("li",S.one("#funds_flow_ul")).each(function(item){
				if(S.DOM.hasClass(item,".funds_currentli"))
				{
					_fundsflow=item.attr("flowtag");
				}
			});
			S.all("li",S.one("#funds_range_ul")).each(function(item){
				if(S.DOM.hasClass(item,".funds_currentli"))
				{
					_fundsrange=item.attr("rangetag");
					_min=item.attr("mintag");
					_max=item.attr("maxtag");
				}
			});

//			S.IO.post("",{dateTag:_datetag,startTime:S.DOM.val(_sd),endTime:S.DOM.val(_ed),fundsFlow:_fundsflow,fundsRange:_fundsrange,min:_min,max:_max},function(data){
//				
//			});
			var url;
			if(_t.url .indexOf("/consume/gadgetlist.do")!= -1){
				url = _t.url+"?"+encodeURI("dateTag="+_datetag+"&startTime="+S.DOM.val(_sd)+"&endTime="+S.DOM.val(_ed)+"&fundsFlow="+_fundsflow+"&fundsRange="+_fundsrange+"&min="+_min+"&max="+_max);
			} else if(_t.url .indexOf("/order/orderlist.do")!= -1){
				url = _t.url+"?"+encodeURI("dateTag="+_datetag+"&startTime="+S.DOM.val(_sd)+"&endTime="+S.DOM.val(_ed));
			} else{
				url = "#";
			}
			window.location.href= url;
		}
		
	});
	return Xhpay;
},{requires: ['node','calendar/1.2/index']});