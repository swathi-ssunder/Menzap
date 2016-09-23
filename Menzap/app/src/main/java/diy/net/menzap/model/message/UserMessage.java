package diy.net.menzap.model.message;

import java.util.concurrent.TimeUnit;

import diy.net.menzap.model.User;
import fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage;

/**
 * Created by vivek-sethia on 23.09.16.
 */
public class UserMessage extends Message{
    private String emailId;
    private long isFriend;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public long getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(long isFriend) {
        this.isFriend = isFriend;
    }


    public UserMessage(String sender, String emailId, int isFriend) {
        super(sender);
        this.setType("USER");
        this.setTtl(24*60);

        this.setEmailId(emailId);
        this.setIsFriend(isFriend);

    }

    public UserMessage(SCAMPIMessage message) {
        super(message);
        this.emailId = message.getString("EMAIL_ID");
        this.isFriend = message.getInteger("IS_FRIEND");
    }

    public UserMessage(String sender, User user) {
        super(sender);
        this.setType("USER");
        this.setTtl(24*60);
        this.setEmailId(user.getEmailId());
        this.setIsFriend(user.getIsFriend());
    }

    public SCAMPIMessage getScampiMsgObj() {
        SCAMPIMessage msg = SCAMPIMessage.builder()
                .lifetime(this.getTtl(), TimeUnit.MINUTES)
                .build();

        msg.putString("SENDER", this.getSender());
        msg.putString("TYPE", this.getType());
        msg.putString("EMAIL_ID", this.getEmailId());
        msg.putInteger("IS_FRIEND", this.getIsFriend());

        return msg;
    }
}
