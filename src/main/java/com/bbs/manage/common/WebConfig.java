package com.bbs.manage.common;

import org.apache.commons.lang3.SystemUtils;
import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.bbs.ext.cron.Cron4jPlugin;
import com.bbs.ext.handler.StaticHandler;
import com.bbs.ext.lucene.LucenePlugin;
import com.bbs.manage.controller.admin.AdminAjaxController;
import com.bbs.manage.controller.admin.AdminController;
import com.bbs.manage.controller.front.AjaxController;
import com.bbs.manage.controller.front.IndexController;
import com.bbs.manage.controller.ueditor.UeditorController;
import com.bbs.manage.model.TMovie;
import com.bbs.manage.model.TUser;
import com.bbs.manage.plugin.log.Slf4jLogFactory;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
/**
 * 
 * @author 		  liuwei
 * @createdate   2015年7月5日下午3:01:44
 * @projectname  bbsManage
 * @packageclass com.bbs.manage.common.WebConfig.java
 */
public class WebConfig extends JFinalConfig {

	// 开发模式
	private boolean devMode = false;

	@Override
	public void configConstant(Constants me) {
		// 判断服务器类型，加载不同的配置文件
		if (SystemUtils.IS_OS_LINUX) {
			loadPropertyFile("config.txt");
		} else {
			loadPropertyFile("config-test.txt");
		}

		// 设置Slf4日志
		me.setLoggerFactory(new Slf4jLogFactory());

		devMode = getPropertyToBoolean("devMode", false);
		me.setDevMode(!devMode);
		// beetl模版配置工厂
		BeetlRenderFactory beetlRenderFactory = new BeetlRenderFactory();
		me.setMainRenderFactory(beetlRenderFactory);

		// 401,403,404,500错误代码处理
		me.setError401View("/commons/404.html");
		me.setError403View("/commons/404.html");
		me.setError404View("/commons/404.html");
		me.setError500View("/commons/404.html");
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/", IndexController.class, "front");
		me.add("/ajax", AjaxController.class);
		// 后台部分
		me.add("/admin", AdminController.class);
		me.add("/admin/ajax", AdminAjaxController.class);
		
		//图片文件上传
		me.add("/ueditor",UeditorController.class);

	}

	@Override
	public void configInterceptor(Interceptors me) {
		
	}

	@Override
	public void configHandler(Handlers me) {
		// 静态文件目录
		me.add(new StaticHandler("/static", "/uploadFile"));
		// Druid监控
		me.add(new DruidStatViewHandler("/admin/druid"));
	}

	@Override
	public void configPlugin(Plugins me) {
		// 数据库信息
		String jdbcUrl  = getProperty("db.jdbcUrl");
		String user     = getProperty("db.user");
		String password = getProperty("db.password");

		// 配置Druid数据库连接池插件
		DruidPlugin dp = new DruidPlugin(jdbcUrl, user, password);
		dp.addFilter(new StatFilter()).addFilter(new Slf4jLogFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");
		dp.addFilter(wall);
		me.add(dp);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		arp.addMapping(TUser.TABLE_NAME, TUser.PRIMARY_KEY, TUser.class);
		arp.addMapping(TMovie.TABLE_NAME, TMovie.PRIMARY_KEY, TMovie.class);

		arp.setShowSql(devMode);
		me.add(arp);

		// ehCache插件
		me.add(new EhCachePlugin());
		
		
		// 搜索索引名
		String searchIndexName 		= getProperty("search.indexName");
		// 搜索主键
		String searchKeyFieldName 	= getProperty("search.keyFieldName");
		// 搜索文件目录
		String searchKeyRootDir 	= getProperty("search.rootDir");
		me.add(new LucenePlugin(searchIndexName, searchKeyFieldName, searchKeyRootDir));
		// 定时器
		me.add(new Cron4jPlugin());
		
//		// event插件
//		EventPlugin plugin = new EventPlugin();
//		// 设置为异步，设置扫描的包，默认全扫描
//		plugin.asyn().scanPackage("com.bbs.manage.event");
//		me.add(plugin);
	}

}