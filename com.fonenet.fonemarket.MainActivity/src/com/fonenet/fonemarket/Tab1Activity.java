package com.fonenet.fonemarket;

/*
 import android.os.Bundle;
 import android.app.Activity;
 import android.view.Menu;

 public class Tab1Activity extends Activity {

 @Override
 public void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_tab1);
 }

 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
 getMenuInflater().inflate(R.menu.activity_tab1, menu);
 return true;
 }
 }
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipException;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.fonenet.fonemarket.FoneNetXmlParser.Page;
import com.fonenet.fonemarket.download.Downloader;
import com.fonenet.fonemarket.download.LoadInfo;

public class Tab1Activity extends ListActivity {

	private FoneNetXmlParser parser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// download file

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
				if (!Thread.currentThread().isInterrupted()) {

					if (msg.what == FoneConstValue.FILE_TYPE_STORE_APP) {
						String url = (String) msg.obj;
						int length = msg.arg1;
						ProgressBar bar = ProgressBars.get(url);
						if (bar != null) {
							// 设置进度条按读取的length长度更新
							bar.incrementProgressBy(length);
							if (bar.getProgress() == bar.getMax()) {

								String filename = url.substring(url
										.lastIndexOf("/") + 1);
								String localfile = FoneConstValue.CONFIG_DOWNLOADPATH
										+ filename;
								// Toast.makeText(this, "下载完成！", 0).show();
								// 下载完成后清除进度条并将map中的数据清空
								LinearLayout layout = (LinearLayout) bar
										.getParent();
								layout.removeView(bar);
								ProgressBars.remove(url);
								downloaders.get(url).delete(url);
								downloaders.get(url).reset();
								downloaders.remove(url);
								Buttons.get(url).setText("xia zai");
								Buttons.remove(url);
								FoneNetUntils.installApk(localfile,
										Tab1Activity.this);

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

		downloadConfigFile(); // zb add download store config xml
		parser = new FoneNetXmlParser(this, FoneConstValue.XML_FOLDER
				+ "store-recommend.xml");
		// 获取虚拟的数据，数据的格式有严格的要求哦
		ArrayList<HashMap<String, Object>> data = getData();
		// 模仿SimpleAdapter实现的自己的adapter
		MyAdapter adapter = new MyAdapter(this, data);

		/**
		 * 有些人很迷糊，我们都知道vlist2.xml相当于存储一行数据的组件布局，我们在前边的代码中，都是有一个主布局文件main.xml的，
		 * 组件布局文件是放在主布局文件上显示的，一般代码中都是通过setContentView()来指定主布局文件的。为何这里根本就没有用到
		 * ，但是listView还能有一个界面来呈现呢。 让我们看看setListAdapter在ListActivity中的实现， public
		 * void setListAdapter(ListAdapter adapter) { synchronized (this) {
		 * ensureList(); mAdapter = adapter; mList.setAdapter(adapter); } }
		 * 里面调用了一个ensureList方法，我们再来看看这个方法： private void ensureList() { if (mList
		 * != null) { return; }
		 * setContentView(com.android.internal.R.layout.list_content);
		 * 
		 * } 现在看到了，这里有个 setContentView方法，里面设置了我们的组件在一个android自己提供的界面上显示。
		 * 原来，我们的理论还是适用的，只不过ListActivity给我进行了隐藏实现。
		 */
		setListAdapter(adapter);

	}

	/**
	 * @author chenzheng_java
	 * @description 准备一些测试数据
	 * @return 一个包含了数据信息的hashMap集合
	 */
	private ArrayList<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		if(parser != null && parser.getPages() != null){
			
			Page page = parser.getPages().get(0);
			int num = parser.getPages().get(0).getItemNum();
			for (int i = 0; i < num; i++) {
				HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
				tempHashMap.put("image", R.drawable.ic_launcher);
				String title = page.items.get(i).name;
				tempHashMap.put("title", title);
				tempHashMap.put("url", "http://192.168.7.66/Market4.apk");
				String info = page.items.get(i).intro;
				tempHashMap.put("info", info);
				arrayList.add(tempHashMap);
			}
		} else {
			for (int i = 0; i < 3; i++) {
				HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
				tempHashMap.put("image", R.drawable.ic_launcher);
				tempHashMap.put("title", "标题" + i);
				tempHashMap.put("info", "描述性信息");
				tempHashMap.put("url", "http://192.168.7.66/Market4.apk");

				arrayList.add(tempHashMap);
			}
		}
		return arrayList;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Log.e("输出信息", "clickitem");
	}

	public void buttonOnClick(View v) {
		RelativeLayout layout = (RelativeLayout) (v.getParent().getParent());
		ZuJian zuJian = (ZuJian) (layout.getTag());
		int threadcount = FoneConstValue.DOWNLOAD_THREAD_COUNT;
		String urlstr = zuJian.url;
		String filename = urlstr.substring(urlstr.lastIndexOf("/") + 1);
		String localfile = FoneConstValue.CONFIG_DOWNLOADPATH + filename;

		// 初始化一个downloader下载器
		Downloader downloader = downloaders.get(urlstr);
		if (downloader == null) {
			downloader = new Downloader(urlstr, localfile, threadcount, this,
					handler, FoneConstValue.FILE_TYPE_STORE_APP);
			downloaders.put(urlstr, downloader);
		}
		Button button = Buttons.get(urlstr);
		if (button == null) {
			button = (Button) v;
			Buttons.put(urlstr, button);
		}
		if (downloader.isdownloading()) {
			button.setText("xia zai");
			downloader.pause();
			return;
		}

		button.setText("zan ting");
		// 得到下载信息类的个数组成集合
		LoadInfo loadInfo = downloader.getDownloaderInfors();
		// 显示进度条
		showProgress(loadInfo, urlstr, v);
		// 调用方法开始下载
		downloader.download();
	}

	private void downloadConfigFile() {
		String urlstr = FoneConstValue.STORE_CONFIG_URL;
		String localfile = FoneConstValue.CONFIG_DOWNLOADPATH
				+ FoneConstValue.STORE_CONFIG_FILENAME;
		int threadcount = FoneConstValue.DOWNLOAD_THREAD_COUNT;

		// 初始化一个downloader下载器
		Downloader downloader = downloaders.get(urlstr);
		if (downloader == null) {
			downloader = new Downloader(urlstr, localfile, threadcount, this,
					handler, FoneConstValue.FILE_TYPE_STORE_CONFIG);
			downloaders.put(urlstr, downloader);
		}
		downloader.delete(urlstr);
		downloader.reset();

		// 得到下载信息类的个数组成集合
		downloader.getDownloaderInfors();

		// 调用方法开始下载
		downloader.download();

	}

	/**
	 * 显示进度条
	 */
	private void showProgress(LoadInfo loadInfo, String url, View v) {
		ProgressBar bar = ProgressBars.get(url);
		if (bar == null) {
			bar = new ProgressBar(this, null,
					android.R.attr.progressBarStyleHorizontal);
			bar.setMax(loadInfo.getFileSize());
			bar.setProgress(loadInfo.getComplete());
			System.out.println(loadInfo.getFileSize() + "--"
					+ loadInfo.getComplete());
			ProgressBars.put(url, bar);

			LinearLayout.LayoutParams params = new LayoutParams(
					LayoutParams.MATCH_PARENT, 5);

			((LinearLayout) (v.getParent())).addView(bar, params);

		}
	}

	private Handler handler;
	private Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
	// 存放与下载器对应的进度条
	private Map<String, ProgressBar> ProgressBars = new HashMap<String, ProgressBar>();
	private Map<String, Button> Buttons = new HashMap<String, Button>();
}