package com.bbs.manage.controller.ueditor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.jfinal.kit.PathKit;

public class LocalUploader implements IUeditorUploader {
	
//	long ueditor_imageMaxSize = 2048000;
//	String ueditor_imageAllowFiles = null;// ".png", ".jpg", ".jpeg", ".gif", ".bmp"

	public LocalUploader() {
		//读取配置文件
//		PropKit.get("ueditor_imageMaxSize");
//		PropKit.get("ueditor_imageAllowFiles");
	}
	
	@Override
	public String uploadFile(File file) {
		
		String basePath = createBasePath(getSuffix(file.getName()));//
		
		File targetFile = new File(PathKit.getWebRootPath()+basePath);
		
		if(!targetFile.getParentFile().exists()){
			targetFile.getParentFile().mkdirs();
		}
		
		try {
			copyFile(file, targetFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return basePath ;
	}
	
	
	private static String getSuffix( String filename ) {
		return filename.substring( filename.lastIndexOf( "." ) ).toLowerCase();
	}

	
	@Override
	public void listFile(MultiState state) {
		
		File dir = new File(PathKit.getWebRootPath()+File.separator+"uploads");
		File files[] = dir.listFiles();
		
		int fileCount = 0;
		
		if(files!=null ){
			
			for(File f: files){
				if(f.isDirectory()){
					File childFiles[] = f.listFiles();
					if(childFiles!=null && childFiles.length > 0){
						for(File cf: childFiles){
							
							State s = new BaseState(true);
							s.putInfo("url", "/uploads/"+f.getName()+File.separator+cf.getName());
							state.addState(s);
							
							fileCount+=1;
							
						}
					}
				}
			}
		}
		
		state.putInfo("total", fileCount);
	}


	@Override
	public String uploadFile(InputStream inStream,String fileSuffix) {
		
		String basePath = createBasePath(fileSuffix);
		
		File targetFile = new File(PathKit.getWebRootPath()+basePath);
		if(!targetFile.getParentFile().exists()){
			targetFile.getParentFile().mkdirs();
		}
		
		try {
			copyFile(inStream, targetFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return basePath;
	}

	
	
	private String createBasePath(String fileSuffix) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date());
		String basePath = File.separator+"uploads/"+date+"/"+UUID.randomUUID().toString().replaceAll("-", "")+fileSuffix;
		return basePath;
	}
	
	
	
	private static void copyFile(File sourceFile, File targetFile) throws IOException {
        copyFile(new FileInputStream(sourceFile), targetFile);
    }
	
	

	private static void copyFile(InputStream is, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(is);

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }


}
