package diy.net.menzap.model.message;

import java.util.concurrent.TimeUnit;

import diy.net.menzap.service.AppLibService;
import fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage;

/**
 * Created by aditya on 21.09.16.
 */

public class ReviewMessage extends Message{
    private String item;
    private ReviewVals review;
    private static enum ReviewVals {
        LIKE(1), DISLIKE(0);
        int value;
        ReviewVals(int val) {value =val;}
        int getVal() {return value;}
    };

    public ReviewVals getReview() {
        return review;
    }

    public void setReview(ReviewVals review) {
        this.review = review;
    }


    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public ReviewMessage(long sender, String menuItemValue, ReviewVals review) {
        super(sender);
        this.setTtl(24*60);  // Decide on this
        this.setType("REVIEW");
        this.setItem(menuItemValue);
        this.setReview(review);
    }

    public SCAMPIMessage getScampiMsgObj() {
        SCAMPIMessage msg = SCAMPIMessage.builder()
                .lifetime(this.getTtl(), TimeUnit.MINUTES)
                .build();

        msg.putInteger("SENDER", this.getSender());
        msg.putString("TYPE", this.getType());
        msg.putInteger("TIMESTAMP", this.getTimestamp());
        msg.putInteger("UNIQUE_ID", this.getUniqueid());
        msg.putInteger("REVIEW", this.getReview().getVal());
        msg.putString("MENU_ITEM", this.getItem());

        return msg;
    }
}

