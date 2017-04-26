package se.rooter.rooterchat;


public class ChannelMessage {

    private String senderID;
    private String message;
    private String chatChannel;

    public ChannelMessage() {

    }

    public ChannelMessage(String senderID, String message, String chatChannel) {
        this.senderID = senderID;
        this.message = message;
        this.chatChannel = chatChannel;
    }

    public String getSenderID() {
        return this.senderID;
    }

    public String getMessage() {
        return this.message;
    }

    public String getChannel() {
        return this.chatChannel;
    }


}
