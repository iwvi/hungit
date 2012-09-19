package com.fonenet.fonemarket.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipException;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.fonenet.fonemarket.xmltools.Page.Item;
import com.fonenet.fonemarket.service.DownloaderService;
import com.fonenet.fonemarket.service.DownloaderService.MyIBinder;

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
	private boolean isBound;
	private Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
	// �������������Ӧ�Ľ�����
	private Map<String, ProgressBar> ProgressBars = new HashMap<String, ProgressBar>();
	private Map<String, Button> Buttons = new HashMap<String, Button>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {// ����һ��Handler�����ڴ��������߳���UI��ͨѶ
				if (!Thread.currentThread().isInterrupted()) {

					Intent i = new Intent(StoreListActivity.this,
							DownloaderService.class);
					Log.i("xiao xi", "ok!!!");
					doBindService(i);
				}
				super.handleMessage(msg);
			}
		};
		{
			Message msg = Message.obtain();
			msg.what = 1;
			handler.sendMessage(msg);
			Log.i("onCreate", "mesage have sent!");
		}
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

	public void onStop() {
		super.onStop();
		doUnbindService();
		finish();
		Log.i("Onstop", "doUnbindService!");
	}
	private void doUnbindService() {
		if (isBound) {
			unbindService(myLocalServiceConnection);
			isBound = false;
		}
	}

	private void doBindService(Intent i) {
		Log.i("bind", "begin to bind");

		bindService(i, myLocalServiceConnection, Context.BIND_AUTO_CREATE);

	}

	private ServiceConnection myLocalServiceConnection = new ServiceConnection() {

		private DownloaderService bsi;

		public void onServiceConnected(android.content.ComponentName name,
				android.os.IBinder service) {

			// ��Ϊ �ͻ��� �� ���� ��ͬһ�������ڣ�����һ�����Ϳ���֪������ "service"�������ˣ�Ҳ�Ϳ��Խ�����ʾ��ǿ������ת���ˡ�
			// ����� �ͻ����������ͬһ�������еĻ�����ô�˴��ǲ����Խ�����ʾǿ������ת���ģ�
			// ��Ϊ��ͨ��Debug�����Է��ִ�ʱ�������� Service �������� BinderProxy
			MyIBinder myIBinder = (MyIBinder) service;
			bsi = (DownloaderService) myIBinder.getService();
			isBound = true;
			Log.i("onbinder", "binded!!");
			bsi.SetActivityHandler(handler);

			bsi.downloadConfigFile();
		};

		public void onServiceDisconnected(android.content.ComponentName name) {

			isBound = false;
		};
	};	
	
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
	 * @description ׼��һЩ��������
	 * @return һ��������������Ϣ��hashMap����
	 */
	private ArrayList<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		if (parser != null && parser.getPages() != null) {
			Page page = parser.getPages().get(0);
			int num = parser.getPages().get(0).getItemNum();
			for (int i = 0; i < num; i++) {
				HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
				Item item = page.getItems().get(i);
				Bitmap bm = BitmapFactory.decodeFile(FoneConstValue.ICON_FOLDER+item.getIconName());
				if(bm!=null){
					tempHashMap.put("image", bm);
				}
				else {
					tempHashMap.put("image", R.drawable.ic_launcher);
				}
				String title = item.getName();
				tempHashMap.put("title", title);
				tempHashMap.put("url", "http://192.168.7.66/Market4.apk");
				String info = item.getIntro();
				tempHashMap.put("info", info);
				arrayList.add(tempHashMap);
			}
		} else {
			for (int i = 0; i < 3; i++) {
				HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
				tempHashMap.put("image", R.drawable.ic_launcher);
				tempHashMap.put("title", "����" + i);
				tempHashMap.put("info", "��������Ϣ");
				tempHashMap.put("url", "http://192.168.7.66/Market4.apk");
				// tempHashMap.put("title", "2222");
				// tempHashMap.put("info", "��������Ϣ");
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

		// ��ʼ��һ��downloader������
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
		// �õ�������Ϣ��ĸ�����ɼ���
		LoadInfo loadInfo = downloader.getDownloaderInfors();
		// ��ʾ������
		showProgress(loadInfo, urlstr, v);
		// ���÷�����ʼ����
		downloader.download();
	}

	/**
	 * ��ʾ������
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