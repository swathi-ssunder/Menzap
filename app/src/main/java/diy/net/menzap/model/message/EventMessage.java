package diy.net.menzap.model.message;

import java.util.concurrent.TimeUnit;

import diy.net.menzap.model.Event;
import fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage;

/**
 * Created by aditya on 21.09.16.
 */

public class EventMessage extends Message{
    private String eventName;
    private String eventDescription;
    private String startTime;
    private String endTime;
    private String location;
    private long isInterested;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getIsInterested() {
        return isInterested;
    }

    public void setIsInterested(long isInterested) {
        this.isInterested = isInterested;
    }


    public EventMessage(String sender, String eventName, String eventDescription, String startTime,
                        String endTime, String location, long isInterested) {
        super(sender);
        this.setType("EVENT");
        this.setTtl(24*60);
        this.setEndTime(endTime);
        this.setEventName(eventName);
        this.setLocation(location);
        this.setStartTime(startTime);
        this.setEventDescription(eventDescription);
        this.setIsInterested(isInterested);
    }

    public EventMessage(SCAMPIMessage message) {
        super(message);
        this.startTime = message.getString("START_TIME");
        this.endTime = message.getString("END_TIME");
        this.eventName = message.getString("EVENT_NAME");
        this.eventDescription = message.getString("EVENT_DESCRIPTION");
        this.location = message.getString("LOCATION");
        this.isInterested = message.getInteger("IS_INTERESTED");
    }

    public EventMessage(String sender, Event event) {
        super(sender);
        this.setType("EVENT");
        this.setTtl(24*60);
        this.setEventName(event.getName());
        this.setEventDescription(event.getDescription());
        this.setLocation(event.getLocation());
        this.setStartTime(event.getFromDate());
        this.setEndTime(event.getToDate());
        this.setIsInterested(event.getIsInterested());
        this.setTimestamp(event.getTs());
        this.setUniqueId(event.getUniqueId());
    }

    public SCAMPIMessage getScampiMsgObj() {
        SCAMPIMessage msg = SCAMPIMessage.builder()
                .lifetime(this.getTtl(), TimeUnit.MINUTES)
                .build();

        msg.putString("SENDER", this.getSender());
        msg.putString("TYPE", this.getType());
        msg.putInteger("TIMESTAMP", this.getTimestamp());
        msg.putInteger("UNIQUE_ID", this.getUniqueId());
        msg.putString("START_TIME", this.getStartTime());
        msg.putString("END_TIME", this.getEndTime());
        msg.putString("EVENT_NAME", this.getEventName());
        msg.putString("EVENT_DESCRIPTION", this.getEventDescription());
        msg.putString("LOCATION", this.getLocation());
        msg.putInteger("IS_INTERESTED", this.getIsInterested());

        return msg;
    }
}
