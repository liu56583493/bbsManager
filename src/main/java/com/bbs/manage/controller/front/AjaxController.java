package com.bbs.manage.controller.front;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;
import com.bbs.base.BaseController;
import com.bbs.manage.common.WebUtils;
import com.bbs.manage.interceptor.UserInterceptor;
import com.bbs.manage.model.TMovie;
import com.bbs.manage.model.TUser;
import com.bbs.manage.validator.RegisterValidator;
import com.bbs.manage.validator.UpdateUserValidator;
import com.bbs.utils.RegexUtils;
import com.bbs.utils.UploadFileUtils;
import com.bbs.utils.UploadFileUtils.FileType;
import com.bbs.vo.AjaxResult;

/**
 * 
 * @description  ajax
 * @author 		  liuwei
 * @createdate   2015年6月30日上午11:47:35
 * @projectname  bbsManage
 * @packageclass com.bbs.manage.controller.front.AjaxController.java
 */
public class AjaxController extends BaseController {

	private AjaxResult result = new AjaxResult();

	public void index() {
		result.addError("404，您访问的内容不存在~");
		renderJson(result);
	}

	public void session() {
		String loginName = getPara("loginName");
		if (StrKit.isBlank(loginName)) {
			result.addError("请输入手机号或者邮箱");
			renderJson(result);
			return;
		}
		String password = getPara("password");
		if (StrKit.isBlank(password)) {
			result.addError("请输入您的密码");
			renderJson(result);
			return;
		}
		// 2.判断登陆名是否合法
		if (!RegexUtils.match(RegexUtils.EMAIL_OR_PHONE, loginName)) {
			result.addError("请检查您的手机号或邮箱地址");
			renderJson(result);
			return;
		}
		// 3.密码长度不合规范
		if (!RegexUtils.match(RegexUtils.USER_PASSWORD, password)) {
			result.addError("请检查您输入的密码");
			renderJson(result);
			return;
		}
		TUser user = TUser.dao.findByPhoneOREmail(loginName);
		if (null == user) {
			result.addError("没有找到该用户，请先注册");
			renderJson(result);
			return;
		}
		// 4.加密密码
		password = WebUtils.pwdEncode(password);
		String userPwd = user.getPassword();
		if (StrKit.isBlank(userPwd) || !userPwd.equalsIgnoreCase(password)) {
			result.addError("您输入的密码有误，请尝试重新输入或者找回密码");
			renderJson(result);
			return;
		}
		// 是否记住密码
		String remember = getPara("remember", "0");
		// 登陆状态维持
		WebUtils.loginUser(this, user, "1".equals(remember));
		renderJson(result);
	}

	/**
	 * 用户注册提交
	 */
	@Before(RegisterValidator.class)
	public void create_user() {
		String userName = getPara("userName");
		String passWord = getPara("passWord");
		TUser user = new TUser();

		// 判断用户是采用的手机还是邮箱注册
		if (RegexUtils.match(RegexUtils.PHONE, userName)) {
			user.setPhone(userName);
		} else {
			user.setEmail(userName);
		}
		user.setAccounName(userName);

		TUser u = TUser.dao.findByPhoneOREmail(userName);
		if (null != u) {
			result.addError("您的手机或者邮箱已经注册过");
			renderJson(result);
			return;
		}
		// 设置密码
		user.setPassword(WebUtils.pwdEncode(passWord));
		//获取Cookie中的邀请码
		boolean temp = user.save();
		if (!temp) {
			result.addError("注册失败，请稍后再试");
		}
		WebUtils.loginUser(this, user);
		renderJson(result);
	}
	/**
	 * 退出
	 */
	public void logout() {
		WebUtils.logoutUser(this);
		renderJson(result);
	}

	//-------------------------------------------------------------------------//
	// 需要登陆的ajax分界线
	//-------------------------------------------------------------------------//

	/**
	 * 更新编辑资料
	 */
	@Before({UserInterceptor.class, UpdateUserValidator.class})
	public void update_user() {
		TUser user = WebUtils.currentUser(this);
		// 头像上传
		UploadFile upFile = getFile("avator", 1024 * 1024 * 5);

		TUser u = getModel(TUser.class, "user");
		u.setId(user.getId());
		if (null != upFile) {
			String filePath = UploadFileUtils.uploadFile(upFile, FileType.FAVICON);
			u.setAvatar(filePath);
		}
		// 清除关键字段，避免注入
		u.removeNullValueAttrs();
		u.remove("accounName", "password", "balance", "steps", "role", "code");
		boolean temp = u.update();
		if (!temp) {
			result.addError("编辑资料失败");
		}
		renderJson(result);
	}
	
	@Before(UserInterceptor.class)
	public void movie(){
		Integer id = getParaToInt(0,0);
		if(id==0){
			result.addError("非法操作");
			renderJson(result);
			return;
		}
		TMovie movie = TMovie.dao.findById(id);
		movie.setDownNum(movie.getDownNum() + 1);
		movie.update();
		renderJson(result);
	}
}
