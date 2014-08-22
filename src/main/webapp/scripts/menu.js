function setActiveLeftMenu(menuId) {
	var menu = document.getElementById(menuId);
	if (window.addEventListener) {// Mozilla, Netscape, Firefox
		menu.setAttribute("class", "on");
	} else {// IE
		if (!isIE8()) {
			menu.setAttribute("className", "on");
		} else {
			menu.setAttribute("class", "on");
		}
	}
}
function setActiveMenu(menuId) {
	var menu = document.getElementById(menuId);
	if (window.addEventListener) {// Mozilla, Netscape, Firefox
		menu.setAttribute("class", "on");
	} else {// IE
		if (!isIE8()) {
			menu.setAttribute("className", "on");
		} else {
			menu.setAttribute("class", "on");
		}
	}
}

/**
 * Returns the version of Windows Internet Explorer or a -1 (indicating the use
 * of another browser).
 * 
 * @return
 */
function getInternetExplorerVersion() {
	var rv = -1; // Return value assumes failure.
	if (navigator.appName == 'Microsoft Internet Explorer') {
		var ua = navigator.userAgent;
		var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
		if (re.exec(ua) != null) {
			rv = parseFloat(RegExp.$1);
		}
	}
	return rv;
}

function isIE8() {
	var b = false;
	var ver = getInternetExplorerVersion();
	if (ver > -1) {
		if (ver >= 8.0) {
			b = true;
		}
	}
	return b;
}