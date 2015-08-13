package com.bbs.manage.cron;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.bbs.ext.lucene.LuceneKit;
import com.bbs.manage.bean.SearchMovie;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class TestJob implements Runnable {

	static int callTime = 0;

	@Override
	public void run() {
		String sql = "select t.id as movieId,t.name,t.actors,t.category,t.content,t.downNum from t_movie t where t.isDeleted = 0 ORDER BY t.createTime desc ";
		List<Record> allMovie = Db.find(sql);
		try {
			for (Record record : allMovie) {
				SearchMovie movie = SearchMovie.recordToBean(record);
				LuceneKit.delete(movie);
				LuceneKit.add(movie);
				Thread.sleep(100);
			}
			LuceneKit.optimize(true);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		callTime++;
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " JobA works,callTime is: " + callTime);
	}
}
