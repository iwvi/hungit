package com.fonenet.fonemarket.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;

import com.fonenet.fonemarket.R;
import com.fonenet.fonemarket.adapter.MyAdapter;
import com.fonenet.fonemarket.download.Downloader;
import com.fonenet.fonemarket.download.LoadInfo;
import com.fonenet.fonemarket.utils.FoneConstValue;
import com.fonenet.fonemarket.utils.FoneNetUntils;
import com.fonenet.fonemarket.xmltools.FoneNetXmlParser;
import com.fonenet.fonemarket.xmltools.Page;

public class StoreListActivity extends ListActivity {

	public final static int LIST_TYPE_RECOMMAND = 0;
	public final static int LIST_TYPE_CATEGORY = 1;
	public final static int LIST_TYPE_RANK = 2;
	public final static int LIST_TYPE_MANAGE = 3;
	public final static int LIST_TYPE_MORE = 4;

	private Integer listType = LIST_TYPE_RECOMMAND;
	private ListView lv;
	private FoneNetXmlParser parser;

	private Handler handler;
	private Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
	// 存放与下载器对应的进度条
	private Map<String, ProgressBar> ProgressBars = new HashMap<String, ProgressBar>();
	private Map<String, Button> Buttons = new HashMap<String, Button>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
										StoreListActivity.this);

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
	}

	public void onStart() {
		super.onStart();
		Intent intent = getIntent();
		// Uri uri;
		// uri = intent.getData();

		listType = intent.getIntExtra("type", LIST_TYPE_RECOMMAND);
		updateListView();
	}

	public void onRestart() {
		super.onRestart();
	}

	public void updateListView() {
		updateListView(listType);
	}

	protected void updateListView(int type) {
		switch (type) {
		case LIST_TYPE_CATEGORY:
			break;
		case LIST_TYPE_RANK:
			break;
		case LIST_TYPE_MANAGE:
			break;
		case LIST_TYPE_MORE:
			break;
		case LIST_TYPE_RECOMMAND:
		default:
			parser = new FoneNetXmlParser(this, FoneConstValue.XML_FOLDER
					+ "store-recommend.xml");
			parser.readXML(FoneConstValue.XML_FOLDER + "store-recommend.xml");
			ArrayList<HashMap<String, Object>> data = getData();
			MyAdapter adapter = new MyAdapter(this, data);
			setListAdapter(adapter);
			break;
		}
	}

	/**
	 * @author chenzheng_java
	 * @description 准备一些测试数据
	 * @return 一个包含了数据信息的hashMap集合
	 */
	private ArrayList<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		if (parser != null && parser.getPages() != null) {
			Page page = parser.getPages().get(0);
			int num = parser.getPages().get(0).getItemNum();
			for (int i = 0; i < num; i++) {
				HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
				tempHashMap.put("image", R.drawable.ic_launcher);
				String title = page.getItems().get(i).getName();
				tempHashMap.put("title", title);
				tempHashMap.put("url", "http://192.168.7.66/Market4.apk");
				String info = page.getItems().get(i).getIntro();
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
				// tempHashMap.put("title", "2222");
				// tempHashMap.put("info", "描述性信息");
				arrayList.add(tempHashMap);
			}
		}
		return arrayList;
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

};