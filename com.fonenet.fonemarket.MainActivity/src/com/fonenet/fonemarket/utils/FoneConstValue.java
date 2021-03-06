package com.fonenet.fonemarket.utils;

import java.io.File;

import android.os.Environment;

public class FoneConstValue {
	public static String CONFIG_DOWNLOADPATH;
	public static String STORE_CONFIG_FILENAME;
	public static String STORE_CONFIG_URL;
	public static final int FILE_TYPE_STORE_CONFIG = 1;
	public static final int FILE_TYPE_STORE_APP = 2;
	public static final int FILE_TYPE_NODOWNLOAD = 3;
	public static final int FILE_DOWNLOAD_ERR = 1;
	public static final int FILE_DOWNLOAD_SUCCSS = 2;

	public static final int MESSAGE_HANDLE_WAHT_DOWNLOADSTART = 1;
	public static final int MESSAGE_HANDLE_WAHT_DOWNLOADCONTINUE = 2;
	public static final int MESSAGE_HANDLE_WAHT_DOWNLOADBINDSERVICE = 3;
	public static final int MESSAGE_HANDLE_WAHT_DOWNLOADSTARTCNF = 5;
	public static final int MESSAGE_HANDLE_ARG_ISDOWNLOADING = 1;
	public static final int MESSAGE_HANDLE_ARG_DOWNLOADISPAUSE = 2;
	public static final String XML_FOLDER = Environment.getExternalStorageDirectory().getPath()+"/FoneNetXml/";
	public static final String ICON_FOLDER = Environment.getExternalStorageDirectory().getPath()+"/FoneNetIcon/";

	public static final int DOWNLOAD_THREAD_COUNT = 1;

	static {
		STORE_CONFIG_FILENAME = "store_config.zip";
		CONFIG_DOWNLOADPATH = Environment.getExternalStorageDirectory()
				.getPath() + File.separator + "test" + File.separator;
		STORE_CONFIG_URL = "http://192.168.7.76:8080/glxt/interface/usstore.jsp?projectid=76&type=3&date=0&version=1&imsi=9001010123456789";
	}
	// CONFIG_DOWNLOADPATH = Environment.
	// CONFIG_DOWNLOADPATH = Environment.getExternalStorageDirectory().getPath()
	// + File.separator + "test" + File.separator;
}
