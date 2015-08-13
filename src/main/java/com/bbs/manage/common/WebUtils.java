package com.bbs.manage.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.EncryptionKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.bbs.manage.model.TUser;
import com.bbs.utils.DESUtils;
import com.bbs.utils.RegexUtils;

/**
 * Web项目帮助类
 * @title WebUtils.java
 * @description 
 * @company 北京思维夫网络科技有限公司
 * @author 卢春梦  
 * @version 1.0
 * @created 2015年4月8日上午10:37:53
 */
public final class WebUtils {

	// 禁止实例化
	private WebUtils(){}

	//-------------------------------------------------------------------------//
	//--项目全局变量部分
	//-------------------------------------------------------------------------//
	public static final String INV_COOKIE_KEY = "inv_code";
	
	//-------------------------------------------------------------------------//

	/**
	 * 获取jfianl项目的basePath
	 * @return String
	 */
	public static String basePath() {
		return JFinal.me().getContextPath();
	}

	/**
	 * 拼接basePath，形成完整路径
	 */
	public static String concatBasePath(String path) {
		return basePath().concat(path);
	}

	/**
	 * 密码:md5hex
	 * @param pwd
	 * @return
	 */
	public static String pwdEncode(String password) {
		return EncryptionKit.md5Encrypt(password);
	}

	/**
	 * cookie时间设计
	 */
	public static final int HOUR = 60 * 60;
	public static final int DAY = HOUR * 24;

	/**
	 * cookie保存的key
	 * 根据客户端类型，分为Admin、Web/Wap
	 * 
	 * des(私钥).encode(uuid~nickName~time~maxAge~ip)
	 */
	private enum ClientType {
		Admin,
		Web;

		/**
		 * 获取type
		 * @return cookie生成的id
		 */
		public String cookieKey() {
			return this.name().toLowerCase().concat("_id");
		}

		/**
		 * 获取对应的密钥，配置与JFinal主配置文件中
		 * @return cookie 加密 secret
		 */
		public String secret() {
			return PropKit.get(this.name().toLowerCase().concat(".secret"));
		}
	}

	/**
	 * 返回前台、Wap当前用户信息
	 * 
	 * @param c
	 * @return UserModel
	 */
	public static TUser currentUser(Controller c) {
		HttpServletRequest  request  = c.getRequest();
		HttpServletResponse response = c.getResponse();
		return currentUser(request, response);
	}

	/**
	 * 返回前台、Wap当前用户
	 * @param request
	 * @param response
	 * @return UserModel
	 */
	public static TUser currentUser(HttpServletRequest request, HttpServletResponse response) {
		String userId = currentClient(request, response, ClientType.Web);
		return TUser.dao.findById(userId);
	}

	/**
	 * 返回后台用户
	 * @param c
	 * @return userId
	 */
	public static TUser currentAdmin(Controller c) {
		HttpServletRequest  request  = c.getRequest();
		HttpServletResponse response = c.getResponse();
		String userId = currentClient(request, response, ClientType.Admin);
		return TUser.dao.findById(userId);
	}

	/**
	 * 根据客户端类型，获取cookie中的用户id
	 * @param request
	 * @param response
	 * @param clientType
	 * @return
	 */
	private static String currentClient(HttpServletRequest request, HttpServletResponse response, ClientType clientType) {
		String cookieKey = clientType.cookieKey();
		// 获取cookie信息
		String userCookie = getCookie(request, cookieKey);
		// 1.cookie为空，直接清除
		if (StrKit.isBlank(userCookie)) {
			removeCookie(response, cookieKey);
			return null;
		}
		// 2.解密cookie
		String cookieInfo = null;
		// cookie 私钥
		String secret = clientType.secret();
		try {
			cookieInfo = new DESUtils(secret).decryptString(userCookie);
		} catch (RuntimeException e) {
			// ignore
		}
		// 3.异常或解密问题，直接清除cookie信息
		if (StrKit.isBlank(cookieInfo)) {
			removeCookie(response, cookieKey);
			return null;
		}
		String[] userInfo = cookieInfo.split("~");
		// 4.规则不匹配
		if (userInfo.length < 4) {
			removeCookie(response, cookieKey);
			return null;
		}
		String userId   = userInfo[0];
//		String nickName = userInfo[1];
		String oldTime  = userInfo[2];
		String maxAge   = userInfo[3];
		// 5.判定时间区间，超时的cookie清理掉
		if (!"-1".equals(maxAge)) {
			long now  = System.currentTimeMillis();
			long time = Long.parseLong(oldTime) + (Long.parseLong(maxAge) * 1000);
			if (time <= now) {
				removeCookie(response, cookieKey);
				return null;
			}
		}
		return userId;
	}

	/**
	 * 用户登陆状态维持
	 * 
	 * cookie设计为: des(私钥).encode(uuid~nickName~time~maxAge~ip)
	 * 
	 * @param Controller 控制器
	 * @param UserModel  用户model
	 * @param remember   是否记住密码、此参数控制cookie的 maxAge，默认为-1（只在当前会话）<br>
	 *                   记住密码默认为一周
	 * @return void
	 */
	public static void loginUser(Controller c, TUser user, boolean... remember) {
		// 获取用户的id、nickName
		String uuid     = user.getId() + "";
		// 用户昵称并清理其中的`~`
		String nickName = user.getAccounName().replace("~", "");
		loginClient(c, uuid, nickName, ClientType.Web, remember);
	}

	/**
	 * 用户登陆状态维持
	 * 
	 * cookie设计为: des(私钥).encode(uuid~nickName~time~maxAge~ip)
	 * 
	 * @param Controller 控制器
	 * @param AdminModel 后台用户model
	 * @param remember   是否记住密码、此参数控制cookie的 maxAge，默认为-1（只在当前会话）<br>
	 *                   记住密码默认为一周
	 * @return void
	 */
	public static void loginAdmin(Controller c, TUser admin, boolean... remember) {
		// 获取用户的id、nickName
		String uuid     = admin.getId() + "";
		// 用户昵称并清理其中的`~`
		String nickName = admin.getAccounName().replace("~", "");
		loginClient(c, uuid, nickName, ClientType.Admin, remember);
	}

	/**
	 * 将用户信息加密保存到cookie中
	 * @param c
	 * @param uuid
	 * @param nickName
	 * @param clientType
	 * @param remember
	 */
	private static void loginClient(Controller c, final String uuid, final String nickName, ClientType clientType, boolean... remember) {
		// 当前毫秒数
		long   now      = System.currentTimeMillis();
		// 超时时间
		int    maxAge   = -1;
		if (remember.length > 0 && remember[0]) {
			maxAge      = DAY * 7;
		}
		// 用户id地址
		String ip		= getIP(c);
		// 构造cookie
		StringBuilder cookieBuilder = new StringBuilder()
			.append(uuid).append("~")
			.append(nickName).append("~")
			.append(now).append("~")
			.append(maxAge).append("~")
			.append(ip);

		// cookie 私钥
		String secret = clientType.secret();
		// 加密cookie
		String userCookie = new DESUtils(secret).encryptString(cookieBuilder.toString());
		HttpServletResponse response = c.getResponse();

		String cookieKey = clientType.cookieKey();
		// 设置用户的cookie、 -1 维持成session的状态
		setCookie(response, cookieKey, userCookie, maxAge);
	}

	/**
	 * 退出即删除用户信息
	 * @param Controller
	 * @return void
	 */
	public static void logoutUser(Controller c) {
		logout(c, ClientType.Web);
	}

	/**
	 * 退出即删除用户信息
	 * @param Controller
	 * @return void
	 */
	public static void logoutAdmin(Controller c) {
		logout(c, ClientType.Admin);
	}

	/**
	 * 退出即删除用户信息
	 * @param Controller
	 * @return void
	 */
	private static void logout(Controller c, ClientType clientType) {
		HttpServletResponse response = c.getResponse();
		removeCookie(response, clientType.cookieKey());
	}

	/**
	 * 读取cookie
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if(null != cookies){
			for (Cookie cookie : cookies) {
				if (key.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @description  功能描述: 获取cookie中的邀请码
	 * @author 		        作        者: liuwei
	 * @param	                    参        数: 
	 * @return        返回类型: String
	 * @createdate   建立日期: 2015年5月21日下午2:19:30
	 */
	public static String getInvCode(HttpServletRequest request){
		String invCode = WebUtils.getCookie(request, "inv_code");
		if(null == invCode){
			return null;
		}
		//解密邀请码的cookie
		String cookie = invCode.split("~")[1];
		return cookie;
	}
	
	
	
	/**
	 * 清除 某个指定的cookie 
	 * @param response
	 * @param key
	 */
	public static void removeCookie(HttpServletResponse response, String key) {
		setCookie(response, key, null, 0);
	}

	/**
	 * 设置cookie
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAgeInSeconds
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeInSeconds);
		// 指定为httpOnly保证安全性
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	/**
	 * 获取浏览器信息
	 * @param Controller
	 * @return String
	 */
	public static String getUserAgent(Controller c) {
		return getUserAgent(c.getRequest());
	}

	/**
	 * 获取浏览器信息
	 * @param HttpServletRequest
	 * @return String
	 */
	public static String getUserAgent(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}
	
	/**
	 * 获取ip
	 * @param request
	 * @return
	 */
	public static String getIP(Controller c) {
		HttpServletRequest request = c.getRequest();
		return getIP(request);
	}

	/**
	 * 获取ip
	 * @param request
	 * @return
	 */
	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Requested-For");
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 手机User-Agent判断，暂定只对iphone、android、wphone的跳入wap站点
	 * 统一小写
	 */
	private static final String MOBILE = ".*(iphone|android|iemobile){1,}.*";

	/**
	 * 判断是否是android或者iphone智能机
	 * @param HttpServletRequest
	 * @return boolean
	 */
	public static boolean isTouch(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		return isTouch(userAgent);
	}

	/**
	 * 判断是否是android、iphone、wp智能机
	 * @param String userAgent
	 * @return boolean
	 */
	public static boolean isTouch(String userAgent) {
		return RegexUtils.match(MOBILE, userAgent);
	}

}
