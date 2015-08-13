package com.bbs.manage.validator;

import com.jfinal.core.Controller;
import com.bbs.base.ShortCircuitValidator;
import com.bbs.utils.RegexUtils;

public class RestPassWordValidator extends ShortCircuitValidator {

	@Override
	protected void validate(Controller c) {
		validateRequired("oldPassWord", "message", "请输入旧密码");
		validateRequired("newPassWord", "message", "请输入新密码");
		validateRequired("rePassWord", "message", "两次输入的新密码不一致");

		validateRegex("oldPassWord", RegexUtils.USER_PASSWORD, "message", "旧密码格式错误");
		validateRegex("newPassWord", RegexUtils.USER_PASSWORD, "message", "新密码格式错误");

		validateEqualField("newPassWord", "rePassWord", "message", "两次输入的新密码不一致");
	}

	@Override
	protected void handleError(Controller c) {
		c.setAttr("code", 1);
		c.renderJson();
	}

}
