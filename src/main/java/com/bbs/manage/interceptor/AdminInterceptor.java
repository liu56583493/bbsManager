package com.bbs.manage.interceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.bbs.manage.common.WebUtils;
import com.bbs.manage.model.TUser;
import com.bbs.vo.AjaxResult;

/**
 * 用户中心拦截器
 * @title AdminInterceptor.java
 * @description 
 * @company 北京思维夫网络科技有限公司
 * @author 卢春梦
 * @version 1.0
 * @created 2015年5月23日下午3:00:25
 */
public class AdminInterceptor implements Interceptor {

	private final Logger logger = Logger.getLogger(getClass());

	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		// 拿取cookie中的用户信息
		TUser user = WebUtils.currentAdmin(controller);
		// 如果用户已经是登陆状态、同事是管理员，则执行
		if (null != user && TUser.IS_ADMIN == user.getRole()) {
			ai.invoke();
			return;
		}

		// 判断是否ajax请求，ajax请求返回json
		String isXMLHttpRequest = controller.getRequest().getHeader("X-Requested-With");
		if (StrKit.notBlank(isXMLHttpRequest)) {
			AjaxResult result = new AjaxResult();
			result.addConfirmError("您的登陆状态已失效");
			controller.renderJson(result);
			return;
		}
		
		// 登陆页URL
		String loginPath = "/admin/land?from=%s";

		HttpServletRequest request = controller.getRequest();
		String queryString = request.getQueryString();
		// 被拦截前的请求URL
		String redirectUrl = request.getRequestURI();
		if (StrKit.notBlank(queryString)) {
			redirectUrl = redirectUrl.concat("?").concat(queryString);
		}
		// 获取项目的path
		String contextPath = WebUtils.basePath();
		// 探测 是否含有ContextPath
		if (StrKit.notBlank(contextPath) && redirectUrl.indexOf(contextPath) != -1) {
			loginPath = contextPath.concat(loginPath);
		}
		// -- end 探测 是否含有ContextPath
		try {
			redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		String tagPath = String.format(loginPath, redirectUrl);
		logger.info("login[redirectUrl]:\t" + tagPath);
		controller.redirect(tagPath);
		return;
	}

}
