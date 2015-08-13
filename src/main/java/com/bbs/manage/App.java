package com.bbs.manage;

import com.jfinal.core.JFinal;

/**
 * 
 * @description  启动
 * @author 		  liuwei
 * @createdate   2015年6月30日下午7:16:09
 * @projectname  bbsManage
 * @packageclass com.bbs.manage.App.java
 */
public class App {

	// 项目启动
	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 8080, "/", 10);
	}
}
