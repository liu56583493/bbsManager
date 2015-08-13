package com.bbs.manage.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.json.JSONObject;

import com.bbs.ext.lucene.annotation.FieldIndex;
import com.bbs.ext.lucene.annotation.FieldStore;
import com.bbs.ext.lucene.annotation.PKey;
import com.jfinal.plugin.activerecord.Record;

/**
 * 搜索所用商品实体
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * @date 2014年11月6日 上午11:34:55
 */
public class SearchMovie implements Serializable {

	private static final long serialVersionUID = 413812439483306249L;

	@PKey
	private int movieid;		//电影Id
	
	@FieldStore
	@FieldIndex("ANALYZED")
	private String mname;		//电影名称 
	
	@FieldStore
	@FieldIndex("ANALYZED")
	private String name;		//电影名称

	@FieldIndex("ANALYZED")
	private String content;		//电影内容
	
	@FieldIndex("ANALYZED")
	private String actors;		//演员

	@FieldStore
	@FieldIndex("ANALYZED")
	private String category;	//导演
	
	@FieldStore
	private Integer downNum;	//下载此时
	
	@FieldStore
	private String recUrl;		//推荐图片
	
	@FieldStore
	@FieldIndex("ANALYZED")
	private String country;		//推荐图片
	

	/**
	 * 将jfinal的结果集转换成搜索实体
	 * @param record
	 * @return
	 */
	public static SearchMovie recordToBean(Record record) {
		Map<String, Object> columns = record.getColumns();
		SearchMovie movie = new SearchMovie();
		try {
			// 获取类属性
			BeanInfo beanInfo = Introspector.getBeanInfo(SearchMovie.class);
			// 给 product 属性赋值
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : propertyDescriptors) {
				String key = descriptor.getName();
				if (columns.containsKey(key)) {
					Object value = columns.get(key);
					if (null == value) continue;
					descriptor.getWriteMethod().invoke( movie, value + "" );
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return movie;
	}



	public int getMovieid() {
		return movieid;
	}



	public void setMovieid(int movieid) {
		this.movieid = movieid;
	}



	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getActors() {
		return actors;
	}


	public void setActors(String actors) {
		this.actors = actors;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public Integer getDownNum() {
		return downNum;
	}


	public void setDownNum(Integer downNum) {
		this.downNum = downNum;
	}

	public String getRecUrl() {
		return recUrl;
	}


	public void setRecUrl(String recUrl) {
		this.recUrl = recUrl;
	}

	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}

	public String getMname() {
		return mname;
	}


	public void setMname(String mname) {
		this.mname = mname;
	}
	
	

}