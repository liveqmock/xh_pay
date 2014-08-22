/*
Copyright 2013, KISSY UI Library v1.30
MIT Licensed
build time: Jan 15 13:39
*/
var KISSY=function(a){var k=this,f,i=0;f={__BUILD_TIME:"20130115133943",Env:{host:k,nodejs:"function"==typeof require&&"object"==typeof exports},Config:{debug:"",fns:{}},version:"1.30",config:function(c,d){var l,b,n=this,e,o=f.Config,h=o.fns;f.isObject(c)?f.each(c,function(a,g){(e=h[g])?e.call(n,a):o[g]=a}):(l=h[c],d===a?b=l?l.call(n):o[c]:l?b=l.call(n,d):o[c]=d);return b},log:function(c,d,l){if(f.Config.debug&&(l&&(c=l+": "+c),k.console!==a&&console.log))console[d&&console[d]?d:"log"](c)},
error:function(a){if(f.Config.debug)throw a instanceof Error?a:Error(a);},guid:function(a){return(a||"")+i++}};f.Env.nodejs&&(f.KISSY=f,module.exports=f);return f}();
(function(a,k){function f(){}function i(a,g){var j;e?j=e(a):(f.prototype=a,j=new f);j.constructor=g;return j}function c(m,g,j,e,b,h){if(!g||!m)return m;j===k&&(j=n);var c=0,f,o,i;g[l]=m;h.push(g);if(e){i=e.length;for(c=0;c<i;c++)f=e[c],f in g&&d(f,m,g,j,e,b,h)}else{o=a.keys(g);i=o.length;for(c=0;c<i;c++)f=o[c],f!=l&&d(f,m,g,j,e,b,h)}return m}function d(m,g,j,e,h,b,d){if(e||!(m in g)||b){var f=g[m],j=j[m];if(f===j)f===k&&(g[m]=f);else if(b&&j&&(a.isArray(j)||a.isPlainObject(j)))j[l]?g[m]=j[l]:(b=f&&
(a.isArray(f)||a.isPlainObject(f))?f:a.isArray(j)?[]:{},g[m]=b,c(b,j,e,h,n,d));else if(j!==k&&(e||!(m in g)))g[m]=j}}var l="__MIX_CIRCULAR",b=this,n=!0,e=Object.create,o=!{toString:1}.propertyIsEnumerable("toString"),h="constructor,hasOwnProperty,isPrototypeOf,propertyIsEnumerable,toString,toLocaleString,valueOf".split(",");(function(a,g){for(var j in g)a[j]=g[j]})(a,{stamp:function(m,g,j){if(!m)return m;var j=j||"__~ks_stamped",e=m[j];if(!e&&!g)try{e=m[j]=a.guid(j)}catch(b){e=k}return e},keys:function(a){var g=
[],j,e;for(j in a)g.push(j);if(o)for(e=h.length-1;0<=e;e--)j=h[e],a.hasOwnProperty(j)&&g.push(j);return g},mix:function(a,g,j,e,b){"object"===typeof j&&(e=j.whitelist,b=j.deep,j=j.overwrite);var h=[],d=0;for(c(a,g,j,e,b,h);g=h[d++];)delete g[l];return a},merge:function(e){var e=a.makeArray(arguments),g={},j,b=e.length;for(j=0;j<b;j++)a.mix(g,e[j]);return g},augment:function(e,g){var j=a.makeArray(arguments),b=j.length-2,h=1,c=j[b],d=j[b+1];a.isArray(d)||(c=d,d=k,b++);a.isBoolean(c)||(c=k,b++);for(;h<
b;h++)a.mix(e.prototype,j[h].prototype||j[h],c,d);return e},extend:function(e,g,j,b){if(!g||!e)return e;var h=g.prototype,c;c=i(h,e);e.prototype=a.mix(c,e.prototype);e.superclass=i(h,g);j&&a.mix(c,j);b&&a.mix(e,b);return e},namespace:function(){var e=a.makeArray(arguments),g=e.length,j=null,h,c,d,f=e[g-1]===n&&g--;for(h=0;h<g;h++){d=(""+e[h]).split(".");j=f?b:this;for(c=b[d[0]]===j?1:0;c<d.length;++c)j=j[d[c]]=j[d[c]]||{}}return j}})})(KISSY);
(function(a,k){var f=Array.prototype,i=f.indexOf,c=f.lastIndexOf,d=f.filter,l=f.every,b=f.some,n=f.map;a.mix(a,{each:function(e,b,h){if(e){var m,g,j=0;m=e&&e.length;g=m===k||"function"===a.type(e);h=h||null;if(g)for(g=a.keys(e);j<g.length&&!(m=g[j],!1===b.call(h,e[m],m,e));j++);else for(g=e[0];j<m&&!1!==b.call(h,g,j,e);g=e[++j]);}return e},indexOf:i?function(a,b){return i.call(b,a)}:function(a,b){for(var h=0,m=b.length;h<m;++h)if(b[h]===a)return h;return-1},lastIndexOf:c?function(a,b){return c.call(b,
a)}:function(a,b){for(var h=b.length-1;0<=h&&b[h]!==a;h--);return h},unique:function(e,b){var h=e.slice();b&&h.reverse();for(var m=0,g,j;m<h.length;){for(j=h[m];(g=a.lastIndexOf(j,h))!==m;)h.splice(g,1);m+=1}b&&h.reverse();return h},inArray:function(e,b){return-1<a.indexOf(e,b)},filter:d?function(a,b,h){return d.call(a,b,h||this)}:function(e,b,h){var m=[];a.each(e,function(g,a,e){b.call(h||this,g,a,e)&&m.push(g)});return m},map:n?function(a,b,h){return n.call(a,b,h||this)}:function(a,b,h){for(var m=
a.length,g=Array(m),j=0;j<m;j++){var c="string"==typeof a?a.charAt(j):a[j];if(c||j in a)g[j]=b.call(h||this,c,j,a)}return g},reduce:function(a,b,h){var m=a.length;if("function"!==typeof b)throw new TypeError("callback is not function!");if(0===m&&2==arguments.length)throw new TypeError("arguments invalid");var g=0,j;if(3<=arguments.length)j=arguments[2];else{do{if(g in a){j=a[g++];break}g+=1;if(g>=m)throw new TypeError;}while(1)}for(;g<m;)g in a&&(j=b.call(k,j,a[g],g,a)),g++;return j},every:l?function(a,
b,h){return l.call(a,b,h||this)}:function(a,b,h){for(var m=a&&a.length||0,g=0;g<m;g++)if(g in a&&!b.call(h,a[g],g,a))return!1;return!0},some:b?function(a,c,h){return b.call(a,c,h||this)}:function(a,b,h){for(var c=a&&a.length||0,g=0;g<c;g++)if(g in a&&b.call(h,a[g],g,a))return!0;return!1},makeArray:function(b){if(null==b)return[];if(a.isArray(b))return b;if("number"!==typeof b.length||b.alert||"string"==typeof b||a.isFunction(b))return[b];for(var c=[],h=0,m=b.length;h<m;h++)c[h]=b[h];return c}})})(KISSY);
(function(a,k){function f(a){var b=typeof a;return null==a||"object"!==b&&"function"!==b}function i(){if(n)return n;var b=d;a.each(l,function(a){b+=a+"|"});b=b.slice(0,-1);return n=RegExp(b,"g")}function c(){if(e)return e;var c=d;a.each(b,function(a){c+=a+"|"});c+="&#(\\d{1,5});";return e=RegExp(c,"g")}var d="",l={"&amp;":"&","&gt;":">","&lt;":"<","&#x60;":"`","&#x2F;":"/","&quot;":'"',"&#x27;":"'"},b={},n,e,o=/[\-#$\^*()+\[\]{}|\\,.?\s]/g;(function(){for(var a in l)b[l[a]]=a})();a.mix(a,{urlEncode:function(a){return encodeURIComponent(""+
a)},urlDecode:function(a){return decodeURIComponent(a.replace(/\+/g," "))},fromUnicode:function(a){return a.replace(/\\u([a-f\d]{4})/ig,function(a,g){return String.fromCharCode(parseInt(g,16))})},escapeHTML:function(a){return(a+"").replace(i(),function(a){return b[a]})},escapeRegExp:function(a){return a.replace(o,"\\$&")},unEscapeHTML:function(a){return a.replace(c(),function(a,g){return l[a]||String.fromCharCode(+g)})},param:function(b,c,g,j){if(!a.isPlainObject(b))return d;c=c||"&";g=g||"=";a.isUndefined(j)&&
(j=!0);var e=[],n,l,i,o,r,s=a.urlEncode;for(n in b)if(r=b[n],n=s(n),f(r))e.push(n),r!==k&&e.push(g,s(r+d)),e.push(c);else if(a.isArray(r)&&r.length){l=0;for(o=r.length;l<o;++l)i=r[l],f(i)&&(e.push(n,j?s("[]"):d),i!==k&&e.push(g,s(i+d)),e.push(c))}e.pop();return e.join(d)},unparam:function(b,c,g){if("string"!=typeof b||!(b=a.trim(b)))return{};for(var g=g||"=",j={},e,d=a.urlDecode,b=b.split(c||"&"),n=0,f=b.length;n<f;++n){e=b[n].indexOf(g);if(-1==e)c=d(b[n]),e=k;else{c=d(b[n].substring(0,e));e=b[n].substring(e+
1);try{e=d(e)}catch(l){}a.endsWith(c,"[]")&&(c=c.substring(0,c.length-2))}c in j?a.isArray(j[c])?j[c].push(e):j[c]=[j[c],e]:j[c]=e}return j}})})(KISSY);
(function(a){function k(a,i,c){var d=[].slice,l=d.call(arguments,3),b=function(){},n=function(){var e=d.call(arguments);return i.apply(this instanceof b?this:c,a?e.concat(l):l.concat(e))};b.prototype=i.prototype;n.prototype=new b;return n}a.mix(a,{noop:function(){},bind:k(0,k,null,0),rbind:k(0,k,null,1),later:function(f,i,c,d,l){var i=i||0,b=f,n=a.makeArray(l),e;"string"==typeof f&&(b=d[f]);f=function(){b.apply(d,n)};e=c?setInterval(f,i):setTimeout(f,i);return{id:e,interval:c,cancel:function(){this.interval?
clearInterval(e):clearTimeout(e)}}},throttle:function(f,i,c){i=i||150;if(-1===i)return function(){f.apply(c||this,arguments)};var d=a.now();return function(){var l=a.now();l-d>i&&(d=l,f.apply(c||this,arguments))}},buffer:function(f,i,c){function d(){d.stop();l=a.later(f,i,0,c||this,arguments)}i=i||150;if(-1===i)return function(){f.apply(c||this,arguments)};var l=null;d.stop=function(){l&&(l.cancel(),l=0)};return d}})})(KISSY);
(function(a,k){function f(b,c,i){var h=b,m,g,j,p;if(!b)return h;if(b[l])return i[b[l]].destination;if("object"===typeof b){p=b.constructor;if(a.inArray(p,[Boolean,String,Number,Date,RegExp]))h=new p(b.valueOf());else if(m=a.isArray(b))h=c?a.filter(b,c):b.concat();else if(g=a.isPlainObject(b))h={};b[l]=p=a.guid();i[p]={destination:h,input:b}}if(m)for(b=0;b<h.length;b++)h[b]=f(h[b],c,i);else if(g)for(j in b)if(j!==l&&(!c||c.call(b,b[j],j,b)!==d))h[j]=f(b[j],c,i);return h}function i(d,e,f,h){if(d[b]===
e&&e[b]===d)return c;d[b]=e;e[b]=d;var m=function(a,g){return null!==a&&a!==k&&a[g]!==k},g;for(g in e)!m(d,g)&&m(e,g)&&f.push("expected has key '"+g+"', but missing from actual.");for(g in d)!m(e,g)&&m(d,g)&&f.push("expected missing key '"+g+"', but present in actual.");for(g in e)g!=b&&(a.equals(d[g],e[g],f,h)||h.push("'"+g+"' was '"+(e[g]?e[g].toString():e[g])+"' in expected, but was '"+(d[g]?d[g].toString():d[g])+"' in actual."));a.isArray(d)&&a.isArray(e)&&d.length!=e.length&&h.push("arrays were not the same length");
delete d[b];delete e[b];return 0===f.length&&0===h.length}var c=!0,d=!1,l="__~ks_cloned",b="__~ks_compared";a.mix(a,{equals:function(b,e,d,h){d=d||[];h=h||[];return b===e?c:b===k||null===b||e===k||null===e?null==b&&null==e:b instanceof Date&&e instanceof Date?b.getTime()==e.getTime():"string"==typeof b&&"string"==typeof e||a.isNumber(b)&&a.isNumber(e)?b==e:"object"===typeof b&&"object"===typeof e?i(b,e,d,h):b===e},clone:function(b,c){var d={},h=f(b,c,d);a.each(d,function(a){a=a.input;if(a[l])try{delete a[l]}catch(g){a[l]=
k}});d=null;return h},now:Date.now||function(){return+new Date}})})(KISSY);
(function(a,k){var f=/^[\s\xa0]+|[\s\xa0]+$/g,i=String.prototype.trim,c=/\\?\{([^{}]+)\}/g;a.mix(a,{trim:i?function(a){return null==a?"":i.call(a)}:function(a){return null==a?"":(a+"").replace(f,"")},substitute:function(a,f,b){return"string"!=typeof a||!f?a:a.replace(b||c,function(a,b){return"\\"===a.charAt(0)?a.slice(1):f[b]===k?"":f[b]})},ucfirst:function(a){a+="";return a.charAt(0).toUpperCase()+a.substring(1)},startsWith:function(a,c){return 0===a.lastIndexOf(c,0)},endsWith:function(a,c){var b=
a.length-c.length;return 0<=b&&a.indexOf(c,b)==b}})})(KISSY);
(function(a,k){var f={},i=Object.prototype.toString;a.mix(a,{isBoolean:0,isNumber:0,isString:0,isFunction:0,isArray:0,isDate:0,isRegExp:0,isObject:0,type:function(a){return null==a?""+a:f[i.call(a)]||"object"},isNull:function(a){return null===a},isUndefined:function(a){return a===k},isEmptyObject:function(a){for(var d in a)if(d!==k)return!1;return!0},isPlainObject:function(c){if(!c||"object"!==a.type(c)||c.nodeType||c.window==c)return!1;try{if(c.constructor&&!Object.prototype.hasOwnProperty.call(c,
"constructor")&&!Object.prototype.hasOwnProperty.call(c.constructor.prototype,"isPrototypeOf"))return!1}catch(d){return!1}for(var f in c);return f===k||Object.prototype.hasOwnProperty.call(c,f)}});a.each("Boolean,Number,String,Function,Array,Date,RegExp,Object".split(","),function(c,d){f["[object "+c+"]"]=d=c.toLowerCase();a["is"+c]=function(c){return a.type(c)==d}})})(KISSY);
(function(a,k){function f(a,g,b){if(a instanceof l)return b(a[o]);var e=a[o];if(a=a[h])a.push([g,b]);else if(c(e))f(e,g,b);else return g&&g(e);return k}function i(a){if(!(this instanceof i))return new i(a);this.promise=a||new d}function c(a){return a&&a instanceof d}function d(a){this[o]=a;a===k&&(this[h]=[])}function l(a){if(a instanceof l)return a;d.apply(this,arguments);return k}function b(a,g,b){function c(a){try{return g?g(a):a}catch(b){return new l(b)}}function e(a){try{return b?b(a):new l(a)}catch(g){return new l(g)}}
function h(a){k||(k=1,n.resolve(c(a)))}var n=new i,k=0;a instanceof d?f(a,h,function(a){k||(k=1,n.resolve(e(a)))}):h(a);return n.promise}function n(a){return!e(a)&&c(a)&&a[h]===k&&(!c(a[o])||n(a[o]))}function e(a){return c(a)&&a[h]===k&&a[o]instanceof l}var o="__promise_value",h="__promise_pendings";i.prototype={constructor:i,resolve:function(b){var g=this.promise,j;if(!(j=g[h]))return k;g[o]=b;j=[].concat(j);g[h]=k;a.each(j,function(a){f(g,a[0],a[1])});return b},reject:function(a){return this.resolve(new l(a))}};
d.prototype={constructor:d,then:function(a,g){return b(this,a,g)},fail:function(a){return b(this,0,a)},fin:function(a){return b(this,function(g){return a(g,!0)},function(g){return a(g,!1)})},isResolved:function(){return n(this)},isRejected:function(){return e(this)}};a.extend(l,d);KISSY.Defer=i;KISSY.Promise=d;d.Defer=i;a.mix(d,{when:b,isPromise:c,isResolved:n,isRejected:e,all:function(a){var g=a.length;if(!g)return a;for(var j=i(),c=0;c<a.length;c++)(function(c,e){b(c,function(b){a[e]=b;0===--g&&
j.resolve(a)},function(a){j.reject(a)})})(a[c],c);return j.promise}})})(KISSY);
(function(a){function k(a,d){for(var f=0,b=a.length-1,i=[],e;0<=b;b--)e=a[b],"."!=e&&(".."===e?f++:f?f--:i[i.length]=e);if(d)for(;f--;f)i[i.length]="..";return i=i.reverse()}var f=/^(\/?)([\s\S]+\/(?!$)|\/)?((?:\.{1,2}$|[\s\S]+?)?(\.[^.\/]*)?)$/,i={resolve:function(){var c="",d,f=arguments,b,i=0;for(d=f.length-1;0<=d&&!i;d--)b=f[d],"string"==typeof b&&b&&(c=b+"/"+c,i="/"==b.charAt(0));c=k(a.filter(c.split("/"),function(a){return!!a}),!i).join("/");return(i?"/":"")+c||"."},normalize:function(c){var d=
"/"==c.charAt(0),f="/"==c.slice(-1),c=k(a.filter(c.split("/"),function(a){return!!a}),!d).join("/");!c&&!d&&(c=".");c&&f&&(c+="/");return(d?"/":"")+c},join:function(){var c=a.makeArray(arguments);return i.normalize(a.filter(c,function(a){return a&&"string"==typeof a}).join("/"))},relative:function(c,d){var c=i.normalize(c),d=i.normalize(d),f=a.filter(c.split("/"),function(a){return!!a}),b=[],k,e,o=a.filter(d.split("/"),function(a){return!!a});e=Math.min(f.length,o.length);for(k=0;k<e&&f[k]==o[k];k++);
for(e=k;k<f.length;)b.push(".."),k++;b=b.concat(o.slice(e));return b=b.join("/")},basename:function(a,d){var i;i=(a.match(f)||[])[3]||"";d&&i&&i.slice(-1*d.length)==d&&(i=i.slice(0,-1*d.length));return i},dirname:function(a){var d=a.match(f)||[],a=d[1]||"",d=d[2]||"";if(!a&&!d)return".";d&&(d=d.substring(0,d.length-1));return a+d},extname:function(a){return(a.match(f)||[])[4]||""}};a.Path=i})(KISSY);
(function(a,k){function f(g){g._queryMap||(g._queryMap=a.unparam(g._query))}function i(a){this._query=a||""}function c(a,b){return encodeURI(a).replace(b,function(a){a=a.charCodeAt(0).toString(16);return"%"+(1==a.length?"0"+a:a)})}function d(g){if(g instanceof d)return g.clone();var b=this;a.mix(b,{scheme:"",userInfo:"",hostname:"",port:"",path:"",query:"",fragment:""});g=d.getComponents(g);a.each(g,function(g,c){g=g||"";"query"==c?b.query=new i(g):b[c]=a.urlDecode(g)});return b}var l=/[#\/\?@]/g,
b=/[#\?]/g,n=/[#@]/g,e=/#/g,o=RegExp("^(?:([\\w\\d+.-]+):)?(?://(?:([^/?#@]*)@)?([\\w\\d\\-\\u0100-\\uffff.+%]*|\\[[^\\]]+\\])(?::([0-9]+))?)?([^?#]+)?(?:\\?([^#]*))?(?:#(.*))?$"),h=a.Path,m={scheme:1,userInfo:2,hostname:3,port:4,path:5,query:6,fragment:7};i.prototype={constructor:i,clone:function(){return new i(this.toString())},reset:function(a){this._query=a||"";this._queryMap=null;return this},count:function(){var b=0,j=this._queryMap,c;f(this);for(c in j)a.isArray(j[c])?b+=j[c].length:b++;return b},
has:function(b){var j;f(this);j=this._queryMap;return b?b in j:!a.isEmptyObject(j)},get:function(a){var b;f(this);b=this._queryMap;return a?b[a]:b},keys:function(){f(this);return a.keys(this._queryMap)},set:function(b,j){var c;f(this);c=this._queryMap;"string"==typeof b?this._queryMap[b]=j:(b instanceof i&&(b=b.get()),a.each(b,function(a,b){c[b]=a}));return this},remove:function(a){f(this);a?delete this._queryMap[a]:this._queryMap={};return this},add:function(b,j){var c=this,e,d;a.isObject(b)?(b instanceof
i&&(b=b.get()),a.each(b,function(a,b){c.add(b,a)})):(f(c),e=c._queryMap,d=e[b],d=d===k?j:[].concat(d).concat(j),e[b]=d);return c},toString:function(b){f(this);return a.param(this._queryMap,k,k,b)}};d.prototype={constructor:d,clone:function(){var b=new d,j=this;a.each(m,function(a,c){b[c]=j[c]});b.query=b.query.clone();return b},resolve:function(b){"string"==typeof b&&(b=new d(b));var j=0,c,e=this.clone();a.each("scheme,userInfo,hostname,port,path,query,fragment".split(","),function(d){if(d=="path")if(j)e[d]=
b[d];else{if(d=b.path){j=1;if(!a.startsWith(d,"/"))if(e.hostname&&!e.path)d="/"+d;else if(e.path){c=e.path.lastIndexOf("/");c!=-1&&(d=e.path.slice(0,c+1)+d)}e.path=h.normalize(d)}}else if(d=="query"){if(j||b.query.toString()){e.query=b.query.clone();j=1}}else if(j||b[d]){e[d]=b[d];j=1}});return e},getScheme:function(){return this.scheme},setScheme:function(a){this.scheme=a;return this},getHostname:function(){return this.hostname},setHostname:function(a){this.hostname=a;return this},setUserInfo:function(a){this.userInfo=
a;return this},getUserInfo:function(){return this.userInfo},setPort:function(a){this.port=a;return this},getPort:function(){return this.port},setPath:function(a){this.path=a;return this},getPath:function(){return this.path},setQuery:function(b){"string"==typeof b&&(a.startsWith(b,"?")&&(b=b.slice(1)),b=new i(c(b,n)));this.query=b;return this},getQuery:function(){return this.query},getFragment:function(){return this.fragment},setFragment:function(b){a.startsWith(b,"#")&&(b=b.slice(1));this.fragment=
b;return this},isSameOriginAs:function(a){return this.hostname.toLowerCase()==a.hostname.toLowerCase()&&this.scheme.toLowerCase()==a.scheme.toLowerCase()&&this.port.toLowerCase()==a.port.toLowerCase()},toString:function(g){var j=[],d,f;if(d=this.scheme)j.push(c(d,l)),j.push(":");if(d=this.hostname){j.push("//");if(f=this.userInfo)j.push(c(f,l)),j.push("@");j.push(encodeURIComponent(d));if(f=this.port)j.push(":"),j.push(f)}if(f=this.path)d&&!a.startsWith(f,"/")&&(f="/"+f),f=h.normalize(f),j.push(c(f,
b));if(g=this.query.toString.call(this.query,g))j.push("?"),j.push(g);if(g=this.fragment)j.push("#"),j.push(c(g,e));return j.join("")}};d.Query=i;d.getComponents=function(b){var c=(b||"").match(o)||[],e={};a.each(m,function(a,b){e[b]=c[a]});return e};a.Uri=d})(KISSY);
(function(a,k){var f=a.Env.host,i=f.document,f=(f=f.navigator)&&f.userAgent||"",c,d="",l="",b,n=[6,9],e=i&&i.createElement("div"),o=[],h=KISSY.UA={webkit:k,trident:k,gecko:k,presto:k,chrome:k,safari:k,firefox:k,ie:k,opera:k,mobile:k,core:k,shell:k,phantomjs:k,os:k,ipad:k,iphone:k,ipod:k,ios:k,android:k,nodejs:k},m=function(a){var b=0;return parseFloat(a.replace(/\./g,function(){return 0===b++?".":""}))};e&&(e.innerHTML="<\!--[if IE {{version}}]><s></s><![endif]--\>".replace("{{version}}",""),o=e.getElementsByTagName("s"));
if(0<o.length){l="ie";h[d="trident"]=0.1;if((b=f.match(/Trident\/([\d.]*)/))&&b[1])h[d]=m(b[1]);b=n[0];for(n=n[1];b<=n;b++)if(e.innerHTML="<\!--[if IE {{version}}]><s></s><![endif]--\>".replace("{{version}}",b),0<o.length){h[l]=b;break}}else if((b=f.match(/AppleWebKit\/([\d.]*)/))&&b[1]){h[d="webkit"]=m(b[1]);if((b=f.match(/Chrome\/([\d.]*)/))&&b[1])h[l="chrome"]=m(b[1]);else if((b=f.match(/\/([\d.]*) Safari/))&&b[1])h[l="safari"]=m(b[1]);if(/ Mobile\//.test(f)&&f.match(/iPad|iPod|iPhone/)){h.mobile=
"apple";if((b=f.match(/OS ([^\s]*)/))&&b[1])h.ios=m(b[1].replace("_","."));c="ios";if((b=f.match(/iPad|iPod|iPhone/))&&b[0])h[b[0].toLowerCase()]=h.ios}else if(/ Android/.test(f)){if(/Mobile/.test(f)&&(c=h.mobile="android"),(b=f.match(/Android ([^\s]*);/))&&b[1])h.android=m(b[1])}else if(b=f.match(/NokiaN[^\/]*|Android \d\.\d|webOS\/\d\.\d/))h.mobile=b[0].toLowerCase();if((b=f.match(/PhantomJS\/([^\s]*)/))&&b[1])h.phantomjs=m(b[1])}else if((b=f.match(/Presto\/([\d.]*)/))&&b[1]){if(h[d="presto"]=m(b[1]),
(b=f.match(/Opera\/([\d.]*)/))&&b[1]){h[l="opera"]=m(b[1]);if((b=f.match(/Opera\/.* Version\/([\d.]*)/))&&b[1])h[l]=m(b[1]);if((b=f.match(/Opera Mini[^;]*/))&&b)h.mobile=b[0].toLowerCase();else if((b=f.match(/Opera Mobi[^;]*/))&&b)h.mobile=b[0]}}else if((b=f.match(/MSIE\s([^;]*)/))&&b[1]){if(h[d="trident"]=0.1,h[l="ie"]=m(b[1]),(b=f.match(/Trident\/([\d.]*)/))&&b[1])h[d]=m(b[1])}else if(b=f.match(/Gecko/)){h[d="gecko"]=0.1;if((b=f.match(/rv:([\d.]*)/))&&b[1])h[d]=m(b[1]);if((b=f.match(/Firefox\/([\d.]*)/))&&
b[1])h[l="firefox"]=m(b[1])}c||(/windows|win32/i.test(f)?c="windows":/macintosh|mac_powerpc/i.test(f)?c="macintosh":/linux/i.test(f)?c="linux":/rhino/i.test(f)&&(c="rhino"));if("object"===typeof process){var g,j;if((g=process.versions)&&(j=g.node))c=process.platform,h.nodejs=m(j)}h.os=c;h.core=d;h.shell=l;h._numberify=m;c="webkit,trident,gecko,presto,chrome,safari,firefox,ie,opera".split(",");var i=i&&i.documentElement,p="";i&&(a.each(c,function(a){var b=h[a];if(b){p=p+(" ks-"+a+(parseInt(b)+""));
p=p+(" ks-"+a)}}),a.trim(p)&&(i.className=a.trim(i.className+p)))})(KISSY);(function(a){var k=a.Env,f=k.host,i=a.UA,c=f.document||{},d="ontouchstart"in c&&!i.phantomjs,l=(c=c.documentMode)||i.ie,b=(k.nodejs&&"object"===typeof global?global:f).JSON;c&&9>c&&(b=0);a.Features={isTouchSupported:function(){return d},isDeviceMotionSupported:function(){return!!f.DeviceMotionEvent},isHashChangeSupported:function(){return"onhashchange"in f&&(!l||7<l)},isNativeJSONSupported:function(){return b}}})(KISSY);
(function(a){function k(a){this.runtime=a}k.Status={INIT:0,LOADING:1,LOADED:2,ERROR:3,ATTACHED:4};a.Loader=k;a.Loader.Status=k.Status})(KISSY);
(function(a){function k(a,c,d){a=a[f]||(a[f]={});d&&(a[c]=a[c]||[]);return a[c]}a.namespace("Loader");var f="__events__"+a.now();KISSY.Loader.Target={on:function(a,c){k(this,a,1).push(c)},detach:function(i,c){var d,l;if(i){if(d=k(this,i))c&&(l=a.indexOf(c,d),-1!=l&&d.splice(l,1)),(!c||!d.length)&&delete (this[f]||(this[f]={}))[i]}else delete this[f]},fire:function(a,c){var d=k(this,a)||[],f,b=d.length;for(f=0;f<b;f++)d[f].call(null,c)}}})(KISSY);
(function(a){function k(a){if("string"==typeof a)return f(a);for(var b=[],c=0,e=a.length;c<e;c++)b[c]=f(a[c]);return b}function f(a){"/"==a.charAt(a.length-1)&&(a+="index");return a}function i(b,c,e){var b=b.Env.mods,d,f,c=a.makeArray(c);for(f=0;f<c.length;f++)if(d=b[c[f]],!d||d.status!==e)return 0;return 1}var c=a.Loader,d=a.Path,l=a.Env.host,b=a.startsWith,n=c.Status,e=n.ATTACHED,o=n.LOADED,h=a.Loader.Utils={},m=l.document;a.mix(h,{docHead:function(){return m.getElementsByTagName("head")[0]||m.documentElement},
normalDepModuleName:function(a,c){var e=0,f;if(!c)return c;if("string"==typeof c)return b(c,"../")||b(c,"./")?d.resolve(d.dirname(a),c):d.normalize(c);for(f=c.length;e<f;e++)c[e]=h.normalDepModuleName(a,c[e]);return c},createModulesInfo:function(b,c){a.each(c,function(a){h.createModuleInfo(b,a)})},createModuleInfo:function(b,e,d){var e=f(e),h=b.Env.mods,i=h[e];return i?i:h[e]=i=new c.Module(a.mix({name:e,runtime:b},d))},isAttached:function(a,b){return i(a,b,e)},isLoaded:function(a,b){return i(a,b,
o)},getModules:function(b,c){var d=[b],f,i,k,m,l=b.Env.mods;a.each(c,function(c){f=l[c];if(!f||"css"!=f.getType())i=h.unalias(b,c),(k=a.reduce(i,function(a,b){m=l[b];return a&&m&&m.status==e},!0))?d.push(l[i[0]].value):d.push(null)});return d},attachModsRecursively:function(a,b,c){var c=c||[],e,d=1,f=a.length,i=c.length;for(e=0;e<f;e++)d=h.attachModRecursively(a[e],b,c)&&d,c.length=i;return d},attachModRecursively:function(b,c,d){var f,i=c.Env.mods[b];if(!i)return 0;f=i.status;if(f==e)return 1;if(f!=
o)return 0;if(a.Config.debug){if(a.inArray(b,d))return d.push(b),0;d.push(b)}return h.attachModsRecursively(i.getNormalizedRequires(),c,d)?(h.attachMod(c,i),1):0},attachMod:function(a,b){if(b.status==o){var c=b.fn;c&&(b.value=c.apply(b,h.getModules(a,b.getRequiresWithAlias())));b.status=e;a.getLoader().fire("afterModAttached",{mod:b})}},getModNamesAsArray:function(a){"string"==typeof a&&(a=a.replace(/\s+/g,"").split(","));return a},normalizeModNames:function(a,b,c){return h.unalias(a,h.normalizeModNamesWithAlias(a,
b,c))},unalias:function(a,b){for(var c=[].concat(b),e,d,f,h=0,i,m=a.Env.mods;!h;){h=1;for(e=c.length-1;0<=e;e--)if((d=m[c[e]])&&(f=d.alias)){h=0;for(i=f.length-1;0<=i;i--)f[i]||f.splice(i,1);c.splice.apply(c,[e,1].concat(k(f)))}}return c},normalizeModNamesWithAlias:function(a,b,c){var a=[],e,d;if(b){e=0;for(d=b.length;e<d;e++)b[e]&&a.push(k(b[e]))}c&&(a=h.normalDepModuleName(c,a));return a},registerModule:function(b,c,e,d){var f=b.Env.mods,i=f[c];if(!i||!i.fn)h.createModuleInfo(b,c),i=f[c],a.mix(i,
{name:c,status:o,fn:e}),a.mix(i,d)},getMappedPath:function(a,b,c){for(var a=c||a.Config.mappedRules||[],e,c=0;c<a.length;c++)if(e=a[c],b.match(e[0]))return b.replace(e[0],e[1]);return b}})})(KISSY);
(function(a){function k(a,c){return c in a?a[c]:a.runtime.Config[c]}function f(b){a.mix(this,b)}function i(b){this.status=d.Status.INIT;a.mix(this,b)}var c=a.Path,d=a.Loader,l=d.Utils;a.augment(f,{getTag:function(){return k(this,"tag")},getName:function(){return this.name},getBase:function(){return k(this,"base")},getPrefixUriForCombo:function(){var a=this.getName();return this.getBase()+(a&&!this.isIgnorePackageNameInUri()?a+"/":"")},getBaseUri:function(){return k(this,"baseUri")},isDebug:function(){return k(this,
"debug")},isIgnorePackageNameInUri:function(){return k(this,"ignorePackageNameInUri")},getCharset:function(){return k(this,"charset")},isCombine:function(){return k(this,"combine")}});d.Package=f;a.augment(i,{setValue:function(a){this.value=a},getType:function(){var a=this.type;a||(this.type=a=".css"==c.extname(this.name).toLowerCase()?"css":"js");return a},getFullPath:function(){var a,d,e,f,h;if(!this.fullpath){e=this.getPackage();d=e.getBaseUri();h=this.getPath();if(e.isIgnorePackageNameInUri()&&
(f=e.getName()))h=c.relative(f,h);d=d.resolve(h);(a=this.getTag())&&d.query.set("t",a);this.fullpath=l.getMappedPath(this.runtime,d.toString())}return this.fullpath},getPath:function(){var a;if(!(a=this.path)){a=this.name;var d="."+this.getType(),e="-min";a=c.join(c.dirname(a),c.basename(a,d));this.getPackage().isDebug()&&(e="");a=this.path=a+e+d}return a},getValue:function(){return this.value},getName:function(){return this.name},getPackage:function(){var b;if(!(b=this.packageInfo)){b=this.runtime;
var c=this.name,e=b.config("packages"),d="",f;for(f in e)a.startsWith(c,f)&&f.length>d.length&&(d=f);b=this.packageInfo=e[d]||b.config("systemPackage")}return b},getTag:function(){return this.tag||this.getPackage().getTag()},getCharset:function(){return this.charset||this.getPackage().getCharset()},getRequiredMods:function(){var b=this.runtime.Env.mods;return a.map(this.getNormalizedRequires(),function(a){return b[a]})},getRequiresWithAlias:function(){var a=this.requiresWithAlias,c=this.requires;
if(!c||0==c.length)return c||[];a||(this.requiresWithAlias=a=l.normalizeModNamesWithAlias(this.runtime,c,this.name));return a},getNormalizedRequires:function(){var a,c=this.normalizedRequiresStatus,e=this.status,d=this.requires;if(!d||0==d.length)return d||[];if((a=this.normalizedRequires)&&c==e)return a;this.normalizedRequiresStatus=e;return this.normalizedRequires=l.normalizeModNames(this.runtime,d,this.name)}});d.Module=i})(KISSY);
(function(a){var k=require("fs"),f=require("vm");a.mix(a,{getScript:function(i,c,d){var l;a.isPlainObject(c)&&(d=c.charset,l=c.error,c=c.success);if(a.startsWith(a.Path.extname(i).toLowerCase(),".css"))c&&c();else{var b=(new a.Uri(i)).getPath();try{var n=k.readFileSync(b,d);f.runInThisContext("(function(KISSY){"+n+"})",i)(a);c&&c()}catch(e){l&&l(e)}}}})})(KISSY);
(function(a,k){function f(b){"/"!=b.charAt(b.length-1)&&(b+="/");l?b=l.resolve(b):(a.startsWith(b,"file:")||(b="file:"+b),b=new a.Uri(b));return b}var i=a.Loader,c=i.Utils,d=a.Env.host.location,l,b,n=a.Config.fns;if(!a.Env.nodejs&&d&&(b=d.href))l=new a.Uri(b);n.map=function(a){var b=this.Config;return!1===a?b.mappedRules=[]:b.mappedRules=(b.mappedRules||[]).concat(a||[])};n.mapCombo=function(a){var b=this.Config;return!1===a?b.mappedComboRules=[]:b.mappedComboRules=(b.mappedComboRules||[]).concat(a||
[])};n.packages=function(b){var c,d=this.Config,m=d.packages=d.packages||{};return b?(a.each(b,function(b,e){c=b.name||e;var d=f(b.base||b.path);b.name=c;b.base=d.toString();b.baseUri=d;b.runtime=a;delete b.path;m[c]=new i.Package(b)}),k):!1===b?(d.packages={},k):m};n.modules=function(b){var d=this,f=d.Env;b&&a.each(b,function(b,e){c.createModuleInfo(d,e,b);a.mix(f.mods[e],b)})};n.base=function(a){var b=this.Config;if(!a)return b.base;a=f(a);b.base=a.toString();b.baseUri=a;return k}})(KISSY);
(function(a){var k=a.Loader,f=a.UA,i=k.Utils;a.augment(k,k.Target,{__currentMod:null,__startLoadTime:0,__startLoadModName:null,add:function(c,d,k){var b=this.runtime;if("string"==typeof c)i.registerModule(b,c,d,k);else if(a.isFunction(c))if(k=d,d=c,f.ie){var c=a.Env.host.document.getElementsByTagName("script"),n,e,o;for(e=c.length-1;0<=e;e--)if(o=c[e],"interactive"==o.readyState){n=o;break}c=n?n.getAttribute("data-mod-name"):this.__startLoadModName;i.registerModule(b,c,d,k);this.__startLoadModName=
null;this.__startLoadTime=0}else this.__currentMod={fn:d,config:k}}})})(KISSY);
(function(a,k){function f(b){a.mix(this,{fn:b,waitMods:{},requireLoadedMods:{}})}function i(a,d,e){var f,i=d.length;for(f=0;f<i;f++){var k=a,m=d[f],l=e,n=k.runtime,q=void 0,t=void 0,q=n.Env.mods,u=q[m];u||(b.createModuleInfo(n,m),u=q[m]);q=u.status;q!=g&&(q===h?l.loadModRequires(k,u):(t=l.isModWait(m),t||(l.addWaitMod(m),q<=o&&c(k,u,l))))}}function c(c,d,f){function g(){d.fn?(e[i]||(e[i]=1),f.loadModRequires(c,d),f.removeWaitMod(i),f.check()):d.status=m}var h=c.runtime,i=d.getName(),l=d.getCharset(),
s=d.getFullPath(),v=0,q=n.ie,t="css"==d.getType();d.status=o;q&&!t&&(c.__startLoadModName=i,c.__startLoadTime=Number(+new Date));a.getScript(s,{attrs:q?{"data-mod-name":i}:k,success:function(){if(d.status==o)if(t)b.registerModule(h,i,a.noop);else if(v=c.__currentMod){b.registerModule(h,i,v.fn,v.config);c.__currentMod=null}a.later(g)},error:g,charset:l})}var d,l,b,n,e={},o,h,m,g;d=a.Loader;l=d.Status;b=d.Utils;n=a.UA;o=l.LOADING;h=l.LOADED;m=l.ERROR;g=l.ATTACHED;f.prototype={check:function(){var b=
this.fn;b&&a.isEmptyObject(this.waitMods)&&(b(),this.fn=null)},addWaitMod:function(a){this.waitMods[a]=1},removeWaitMod:function(a){delete this.waitMods[a]},isModWait:function(a){return this.waitMods[a]},loadModRequires:function(a,b){var c=this.requireLoadedMods,d=b.name;c[d]||(c[d]=1,c=b.getNormalizedRequires(),i(a,c,this))}};a.augment(d,{use:function(a,c,d){var e,g=new f(function(){b.attachModsRecursively(e,h);c&&c.apply(h,b.getModules(h,a))}),h=this.runtime,a=b.getModNamesAsArray(a),a=b.normalizeModNamesWithAlias(h,
a);e=b.unalias(h,a);i(this,e,g);d?g.check():setTimeout(function(){g.check()},0);return this}})})(KISSY);
(function(a,k){function f(){var b=/^(.*)(seed|kissy)(?:-min)?\.js[^/]*/i,d=/(seed|kissy)(?:-min)?\.js/i,e,f,h=c.host.document.getElementsByTagName("script"),i=h[h.length-1],h=i.src,i=(i=i.getAttribute("data-config"))?(new Function("return "+i))():{};e=i.comboPrefix=i.comboPrefix||"??";f=i.comboSep=i.comboSep||",";var g,j=h.indexOf(e);-1==j?g=h.replace(b,"$1"):(g=h.substring(0,j),"/"!=g.charAt(g.length-1)&&(g+="/"),h=h.substring(j+e.length).split(f),a.each(h,function(a){return a.match(d)?(g+=a.replace(b,
"$1"),!1):k}));return a.mix({base:g},i)}a.mix(a,{add:function(a,c,d){this.getLoader().add(a,c,d)},use:function(a,c){var d=this.getLoader();d.use.apply(d,arguments)},getLoader:function(){var a=this.Env;return this.Config.combine&&!a.nodejs?a._comboLoader:a._loader},require:function(a){return d.getModules(this,[a])[1]}});var i=a.Loader,c=a.Env,d=i.Utils,l=a.Loader.Combo;a.Env.nodejs?a.config({charset:"utf-8",base:__dirname.replace(/\\/g,"/").replace(/\/$/,"")+"/"}):a.config(a.mix({comboMaxUrlLength:2E3,
comboMaxFileNum:40,charset:"utf-8",tag:"20130115133943"},f()));a.config("systemPackage",new i.Package({name:"",runtime:a}));c.mods={};c._loader=new i(a);l&&(c._comboLoader=new l(a))})(KISSY);
(function(a,k){var f=a.Env.host,i=a.UA,c=f.document,d=c&&c.documentElement,l=f.location,b=new a.Defer,n=b.promise,e=/^#?([\w-]+)$/,o=/\S/;a.mix(a,{isWindow:function(a){return null!=a&&a==a.window},parseXML:function(a){if(a.documentElement)return a;var b;try{f.DOMParser?b=(new DOMParser).parseFromString(a,"text/xml"):(b=new ActiveXObject("Microsoft.XMLDOM"),b.async=!1,b.loadXML(a))}catch(c){b=k}!b||!b.documentElement||b.getElementsByTagName("parsererror");return b},globalEval:function(a){a&&o.test(a)&&
(f.execScript||function(a){f.eval.call(f,a)})(a)},ready:function(a){n.then(a);return this},available:function(b,d){if((b=(b+"").match(e)[1])&&a.isFunction(d))var f=1,h,i=a.later(function(){((h=c.getElementById(b))&&(d(h)||1)||500<++f)&&i.cancel()},40,!0)}});if(l&&-1!==(l.search||"").indexOf("ks-debug"))a.Config.debug=!0;(function(){var e=d&&d.doScroll,g=e?"onreadystatechange":"DOMContentLoaded",h=function(){b.resolve(a)};if(!c||"complete"===c.readyState)return h();if(c.addEventListener){var i=function(){c.removeEventListener(g,
i,!1);h()};c.addEventListener(g,i,!1);f.addEventListener("load",h,!1)}else{var k=function(){"complete"===c.readyState&&(c.detachEvent(g,k),h())};c.attachEvent(g,k);f.attachEvent("onload",h);var l;try{l=null===f.frameElement}catch(n){l=!1}if(e&&l){var o=function(){try{e("left"),h()}catch(a){setTimeout(o,40)}};o()}}return 0})();if(i.ie)try{c.execCommand("BackgroundImageCache",!1,!0)}catch(h){}})(KISSY,void 0);
(function(a,k,f){a({ajax:{requires:["dom","json","event"]}});a({anim:{requires:["dom","event"]}});a({base:{requires:["event/custom"]}});a({button:{requires:["component/base","event"]}});a({calendar:{requires:["node","event"]}});a({color:{requires:["base"]}});a({combobox:{requires:["dom","component/base","node","menu","ajax"]}});a({"component/base":{requires:["rich-base","node","event"]}});a({"component/extension":{requires:["dom","node"]}});a({"component/plugin/drag":{requires:["rich-base","dd/base"]}});
a({"component/plugin/resize":{requires:["resizable"]}});a({datalazyload:{requires:["dom","event","base"]}});a({dd:{alias:["dd/base","dd/droppable"]}});a({"dd/base":{requires:["dom","node","event","rich-base","base"]}});a({"dd/droppable":{requires:["dd/base","dom","node","rich-base"]}});a({"dd/plugin/constrain":{requires:["base","node"]}});a({"dd/plugin/proxy":{requires:["node","base","dd/base"]}});a({"dd/plugin/scroll":{requires:["dd/base","base","node","dom"]}});a({dom:{alias:["dom/base",9>f.ie?
"dom/ie":""]}});a({"dom/ie":{requires:["dom/base"]}});a({editor:{requires:["htmlparser","component/base","core"]}});a({event:{alias:["event/base","event/dom","event/custom"]}});a({"event/custom":{requires:["event/base"]}});a({"event/dom":{alias:["event/dom/base",k.isTouchSupported()?"event/dom/touch":"",k.isDeviceMotionSupported()?"event/dom/shake":"",k.isHashChangeSupported()?"":"event/dom/hashchange",9>f.ie?"event/dom/ie":"",f.ie?"":"event/dom/focusin"]}});a({"event/dom/base":{requires:["dom","event/base"]}});
a({"event/dom/focusin":{requires:["event/dom/base"]}});a({"event/dom/hashchange":{requires:["event/dom/base","dom"]}});a({"event/dom/ie":{requires:["event/dom/base","dom"]}});a({"event/dom/shake":{requires:["event/dom/base"]}});a({"event/dom/touch":{requires:["event/dom/base","dom"]}});a({imagezoom:{requires:["node","overlay"]}});a({json:{requires:[KISSY.Features.isNativeJSONSupported()?"":"json/json2"]}});a({kison:{requires:["base"]}});a({menu:{requires:["component/extension","node","component/base",
"event"]}});a({menubutton:{requires:["node","menu","button","component/base"]}});a({mvc:{requires:["event","base","ajax","json","node"]}});a({node:{requires:["dom","event/dom","anim"]}});a({overlay:{requires:["node","component/base","component/extension","event"]}});a({resizable:{requires:["node","rich-base","dd/base"]}});a({"rich-base":{requires:["base"]}});a({separator:{requires:["component/base"]}});a({"split-button":{requires:["component/base","button","menubutton"]}});a({stylesheet:{requires:["dom"]}});
a({swf:{requires:["dom","json","base"]}});a({switchable:{requires:["dom","event","anim",KISSY.Features.isTouchSupported()?"dd/base":""]}});a({tabs:{requires:["button","toolbar","component/base"]}});a({toolbar:{requires:["component/base","node"]}});a({tree:{requires:["node","component/base","event"]}});a({waterfall:{requires:["node","base"]}});a({xtemplate:{alias:["xtemplate/facade"]}});a({"xtemplate/compiler":{requires:["xtemplate/runtime"]}});a({"xtemplate/facade":{requires:["xtemplate/runtime",
"xtemplate/compiler"]}})})(function(a){KISSY.config("modules",a)},KISSY.Features,KISSY.UA);(function(a){a.add("empty",a.noop);a.add("promise",function(){return a.Promise});a.add("ua",function(){return a.UA});a.add("uri",function(){return a.Uri});a.add("path",function(){return a.Path})})(KISSY);
