package diy.net.menzap.model;

/**
 * Created by vivek-sethia on 23.09.16.
 */
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private int id;
    private String sender;
    private String emailId;
    private int isFriend;
    private long ts;
    private long uniqueId;


    public User(String emailId,  int isFriend) {
        this.setEmailId(emailId);
        this.setIsFriend(isFriend);
    }

    public User(String sender, String emailId, int isFriend, long ts, long uniqueId) {
        this.setSender(sender);
        this.setEmailId(emailId);
        this.setIsFriend(isFriend);
        this.setTs(ts);
        this.setUniqueId(uniqueId);
    }

    /**
     * Retrieving User data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private User(Parcel in){
        this.setEmailId(in.readString());
        this.setIsFriend(in.readInt());
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
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
        dest.writeString(this.emailId);
        dest.writeInt(this.isFriend);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
