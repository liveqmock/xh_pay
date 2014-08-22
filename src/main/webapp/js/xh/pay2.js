// JavaScript Document
KISSY.use('cookie,sizzle', function(S,Cookie) {
	KISSY.ready(function(S) {
		var $ = KISSY.all;
		var curbank=S.Cookie.get('bankname');
		$(".curbank").attr("id",curbank);
		$("#paynow_btn").on("click", function() {
			var docHeight=S.DOM.docHeight ();
			var docWidth = S.DOM.docWidth();
			$(".checkbg").width(docWidth);
			$(".checkbg").height(docHeight);
			$(".checkbg").show();
			});
		$(".checkbg").on("click", function(e) {
			e.stopPropagation();
			});
	});
});