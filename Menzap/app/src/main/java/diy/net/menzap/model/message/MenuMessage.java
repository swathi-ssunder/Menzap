package diy.net.menzap.model.message;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Dictionary;
import java.util.concurrent.TimeUnit;

import diy.net.menzap.service.AppLibService;
import fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage;

/**
 * Created by aditya on 21.09.16.
 */

public class MenuMessage extends Message{
    private JSONObject menu;

    public JSONObject getMenu() {
        return menu;
    }

    public void setMenu(JSONObject menu) {
        this.menu = menu;
    }

    public MenuMessage(long sender, JSONObject menu) {
        super(sender);
        this.setMenu(menu);
        this.setTtl(24*60);
        this.setType("MENU");
    }

    public MenuMessage(SCAMPIMessage message) {
        super(message);
        try {
            this.menu = new JSONObject(message.getString("MENU"));
        } catch (JSONException e) {
            Log.d("MenuMessage : ", "Error while handling JSON");
        }
    }


    public SCAMPIMessage getScampiMsgObj() {
        SCAMPIMessage msg = SCAMPIMessage.builder()
                .lifetime(this.getTtl(), TimeUnit.MINUTES)
                .build();

        msg.putInteger("SENDER", this.getSender());
        msg.putString("TYPE", this.getType());
        msg.putInteger("TIMESTAMP", this.getTimestamp());
        msg.putInteger("UNIQUE_ID", this.getUniqueid());
        msg.putString("MENU", this.getMenu().toString());
        return msg;
    }
}
