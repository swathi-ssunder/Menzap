package diy.net.menzap.model.message;

import java.util.concurrent.TimeUnit;

import diy.net.menzap.model.User;
import fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage;

/**
 * Created by vivek-sethia on 23.09.16.
 */
public class UserMessage extends Message{
    private String emailId;
    private String name;
    private long isFriend;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(long isFriend) {
        this.isFriend = isFriend;
    }


    public UserMessage(String type, String sender, String emailId, String name, int isFriend) {
        super(sender);
        this.setType(type);
        this.setTtl(24*60);

        this.setEmailId(emailId);
        this.setName(name);
        this.setIsFriend(isFriend);

    }

    public UserMessage(SCAMPIMessage message) {
        super(message);
        this.emailId = message.getString("EMAIL_ID");
        this.name = message.getString("NAME");
        this.isFriend = message.getInteger("IS_FRIEND");
    }

    public UserMessage(String type, String sender, User user) {
        super(sender);
        this.setType(type);
        this.setTtl(24*60);
        this.setEmailId(user.getEmailId());
        this.setName(user.getName());
        this.setIsFriend(user.getIsFriend());
        this.setTimestamp(user.getTs());
        this.setUniqueId(user.getUniqueId());
    }

    public SCAMPIMessage getScampiMsgObj() {
        SCAMPIMessage msg = SCAMPIMessage.builder()
                .lifetime(this.getTtl(), TimeUnit.MINUTES)
                .build();

        msg.putString("SENDER", this.getSender());
        msg.putString("TYPE", this.getType());
        msg.putInteger("TIMESTAMP", this.getTimestamp());
        msg.putInteger("UNIQUE_ID", this.getUniqueId());
        msg.putString("EMAIL_ID", this.getEmailId());
        msg.putString("NAME", this.getName());
        msg.putInteger("IS_FRIEND", this.getIsFriend());

        return msg;
    }
}
