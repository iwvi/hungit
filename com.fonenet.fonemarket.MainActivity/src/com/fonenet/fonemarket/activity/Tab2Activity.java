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
		// ��ȡ��������ݣ����ݵĸ�ʽ���ϸ��Ҫ��Ŷ
		ArrayList<HashMap<String, Object>> data = getData();
		// ģ��SimpleAdapterʵ�ֵ��Լ���adapter
		MyAdapter adapter = new MyAdapter(this,null, data);

		/**
		 * ��Щ�˺��Ժ������Ƕ�֪��vlist2.xml�൱�ڴ洢һ�����ݵ�������֣�������ǰ�ߵĴ����У�������һ���������ļ�main.xml�ģ�
		 * ��������ļ��Ƿ����������ļ�����ʾ�ģ�һ������ж���ͨ��setContentView()��ָ���������ļ��ġ�Ϊ�����������û���õ�
		 * ������listView������һ�������������ء� �����ǿ���setListAdapter��ListActivity�е�ʵ�֣� public
		 * void setListAdapter(ListAdapter adapter) { synchronized (this) {
		 * ensureList(); mAdapter = adapter; mList.setAdapter(adapter); } }
		 * ���������һ��ensureList��������������������������� private void ensureList() { if (mList
		 * != null) { return; }
		 * setContentView(com.android.internal.R.layout.list_content);
		 * 
		 * } ���ڿ����ˣ������и� setContentView�������������������ǵ������һ��android�Լ��ṩ�Ľ�������ʾ��
		 * ԭ�������ǵ����ۻ������õģ�ֻ����ListActivity���ҽ���������ʵ�֡�
		 */
		setListAdapter(adapter);

	}

	private ArrayList<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 3; i++) {
			HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
			tempHashMap.put("image", R.drawable.ic_launcher);
			tempHashMap.put("title", "����" + i);
			tempHashMap.put("info", "��������Ϣ");
			// tempHashMap.put("title", "2222");
			// tempHashMap.put("info", "��������Ϣ");
			arrayList.add(tempHashMap);
		}
		return arrayList;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Log.i("�����Ϣ", v.toString());
	}

}