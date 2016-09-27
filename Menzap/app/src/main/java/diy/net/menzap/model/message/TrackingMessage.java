package diy.net.menzap.model.message;

import java.util.concurrent.TimeUnit;

import diy.net.menzap.model.Tracking;
import fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage;

/**
 * Created by swathissunder on 27/09/16.
 */

public class TrackingMessage extends Message {
    private String userName;
    private String url;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TrackingMessage(String sender, String userName, String url) {
        super(sender);
        this.setType("TRACKING");
        this.setTtl(24*60);

        this.setUserName(userName);
        this.setUrl(url);
    }

    public TrackingMessage(SCAMPIMessage message) {
        super(message);
        this.userName = message.getString("USER_NAME");
        this.url = message.getString("URL");
    }

    public TrackingMessage(String sender, Tracking tracking) {
        super(sender);
        this.setType("TRACKING");
        this.setTtl(24*60);
        this.setUserName(tracking.getUserName());
        this.setUrl(tracking.getUrl());
        this.setTimestamp(tracking.getTs());
        this.setUniqueId(tracking.getUniqueId());
    }

    public SCAMPIMessage getScampiMsgObj() {
        SCAMPIMessage msg = SCAMPIMessage.builder()
                .lifetime(this.getTtl(), TimeUnit.MINUTES)
                .build();

        msg.putString("SENDER", this.getSender());
        msg.putString("TYPE", this.getType());
        msg.putInteger("TIMESTAMP", this.getTimestamp());
        msg.putInteger("UNIQUE_ID", this.getUniqueId());
        msg.putString("USER_NAME", this.getUserName());
        msg.putString("URL", this.getUrl());

        return msg;
    }
}
