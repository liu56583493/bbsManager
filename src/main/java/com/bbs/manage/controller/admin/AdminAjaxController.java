package com.bbs.manage.controller.admin;

import java.util.Date;
import java.util.List;

import com.bbs.base.BaseController;
import com.bbs.manage.common.WebUtils;
import com.bbs.manage.model.TMovie;
import com.bbs.manage.model.TUser;
import com.bbs.manage.validator.UpdateUserValidator;
import com.bbs.utils.RegexUtils;
import com.bbs.utils.UploadFileUtils;
import com.bbs.utils.UploadFileUtils.FileType;
import com.bbs.vo.AjaxResult;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;

/**
 * 
 * @description  管理平台ajax控制器 
 * @author 		  liuwei
 * @createdate   2015年6月30日上午11:48:19
 * @projectname  bbsManage
 * @packageclass com.bbs.manage.controller.admin.AdminAjaxController.java
 */
public class AdminAjaxController extends BaseController{

	private AjaxResult result = new AjaxResult();

	public void index() {
		result.addError("404，您访问的内容不存在~");
		renderJson(result);
	}

	/**
	 * 后台用户登陆
	 */
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
		int role = user.getRole();
		if (role != TUser.IS_ADMIN) {
			result.addError("您没有访问权限");
			renderJson(result);
			return;
		}
		// 是否记住密码
		String remember = getPara("remember", "0");
		// 登陆状态维持
		WebUtils.loginAdmin(this, user, "1".equals(remember));
		renderJson(result);
	}

	/**
	 * 退出
	 */
	public void logout() {
		WebUtils.logoutAdmin(this);
		renderJson(result);
	}

	
	
	//删除用户
	public void del_user(){
		String userIds = getPara("userIds");
		if(StrKit.isBlank(userIds)){
			result.addError("非法操作~");
			renderJson(result);
			return;
		}
		List<TUser> userList = TUser.dao.findUsersByUserIds(userIds);
		if(null == userList){
			result.addError("非法操作~");
			renderJson(result);
			return;
		}
		for(TUser user : userList ){
			user.setIsDeleted(TUser.IS_DELETE_YES);
			user.update();
		}
		renderJson(result);
	}
	
	/**
	 * 
	 * @description  功能描述: 后台添加或者修改用户
	 * @author 		        作        者: liuwei
	 * @return        返回类型: void
	 * @createdate   建立日期: 2015年5月25日下午1:26:52
	 */
	@Before(UpdateUserValidator.class)
	public void add_update_user() {
		
		// 头像上传
		UploadFile upFile = getFile("avatar", 1024 * 1024 * 5);
		
		//param 为 type 1:用户的账户，2：邮箱,3:手机号码
		String param = "";
		//flag 标志为增加用户，还是修改用户时候的校验
		Integer flag = 0;
		TUser u = getModel(TUser.class, "user");
		if(u.getId() != null){
			flag = 1;
		}
		u.setPassword(WebUtils.pwdEncode(u.getPassword()));
		//判断账户的信息是否存在
		param = u.getAccounName();
		List<TUser> userList = TUser.dao.findUserList(param, TUser.IS_ACCOUNNAME);
		if((flag == 0 && (null != userList && userList.size() > 0)) || (flag == 1 && (null != userList && userList.size() > 1))){
			result.addError("账号信息已存在！");
			renderJson(result);
			return;
		}
		param = u.getEmail();
		userList = TUser.dao.findUserList(param, TUser.IS_EMAIL);
		if((flag == 0 && (null != userList && userList.size() > 0)) || (flag == 1 && (null != userList && userList.size() > 1))){
			result.addError("邮箱信息已存在！");
			renderJson(result);
			return;
		}
		param = u.getPhone();
		userList = TUser.dao.findUserList(param, TUser.IS_PHONE);
		if((flag == 0 && (null != userList && userList.size() > 0)) || (flag == 1 && (null != userList && userList.size() > 1))){
			result.addError("手机号码已存在！");
			renderJson(result);
			return;
		}
		if (null != upFile) {
			String filePath = UploadFileUtils.uploadFile(upFile, FileType.FAVICON);
			u.setAvatar(filePath);
		}
		// 清除关键字段，避免注入
		u.removeNullValueAttrs();
		boolean temp = false;
		if(u.getId() == null){
			u.setIsDeleted(TUser.IS_DELETE_NO);
			u.setCreateAt(new Date());
			temp = u.save();
		}else{
			u.setId(u.getId());
			temp = u.update();
		}
		if (!temp) {
			result.addError("添加或者修改用户失败！");
		}
		renderJson(result);
	}
	
	
	
	
	/**
	 * 
	 * @description  功能描述: 后台添加或者修改电影信息
	 * @author 		        作        者: liuwei
	 * @return        返回类型: void
	 * @createdate   建立日期: 2015年5月25日下午1:26:52
	 */
	public void add_update_mv() {
		TMovie m = getModel(TMovie.class, "movie");
		boolean temp = false;
		if(m.getId() == null){
			m.setIsDeleted(0);
			m.setCreateTime(new Date());
			temp = m.save();
		}else{
			m.setId(m.getId());
			temp = m.update();
		}
		if (!temp) {
			result.addError("添加或者修改电影信息错误！");
		}
		renderJson(result);
	}
	
}
