package com.bbs.manage;

import com.bbs.utils.EmailUtils.MailSender;

public class EmailTest {

	/**
	 * main测试
	 * @return void
	 */
	public static void main(String[] args) {
		MailSender.New()
			.to("15321971211@139.com")
			.content("hello,这是测试邮件")
			.subject("测试")
			.send();
	}
}
