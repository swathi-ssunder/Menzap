package diy.net.menzap.helper;

import android.content.Context;

/**
 * Created by swathissunder on 22/09/16.
 */

public class DataHolder {
    private AppLibHelper helper;
    private NotificationHelper notificationHelper;
    private EddystoneHelper eddystoneHelper;

    public AppLibHelper getHelper() {
        return helper;
    }

    public NotificationHelper getNotificationHelper() {
        return notificationHelper;
    }

    public EddystoneHelper getEddystoneHelper() {
        return eddystoneHelper;
    }

    public void initHelper(Context context) {
        /*Initialize the AppLibHelper, required for SCAMPI*/
        this.helper = new AppLibHelper(context);
        this.helper.initService();

        /*Initialize the NotificationHelper, required to send push notifications*/
        this.notificationHelper = new NotificationHelper(context);

        /*Initialize the EddystoneHelper, required for Beacon detection*/
        this.eddystoneHelper = new EddystoneHelper(context);
        this.eddystoneHelper.initService();
    }

    public void destroyHelper() {
        this.helper.destroy();
        this.eddystoneHelper.destroy();
    }

    private static final DataHolder holder = new DataHolder();

    public static DataHolder getInstance() {
        return holder;
    }
}