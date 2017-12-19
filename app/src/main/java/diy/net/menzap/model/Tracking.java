package diy.net.menzap.model;

/**
 * Created by swathissunder on 27/09/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Tracking implements Parcelable {

    private long id;
    private String sender;
    private String userName;
    private String url;
    private long ts;
    private long uniqueId;

    public Tracking(String sender, String userName, String url) {
        this.setSender(sender);
        this.setUserName(userName);
        this.setUrl(url);
    }

    public Tracking(String sender, String userName, String url, long ts, long uniqueId) {
        this.setSender(sender);
        this.setUserName(userName);
        this.setUrl(url);
        this.setTs(ts);
        this.setUniqueId(uniqueId);
    }

    /**
     * Retrieving Event data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private Tracking(Parcel in){
        this.setUserName(in.readString());
        this.setUrl(in.readString());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

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

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.url);
    }

    public static final Parcelable.Creator<Tracking> CREATOR = new Parcelable.Creator<Tracking>() {

        @Override
        public Tracking createFromParcel(Parcel source) {
            return new Tracking(source);
        }

        @Override
        public Tracking[] newArray(int size) {
            return new Tracking[size];
        }
    };
}