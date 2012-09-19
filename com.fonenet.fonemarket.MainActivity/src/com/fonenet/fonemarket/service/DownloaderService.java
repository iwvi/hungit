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

	private static Handler servicehandler;
	private static Map<String, Downloader> downloaders = new HashMap<String, Downloader>();

	/**
	 * bindService() ʱ�����õ���������������� onStartCommnad() ����
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// ���� Activity �ϵ� TextView �д�ӡ��һ��LOG
		Log.i("DownloaderService", "onBind!");

		return myIBinder;
	}

	@Override
	public void onCreate() {

		super.onCreate();
		servicehandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {// ����һ��Handler�����ڴ��������߳���UI��ͨѶ
				if (!Thread.currentThread().isInterrupted()) {
					if (msg.what == FoneConstValue.MESSAGE_HANDLE_WAHT_DOWNLOADSTART) {
						String urlstr = (String) msg.obj;
						Message msgSend = Message.obtain();
						LoadInfo loadInfo = downloadApp(urlstr);

						if (loadInfo == null) {
							loadInfo = new LoadInfo(0, 0, urlstr);
							// loadInfo.setUrlstring(urlstr);
							msgSend.arg1 = FoneConstValue.MESSAGE_HANDLE_ARG_DOWNLOADISPAUSE;

						} else {
							msgSend.arg1 = FoneConstValue.MESSAGE_HANDLE_ARG_ISDOWNLOADING;
						}

						msgSend.what = FoneConstValue.MESSAGE_HANDLE_WAHT_DOWNLOADSTARTCNF;
						msgSend.obj = loadInfo;
						activityhandler.sendMessage(msgSend);

					} else if (msg.what == FoneConstValue.MESSAGE_HANDLE_WAHT_DOWNLOADCONTINUE) {

						if (msg.arg2 == FoneConstValue.FILE_TYPE_STORE_APP) {
							String url = (String) msg.obj;
							Downloader downloader = downloaders.get(url);

							if (downloader != null) {
								LoadInfo loadInfo = downloader
										.getDownloaderInfors();
								Message msgSend = Message.obtain();

								if (loadInfo.getFileSize() <= loadInfo
										.getComplete()) {

									// String filename = url.substring(url
									// .lastIndexOf("/") + 1);
									// String localfile =
									// FoneConstValue.CONFIG_DOWNLOADPATH
									// + filename;
									// Toast.makeText(this, "������ɣ�", 0).show();
									// ������ɺ��������������map�е��������
									downloaders.get(url).delete(url);
									downloaders.get(url).reset();
									downloaders.remove(url);
									Log.i("DownloaderService",
											"download app OK!!!");
									// FoneNetUntils.installApk(localfile,
									// Tab1Activity.this);

								}
								Log.i("downloadhandler",
										"complete " + loadInfo.getComplete());
								msgSend.what = FoneConstValue.MESSAGE_HANDLE_WAHT_DOWNLOADCONTINUE;
								msgSend.arg1 = loadInfo.getComplete();
								msgSend.arg2 = FoneConstValue.FILE_TYPE_STORE_APP;
								msgSend.obj = url;
								activityhandler.sendMessage(msgSend);
							}
						} else if (msg.arg2 == FoneConstValue.FILE_TYPE_STORE_CONFIG) {
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
				}
				super.handleMessage(msg);
			}
		};

		Log.i("DownloaderService", "onCreate ok!!!!");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onRebind(Intent intent) {
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

	@Override
	public boolean onUnbind(Intent intent) {

		return super.onUnbind(intent);
	}

	/**
	 * ����һ�� Binder ���ʵ���࣬���� onBind() �����з��ظ����һ��ʵ��
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
	 * service �ṩ�Ĺ�����������activity�п��Ե���
	 */

	public void SetActivityHandler(Handler h) {
		activityhandler = h;
	}

	public void downloadConfigFile() {
		String urlstr = FoneConstValue.STORE_CONFIG_URL;
		String localfile = FoneConstValue.CONFIG_DOWNLOADPATH
				+ FoneConstValue.STORE_CONFIG_FILENAME;
		int threadcount = FoneConstValue.DOWNLOAD_THREAD_COUNT;

		// ��ʼ��һ��downloader������
		Downloader downloader = downloaders.get(urlstr);
		if (downloader == null) {
			downloader = new Downloader(urlstr, localfile, threadcount, this,
					servicehandler, FoneConstValue.FILE_TYPE_STORE_CONFIG);
			downloaders.put(urlstr, downloader);
		}
		downloader.delete(urlstr);
		downloader.reset();

		// �õ�������Ϣ��ĸ�����ɼ���
		downloader.getDownloaderInfors();

		// ���÷�����ʼ����
		downloader.download();

	}

	public LoadInfo downloadApp(String urlstr) {

		int threadcount = FoneConstValue.DOWNLOAD_THREAD_COUNT;
		String filename = urlstr.substring(urlstr.lastIndexOf("/") + 1);
		String localfile = FoneConstValue.CONFIG_DOWNLOADPATH + filename;

		// ��ʼ��һ��downloader������
		Downloader downloader = downloaders.get(urlstr);
		if (downloader == null) {
			downloader = new Downloader(urlstr, localfile, threadcount, this,
					servicehandler, FoneConstValue.FILE_TYPE_STORE_APP);
			downloaders.put(urlstr, downloader);
		}

		if (downloader.isdownloading()) {

			downloader.pause();
			return null;
		}

		// �õ�������Ϣ��ĸ�����ɼ���
		LoadInfo loadInfo = downloader.getDownloaderInfors();

		// ���÷�����ʼ����
		downloader.download();
		return loadInfo;
	}

	public static LoadInfo getDownloaderInfo(String urlstr) {
		Downloader downloader = downloaders.get(urlstr);
		if (downloader == null)
			return null;
		LoadInfo loadInfo = downloader.getDownloaderInfors();
		return loadInfo;
	}

	public static boolean isDownloading(String urlstr) {
		Downloader downloader = downloaders.get(urlstr);
		if (downloader == null)
			return false;
		return downloader.isdownloading();
	}

	public static void downloadPause(String urlstr) {
		Downloader downloader = downloaders.get(urlstr);
		if (downloader == null)
			return;
		downloader.pause();
	}

	public static Handler GetServiceHandler() {
		Log.i("DownloaderService", "GetServiceHandler!");
		// return null;
		return servicehandler;
	}
}
