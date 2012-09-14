package com.fonenet.fonemarket.activity;

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
			break;
		}
	}
};