package se.rooter.rooterchat;

/**
 * Class representing messages in channels
 */

public class ChannelMessage {

    private String msgID;
    private String senderID;
    private String senderName;
    private String message;
    private String chatChannel;
    private String imgPath;

    /**
     * Default empty constructor
     *
     * @constructor
     */
    public ChannelMessage() {

    }

    /**
     * Create a new ChannelMessage with senders id, message and chat channel
     *
     * @param senderID    the senders id
     * @param message     the message
     * @param chatChannel the chat channel
     * @constructor
     */
    public ChannelMessage(String senderID, String message, String chatChannel) {
        this.senderID = senderID;
        this.message = message;
        this.chatChannel = chatChannel;
    }

    /**
     * Create a new ChannelMessage with id,  senders id, message and chat channel
     *
     * @param msgID       the id
     * @param senderID    the senders id
     * @param message     the message
     * @param chatChannel the chat channel
     * @constructor
     */
    public ChannelMessage(String msgID, String senderID, String message, String chatChannel) {
        this.msgID = msgID;
        this.senderID = senderID;
        this.message = message;
        this.chatChannel = chatChannel;
    }

    /**
     * Set the imgpath
     *
     * @param imgPath the imgpath
     */
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    /**
     * Get the imgpath
     *
     * @return String the imgpath
     */
    public String getImgPath() {
        return this.imgPath;
    }

    /**
     * Set the sender name
     *
     * @param senderName the sender name
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * Get the sender name
     *
     * @return String the sender name
     */
    public String getSenderName() {
        return this.senderName;
    }

    /**
     * Set the message id
     *
     * @param msgID the message id
     */
    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    /**
     * Get the message id
     *
     * @return String the message id
     */
    public String getMsgID() {
        return this.msgID;
    }

    /**
     * Set the sender id
     *
     * @param senderID the sender id
     */
    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    /**
     * Get the sender id
     *
     * @return String the sender id
     */
    public String getSenderID() {
        return this.senderID;
    }

    /**
     * Set the message
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Return the message
     *
     * @return String the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Set the channel
     *
     * @param chatChannel the channel
     */
    public void setChannel(String chatChannel) {
        this.chatChannel = chatChannel;
    }

    /**
     * Get the channel
     *
     * @return String the channel
     */
    public String getChannel() {
        return this.chatChannel;
    }

    /**
     * Overridden equals method based on message id
     *
     * @param obj the other obj
     * @return boolean true if equals, false if not
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ChannelMessage)) {
            return false;
        }

        ChannelMessage chmsg = (ChannelMessage) obj;

        return this.msgID.equals(chmsg.msgID);


    }

    /**
     * Overridden hashCode method based on message id
     *
     * @return int the hashcode
     */
    @Override
    public int hashCode() {
        return this.msgID.hashCode();
    }
}
