package com.bbs.manage.controller.ueditor;

import java.io.File;
import java.io.IOException;

import com.qiniu.api.auth.digest.Mac;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StringKit;
import com.jfinal.log.Logger;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rsf.ListItem;
import com.qiniu.api.rsf.ListPrefixRet;
import com.qiniu.api.rsf.RSFClient;

/**
 * 七牛云储存的帮助类
 * @author l.cm
 * @site:www.dreamlu.net
 * 2014年1月12日 下午11:07:36
 */
@SuppressWarnings("deprecation")
public class QiniuUtil {
	
	private static final Logger logger = Logger.getLogger(QiniuUtil.class);
	
	public static final String IMG_TYPE = ".jpg|.jepg|.gif|.png|.bmp";
    public static final String ALL_TYPE = ".jpg|.jepg|.gif|.png|.bmp|.gz|.rar|.zip|.pdf|.txt|.swf|.wmv";
    

    
    /**
     * 获取文件类型
     * @param @param fileName
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String getFileType(String fileName){
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }
    
    /**
     * 检查文件类型
     * @param @param fileName
     * @param @param isImg
     * @param @return    设定文件
     * @return boolean    返回类型
     * @throws
     */
    public static boolean checkFileType(String fileName, boolean isImg) {
        String fileType = getFileType(fileName);
        if (isImg) {
            return IMG_TYPE.indexOf(fileType.toLowerCase()) == -1;
        } else {
            return ALL_TYPE.indexOf(fileType.toLowerCase()) == -1;
        }
    }

    /**
     * 上传文件
     * @param file
     * @param fileName
     * @return
     * @throws JSONException 
     * @throws AuthException 
     */
	@SuppressWarnings("deprecation")
	private static String upload(String filePath, String newName) throws Exception {
		Mac mac = new Mac( Config.ACCESS_KEY, Config.SECRET_KEY );
		PutPolicy putPolicy = new PutPolicy(Config.QINIU_SPACE);
		String uptoken = putPolicy.token(mac);
		PutExtra extra = new PutExtra();
		PutRet ret = IoApi.putFile(uptoken, newName, filePath, extra);
		logger.info(ret.toString());
		String key = ret.getKey();
		if (StringKit.isBlank(key)) {
			throw new AuthException("用户信息获取失败！");
		}
		return Config.QINIU_URL + ret.getKey();
	}

	/**
	 * 上传文件，为管理图片而抽出来了
	 * @param filePath
	 * @param fileName
	 * @return
	 * @throws AuthException
	 * @throws JSONException
	 */
	public static String uploadFile(String filePath, String fileName) throws  Exception {
		String fileType = getFileType(fileName);
		String newName = System.currentTimeMillis() + fileType;
		return upload(filePath, newName);
	}
	
	/**
	 * 上传图片
	 * @param file
	 * @param fileName
	 * @return
	 * @throws JSONException 
	 * @throws AuthException 
	 */
	public static String uploadImg(String filePath, String fileName) throws Exception {
		String fileType = getFileType(fileName);
		String newName = "img" + System.currentTimeMillis() + fileType;
		return upload(filePath, newName);
	}

	/**
	 * 上传涂鸦
	 * @param content
	 * @param temPath
	 * @return
	 * @throws IOException
	 * @throws AuthException
	 * @throws JSONException
	 */
	public static Object uploadBase64(String content, String temPath) throws Exception {
		byte[] bytes = Base64.decodeBase64(content);
		for (int i = 0; i < bytes.length; ++i) {
			if (bytes[i] < 0) {
				bytes[i] += 256;
			}
		}
		String path = PathKit.getWebRootPath() + temPath + File.separator;
		String newName = "img" +  System.currentTimeMillis() + ".jpg";
		String filePath = path.concat(newName);
		FileUtils.writeByteArrayToFile(new File(filePath), bytes);
		return upload(filePath, newName);
	}

	public static String listObject(int count) {
		Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
		RSFClient client = new RSFClient(mac);
		ListPrefixRet list = client.listPrifix(Config.QINIU_SPACE, "img", "", count);
		StringBuffer sb = new StringBuffer();
		for (ListItem item : list.results) {
			sb.append("/");
			sb.append(item.key);
			sb.append("ue_separate_ue");
		}
		String imgStr = sb.toString();
		if (imgStr != "") {
			imgStr = imgStr.substring(0, imgStr.lastIndexOf("ue_separate_ue")).replace(File.separator, "/").trim();
		}
		return imgStr;
	}
}
