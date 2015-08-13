package com.bbs.manage.controller.ueditor;



import java.util.HashMap;
import java.util.Map;

public class MIMEType {

	public static final Map<String, String> types = new HashMap<String, String>();
	static{
		types.put( "image/gif", ".gif" );
		types.put( "image/jpeg", ".jpg" );
		types.put( "image/jpg", ".jpg" );
		types.put( "image/png", ".png" );
		types.put( "image/bmp", ".bmp" );
		types.put( "audio/x-wav", ".wav" );
		types.put( "audio/mpeg", ".mp3" );
		types.put( "application/octet-stream", ".mp4" );
		types.put( "video/mp4", ".mp4" );
		types.put( "video/x-msvideo", ".avi" );
	};
	
	public static String getSuffix ( String mime ) {
		return MIMEType.types.get( mime );
	}
	
}
