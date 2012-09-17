package com.fonenet.fonemarket.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.fonenet.fonemarket.download.Downloader;
import com.fonenet.fonemarket.download.LoadInfo;
import com.fonenet.fonemarket.utils.FoneConstValue;
import com.fonenet.fonemarket.utils.FoneNetUntils;

/**
 * @author zngbn
 * 
 */
public class DownloaderService extends Service {

	private final MyIBinder myIBinder = new MyIBinder();

	private Handler activityhandler;

	private Handler servicehandler;
	private static Map<String, Downloader> downloaders = new HashMap<String, Downloader>();

	/**
	 * bindService() 时，调用的是这个方法，而非 onStartCommnad() 方法
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// 在主 Activity 上的 TextView 中打印出一行LOG
		Log.i("DownloaderService", "onBind!");

		return myIBinder;
	}

	@Override
	public void onCreate() {

		servicehandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
				if (!Thread.currentThread().isInterrupted()) {

					if (msg.what == FoneConstValue.FILE_TYPE_STORE_APP) {
						String url = (String) msg.obj;
						int length = msg.arg1;
						Downloader downloader = downloaders.get(url);

						if (downloader != null) {
							LoadInfo loadInfo = downloader
									.getDownloaderInfors();
							Message msgSend = Message.obtain();
							msgSend.arg1 = loadInfo.getFileSize();
							msgSend.arg2 = loadInfo.getComplete();
							msgSend.obj = url;
							activityhandler.sendMessage(msgSend);

							if (loadInfo.getFileSize() == loadInfo
									.getComplete()) {

								// String filename = url.substring(url
								// .lastIndexOf("/") + 1);
								// String localfile =
								// FoneConstValue.CONFIG_DOWNLOADPATH
								// + filename;
								// Toast.makeText(this, "下载完成！", 0).show();
								// 下载完成后清除进度条并将map中的数据清空
								downloaders.get(url).delete(url);
								downloaders.get(url).reset();
								downloaders.remove(url);
								// FoneNetUntils.installApk(localfile,
								// Tab1Activity.this);

							}
						}
					} else if (msg.what == FoneConstValue.FILE_TYPE_STORE_CONFIG) {
						String url = (String) msg.obj;
						Downloader downloader = downloaders.get(url);
						if (downloader != null) {
							LoadInfo loadInfo = downloader
									.getDownloaderInfors();
							if (loadInfo.getFileSize() == loadInfo
									.getComplete()) {

								String filename = FoneConstValue.CONFIG_DOWNLOADPATH
										+ FoneConstValue.STORE_CONFIG_FILENAME;
								downloaders.get(url).delete(url);
								downloaders.get(url).reset();
								downloaders.remove(url);
								try {

									File zFile = new File(filename);

									FoneNetUntils.upZipFile(zFile,
											FoneConstValue.XML_FOLDER);
								} catch (ZipException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}

					}
				}
				super.handleMessage(msg);
			}
		};

		Log.i("DownloaderService", "onCreate ok!!!!");

	}

	@Override
	public void onDestroy() {

	}

	@Override
	public void onRebind(Intent intent) {

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return START_STICKY;
	}

	@Override
	public boolean onUnbind(Intent intent) {

		return super.onUnbind(intent);
	}

	/**
	 * 声明一个 Binder 类的实现类，供在 onBind() 方法中返回该类的一个实例
	 * 
	 * @author 001718
	 * 
	 */
	public class MyIBinder extends Binder {
		public Service getService() {

			return DownloaderService.this;
		}
	}

	/**
	 * service 提供的公共方法，在activity中可以调用
	 */

	public void SetActivityHandler(Handler h) {
		activityhandler = h;
	}

	public Handler GetActivityHandler() {
		Log.i("DownloaderService", "GetHandler!");
		return activityhandler;
	}

	public void downloadConfigFile() {
		String urlstr = FoneConstValue.STORE_CONFIG_URL;
		String localfile = FoneConstValue.CONFIG_DOWNLOADPATH
				+ FoneConstValue.STORE_CONFIG_FILENAME;
		int threadcount = FoneConstValue.DOWNLOAD_THREAD_COUNT;

		// 初始化一个downloader下载器
		Downloader downloader = downloaders.get(urlstr);
		if (downloader == null) {
			downloader = new Downloader(urlstr, localfile, threadcount, this,
					servicehandler, FoneConstValue.FILE_TYPE_STORE_CONFIG);
			downloaders.put(urlstr, downloader);
		}
		downloader.delete(urlstr);
		downloader.reset();

		// 得到下载信息类的个数组成集合
		downloader.getDownloaderInfors();

		// 调用方法开始下载
		downloader.download();

	}

	public void downloadApp(String urlstr) {

		int threadcount = FoneConstValue.DOWNLOAD_THREAD_COUNT;
		String filename = urlstr.substring(urlstr.lastIndexOf("/") + 1);
		String localfile = FoneConstValue.CONFIG_DOWNLOADPATH + filename;

		// 初始化一个downloader下载器
		Downloader downloader = downloaders.get(urlstr);
		if (downloader == null) {
			downloader = new Downloader(urlstr, localfile, threadcount, this,
					servicehandler, FoneConstValue.FILE_TYPE_STORE_APP);
			downloaders.put(urlstr, downloader);
		}

		if (downloader.isdownloading()) {

			downloader.pause();
			return;
		}

		// 得到下载信息类的个数组成集合
		downloader.getDownloaderInfors();

		// 调用方法开始下载
		downloader.download();
	}

	public static LoadInfo getDownloaderInfo(String urlstr) {
		Downloader downloader = downloaders.get(urlstr);
		if (downloader == null)
			return null;
		LoadInfo loadInfo = downloader.getDownloaderInfors();
		return loadInfo;
	}
}
