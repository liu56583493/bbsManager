package com.bbs.manage.controller.ueditor;

import java.io.File;
import java.io.InputStream;

public interface IUeditorUploader {
	
	public String uploadFile(File file);
	public String uploadFile(InputStream inStream,String fileSuffix);
	public void  listFile(MultiState state);

}
