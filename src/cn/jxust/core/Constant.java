package cn.jxust.core;

import java.util.Date;

import com.jfinal.ext.kit.DateKit;

public class Constant
{
	public static final String UPLOAD_IMAGE_DIR = "upload/image/";// 图片文件上传目录
	public static final String UPLOAD_MEDIA_DIR = "upload/media/";// 媒体文件上传目录
	public static final String UPLOAD_FILE_DIR = "upload/file/";// 其它文件上传目录

	public static final int pageSize = 20;
	
	public static final String WEB_NAME = "题海泛舟在线考试系统";
	
	public static final String COPYRIGHT = "Copyright &copy; 2013-" + DateKit.toStr(new Date()).substring(0, 4) + " 江西理工大学 陈光送  Inc. All Rights Reserved.";
	
	public static String VERSION = "0.0";
}
