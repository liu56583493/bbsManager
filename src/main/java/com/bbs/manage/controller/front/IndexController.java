package com.bbs.manage.controller.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.bbs.base.BaseController;
import com.bbs.ext.lucene.LuceneKit;
import com.bbs.ext.lucene.LucenePlugin;
import com.bbs.ext.lucene.client.QueryResults;
import com.bbs.ext.lucene.index.IndexConfig;
import com.bbs.ext.lucene.index.IndexContext;
import com.bbs.ext.lucene.search.PagedResultSet;
import com.bbs.ext.render.JCaptchaRender;
import com.bbs.manage.bean.SearchMovie;
import com.bbs.manage.common.WebUtils;
import com.bbs.manage.model.TMovie;
import com.bbs.manage.model.TUser;
import com.bbs.utils.StringUtils;

/**
 * 
 * @description  首页控制器
 * @author 		  liuwei
 * @createdate   2015年6月30日上午11:47:53
 * @projectname  bbsManage
 * @packageclass com.bbs.manage.controller.front.IndexController.java
 */
public class IndexController extends BaseController {

	/**
	 * 默认跳转到登陆页
	 */
	public void index() {
		//查询前台推荐的
		List<TMovie> recList = TMovie.dao.findRecommendIndex(new HashMap<String, Object>());
		setAttr("recList", recList);
	}
	
	/**
	 * 默认跳转到登陆页
	 */
	public void recommend() {
		Integer type = getParaToInt(0,1);
		setAttr("flag", type +1 );
		
		Integer pageNum = getParaToInt(1, 1);
		setAttr("pageNum", pageNum);
		setAttr("type", type);
		
		Integer pageSize = getParaToInt("pageSize", 6);
		Map<String, Object> params = new HashMap<String, Object>();
		
		String word = getPara("word");
		if(!StrKit.isBlank(word)){
			params.put("word", word);
		}
		params.put("type", type);
		//查询前台推荐的
		Page<TMovie> page = TMovie.dao.findIndex(pageNum, pageSize,params);
		setAttr("page", page);
		
	}
	
	
	/**
	 * 默认跳转到登陆页
	 */
	public void movie() {
		setAttr("flag", 10);
		Integer id = getParaToInt(0,0);
		if(null == id || id == 0){
			render("/commons/404.html");
			return;
		}
		TMovie movie = TMovie.dao.findById(id);
		setAttr("movie", movie);
		
		List<TMovie> ratMaxList = TMovie.dao.findRatingMax();
		setAttr("ratMaxList", ratMaxList);
		// 2.判定用户是否已经登陆，已经登陆的不再跳转到登录页
		TUser user = WebUtils.currentUser(this);
		setAttr("user", user);
	}
	
	/**
	 * 默认跳转到登陆页
	 */
	public void about() {
		setAttr("flag", 3);
	}


	/**
	 * 登录页
	 */
	public void land() {
		// 1.用户注册，跳回
		String baseFrom = WebUtils.concatBasePath("/");
		String from = getPara("from", baseFrom);
		// 2.判定用户是否已经登陆，已经登陆的不再跳转到登录页
		TUser user = WebUtils.currentUser(this);
		if (null != user) {
			redirect(from);
			return;
		}
		setAttr("from", from);
	}

	/**
	 * 注册页
	 */
	public void regiter() {
		// 1.用户注册，跳回
		String baseFrom = WebUtils.concatBasePath("/");
		String from = getPara("from", baseFrom);
		// 2.判定用户是否已经登陆，已经登陆的不再跳转到登录页
		TUser user = WebUtils.currentUser(this);
		if (null != user) {
			redirect(from);
			return;
		}
		setAttr("from", "/");
	}

	/**
	 * 验证码
	 */
	public void img_code() {
		render(new JCaptchaRender());
	}
	
	
	
	/**
	 * 
	 * @description 搜索暂时不用SQL有时间改了--第二版本   
	 * @author 		 liuwei
	 * @param	      
	 * @createdate  2015年6月30日下午8:21:47
	 */
	public void search() {
		setAttr("flag", 9);
		Map<String, Object> params = new HashMap<String, Object>();
		String word = getPara("word");
		if(!StrKit.isBlank(word)){
			params.put("word", word);
		}
		setAttr("params", params);
//		//查询前台推荐的
		List<TMovie> searList = new ArrayList<TMovie>();
//		setAttr("searList", searList);
		StringBuilder queryBuilder = new StringBuilder();
		// 查询关键字
		if(!StrKit.isBlank(word)){
			queryBuilder.append("mname:'" + word + "'");
			IndexContext content = LucenePlugin.INIT();
			Query query = IKQueryParser.parse(queryBuilder.toString());
			Sort querySort = new Sort(new SortField("movieid", SortField.INT , true));
			PagedResultSet resultSet = content.search(query, 1, 60, querySort,false);
			if(null != resultSet && null != resultSet.getResults() && resultSet.getResults().size() > 0){
				for(Map<String, String> m : resultSet.getResults()){
					TMovie movie = new TMovie();
					movie.setActors(m.get("actors"));
					movie.setName(m.get("name"));
					movie.setId(Integer.valueOf(m.get("movieid")));
					movie.setCountry(m.get("country"));
					movie.setCategory(m.get("category"));
					movie.setDownNum(Integer.valueOf(m.get("downNum")));
					movie.setRecUrl(m.get("recUrl"));
					searList.add(movie);
				}
			}
		}else{
			searList  = TMovie.dao.search(params);
		}
		setAttr("searList", searList);
	}
	
}
