package com.xinhuanet.pay.common;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/**
 * @author duanwc
 */
public class PageProcesser implements Tag {
    private PageContext pageContext;
    private Tag parent;
    //标签的属性定义
    /**
     * 转发的url
     */
    private String url;
    /**
     * 当前页属性名
     */
    private String currentPageAN;
    /**
     * 总页数属性名
     */
    private String maxPageCountAN;
    /**
     * 总行数属性名
     */
    private String maxRowCountAN;
    /**
     * 分页样式目前支持两种:
     * 1. simple 默认 简单样式
     * 2. page 页码样式
     * 3. xhtml 完整样式
     */
    private String theme = "simple";
    /**
     * 仅对theme="simple"样式有效，指定页面跳转方式，支持以下两种:
     * 1. input 默认 文本框输入跳转
     * 2. select 下拉选择框方式跳转
     */
    private String skip = "input";
    //标签的属性定义结束
    private int currentPage = 1;//当前页
    private int maxPageCount;//总页数
    private int maxRowCount;//总行数
    private int prePage;
    private int nextPage;
    private int beginPage;//开始页
    private int endPage;//结束页

    public PageProcesser() {
        super();
    }

    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    public Tag getParent() {
        return parent;
    }

    public void setCurrentPageAN(String currentPageAN) {
        this.currentPageAN = currentPageAN;
    }

    public void setMaxPageCountAN(String maxPageCountAN) {
        this.maxPageCountAN = maxPageCountAN;
    }

    public void setMaxRowCountAN(String maxRowCountAN) {
        this.maxRowCountAN = maxRowCountAN;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setSkip(String skip) {
        this.skip = skip;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //共 39 条,共 2 页,目前为第 1 页    [首页][上页] [下页] [尾页]
    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        Integer maxRowCountIn = (Integer) request.getAttribute(maxRowCountAN);
        Integer maxPageCountIn = (Integer) request.getAttribute(maxPageCountAN);
        Integer currentPageIn = (Integer) request.getAttribute(currentPageAN);
        StringBuffer sb = new StringBuffer();
        try {
            //处理url应该加?还是&
            if (url.indexOf("?") != -1) {
                this.setUrl(url + "&");
            } else {
                this.setUrl(url + "?");
            }
            if ("simple".equals(theme)) {//简单样式
            	sb = simpleTheme(maxRowCountIn,maxPageCountIn,currentPageIn,sb);
            } else if ("xhtml".equals(theme)) {//输入框样式
            	sb = xhtmlTheme(maxRowCountIn,maxPageCountIn,currentPageIn,sb);
            } else if ("page".equals(theme)) { //页码样式
            	sb = pageTheme(maxRowCountIn,maxPageCountIn,currentPageIn,sb);
            }
            pageContext.getOut().write(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }
    /**
     * 简单样式分页
     * @param maxRowCountIn
     * @param maxPageCountIn
     * @param currentPageIn
     * @param sb
     * @return
     */
    private StringBuffer simpleTheme(Integer maxRowCountIn,Integer maxPageCountIn,Integer currentPageIn,StringBuffer sb){
        if ((maxRowCountIn != null && maxRowCountIn.intValue() != 0) && (maxPageCountIn != null && maxPageCountIn.intValue() != 0) && (currentPageIn != null && currentPageIn.intValue() != 0)) {
            maxRowCount = maxRowCountIn.intValue();
            maxPageCount = maxPageCountIn.intValue();
            currentPage = currentPageIn.intValue();
            if (currentPage < 1) {
                currentPage = 1;
            }
            prePage = currentPage - 1;
            nextPage = currentPage + 1;
            sb.append(" 共 " + maxRowCount + " 条 |");
            sb.append(" 共 " + maxPageCount + " 页 |");
            sb.append(" 当前为第 " + currentPage + " 页 | ");
            if (currentPage != 1) {
                sb.append("<a href=\"" + url + "page=1\">[首页]</a>  ");
                sb.append("<a href=\"" + url + "page=" + prePage + "\">[上页]</a>  ");
            } else {
                sb.append("[首页]");
                sb.append("[上页]");
            }
            if (currentPage != maxPageCount && maxPageCount != 1) {
                sb.append("<a href=\"" + url + "page=" + nextPage + "\">[下页]</a>  ");
                sb.append("<a href=\"" + url + "page=" + maxPageCount + "\">[尾页]</a>  ");
            } else {
                sb.append("[下页]");
                sb.append("[尾页]");
            }

            if ("select".equals(skip)) { //select框选择跳转
                sb.append(" 跳转到<select id=\"_page_skip\" onchange=\"pageSkip();\">");
                for (int i = 1; i <= maxPageCount; i++) {
                    sb.append("<option value=\"" + i + "\" " + ((currentPage == i) ? "selected" : "") + ">");
                    sb.append(i);
                    sb.append("</option>");
                }
                sb.append("</select>页");
            } else if ("input".equals(skip)) {//input框选择跳转
                sb.append("跳转到<input id=\"_page_skip\" size=\"3\" />页 ");
                sb.append("<input type=\"button\" value=\"跳 转\" onclick=\"pageSkip();\" />");
            }

            sb.append("<script type=\"text/javascript\">");
            sb.append("function pageSkip(){" +
                    "var pageObj = document.getElementById(\"_page_skip\");" +
                    "var page = pageObj.value;" +
                    "var result = /^[1-9][0-9]*$/.test(page);" +
                    "if(page>" + maxPageCount + "){" +
                    "result=false;" +
                    "}" +
                    "if(result){" +
                    "var url;" +
                    "url=\"" + url + "page=\"+page;" +
                    "window.location.href = url;" +
                    "} else{" +
                    "pageObj.focus();" +
                    "pageObj.select();" +
                    "}" +
                    "}");
            sb.append("</script>");
        } else {
            sb.append(" 共 " + "0" + " 条 |");
            sb.append(" 共 " + "0" + " 页 |");
            sb.append(" 当前为第 " + "0" + " 页 | ");
            sb.append("[首页]");
            sb.append("[上页]");
            sb.append("[下页]");
            sb.append("[尾页]");
        }
        return sb;
    }
    /**
     * 有格式的分页
     * @param maxRowCountIn
     * @param maxPageCountIn
     * @param currentPageIn
     * @param sb
     * @return
     */
    private StringBuffer xhtmlTheme(Integer maxRowCountIn,Integer maxPageCountIn,Integer currentPageIn,StringBuffer sb){
    	if ((maxRowCountIn != null && maxRowCountIn.intValue() != 0) && (maxPageCountIn != null && maxPageCountIn.intValue() != 0) && (currentPageIn != null && currentPageIn.intValue() != 0)) {
            maxRowCount = maxRowCountIn.intValue();
            maxPageCount = maxPageCountIn.intValue();
            currentPage = currentPageIn.intValue();
            if (currentPage < 1) {
                currentPage = 1;
            }
            prePage = currentPage - 1;
            nextPage = currentPage + 1;
            
            sb.append(" 共 <span>" + maxRowCount + "</span> 条 | ");
            if (currentPage != 1) {
                sb.append("<a href=\"" + url + "page=1\">[首页]</a>  ");
                sb.append("<a href=\"" + url + "page=" + prePage + "\">[上一页]</a>  ");
            } else {
                sb.append("[首页]");
                sb.append("[上一页]");
            }
            sb.append("<input id=\"_page_skip_readonly\" type=\"text\" style=\"text-align:center\" size=\"4\" value=\""+ currentPage+"/"+maxPageCount +"\" readonly=\"true\" />");
            if (currentPage != maxPageCount && maxPageCount != 1) {
                sb.append("<a href=\"" + url + "page=" + nextPage + "\">[下一页]</a>  ");
                sb.append("<a href=\"" + url + "page=" + maxPageCount + "\">[末页]</a>  ");
            } else {
                sb.append("[下一页]");
                sb.append("[末页]");
            }

            if ("select".equals(skip)) { //select框选择跳转
                sb.append(" &nbsp;&nbsp;&nbsp;&nbsp;跳转到<select id=\"_page_skip\" onchange=\"pageSkip();\">");
                for (int i = 1; i <= maxPageCount; i++) {
                    sb.append("<option value=\"" + i + "\" " + ((currentPage == i) ? "selected" : "") + ">");
                    sb.append(i);
                    sb.append("</option>");
                }
                sb.append("</select>页");
            } else if ("input".equals(skip)) {//input框选择跳转
                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;跳转到<input id=\"_page_skip\" style=\"text-align:center\" size=\"3\" value=\""+currentPage+"\" />页 ");
                sb.append("<input id=\"_page_skip_button\" type=\"button\" value=\"跳 转\" onclick=\"pageSkip();\" />");
            }

            sb.append("<script type=\"text/javascript\">");
            sb.append("function pageSkip(){" +
                    "var pageObj = document.getElementById(\"_page_skip\");" +
                    "var page = pageObj.value;" +
                    "var result = /^[1-9][0-9]*$/.test(page);" +
                    "if(page>" + maxPageCount + "){" +
                    "result=false;" +
                    "}" +
                    "if(result){" +
                    "var url;" +
                    "url=\"" + url + "page=\"+page;" +
                    "window.location.href = url;" +
                    "} else{" +
                    "pageObj.focus();" +
                    "pageObj.select();" +
                    "}" +
                    "}");
            sb.append("</script>");
        } else {
            sb.append("[首页]");
            sb.append("[上一页]");
            sb.append("<input id=\"_page_skip_readonly\"  type=\"text\" style=\"text-align:center\" size=\"5\" value=\"0/0\" readonly=\"true\" />");
            sb.append("[下一页]");
            sb.append("[末页]");
        }
    	return sb;
    }
    /**
     * 页码样式分页
     * @param maxRowCountIn
     * @param maxPageCountIn
     * @param currentPageIn
     * @param sb
     * @return
     */
    private StringBuffer pageTheme(Integer maxRowCountIn,Integer maxPageCountIn,Integer currentPageIn,StringBuffer sb){
    	if ((maxRowCountIn != null && maxRowCountIn.intValue() != 0) && (maxPageCountIn != null && maxPageCountIn.intValue() != 0) && (currentPageIn != null && currentPageIn.intValue() != 0)) {
            maxRowCount = maxRowCountIn.intValue();
            maxPageCount = maxPageCountIn.intValue();
            currentPage = currentPageIn.intValue();
            if (currentPage < 1) {
                currentPage = 1;
            }
            prePage = currentPage - 1;
            nextPage = currentPage + 1;
            
//            sb.append(" 共 " + maxPageCount + " 页 | ");
            if (currentPage != 1) {
                sb.append("<a href=\"" + url + "page=" + prePage + "\">上一页</a>  ");
            }
            //页码配置开始
            if ((currentPage - 5) > 0) {
                beginPage = currentPage - 4;
            } else {
                beginPage = 1;
            }
            if (maxPageCount >= (currentPage + 5)) {
                endPage = currentPage + 4;
            } else {
                endPage = maxPageCount;
            }
            if (maxPageCount > 1) {
                for (int i = beginPage; i <= endPage; i++) {
                    if (i == currentPage) {
                        sb.append("<span><b>").append(i).append("</b></span> ");
                    } else {
                        sb.append("<a href=\"" + url + "page=" + i + "\">" + i + "</a>  ");
                    }
                }
            }
            //页码配置结束
            if (currentPage != maxPageCount && maxPageCount != 1) {
                sb.append("<a href=\"" + url + "page=" + nextPage + "\">下一页</a>  ");
            }
            sb.append(" 共 " + maxRowCount + " 条 ");
        }
    	return sb;
    }
    
    
//    /**
//     * 页码样式分页
//     * @param maxRowCountIn
//     * @param maxPageCountIn
//     * @param currentPageIn
//     * @param sb
//     * @return
//     */
//    private StringBuffer pageTheme(Integer maxRowCountIn,Integer maxPageCountIn,Integer currentPageIn,StringBuffer sb){
//    	if ((maxRowCountIn != null && maxRowCountIn.intValue() != 0) && (maxPageCountIn != null && maxPageCountIn.intValue() != 0) && (currentPageIn != null && currentPageIn.intValue() != 0)) {
//            maxRowCount = maxRowCountIn.intValue();
//            maxPageCount = maxPageCountIn.intValue();
//            currentPage = currentPageIn.intValue();
//            if (currentPage < 1) {
//                currentPage = 1;
//            }
//            prePage = currentPage - 1;
//            nextPage = currentPage + 1;
//            sb.append(" 共 " + maxRowCount + " 条 |");
//            sb.append(" 共 " + maxPageCount + " 页 | ");
//            if (currentPage != 1) {
//                sb.append("<a href=\"" + url + "page=" + prePage + "\">上一页</a>  ");
//            }
//            //页码配置开始
//            if ((currentPage - 10) > 0) {
//                beginPage = currentPage - 9;
//            } else {
//                beginPage = 1;
//            }
//            if (maxPageCount >= (currentPage + 10)) {
//                endPage = currentPage + 9;
//            } else {
//                endPage = maxPageCount;
//            }
//            if (maxPageCount > 1) {
//                for (int i = beginPage; i <= endPage; i++) {
//                    if (i == currentPage) {
//                        sb.append("<span><b>").append(i).append("</b></span> ");
//                    } else {
//                        sb.append("<a href=\"" + url + "page=" + i + "\">" + i + "</a>  ");
//                    }
//                }
//            }
//            //页码配置结束
//            if (currentPage != maxPageCount && maxPageCount != 1) {
//                sb.append("<a href=\"" + url + "page=" + nextPage + "\">下一页</a>  ");
//            }
//        }
//    	return sb;
//    }

    public void release() {
    }
}