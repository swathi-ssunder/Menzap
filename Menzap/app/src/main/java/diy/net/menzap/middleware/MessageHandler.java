package diy.net.menzap.middleware;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import diy.net.menzap.helper.EventDBHelper;
import diy.net.menzap.model.Event;
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
    //==========================================================================//

    private Context context;


    public MessageHandler(Context context) {
        this.context = context;
    }

    public void handleIncomingMessage(SCAMPIMessage msg )
            throws IOException {

        // Database where incoming messages are to be stored
        EventDBHelper db;

        Log.d("event name", msg.getString( "EVENT_NAME" ));

        switch(Message.MessageType.valueOf(msg.getString( MSG_TYPE ))) {
            case ENTER:
                break;
            case EXIT:
                break;
            case MENU:
                break;
            case REVIEW:
                break;
            case EVENT:

                Event event = new Event(msg.getInteger(MSG_SENDER), msg.getString(MSG_EVENT_NAME), msg.getString(MSG_EVENT_DESCRIPTION),
                        msg.getString(MSG_LOCATION), msg.getString(MSG_START_TIME), msg.getString(MSG_END_TIME),
                        msg.getInteger(MSG_TIMESTAMP), msg.getInteger(MSG_UNIQUE_ID));

                // Create database helper
                db = new EventDBHelper(this.context);
                // Insert into the database
                if (db.insert(event)) {
                    Log.d("EVENT added", db.getAll().toString());
                }
                break;
        }
    }
}
