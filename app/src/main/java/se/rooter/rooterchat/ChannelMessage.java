package se.rooter.rooterchat;


import java.nio.channels.Channel;

public class ChannelMessage {

    private String msgID;
    private String senderID;
    private String senderName;
    private String message;
    private String chatChannel;

    public ChannelMessage() {

    }

    public ChannelMessage(String senderID, String message, String chatChannel) {
        this.senderID = senderID;
        this.message = message;
        this.chatChannel = chatChannel;
    }

    public ChannelMessage(String msgID, String senderID, String message, String chatChannel) {
        this.msgID = msgID;
        this.senderID = senderID;
        this.message = message;
        this.chatChannel = chatChannel;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getMsgID() {
        return this.msgID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderID() {
        return this.senderID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setChannel(String chatChannel) {
        this.chatChannel = chatChannel;
    }

    public String getChannel() {
        return this.chatChannel;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ChannelMessage)) {
            return false;
        }

        ChannelMessage chmsg = (ChannelMessage) obj;

        return this.msgID.equals(chmsg.msgID);


    }

    @Override
    public int hashCode() {
        return this.msgID.hashCode();
    }
}
