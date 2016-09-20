package diy.net.menzap.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Event implements Parcelable {

    private String name;
    private String location;
    private Date fromDate;
    private Date toDate;

    public Event(String name, String location, Date fromDate, Date toDate) {
        this.name = name;
        this.location = location;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    /**
     * Retrieving Event data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private Event(Parcel in){
        this.name = in.readString();
        this.location = in.readString();
        this.fromDate = new Date(in.readLong());
        this.toDate = new Date(in.readLong());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeLong(this.fromDate.getTime());
        dest.writeLong(this.toDate.getTime());
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {

        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
