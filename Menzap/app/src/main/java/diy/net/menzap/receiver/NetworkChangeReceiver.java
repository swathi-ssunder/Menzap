package diy.net.menzap.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import diy.net.menzap.helper.DataHolder;
import diy.net.menzap.helper.NetworkHelper;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {

		switch(NetworkHelper.isConnected(context)) {
			case 1:
				DataHolder.getInstance().getNotificationHelper().notifyForWAP(1);
				DataHolder.getInstance().getEddystoneHelper().startScan();
				break;
			case 0:
				DataHolder.getInstance().getNotificationHelper().notifyForWAP(0);
				DataHolder.getInstance().getEddystoneHelper().stopScan();
				break;
			default:
				break;
		}
	}
}