package com.fonenet.fonemarket.activity;

import java.util.HashMap;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.fonenet.fonemarket.R;
import com.fonenet.fonemarket.service.DownloaderService;
import com.fonenet.fonemarket.service.DownloaderService.MyIBinder;

public class MainActivity extends /* FragmentActivity */TabActivity {

	private boolean isBound;
	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Get tabHost Object
		tabHost = (TabHost) this.findViewById(android.R.id.tabhost);
		// tabHost.setup(); //if not TabActivity,must call this

		// mTabManager = new TabManager(this, tabHost, android.R.id.tabcontent);

		// ��һ��TAB
		Intent intent = new Intent(this, StoreListActivity.class);// �½�һ��Intent����Tab1��ʾ������
		intent.putExtra("type", StoreListActivity.LIST_TYPE_RECOMMAND);
		TabSpec spec = tabHost.newTabSpec("tab1");// �½�һ�� Tab
		spec.setIndicator("Home");// ���������Լ�ͼ��
		spec.setContent(intent);// ������ʾ��intent������Ĳ���Ҳ������R.id.xxx
		tabHost.addTab(spec);// ��ӽ�tabHost

		// �ڶ���TAB
		intent = new Intent(this, Tab2Activity.class);// �ڶ���Intent����Tab1��ʾ������
		spec = tabHost.newTabSpec("tab2");// �½�һ�� Tab
		spec.setIndicator("Order");// ���������Լ�ͼ��
		spec.setContent(intent);// ������ʾ��intent������Ĳ���Ҳ������R.id.xxx
		tabHost.addTab(spec);// ��ӽ�tabHost

		tabHost.setCurrentTab(0);
		/*
		 * tabHost.setOnTabChangedListener(changeLis); OnTabChangeListener
		 * changeLis=new OnTabChangeListener(){
		 * 
		 * @Override public void onTabChanged(String tabId) { // TODO
		 * Auto-generated method stub setTitle(tabId);
		 * 
		 * }
		 * 
		 * };
		 */
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {// ����һ��Handler�����ڴ��������߳���UI��ͨѶ
				if (!Thread.currentThread().isInterrupted()) {

					Intent i = new Intent(MainActivity.this,
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onStop() {
		doUnbindService();
		super.onStop();
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
			bsi.SetHandler(handler);

			bsi.downloadConfigFile();
		};

		public void onServiceDisconnected(android.content.ComponentName name) {

			isBound = false;
		};
	};

	/**
	 * This is a helper class that implements a generic mechanism for
	 * associating fragments with the tabs in a tab host. It relies on a trick.
	 * Normally a tab host has a simple API for supplying a View or Intent that
	 * each tab will show. This is not sufficient for switching between
	 * fragments. So instead we make the content part of the tab host 0dp high
	 * (it is not shown) and the TabManager supplies its own dummy view to show
	 * as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct fragment shown in a separate content area whenever
	 * the selected tab changes.
	 */
	public static class TabManager implements TabHost.OnTabChangeListener {
		private final FragmentActivity mActivity;
		private final TabHost mTabHost;
		private final int mContainerId;
		private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
		TabInfo mLastTab;

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;
			private Fragment fragment;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabManager(FragmentActivity activity, TabHost tabHost,
				int containerId) {
			mActivity = activity;
			mTabHost = tabHost;
			mContainerId = containerId;
			mTabHost.setOnTabChangedListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mActivity));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			info.fragment = mActivity.getSupportFragmentManager()
					.findFragmentByTag(tag);
			if (info.fragment != null && !info.fragment.isDetached()) {
				FragmentTransaction ft = mActivity.getSupportFragmentManager()
						.beginTransaction();
				ft.detach(info.fragment);
				ft.commit();
			}

			mTabs.put(tag, info);
			mTabHost.addTab(tabSpec);
		}

		public void onTabChanged(String tabId) {
			TabInfo newTab = mTabs.get(tabId);
			if (mLastTab != newTab) {
				FragmentTransaction ft = mActivity.getSupportFragmentManager()
						.beginTransaction();
				if (mLastTab != null) {
					if (mLastTab.fragment != null) {
						ft.detach(mLastTab.fragment);
					}
				}
				if (newTab != null) {
					if (newTab.fragment == null) {
						newTab.fragment = Fragment.instantiate(mActivity,
								newTab.clss.getName(), newTab.args);
						ft.add(mContainerId, newTab.fragment, newTab.tag);
					} else {
						ft.attach(newTab.fragment);
					}
				}

				mLastTab = newTab;
				ft.commit();
				mActivity.getSupportFragmentManager()
						.executePendingTransactions();
			}
		}
	}

	private TabHost tabHost;
	private TabManager mTabManager;

}
