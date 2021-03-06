package diy.net.menzap.model;

/**
 * Created by viveksethia on 22/09/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Menu implements Parcelable {

    private long id;
    private String sender;
    private String name;
    private String description;
    private long category;
    private String servedOn;
    private long isLiked;
    private long isFavourite;
    private long likeCount;
    private long ts;
    private long uniqueId;


    public Menu(String sender, String name, String description, long category, String servedOn, long isLiked,
                long isFavourite, long likeCount) {
        this.setSender(sender);
        this.setName(name);
        this.setDescription(description);
        this.setCategory(category);
        this.setServedOn(servedOn);
        this.setIsLiked(isLiked);
        this.setIsFavourite(isFavourite);
        this.setLikeCount(likeCount);
    }

    public Menu(String sender, String name, String description, long category, String servedOn, long isLiked, long isFavourite,
                long likeCount,long ts, long uniqueId) {
        this.setSender(sender);
        this.setName(name);
        this.setDescription(description);
        this.setCategory(category);
        this.setServedOn(servedOn);
        this.setIsLiked(isLiked);
        this.setIsFavourite(isFavourite);
        this.setLikeCount(likeCount);
        this.setTs(ts);
        this.setUniqueId(uniqueId);
    }

    /**
     * Retrieving Menu data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private Menu(Parcel in){
        this.setName(in.readString());
        this.setDescription(in.readString());
        this.setCategory(in.readInt());
        this.setServedOn(in.readString());
        this.setIsLiked(in.readLong());
        this.setIsFavourite(in.readLong());
        this.setLikeCount(in.readLong());
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

    public long getCategory() {
        return category;
    }

    private void setCategory(long category) {
        this.category = category;
    }

    public String getServedOn() {
        return servedOn;
    }

    private void setServedOn(String servedOn) {
        this.servedOn = servedOn;
    }

    public long getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(long isLiked) {
        this.isLiked = isLiked;
    }

    public long getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(long isFavourite) {
        this.isFavourite = isFavourite;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
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
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeLong(this.category);
        dest.writeString(this.servedOn);
        dest.writeLong(this.isLiked);
        dest.writeLong(this.isFavourite);
        dest.writeLong(this.likeCount);
    }

    public static final Parcelable.Creator<Menu> CREATOR = new Parcelable.Creator<Menu>() {

        @Override
        public Menu createFromParcel(Parcel source) {
            return new Menu(source);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };
}
