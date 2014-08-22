package com.xinhuanet.pay.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.log.OperatorLog;

/**
 * Handles requests for the application welcome page.
 */
@Controller
public class IndexController extends BaseController {
	
	private @Autowired OperatorLog operatorLog;
	
	/**
	 * Simply selects the welcome view to render by returning void and relying
	 * on the default request-to-view-translator. 首页
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/index.htm")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		
//		String path = getClass().getClassLoader().getResource("/").getPath()+"security/MerPrk"+140+".key";
//		File file = new File(path);
//		System.out.println(file.exists());

//		operatorLog.requestOperLog(request, "人奔顶戴顶戴顶戴顶戴");
		return "forward:account/index";
	}
	
	
	@RequestMapping(value = "/index.do")
	public String indexDemo(HttpServletRequest request, HttpServletResponse response) {
		return "index";
	}
}
