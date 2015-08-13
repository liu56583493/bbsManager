package com.bbs.manage.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;

import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.sun.xml.internal.fastinfoset.sax.Properties;
import com.bbs.base.BaseController;
import com.bbs.manage.interceptor.AdminInterceptor;
import com.bbs.manage.model.TMovie;
import com.bbs.manage.model.TUser;
import com.bbs.utils.FileOperate;

/**
 * 
 * @description  后台管理平台
 * @author 		  liuwei
 * @createdate   2015年6月30日上午11:48:41
 * @projectname  bbsManage
 * @packageclass com.bbs.manage.controller.admin.AdminController.java
 */
public class AdminController extends BaseController {

	/**
	 * 首页
	 */
	@Before(AdminInterceptor.class)
	public void index() {
	
	}

	/**
	 * 登录页
	 */
	public void land() {
		
	}
	
	
	/**
	 * 
	 * @description  功能描述: 用户列表
	 * @author 		        作        者: liuwei
	 * @return        返回类型: void
	 * @createdate   建立日期: 2015年5月24日下午3:13:42
	 */
	@Before(AdminInterceptor.class)
	public void user_list() {
		//分页参数
		Integer pageNumber = getParaToInt("pageNum", 1);
		Integer pageSize = getParaToInt("pageSize", 15);
		setAttr("pageNumber", pageNumber);
		setAttr("pageSize", pageSize);
		//设置从前台获取的参数Map
		Map<String, Object> params = new HashMap<String, Object>();
		if(getParaToInt("role",3) != 3){
			params.put("role", getParaToInt("role",3));
		}
		//用户账户
		if(StrKit.notBlank(getPara("accounName"))){
			params.put("accounName", getPara("accounName"));
		}
		setAttr("params", params);
		//查询所有的用户列表
		Page <TUser> userPage = TUser.dao.findAllUserPage(pageNumber, pageSize, params);
		setAttr("userPage", userPage);
	}
	
	
	/**
	 * 
	 * @description  功能描述: 影片列表
	 * @author 		        作        者: liuwei
	 * @return        返回类型: void
	 * @createdate   建立日期: 2015年5月24日下午3:13:42
	 */
	@Before(AdminInterceptor.class)
	public void mv_list() {
		//分页参数
		Integer pageNumber = getParaToInt("pageNum", 1);
		Integer pageSize = getParaToInt("pageSize", 15);
		setAttr("pageNumber", pageNumber);
		setAttr("pageSize", pageSize);
		//设置从前台获取的参数Map
		Map<String, Object> params = new HashMap<String, Object>();
		String name = getPara("name");
		if(!StrKit.isBlank(name)){
			params.put("name", name);
		}
		//演员
		String actors = getPara("actors");
		if(!StrKit.isBlank(actors)){
			params.put("actors", actors);
		}
		String content = getPara("content");
		if(!StrKit.isBlank(content)){
			params.put("content", content);
		}
		//用户账户
		if(StrKit.notBlank(getPara("accounName"))){
			params.put("accounName", getPara("accounName"));
		}
		setAttr("params", params);
		//查询所有的用户列表
		Page <TMovie> page = TMovie.dao.findAllMvPage(pageNumber, pageSize, params);
		setAttr("page", page);
	}
	
	
	/**
	 * 
	 * @description  功能描述: 跳转至新增或者添加用户页面
	 * @author 		        作        者: liuwei
	 * @return        返回类型: void
	 * @createdate   建立日期: 2015年5月25日上午10:41:31
	 */
	@Before(AdminInterceptor.class)
	public void add_update_user(){
		Long userId = getParaToLong("id", 0l);
		//如果用户ID不为空，则是编辑用户
		if(userId != 0l){
			//查询用户
			TUser user = TUser.dao.findById(userId);
			setAttr("user", user);
		}
		
	}
	
	
	/**
	 * 
	 * @description  功能描述: 跳转至新增或者添加用户电影
	 * @author 		        作        者: liuwei
	 * @return        返回类型: void
	 * @createdate   建立日期: 2015年5月25日上午10:41:31
	 */
	@Before(AdminInterceptor.class)
	public void add_update_mv(){
		Long movieId = getParaToLong("id", 0l);
		//如果用户ID不为空，则是编辑用户
		if(movieId != 0l){
			//查询用户
			TMovie movie = TMovie.dao.findById(movieId);
			setAttr("movie", movie);
		}
		
	}
	
	public void search(){
		java.util.Properties props = new java.util.Properties();
		if (SystemUtils.IS_OS_LINUX) {
			props = PropKit.use("config.txt").getProperties();
		} else {
			props = PropKit.use("config-test.txt").getProperties();
		}
		String luceneRootDir = props.getProperty("search.rootDir");
		FileOperate.delAllFile(luceneRootDir);
		TMovie.dao.createMovies(TMovie.dao.querySearch());
		renderJson("1");
	}
	
	public void help() {
		
	}

	public void gallery() {
		
	}

	public void log() {
		
	}

	public void error() {
		
	}

	public void table() {
		
	}

	public void form() {
		
	}
	
	
}
