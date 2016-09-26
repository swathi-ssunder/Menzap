package diy.net.menzap.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import diy.net.menzap.R;
import diy.net.menzap.activity.TabsActivity;
import diy.net.menzap.model.Event;
import diy.net.menzap.model.Menu;
import diy.net.menzap.model.User;
import diy.net.menzap.model.message.UserMessage;

/**
 * Created by swathissunder on 22/09/16.
 */

public class NotificationHelper {

    private Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public void notifyForEvent(Event event, boolean isToday) {
        Intent intent = new Intent(context, TabsActivity.class);
        intent.setAction("TAB_EVENT");
        PendingIntent pIntent = PendingIntent.getActivity(context, (int)System.currentTimeMillis(), intent, 0);
        String contentText = isToday ? ("Don't miss " + event.getName() + " today!") :
                ("Upcoming Event-" + event.getName());

        // Build notification
        Notification noti = new Notification.Builder(this.context)
                .setContentTitle("Menzap")
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_tab_events)
                .setContentIntent(pIntent)
                .build();

        android.app.NotificationManager notificationManager = (NotificationManager) (context.getSystemService(context.NOTIFICATION_SERVICE));
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }

    public void notifyForMenu(Menu menu) {
        Intent intent = new Intent(context, TabsActivity.class);
        intent.setAction("TAB_MENU");
        PendingIntent pIntent = PendingIntent.getActivity(context, (int)System.currentTimeMillis(), intent, 0);

        // Build notification
        Notification noti = new Notification.Builder(this.context)
                .setContentTitle("Menzap")
                .setContentText("New Dish on the Menu-" + menu.getName())
                .setSmallIcon(R.drawable.ic_tab_menu)
                .setContentIntent(pIntent)
                .build();

        android.app.NotificationManager notificationManager = (NotificationManager) (context.getSystemService(context.NOTIFICATION_SERVICE));
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }

    public void notifyForWAP(int isConnected) {
        Intent intent = new Intent(context, TabsActivity.class);
        intent.setAction("TAB_MENU");
        PendingIntent pIntent = PendingIntent.getActivity(context, (int)System.currentTimeMillis(), intent, 0);

        String contentText = (isConnected == 1) ? "Welcome to Mensa" : "Hope you had a good meal. See you again!";
        // Build notification
        Notification noti = new Notification.Builder(this.context)
                .setContentTitle("Menzap")
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_tab_menu)
                .setContentIntent(pIntent)
                .build();

        android.app.NotificationManager notificationManager = (NotificationManager) (context.getSystemService(context.NOTIFICATION_SERVICE));
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

        /*Fetching sender details from preferences*/
        SharedPreferences pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String emailId = pref.getString("emailId", "");

        /*Return if the user is not registered.*/
        if(emailId.isEmpty()) {
            return;
        }

        /*If registered, publish entry/exit message*/
        UserDBHelper db = new UserDBHelper(this.context);
        User person = db.getByEmailId(emailId);

        if(isConnected == 1) {
            /*Send notification upon entry*/
            UserMessage enterMessage = new UserMessage("ENTER", emailId, person);
            DataHolder.getInstance().getHelper().saveAndPublish(enterMessage.getScampiMsgObj());
        } else {
            /*Send notification upon exit*/
            UserMessage exitMessage = new UserMessage("EXIT", emailId, person);
            DataHolder.getInstance().getHelper().saveAndPublish(exitMessage.getScampiMsgObj());
        }
    }

    public void notifyForFriend(User friend, boolean isEntry) {
        Intent intent = new Intent(context, TabsActivity.class);
        intent.setAction("TAB_FRIEND");
        PendingIntent pIntent = PendingIntent.getActivity(context, (int)System.currentTimeMillis(), intent, 0);
        String contentText = isEntry ? ("Say Hi to " + friend.getName() + " right here!") :
                (friend.getName() + " is leaving from Mensa!");

        // Build notification
        Notification noti = new Notification.Builder(this.context)
                .setContentTitle("Menzap")
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_tab_contacts)
                .setContentIntent(pIntent)
                .build();

        android.app.NotificationManager notificationManager = (NotificationManager) (context.getSystemService(context.NOTIFICATION_SERVICE));
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }
}
