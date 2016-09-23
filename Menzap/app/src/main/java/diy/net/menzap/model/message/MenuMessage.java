package diy.net.menzap.model.message;

import java.util.concurrent.TimeUnit;

import diy.net.menzap.model.Menu;
import fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage;

/**
 * Created by aditya on 21.09.16.
 */

public class MenuMessage extends Message{
    private String menuName;
    private String menuDescription;
    private long category;
    private String servedOn;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public String getServedOn() {
        return servedOn;
    }

    public void setServedOn(String servedOn) {
        this.servedOn = servedOn;
    }



    public MenuMessage(String sender, String menuName, String menuDescription, long category,
                        String servedOn) {
        super(sender);
        this.setType("MENU");
        this.setTtl(24*60);

        this.setMenuName(menuName);
        this.setMenuDescription(menuDescription);
        this.setCategory(category);
        this.setServedOn(servedOn);

    }

    public MenuMessage(SCAMPIMessage message) {
        super(message);
        this.menuName = message.getString("MENU_NAME");
        this.menuDescription = message.getString("MENU_DESCRIPTION");
        this.category = message.getInteger("CATEGORY");
        this.servedOn = message.getString("SERVED_ON");

    }

    public MenuMessage(String sender, Menu menu) {
        super(sender);
        this.setType("MENU");
        this.setTtl(24*60);
        this.setMenuName(menu.getName());
        this.setMenuDescription(menu.getDescription());
        this.setCategory(menu.getCategory());
        this.setServedOn(menu.getServedOn());
        this.setTimestamp(menu.getTs());
        this.setUniqueId(menu.getUniqueId());
    }

    public SCAMPIMessage getScampiMsgObj() {
        SCAMPIMessage msg = SCAMPIMessage.builder()
                .lifetime(this.getTtl(), TimeUnit.MINUTES)
                .build();

        msg.putString("SENDER", this.getSender());
        msg.putString("TYPE", this.getType());
        msg.putInteger("TIMESTAMP", this.getTimestamp());
        msg.putInteger("UNIQUE_ID", this.getUniqueId());
        msg.putString("MENU_NAME", this.getMenuName());
        msg.putString("MENU_DESCRIPTION", this.getMenuDescription());
        msg.putInteger("CATEGORY", this.getCategory());
        msg.putString("SERVED_ON", this.getServedOn());

        return msg;
    }
}
