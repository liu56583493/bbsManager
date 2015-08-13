package com.bbs.manage.ui.beetl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bbs.manage.common.WebUtils;
import com.bbs.manage.model.TUser;
import com.bbs.utils.PrettyTimeUtils;

/**
 * beetl函数扩展
 * @title FunctionPackage.java
 * @description 
 * @company 北京思维夫网络科技有限公司
 * @author 卢春梦
 * @version 1.0
 * @created 2015年5月19日上午10:25:23
 */
public class FunctionPackage {

	/**
	 * 继续encode URL (url,传参tomcat会自动解码)
	 * 要作为参数传递的话，需要再次encode
	 * @param encodeUrl
	 * @return String
	 */
	public String encodeUrl(String url) {
		try {
			url = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// ignore
		}
		return url;
	}

	/**
	 * 模版中拿取cookie中的用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public TUser currentUser(HttpServletRequest request, HttpServletResponse response) {
		return WebUtils.currentUser(request, response);
	}

	/**
	 * 格式化docker的时间
	 * @param time
	 * @return
	 */
	public String dockerTime(long time) {
		return PrettyTimeUtils.prettyTime(new Date(time * 1000));
	}
}
