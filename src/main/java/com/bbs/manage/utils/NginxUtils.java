package com.bbs.manage.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;
import com.bbs.utils.EmailUtils.MailSender;
import com.bbs.utils.StringUtils;

/**
 * Nginx 插件，用于nginx重启，配置文件添加或者更新
 * @title NginxUtils.java
 * @description 
 * @company 北京思维夫网络科技有限公司
 * @author 卢春梦
 * @version 1.0
 * @created 2015年5月22日下午2:01:55
 */
public final class NginxUtils {

	protected static final Logger logger = Logger.getLogger(NginxUtils.class);

	private NginxUtils() {}

	/**
	 * nginx域名配置
	 */
	public synchronized static void conf(String domain, int port) {
		String nginxPath = PropKit.get("nginx.path");

		// 1. 生成配置文件
		String confDir = PropKit.get("nginx.conf-dir");

		// 构造server_name
		String temp = PropKit.get("nginx.server_name.temp");
		String server_name = StringUtils.format(temp, domain);
		// 新生成配置文件
		String configFile = resetPath(confDir, server_name.concat(".conf"));

		Map<String, String> paras = new HashMap<String, String>();
		// 参数完善
		paras.put("port", String.valueOf(port));
		paras.put("domain", domain);
		paras.put("server_name", server_name);

		// 写入nginx配置
		try {
			String conf = StringUtils.render("/WEB-INF/config/nginx.temp", paras);
			FileUtils.writeStringToFile(new File(configFile), conf, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		String sbin = resetPath(nginxPath, "/sbin/nginx");
		// 2. 重启nginx
		boolean result = restart(sbin);
		if (!result) {
			// send error message to some one~
			MailSender.New()
				.to("15321971211@139.com")
				.content("nginx reload error, please check it! <br>" + configFile)
				.subject("Nginx reload error")
				.send();
			return;
		}
	}

	/**
	 * 路径优化
	 * @param dir
	 * @param fileName
	 * @return path
	 */
	private static String resetPath(String dir, String fileName) {
		if (fileName.startsWith("/")) {
			fileName = fileName.substring(1);
		}
		if (dir.endsWith("/")) {
			return dir.concat(fileName);
		} else {
			return dir.concat("/").concat(fileName);
		}
	}

	/*
	  -?,-h         : this help
	  -v            : show version and exit
	  -V            : show version and configure options then exit
	  -t            : test configuration and exit
	  -q            : suppress non-error messages during configuration testing
	  -s signal     : send signal to a master process: stop, quit, reopen, reload
	  -p prefix     : set prefix path (default: /usr/local/nginx/)
	  -c filename   : set configuration file (default: conf/nginx.conf)
	  -g directives : set global directives out of configuration file
	*/
	private static boolean restart(String sbin) {
		String bash = sbin.concat(" -s reload");
		// 调用进程执行命令
		try {
			Runtime.getRuntime().exec(bash);
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

}
