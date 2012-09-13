package com.fonenet.fonemarket.activity;

/*
 import android.os.Bundle;
 import android.app.Activity;
 import android.view.Menu;

 public class Tab2Activity extends Activity {

 @Override
 public void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_tab2);
 }

 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
 getMenuInflater().inflate(R.menu.activity_tab2, menu);
 return true;
 }
 }
 */

import java.util.ArrayList;
import java.util.HashMap;

import com.fonenet.fonemarket.R;
import com.fonenet.fonemarket.adapter.MyAdapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class Tab2Activity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取虚拟的数据，数据的格式有严格的要求哦
		ArrayList<HashMap<String, Object>> data = getData();
		// 模仿SimpleAdapter实现的自己的adapter
		MyAdapter adapter = new MyAdapter(this,null, data);

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

	private ArrayList<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 3; i++) {
			HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
			tempHashMap.put("image", R.drawable.ic_launcher);
			tempHashMap.put("title", "标题" + i);
			tempHashMap.put("info", "描述性信息");
			// tempHashMap.put("title", "2222");
			// tempHashMap.put("info", "描述性信息");
			arrayList.add(tempHashMap);
		}
		return arrayList;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Log.i("输出信息", v.toString());
	}

}