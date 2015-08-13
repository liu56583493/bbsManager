package com.bbs.manage.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;
import org.apache.lucene.analysis.Analyzer;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.bbs.ext.lucene.LuceneKit;
import com.bbs.ext.lucene.LucenePlugin;
import com.bbs.ext.lucene.client.IndexServiceFactory;
import com.bbs.ext.lucene.index.IndexConfig;
import com.bbs.ext.lucene.index.IndexContext;
import com.bbs.manage.bean.SearchMovie;
import com.jfinal.core.Const;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sun.xml.internal.fastinfoset.sax.Properties;

/**
 * TODO 在此加入类描述
 * @title TMovie.java
 * @description 
 * @company 北京思维夫网络科技有限公司
 * @author 卢春梦  
 * @version 1.0
 * @created 2015年06月22日 15:23:14
 */
@SuppressWarnings("serial")
public class TMovie extends Model<TMovie> {

	public static final String TABLE_NAME = "t_movie";
	public static final String PRIMARY_KEY = "id";
	
	private static IndexConfig  IndexConfig = null;
	private static IndexContext IndexContext = null;

	// "id" -> 
	// "name" -> 电影名字
	// "address" -> 下载地址
	// "content" -> 推荐内容
	// "year" -> 上映时间
	// "actors" -> 演员
	// "downNum" -> 下载次数
	// "country" -> 国家地区
	// "img" -> 详情图片地址
	// "isrecommend" -> 是否是首页推荐电影0否，1是
	// "recUrl" -> 推荐URL
	// "rating" -> 推荐指数：0~5
	// "smallUrl" -> 小图地址
	// "createTime" -> 添加时间
	// "isDeleted" -> 是否删除0：否，1是
	// "category" -> 导演
	// "type" -> 类别1:推荐,2搞笑，3恐怖，4记录，5专题，6动作
	

	public static final TMovie dao = new TMovie();

	public Integer getId(){
		return get("id");
	}
	public TMovie setId(Integer id){
		return set("id", id);
	}
	
	public Integer getType(){
		return get("type");
	}
	public TMovie setType(Integer type){
		return set("type", type);
	}

	public String getName(){
		return get("name");
	}
	public TMovie setName(String name){
		return set("name", name);
	}
	
	
	public String getCategory(){
		return get("category");
	}
	public TMovie setCategory(String category){
		return set("category", category);
	}

	public String getAddress(){
		return get("address");
	}
	public TMovie setAddress(String address){
		return set("address", address);
	}

	public String getContent(){
		return get("content");
	}
	public TMovie setContent(String content){
		return set("content", content);
	}

	public String getYear(){
		return get("year");
	}
	public TMovie setYear(String year){
		return set("year", year);
	}

	public String getActors(){
		return get("actors");
	}
	public TMovie setActors(String actors){
		return set("actors", actors);
	}

	public Integer getDownNum(){
		return get("downNum");
	}
	public TMovie setDownNum(Integer downNum){
		return set("downNum", downNum);
	}

	public String getCountry(){
		return get("country");
	}
	public TMovie setCountry(String country){
		return set("country", country);
	}

	public String getImg(){
		return get("img");
	}
	public TMovie setImg(String img){
		return set("img", img);
	}

	public Integer getIsrecommend(){
		return get("isrecommend");
	}
	public TMovie setIsrecommend(Integer isrecommend){
		return set("isrecommend", isrecommend);
	}

	public String getRecUrl(){
		return get("recUrl");
	}
	public TMovie setRecUrl(String recUrl){
		return set("recUrl", recUrl);
	}

	public Integer getRating(){
		return get("rating");
	}
	public TMovie setRating(Integer rating){
		return set("rating", rating);
	}

	public String getSmallUrl(){
		return get("smallUrl");
	}
	public TMovie setSmallUrl(String smallUrl){
		return set("smallUrl", smallUrl);
	}

	public Date getCreateTime(){
		return get("createTime");
	}
	public TMovie setCreateTime(Date createTime){
		return set("createTime", createTime);
	}

	public Integer getIsDeleted(){
		return get("isDeleted");
	}
	public TMovie setIsDeleted(Integer isDeleted){
		return set("isDeleted", isDeleted);
	}
	
	
	/**
	 * 
	 * @description  功能描述: 分页查询所有的电影
	 * @author 		        作        者: liuwei
	 * @return        返回类型: Page<TUser>
	 * @createdate   建立日期: 2015年5月24日下午3:16:34
	 */
	public Page<TMovie> findAllMvPage(Integer pageNumber, Integer pageSize, Map<String, Object> params){
		String select = "SELECT t.*,DATE_FORMAT(t.createTime,'%Y-%m-%d %h:%i') as createAtr  ";
		
		List<Object> paras = new ArrayList<Object>();

		StringBuffer sqlExceptSelect = new StringBuffer(" FROM t_movie t WHERE 1 = 1 ");
		//用户类型不为空
		if(params.containsKey("name")){
			sqlExceptSelect.append(" AND t.name LIKE  ? ");
			paras.add("%"+ params.get("name") + "%");
		}
		//演员
		if(params.containsKey("actors")){
			sqlExceptSelect.append(" AND t.actors LIKE ? ");
			paras.add("%"+ params.get("actors") + "%");
		}
		//用户的账户不为空
		if(params.containsKey("content")){
			sqlExceptSelect.append(" AND t.content LIKE ?  ");
			paras.add("%" + params.get("content") + "%");
		}
		sqlExceptSelect.append(" AND t.isDeleted=0 ORDER BY t.createTime DESC");
		return super.paginate(pageNumber, pageSize, select, sqlExceptSelect.toString(),paras.toArray());
	}
	
	
	/**
	 * 
	 * @description  功能描述: 分页查询所有的电影
	 * @author 		        作        者: liuwei
	 * @return        返回类型: Page<TUser>
	 * @createdate   建立日期: 2015年5月24日下午3:16:34
	 */
	public Page<TMovie> findIndex(Integer pageNumber,Integer pageSize,Map<String, Object> params){
		String select = "SELECT t.*,DATE_FORMAT(t.createTime,'%Y-%m-%d %h:%i') as createAtr  ";
		List<Object> paras = new ArrayList<Object>();
		StringBuffer sqlExceptSelect = new StringBuffer(" FROM t_movie t WHERE 1 = 1 ");
		//用户类型不为空
		if(params.containsKey("word")){
			sqlExceptSelect.append(" AND ( t.name LIKE  ? OR t.actors LIKE ? OR t.content LIKE ?  OR t.category LIKE ? OR t.country LIKE ?  ) ");
			paras.add("%"+ params.get("word") + "%");
			paras.add("%"+ params.get("word") + "%");
			paras.add("%" + params.get("word") + "%");
			paras.add("%" + params.get("word") + "%");
			paras.add("%" + params.get("word") + "%");
		}
		if(params.containsKey("type") && !params.get("type").toString().equals("7")){
			sqlExceptSelect.append(" AND t.type = ?  ");
			paras.add(params.get("type"));
		}
		sqlExceptSelect.append(" AND t.isDeleted=0 ");
		if(params.containsKey("type") && params.get("type").toString().equals("7")){
			sqlExceptSelect.append(" ORDER BY t.downNum DESC");
		}else{
			sqlExceptSelect.append(" ORDER BY t.createTime  DESC");
		}
		return super.paginate(pageNumber, pageSize, select, sqlExceptSelect.toString(), paras.toArray());
	}
	
	
	/**
	 * 
	 * @description  功能描述: 分页查询所有的电影
	 * @author 		        作        者: liuwei
	 * @return        返回类型: Page<TUser>
	 * @createdate   建立日期: 2015年5月24日下午3:16:34
	 */
	public List<TMovie> findRecommendIndex(Map<String, Object> params){
		String select = "SELECT t.*,DATE_FORMAT(t.createTime,'%Y-%m-%d %h:%i') as createAtr  ";
		List<Object> paras = new ArrayList<Object>();
		StringBuffer sqlExceptSelect = new StringBuffer(" FROM t_movie t WHERE 1 = 1 ");
		//用户类型不为空
		if(params.containsKey("name")){
			sqlExceptSelect.append(" AND t.name LIKE  ? ");
			paras.add("%"+ params.get("name") + "%");
		}
		//演员
		if(params.containsKey("actors")){
			sqlExceptSelect.append(" AND t.actors LIKE ? ");
			paras.add("%"+ params.get("actors") + "%");
		}
		//用户的账户不为空
		if(params.containsKey("content")){
			sqlExceptSelect.append(" AND t.content LIKE ?  ");
			paras.add("%" + params.get("content") + "%");
		}
		sqlExceptSelect.append(" AND t.isrecommend = 1 AND t.isDeleted=0 ORDER BY t.createTime  DESC limit 12 ");
		return super.find(select + sqlExceptSelect.toString(), paras.toArray());
	}
	
	/**
	 * 
	 * @description  功能描述: 分页查询所有的电影
	 * @author 		        作        者: liuwei
	 * @return        返回类型: Page<TUser>
	 * @createdate   建立日期: 2015年5月24日下午3:16:34
	 */
	public List<TMovie> findYestaday(String date){
		String select = "SELECT t.*,DATE_FORMAT(t.createTime,'%Y-%m-%d %h:%i') as createAtr  ";
		List<Object> paras = new ArrayList<Object>();
		StringBuffer sqlExceptSelect = new StringBuffer(" FROM t_movie t WHERE 1 = 1 ");
		//创建的开始时间
		sqlExceptSelect.append(" AND t.createTime >= DATE_FORMAT(?,'%Y-%c-%d %h:%i:%s') ");
		paras.add( date + " 00:00:00 " );
		//创建的结束时间
		sqlExceptSelect.append(" AND t.createTime <= DATE_FORMAT(?,'%Y-%c-%d %h:%i:%s') ");
		paras.add( date + " 23:59:59 " );
		sqlExceptSelect.append(" AND t.isDeleted=0 ORDER BY t.rating,t.downNum,t.createTime DESC limit 8 ");
		return super.find(select + sqlExceptSelect.toString(), paras.toArray());
	}
	
	/**
	 * 
	 * @description  功能描述: 分页查询所有的电影
	 * @author 		        作        者: liuwei
	 * @return        返回类型: Page<TUser>
	 * @createdate   建立日期: 2015年5月24日下午3:16:34
	 */
	public List<TMovie> findDownMax(){
		String select = "SELECT t.*,DATE_FORMAT(t.createTime,'%Y-%m-%d %h:%i') as createAtr  ";
		List<Object> paras = new ArrayList<Object>();
		StringBuffer sqlExceptSelect = new StringBuffer(" FROM t_movie t WHERE 1 = 1 ");
		sqlExceptSelect.append(" AND t.isDeleted=0 ORDER BY t.downNum DESC limit 8 ");
		return super.find(select + sqlExceptSelect.toString(), paras.toArray());
	}
	
	/**
	 * 
	 * @description  功能描述: 分页查询所有的电影
	 * @author 		        作        者: liuwei
	 * @return        返回类型: Page<TUser>
	 * @createdate   建立日期: 2015年5月24日下午3:16:34
	 */
	public List<TMovie> findRatingMax(){
		String select = "SELECT t.*,DATE_FORMAT(t.createTime,'%Y-%m-%d %h:%i') as createAtr  ";
		List<Object> paras = new ArrayList<Object>();
		StringBuffer sqlExceptSelect = new StringBuffer(" FROM t_movie t WHERE 1 = 1 ");
		sqlExceptSelect.append(" AND t.isrecommend = 1 AND t.isDeleted=0 ORDER BY t.createTime,t.rating DESC limit 3 ");
		return super.find(select + sqlExceptSelect.toString(), paras.toArray());
	}
	
	
	/**
	 * 
	 * @description  功能描述: 搜索
	 * @author 		        作        者: liuwei
	 * @return        返回类型: Page<TUser>
	 * @createdate   建立日期: 2015年5月24日下午3:16:34
	 */
	public List<TMovie> search(Map<String, Object> params){
		String select = "SELECT t.*,DATE_FORMAT(t.createTime,'%Y-%m-%d %h:%i') as createAtr  ";
		List<Object> paras = new ArrayList<Object>();
		StringBuffer sqlExceptSelect = new StringBuffer(" FROM t_movie t WHERE 1 = 1 ");
		//用户类型不为空
		if(params.containsKey("word")){
			sqlExceptSelect.append(" AND ( t.name LIKE  ? OR t.actors LIKE ? OR t.content LIKE ?  OR t.category LIKE ? OR t.country LIKE ?  ) ");
			paras.add("%"+ params.get("word") + "%");
			paras.add("%"+ params.get("word") + "%");
			paras.add("%" + params.get("word") + "%");
			paras.add("%" + params.get("word") + "%");
			paras.add("%" + params.get("word") + "%");
		}
		sqlExceptSelect.append(" AND t.isDeleted=0 ");
		sqlExceptSelect.append(" ORDER BY t.createTime  DESC limit 40 ");
		return super.find(select + sqlExceptSelect.toString(), paras.toArray());
	}
	
	/**
	 * 
	 * @description 查询所有索引的  
	 * @author 		 liuwei
	 * @createdate  2015年7月4日下午8:47:21
	 */
	public List<TMovie> querySearch(){
		String sql = "select t.id,CONCAT_WS(',',t.name,t.actors,t.country,t.category,t.content) as mname,t.name,t.actors,t.country,t.category,t.content,t.downNum,t.recUrl from t_movie t where t.isDeleted = 0 ORDER BY t.createTime desc ";
		return this.find(sql);
	}
	
	/**
	 * @description 初始化索引单个搜索  
	 * @author 		 liuwei
	 * @createdate  2015年7月4日下午8:47:07
	 */
	public SearchMovie newMovieSearchIndex(TMovie movie) {
		if(movie == null){
			return null;
		}
		int id = movie.getId();	//商品ID
		try{
			SearchMovie search = new SearchMovie();
			search.setMovieid(movie.getId());
//			search.setMname(movie.get("mname"));
			search.setName(movie.getName());
			search.setCategory(movie.getCategory());
			search.setContent(movie.getContent());
			search.setCountry(movie.getCountry());
			search.setRecUrl(movie.getRecUrl());
			search.setActors(movie.getActors());
			search.setDownNum(movie.getDownNum());
			return search;
		}catch (Exception e) {
			System.out.println("电影 [ "+id+" ] 缺属性,对象创建失败...");
			return null;
		}
	}
	
	/**
	 * 
	 * @description 增加电影的索引文件  
	 * @author 		 liuwei
	 * @createdate  2015年7月4日下午8:46:49
	 */
	public boolean createMovies(List<TMovie> movies) {
		IndexContext content = LucenePlugin.INIT();
		for (TMovie movie : movies) {
			String pname = movie.get("name") + "";	//商品名称
			try {
				SearchMovie search = newMovieSearchIndex(movie);
				if(search!=null){
					Thread.sleep(800);
					content.add(com.bbs.ext.lucene.client.DocumentDataFormatter.createDocumentfromBean(search , content.getIndexConfig()));
				}else{
					System.out.println("电影 ".concat("[ ").concat(pname).concat(" ]").concat(" 缺属性,生成索引失败..."));
					continue;
				}
			} catch (Exception e) {
				System.out.println("电影  ".concat("[ ").concat(pname).concat(" ]").concat(" 缺属性,生成索引失败..."));
				continue;
			}
		}
		try {	
			Thread.sleep(800);
			content.optimize(true);
		} catch (Exception e) {
			return false;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @description 移除索引 
	 * @author 		 liuwei
	 * @createdate  2015年7月4日下午8:24:12
	 */
	public boolean removeMovies(List<TMovie> movies) {
		IndexContext content = LucenePlugin.INIT();
		for (TMovie movie : movies) {
			String name = movie.get("name") + "";	//商品名称
			try {
				SearchMovie search = newMovieSearchIndex(movie);
				if(search!=null){
					content.delete(com.bbs.ext.lucene.client.DocumentDataFormatter.createDocumentfromBean(search , content.getIndexConfig()));
				}else{
					continue;
				}
			} catch (Exception e) {
				System.out.println("电影【"+name+"】,删除索引失败");
				continue;
			}
		}
		try {	
			content.optimize(true);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
}