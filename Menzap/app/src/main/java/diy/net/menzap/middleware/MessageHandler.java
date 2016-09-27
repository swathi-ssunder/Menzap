package diy.net.menzap.middleware;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

import diy.net.menzap.helper.DataHolder;
import diy.net.menzap.helper.EventDBHelper;
import diy.net.menzap.helper.MenuDBHelper;
import diy.net.menzap.helper.TrackingDBHelper;
import diy.net.menzap.helper.UserDBHelper;
import diy.net.menzap.model.Event;
import diy.net.menzap.model.Menu;
import diy.net.menzap.model.Tracking;
import diy.net.menzap.model.User;
import diy.net.menzap.model.message.Message;
import fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage;

/**
 * Created by swathissunder on 22/09/16.
 */

public class MessageHandler {

    //==========================================================================//
    // Definitions of the SCAMPIMessage fields
    //==========================================================================//
    public static final String MSG_TYPE = "TYPE";
    public static final String MSG_SENDER = "SENDER";
    public static final String MSG_TIMESTAMP = "TIMESTAMP";
    public static final String MSG_UNIQUE_ID = "UNIQUE_ID";
    public static final String MSG_EVENT_NAME = "EVENT_NAME";
    public static final String MSG_EVENT_DESCRIPTION = "EVENT_DESCRIPTION";
    public static final String MSG_LOCATION = "LOCATION";
    public static final String MSG_START_TIME = "START_TIME";
    public static final String MSG_END_TIME = "END_TIME";
    public static final String MSG_IS_INTERESTED = "IS_INTERESTED";

    public static final String MSG_MENU_NAME = "MENU_NAME";
    public static final String MSG_MENU_DESCRIPTION = "MENU_DESCRIPTION";
    public static final String MSG_CATEGORY = "CATEGORY";
    public static final String MSG_SERVED_ON = "SERVED_ON";

    public static final String MSG_EMAIL_ID = "EMAIL_ID";
    public static final String MSG_NAME = "NAME";
    public static final String MSG_IS_FRIEND = "IS_FRIEND";

    public static final String MSG_USER_NAME = "USER_NAME";
    public static final String MSG_URL = "URL";
    //==========================================================================//

    private Context context;


    public MessageHandler(Context context) {
        this.context = context;
    }

    public void handleIncomingMessage(SCAMPIMessage msg )
            throws IOException {

        // Database where incoming messages are to be stored
        SQLiteOpenHelper db;
        User person;

        /*Fetching user details from preferences*/
        SharedPreferences pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String emailId = pref.getString("emailId", "");

        /*Return if the user is not registered.*/
        if(emailId.isEmpty()) {
            return;
        }

        switch(Message.MessageType.valueOf(msg.getString( MSG_TYPE ))) {
            case ENTER:

                db = new UserDBHelper(this.context);
                person = ((UserDBHelper)db).getByEmailId(msg.getString(MSG_EMAIL_ID));

                if (person != null && person.getIsFriend() == 1) {

                    User user = new User(person.getSender(), person.getEmailId(), person.getName(),
                            person.getIsFriend(), person.getTs(), person.getUniqueId());

                    DataHolder.getInstance().getNotificationHelper().notifyForFriend(user, true);
                }
                break;

            case EXIT:
                db = new UserDBHelper(this.context);
                person = ((UserDBHelper)db).getByEmailId(msg.getString(MSG_EMAIL_ID));

                if (person != null && person.getIsFriend() == 1) {

                    User user = new User(person.getSender(), person.getEmailId(), person.getName(),
                            person.getIsFriend(), person.getTs(), person.getUniqueId());

                    DataHolder.getInstance().getNotificationHelper().notifyForFriend(user, false);
                }
                break;

            case MENU:

                Menu menu = new Menu(msg.getString(MSG_SENDER), msg.getString(MSG_MENU_NAME), msg.getString(MSG_MENU_DESCRIPTION),
                        msg.getInteger(MSG_CATEGORY), msg.getString(MSG_SERVED_ON),
                        msg.getInteger(MSG_TIMESTAMP), msg.getInteger(MSG_UNIQUE_ID));

                db = new MenuDBHelper(this.context);
                // Insert into the database
                if (((MenuDBHelper)db).insert(menu)) {
                    Log.d("MENU added", ((MenuDBHelper)db).getAll().toString());

                    // notifying only when the difference of time with current time is 5 seconds
                    if( System.currentTimeMillis() - msg.getInteger(MSG_TIMESTAMP) < 300000)
                        DataHolder.getInstance().getNotificationHelper().notifyForMenu(menu);
                }
                db.close();

                break;

            case REVIEW:
                break;

            case EVENT:

                Event event = new Event(msg.getString(MSG_SENDER), msg.getString(MSG_EVENT_NAME), msg.getString(MSG_EVENT_DESCRIPTION),
                        msg.getString(MSG_LOCATION), msg.getString(MSG_START_TIME), msg.getString(MSG_END_TIME),msg.getInteger(MSG_IS_INTERESTED),
                        msg.getInteger(MSG_TIMESTAMP), msg.getInteger(MSG_UNIQUE_ID));

                db = new EventDBHelper(this.context);
                // Insert into the database
                if (((EventDBHelper)db).insert(event)) {
                    Log.d("EVENT added", ((EventDBHelper)db).getAll().toString());

                    // notifying only when the difference of time with current time is 5 seconds
                    if( System.currentTimeMillis() - msg.getInteger(MSG_TIMESTAMP) < 300000)
                        DataHolder.getInstance().getNotificationHelper().notifyForEvent(event, false);
                }
                db.close();
                break;

            case REGISTER:

                User user = new User(msg.getString(MSG_SENDER), msg.getString(MSG_EMAIL_ID), msg.getString(MSG_NAME),
                        msg.getInteger(MSG_IS_FRIEND), msg.getInteger(MSG_TIMESTAMP), msg.getInteger(MSG_UNIQUE_ID));

                db = new UserDBHelper(this.context);
                // Insert into the database
                ((UserDBHelper)db).insert(user);
                db.close();
                break;

            case TRACKING:
                Tracking tracking = new Tracking(msg.getString(MSG_SENDER), msg.getString(MSG_USER_NAME), msg.getString(MSG_URL),
                        msg.getInteger(MSG_TIMESTAMP), msg.getInteger(MSG_UNIQUE_ID));

                db = new TrackingDBHelper(this.context);
                // Insert into the database
                ((TrackingDBHelper)db).insert(tracking);
                db.close();
                break;
        }
    }
}
