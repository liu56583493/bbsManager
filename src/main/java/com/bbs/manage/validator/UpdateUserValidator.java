package com.bbs.manage.validator;

import com.jfinal.core.Controller;
import com.bbs.base.BaseController;
import com.bbs.base.ShortCircuitValidator;
import com.bbs.utils.RegexUtils;

public class UpdateUserValidator extends ShortCircuitValidator {

	@Override
	protected void validate(Controller c) {
		((BaseController)c).getFile("avator", 1024 * 1024 * 5);

		validateRequired("user.phone", "message", "请输入您的手机号");
		validateRequired("user.email", "message", "请输入您的邮箱地址");

		validateRegex("user.phone", RegexUtils.PHONE, "message", "请检查您的手机号");
		validateRegex("user.email", RegexUtils.EMAIL, "message", "请检查您的邮箱地址");
	}

	@Override
	protected void handleError(Controller c) {
		c.setAttr("code", 1);
		c.renderJson();
	}

}
