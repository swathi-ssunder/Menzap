package diy.net.menzap.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {

    private String name;
    private String description;
    private String location;
    private String fromDate;
    private String toDate;

    public Event(String name, String description, String location, String fromDate, String toDate) {

        this.setName(name);
        this.setDescription(description);
        this.setLocation(location);
        this.setFromDate(fromDate);
        this.setToDate(toDate);
    }

    /**
     * Retrieving Event data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private Event(Parcel in){
        this.setName(in.readString());
        this.setDescription(in.readString());
        this.setLocation(in.readString());
        this.setFromDate(in.readString());
        this.setToDate(in.readString());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    private void setLocation(String location) {
        this.location = location;
    }

    public String getFromDate() {
        return fromDate;
    }

    private void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    private void setToDate(String toDate) {
        this.toDate = toDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.location);
        dest.writeString(this.fromDate);
        dest.writeString(this.toDate);
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
