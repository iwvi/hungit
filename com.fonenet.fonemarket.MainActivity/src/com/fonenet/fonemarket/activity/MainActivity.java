package com.fonenet.fonemarket.activity;

import com.fonenet.fonemarket.R;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
	ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TabHost tabHost= (TabHost)getTabHost();
		tabHost.addTab(tabHost.newTabSpec("Home").setIndicator("HomePage").setContent(new Intent(this, TabHomepageActivity.class))); 
		tabHost.addTab(tabHost.newTabSpec("Rank").setIndicator("Rank").setContent(new Intent(this, TabHomepageActivity.class))); 
		tabHost.addTab(tabHost.newTabSpec("Category").setIndicator("Category").setContent(new Intent(this, TabHomepageActivity.class))); 
		tabHost.addTab(tabHost.newTabSpec("Search").setIndicator("Search").setContent(new Intent(this, TabHomepageActivity.class))); 
		tabHost.addTab(tabHost.newTabSpec("Manage").setIndicator("Manage").setContent(new Intent(this, TabHomepageActivity.class)));
	}
}
