package se.rooter.chapp;

/**
 * Class representing a chat channel
 */
public class ChatInformation {

    private String channelName;

    /**
     * Default empty constructor
     *
     * @constructor
     */
    public ChatInformation() {
    }

    /**
     * Create a ne ChatInformation object with channel name
     *
     * @param channelName the channel name
     */
    public ChatInformation(String channelName) {
        this.channelName = channelName;
    }

    /**
     * Get the channel name
     *
     * @return String the channel name
     */
    public String getChannelName() {
        return this.channelName;
    }

    /**
     * Set the channel name
     *
     * @param channelName the channel name
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
