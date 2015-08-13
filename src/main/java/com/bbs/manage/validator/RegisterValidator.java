package com.bbs.manage.validator;

import com.jfinal.core.Controller;
import com.bbs.base.ShortCircuitValidator;
import com.bbs.utils.RegexUtils;

/**
 * 注册校验
 */
public class RegisterValidator extends ShortCircuitValidator {

	@Override
	protected void validate(Controller c) {
		validateRequired("userName", "message", "请输入手机号或邮箱地址");
		validateRegex("userName", RegexUtils.EMAIL_OR_PHONE, "message", "请检查您的手机号或邮箱地址");

		validateRequired("passWord", "message", "请输入您的密码");
		validateRegex("passWord", RegexUtils.USER_PASSWORD, "message", "请输入您的密码");

		validateRequired("randomCode", "message", "请输入验证码");
		validateJCaptcha(c, "randomCode", "message", "请输入正确的验证码");
	}

	@Override
	protected void handleError(Controller c) {
		c.setAttr("code", 1);
		c.renderJson();
	}

}
