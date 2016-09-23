package diy.net.menzap.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import diy.net.menzap.R;
import diy.net.menzap.model.Event;
import diy.net.menzap.model.Menu;

/**
 * Created by swathissunder on 22/09/16.
 */

public class NotificationHelper {

    private Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public void notifyForEvent(Event event) {
        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this.context)
                .setContentTitle(event.getName())
                .setContentText("Upcoming Event")
                .setSmallIcon(R.drawable.ic_star)
                .build();

        android.app.NotificationManager notificationManager = (NotificationManager) (context.getSystemService(context.NOTIFICATION_SERVICE));
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }

    public void notifyForMenu(Menu menu) {
        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this.context)
                .setContentTitle(menu.getName())
                .setContentText("New Dish on the Menu")
                .setSmallIcon(R.drawable.ic_tab_call)
                .build();

        android.app.NotificationManager notificationManager = (NotificationManager) (context.getSystemService(context.NOTIFICATION_SERVICE));
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }
}
