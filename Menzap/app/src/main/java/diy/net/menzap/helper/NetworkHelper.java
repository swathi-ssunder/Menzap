package diy.net.menzap.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

public class NetworkHelper {

    private static int lastState;

    public static int isConnected(Context context) {
        int isConnectedToWifi = 0;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        Network[] nets = cm.getAllNetworks();

        for (Network net : nets) {
            NetworkInfo ni = cm.getNetworkInfo(net);

            try {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected() && ni.getExtraInfo().replace("\"", "").equals("Pi3-AP"))
                        isConnectedToWifi = 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (lastState != isConnectedToWifi) {
            lastState = isConnectedToWifi;
            return lastState;
        }
        return -1;
    }
}