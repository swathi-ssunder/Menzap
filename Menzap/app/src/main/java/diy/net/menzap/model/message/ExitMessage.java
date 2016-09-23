package diy.net.menzap.model.message;

import java.util.concurrent.TimeUnit;

import diy.net.menzap.service.AppLibService;
import fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage;

/**
 * Created by aditya on 21.09.16.
 */

public class ExitMessage extends Message{
    public ExitMessage(String sender) {
        super(sender);
        this.setTtl(60); // Set this
        this.setType("EXIT");
    }

    public ExitMessage(SCAMPIMessage message) {
        super(message);
    }

    public SCAMPIMessage getScampiMsgObj() {
        SCAMPIMessage msg = SCAMPIMessage.builder()
                .lifetime(this.getTtl(), TimeUnit.MINUTES)
                .build();

        msg.putString("SENDER", this.getSender());
        msg.putString("TYPE", this.getType());
        msg.putInteger("TIMESTAMP", this.getTimestamp());
        msg.putInteger("UNIQUE_ID", this.getUniqueId());

        return msg;
    }
}
