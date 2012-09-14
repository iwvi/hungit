package com.fonenet.fonemarket.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.fonenet.fonemarket.R;
import com.fonenet.fonemarket.adapter.MyAdapter;
import com.fonenet.fonemarket.utils.FoneConstValue;
import com.fonenet.fonemarket.xmltools.FoneNetXmlParser;
import com.fonenet.fonemarket.xmltools.Page;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;

public class StoreListActivity extends ListActivity {

	public final static int LIST_TYPE_RECOMMAND = 0;
	public final static int LIST_TYPE_CATEGORY = 1;
	public final static int LIST_TYPE_RANK = 2;
	public final static int LIST_TYPE_MANAGE = 3;
	public final static int LIST_TYPE_MORE = 4;

	private Integer listType = LIST_TYPE_RECOMMAND;
	private ListView lv;
	private FoneNetXmlParser parser ;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			parser = new FoneNetXmlParser(this, FoneConstValue.XML_FOLDER+"store-recommend.xml");
			parser.readXML(FoneConstValue.XML_FOLDER+"store-recommend.xml");
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
		if(parser != null && parser.getPages() != null){
			Page page = parser.getPages().get(0);
			int num = parser.getPages().get(0).getItemNum();
			for(int i=0;i<num;i++){
				HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
				tempHashMap.put("image", R.drawable.ic_launcher);
				String title = page.getItems().get(i).getName();
				tempHashMap.put("title", title);
				tempHashMap.put("url", "http://192.168.7.66/Market4.apk");
				String info = page.getItems().get(i).getIntro();
				tempHashMap.put("info", info);
				arrayList.add(tempHashMap);
			}
		}
		else {
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
};