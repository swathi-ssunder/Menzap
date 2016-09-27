package diy.net.menzap.model.message;

import fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage;

import static fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage.RNG;

/**
 * Created by aditya on 21.09.16.
 */



public abstract class Message {
    private String type;
    private String sender;
    private long ttl;
    private long timestamp;
    private long uniqueId;

    public long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public enum MessageType {REVIEW, MENU, ENTER, EXIT, EVENT, REGISTER, TRACKING};

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Message(String sender) {
        this.sender = sender;
        this.timestamp = System.currentTimeMillis();
        this.uniqueId = RNG.nextLong();
    }

    public Message(SCAMPIMessage message) {
        this.sender = message.getString("SENDER");
        this.timestamp = message.getInteger("TIMESTAMP");
        this.uniqueId = message.getInteger("UNIQUE_ID");
        this.type = message.getString("TYPE");
        this.ttl = message.getLifetime();
    }

    public abstract SCAMPIMessage getScampiMsgObj();

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
