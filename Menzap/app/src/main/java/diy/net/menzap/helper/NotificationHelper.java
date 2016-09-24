package diy.net.menzap.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import diy.net.menzap.R;
import diy.net.menzap.activity.TabsActivity;
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
        Intent intent = new Intent(context, TabsActivity.class);
        intent.setAction("TAB_EVENT");
        PendingIntent pIntent = PendingIntent.getActivity(context, (int)System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this.context)
                .setContentTitle("Menzap")
                .setContentText("Upcoming Event-" + event.getName())
                .setSmallIcon(R.drawable.ic_tab_events)
                .setContentIntent(pIntent)
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
