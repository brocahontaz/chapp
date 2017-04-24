package se.rooter.rooterchat;

public class ChatInformation {

    private String channelName;

    public ChatInformation(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
