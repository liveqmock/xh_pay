/*
Copyright 2013, KISSY UI Library v1.30
MIT Licensed
build time: Jan 17 14:28
*/
KISSY.add("editor/plugin/remove-format/index",function(c,d,e){function b(){}c.augment(b,{pluginRenderUI:function(a){e.init(a);a.addButton("removeFormat",{tooltip:"清除格式",listeners:{click:function(){a.execCommand("removeFormat")}},mode:d.WYSIWYG_MODE})}});return b},{requires:["editor","./cmd","../button/"]});
