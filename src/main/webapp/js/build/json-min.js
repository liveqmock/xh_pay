/*
Copyright 2012, KISSY UI Library v1.30
MIT Licensed
build time: Dec 20 22:27
*/
KISSY.add("json",function(c,a){"undefined"!==typeof JSON&&(a=JSON);return c.JSON={parse:function(b){return null==b||""===b?null:a.parse(b)},stringify:a.stringify}},{requires:[KISSY.Features.isNativeJSONSupported()?"":"json/json2"]});
