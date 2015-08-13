package com.bbs.manage.controller.ueditor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.bbs.base.BaseController;
import com.jfinal.upload.UploadFile;


public class UeditorController extends BaseController {

	
	private IUeditorUploader uploader = new LocalUploader();

	public void index() {
		String actionType = getPara("action");
		State state = null;
		int actionCode = ActionMap.getType(actionType);
		switch (actionCode) {
		case ActionMap.CONFIG:
			renderConfig();
			return;
		case ActionMap.UPLOAD_IMAGE:
		case ActionMap.UPLOAD_SCRAWL:
		case ActionMap.UPLOAD_VIDEO:
		case ActionMap.UPLOAD_FILE:
			state = doUpload();
			break;
		case ActionMap.CATCH_IMAGE:
			String[] urls = getParaValues("source[]");
			state = doCatchImage(urls);
			break;
		case ActionMap.LIST_IMAGE:                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
		case ActionMap.LIST_FILE:
			state = doListImage();
			break;
		}
		renderText(state.toJSONString());
	}

	
	
	private void renderConfig() {
		InputStream is = UeditorController.class.getClassLoader().getResourceAsStream("ueditorConfig.json");
		try {
			String string = inputStream2String(is);
			is.close();
			String text = string.replace("{website}", "movie.dreamlu.net");
			renderText(text);
			return;
		} catch (IOException e) {
			logger.error("ueditorController render config error", e);
		}
		
		renderText(new BaseState(false, AppInfo.CONFIG_ERROR).toJSONString());
	}
	
	
	
	private static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}


	private State doUpload() {

		UploadFile uploadFile = getFile("upfile", 100 * 1024 * 1024 );

		String fileName = uploadFile.getFileName();
		String suffix = getSuffix(fileName);

		String url = uploader.uploadFile(uploadFile.getFile());
		State state = new BaseState(true, "SUCCESS");
		
		String path = this.getRequest().getServletContext().getRealPath("/");
		String urlPath = path + url;
		try {
			url = QiniuUtil.uploadFile(urlPath, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		state.putInfo("url",url.replace("\\", "/"));
		state.putInfo("original", fileName);
		state.putInfo("title", fileName);
		state.putInfo("size", "" + uploadFile.getFile().length());
		state.putInfo("type", suffix);
		uploadFile.getFile().delete();
		//删除本地的文件
		File file = new File(urlPath);
		if(file.exists()){
			file.delete();
		}
		return state;
	}
	
	private static String getSuffix( String filename ) {
		return filename.substring( filename.lastIndexOf( "." ) ).toLowerCase();
	}
	
	
	public State doCatchImage(String[] urls){
		MultiState state = new MultiState( true );
		
		if(urls!=null && urls.length > 0){
			for ( String source : urls ) {
				state.addState( captureRemoteData( source ) );
			}
		}
		
		return state;
	}
	

	private State captureRemoteData(String urlStr) {
		
		HttpURLConnection connection = null;
		URL url = null;
		State state = null;
		try {
			url = new URL( urlStr );

			
			connection = (HttpURLConnection) url.openConnection();
		
			connection.setInstanceFollowRedirects( true );
			connection.setUseCaches( true );
			
			String suffix = MIMEType.getSuffix( connection.getContentType() );
			InputStream is = connection.getInputStream();
			
			String serverUrl = uploader.uploadFile(is,suffix);
			
			if(serverUrl!=null)
			{
				state = new BaseState(true);
				state.putInfo("url", serverUrl);
				state.putInfo( "source", urlStr );
				state.putInfo("type", suffix);
					
			}else
			{
				state = new BaseState(false);
			}
			
		} catch ( Exception e ) {
			logger.error("ueditor controller doCatchImage", e);
			return new BaseState( false, AppInfo.REMOTE_FAIL );
		}
		
		return state;
	}
	
	

	private State doListImage() {
		int start = getParaToInt("start");
		MultiState state = new MultiState(true);
		state.putInfo("start", start);
		uploader.listFile(state);
		return state;
	}

}