package se.rooter.rooterchat;

/**
 * Class representing messages in private conversations
 */
public class ChatMessage {

    private String senderID;
    private String message;
    private String conversationID;
    private String msgID;
    private String imgPath;
    private String senderName;
    private String postDate;
    private boolean isViewed;

    /**
     * Default empty constructor
     *
     * @constructor
     */
    public ChatMessage() {

    }

    /**
     * Create a new ChatMessage with sender id, message and conversation id
     *
     * @param senderID       the sender id
     * @param message        the message
     * @param conversationID the conversation id
     */
    public ChatMessage(String senderID, String message, String conversationID) {
        this.senderID = senderID;
        this.message = message;
        this.conversationID = conversationID;
    }

    /**
     * Create a new ChatMessage with sender id, message, conversation id and date
     *
     * @param senderID       the sender id
     * @param message        the message
     * @param conversationID the conversation id
     * @param postDate       the date
     */
    public ChatMessage(String senderID, String message, String conversationID, String postDate) {
        this.senderID = senderID;
        this.message = message;
        this.conversationID = conversationID;
        this.postDate = postDate;
    }


    /**
     * Check if message is viewed
     * @return boolean true if viewed, false otherwise
     */
    public boolean getIsViewed() {
        return this.isViewed;
    }

    /**
     * Set if message is viewed
     * @param isViewed the boolean for viewed
     */
    public void setIsViewed(boolean isViewed) {
        this.isViewed = isViewed;
    }

    /**
     * Set the datetime
     * @param postDate the datetime
     */
    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    /**
     * Get the datetime
     * @return String the datetime
     */
    public String getPostDate() {
        return this.postDate;
    }

    /**
     * Set the message id
     * @param msgID the message id
     */
    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    /**
     * Get the message id
     * @return String the message id
     */
    public String getMsgID() {
        return this.msgID;
    }

    /**
     * Set the imgpath
     * @param imgPath the imgpath
     */
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    /**
     * Get the imgpath
     * @return String the imgpath
     */
    public String getImgPath() {
        return this.imgPath;
    }

    /**
     * Set the sender name
     * @param senderName the sender name
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * Get the sender name
     * @return String the sender name
     */
    public String getSenderName() {
        return this.senderName;
    }

    /**
     * Set sender id
     * @param senderID the sender id
     */
    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    /**
     * Get the sender id
     * @return String the sender id
     */
    public String getSenderID() {
        return this.senderID;
    }

    /**
     * Set the message
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the message
     * @return String the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Set the conversation id
     * @param conversationID the conversation id
     */
    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    /**
     * Get the conversation id
     * @return String the conversation id
     */
    public String getConversationID() {
        return this.conversationID;
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
        if (!(obj instanceof ChatMessage)) {
            return false;
        }

        ChatMessage chmsg = (ChatMessage) obj;

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
