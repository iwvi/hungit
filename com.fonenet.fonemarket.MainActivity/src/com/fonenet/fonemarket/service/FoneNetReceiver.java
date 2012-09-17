package com.fonenet.fonemarket.service;





import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FoneNetReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, DownloaderService.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startService(i);
	}

}
