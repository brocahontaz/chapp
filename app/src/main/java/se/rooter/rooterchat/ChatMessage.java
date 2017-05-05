package se.rooter.rooterchat;

import java.util.Date;
import java.util.Random;

public class ChatMessage {

    private String senderID;
    private String message;
    private String conversationID;
    private String msgID;
    private String imgPath;
    private String senderName;
    private String postDate;
    private boolean isViewed;

    public ChatMessage() {

    }

    public ChatMessage(String senderID, String message, String conversationID) {
        this.senderID = senderID;
        this.message = message;
        this.conversationID = conversationID;
    }

    public ChatMessage(String senderID, String message, String conversationID, String postDate) {
        this.senderID = senderID;
        this.message = message;
        this.conversationID = conversationID;
        this.postDate = postDate;
    }


    public boolean getIsViewed() {
        return this.isViewed;
    }

    public void setIsViewed(boolean isViewed) {
        this.isViewed = isViewed;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostDate() {
        return this.postDate;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getMsgID() {
        return this.msgID;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public String getSenderID() {
        return this.senderID;
    }

    public String getMessage() {
        return this.message;
    }

    public String getConversationID() {
        return this.conversationID;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ChatMessage)) {
            return false;
        }

        ChatMessage chmsg = (ChatMessage) obj;

        return this.msgID.equals(chmsg.msgID);


    }

    @Override
    public int hashCode() {
        return this.msgID.hashCode();
    }

}
