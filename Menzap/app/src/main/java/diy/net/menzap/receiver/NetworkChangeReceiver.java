package diy.net.menzap.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import diy.net.menzap.helper.NetworkHelper;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		NetworkHelper.onNetworkChange(context);
	}
}