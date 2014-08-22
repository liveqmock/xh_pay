// JavaScript Document 
//鼠标覆盖颜色
function setOnMouseOver(obj){
	obj.style.cursor = "pointer";
	obj.style.backgroundColor = "#e6f2f8";
}
//鼠标移走颜色
function setOnMouseOut(obj){
		obj.style.backgroundColor = "#ffffff";
}
//显示，隐藏层
function discontrol(itemid,obj){
	var item = document.getElementById(itemid);
	var link = obj.firstChild.firstChild;
	
	var linkClass = "";
	if (window.addEventListener){// Mozilla, Netscape, Firefox
		linkClass = link.getAttribute("class");
	}else{// IE
		linkClass = link.getAttribute("className");
	}
	
	if(item.style.display=='none'){
		item.style.display="";
		item.style.backgroundColor="#e6f2f8";
		if (link != null
			&& linkClass == "expand") {
			if (window.addEventListener){// Mozilla, Netscape, Firefox
				link.setAttribute("class","collapse");
			}else{// IE
				link.setAttribute("className","collapse");
			}
			//link.innerHTML = "↑↑";
		}
	} else{
		item.style.display="none";
		obj.style.backgroundColor="#e6f2f8";
		if (link != null
			&& linkClass == "collapse") {
			
			if (window.addEventListener){// Mozilla, Netscape, Firefox
				link.setAttribute("class","expand");
			}else{// IE
				link.setAttribute("className","expand");
			}
			//link.innerHTML = "↓↓";
		}
	}
}


//form验证
$(document).ready(function(){
	$("#transform").submit(function(){
		return checkAmt();
	});
});
function exChange(json){
	//清空上次数据
	clear();
	//设置页面元素信息
	setPageElements(json);
	//显示页面
	$.blockUI({
        message: $('#maskLayer'),
        css: { 
            width: '380px',
            border: '6px solid #AAAAAA',
            cursor:'default'
        },
     	// styles for the overlay 
        overlayCSS:  { 
            backgroundColor: '#000', 
            opacity:         0.5 
        }
    });
    $('#cancel').click(function (){
    	$.unblockUI();
    });
	$("#byCurAmt").blur(function (){
		if(this.value != "" && checkAmt()){
			compute(json);
		}
	});
    function clear(){
    	$("#changeType").val("");
    	$("#byCurAmt").val("");
    	$("#result").html("");
    }
}

function checkAmt() {
	var byCurAmtObj = $("#byCurAmt");
	var byCurAmt = byCurAmtObj.val();
	var result = /^[1-9][0-9]*$/.test(byCurAmt);
	var balance = Number($("#balance").children("span").text(), 10);
	var rate = Number($("#rate").children("span:first-child").text(), 10);

	if (!result) {
		$("#result").html("<span>您的输入有误，请您输入非零的正整数</span>");
		byCurAmtObj.focus();
		return false;
	} else if ((Number(byCurAmt) > balance)) {
		$("#result").html("<span>您输入的数字不能大于" + balance + "</span>");
		byCurAmtObj.focus();
		return false;
	} else if ((Number(byCurAmt) < rate)) {
		$("#result").html("<span>您输入的数字不能小于" + rate + "</span>");
		byCurAmtObj.focus();
		return false;
	} else {
		return true;
	}
}
function compute(json){
	var byCurAmt = $("#byCurAmt").val();
	if(json.changeType == 3){
		var mod = parseInt(byCurAmt % json.byRate,10);
		var sub = byCurAmt - mod;
		var div = parseInt(sub / json.byRate,10);
		$("#result").html("可以兑换："+div+json.repCurTypeName+",未使用："+mod+json.byCurTypeName);
	} else{
		var result = Math.round((byCurAmt * json.repRate)*100)/100.00;
		$("#result").html("可以兑换："+result+json.repCurTypeName);
	}
}
function setPageElements(json){
	//设置CSS样式
	$("#byCur").removeClass();
	$("#repCur").removeClass();
	$("#byCur").addClass(json.byClass);
	$("#repCur").addClass(json.repClass);
	//初始化页面信息
	$("#changeType").val(json.changeType);
	$("#title").html(json.byCurTypeName + "兑换" +json.repCurTypeName);
	$("#balance").html("可用"+json.byCurTypeName + "：<span>" +json.balance+"</span>");
	$("#rate").html("兑换比例：<span>" +json.byRate+"</span>"+json.byCurTypeName+" = <span>"+json.repRate+"</span>"+json.repCurTypeName);
	$("#unit").html(json.byCurTypeName);
}