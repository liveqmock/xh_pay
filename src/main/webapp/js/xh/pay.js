KISSY.use('cookie,sizzle', function(S,Cookie) {
	KISSY.ready(function(S) {
		var $ = KISSY.all;
		var paytype=S.Cookie.get("paytype");
		var curbankcook = S.Cookie.get('bankname');
		if(curbankcook){
			var curbankcookthat = $(".online-banks li a").filter("#"+curbankcook);
			var curbankcookliicon = curbankcookthat.parent("li").siblings();
			curbankcookliicon.each(function() {
				$(this).children("a").removeClass("curbank");
			});
			curbankcookthat.addClass("curbank");
			curbankcookthat.siblings("input").attr("checked", "checked");
		}
		if(paytype == "quickpay") {
			var selectbank=S.Cookie.get("bankname") || "icbc";
			$(".quick-payment .bank").attr("id",selectbank);
		} else if (paytype == "onlinebank" ) {
			var selectbank=S.Cookie.get("bankname");
			selectbank=".online-banks li a#"+selectbank;
			$(".curbank").removeClass("curbank");
			$(selectbank).addClass("curbank");
			var previnput=$(selectbank).siblings("input");
			$(previnput).attr("checked","checked")
		}

		$(".quick-payment .bank").on("click", function() {
			var that=$(this);
			var bankname=that.attr("id");
			that.addClass("curbank");
			var preva=that.prev("input");
			$(preva).attr("checked", "checked");
			S.Cookie.set("paytype","quickpay");
			S.Cookie.set("bankname",bankname);
		});

		$(".online-banks li a").on('click', function() {
			var that = $(this);
			that.siblings("input").attr("checked", "checked");
			var liicon = that.parent("li").siblings();
			liicon.each(function() {
				$(this).children("a").removeClass("curbank");
			});
			that.addClass("curbank");
			var curbank = $(".online-banks li a.curbank").attr("id");
			if (!curbank) return;
			$(".quick-payment .bank").attr("id", curbank).addClass("curbank");
			$(".quick-payment input").attr("checked", "checked");
			S.Cookie.set("paytype","quickpay");
			S.Cookie.set("bankname",curbank);			
		});
		$(".selectbankradio").on("click", function() {
			var that=$(this);
			var nexta=that.next("a");
			$(nexta).fire("click");
		});
		$(".pay_platform li a").on('click', function() {
			var that = $(this);
			that.siblings("input").attr("checked", "checked");
			var liicon = that.parent("li").siblings();
			liicon.each(function() {
				$(this).children("input").removeAttr("checked");
				$(this).children("a").removeClass("curbank");
			});
			that.addClass("curbank");
			var curbank = $(".pay_platform li a.curbank").attr("id");
			S.DOM.css(S.one(".platform_info").children(),"display","none");
			S.DOM.css(S.one("."+curbank+"_info"),"display","block");

		});
		
	});
});