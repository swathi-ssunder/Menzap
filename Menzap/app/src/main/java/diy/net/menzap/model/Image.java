package diy.net.menzap.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vivek-sethia on 28.09.16.
 */
public class Image implements Parcelable {

    private long id;
    private String sender;
    private long ts;
    private long uniqueId;


    public Image(String sender, long ts, long uniqueId) {
        this.setSender(sender);
        this.setTs(ts);
        this.setUniqueId(uniqueId);
    }

    private Image(Parcel in){
        this.setSender(in.readString());
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
        dest.writeString(this.sender);

    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {

        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}