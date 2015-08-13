package com.bbs.manage.utils;

import java.net.ServerSocket;

/**
 * 随机生成一个可用的端口号的
 * @title PortRandomUtils.java
 * @description 
 * @company 北京思维夫网络科技有限公司
 * @author 卢春梦
 * @version 1.0
 * @created 2015年5月21日上午10:49:05
 */
public class PortUtils {

	/**
	 * 递归生成一个指定区间的端口
	 * 
	 * 存在递归死循环风险导致：java.lang.StackOverflowError
	 * 
	 * 请尽量使用大区间
	 * 
	 * @return 返回可用的端口
	 */
	public static synchronized int random(int begin, int end) {
		boolean temp = tryPort(begin);
		if (temp) {
			return begin;
		}
		return random(begin + 1, end);
	}

	/**
	 * 尝试端口时候被占用
	 * @param port
	 * @return 没有被占用：true,被占用：false
	 */
	private static boolean tryPort(int port) {
		try (ServerSocket ss = new ServerSocket(port)){
			return true;
		} catch (Exception e) {
			return false;
		}  
	}

}
