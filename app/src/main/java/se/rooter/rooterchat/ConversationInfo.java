package se.rooter.rooterchat;


import android.support.annotation.NonNull;

/**
 * Class representing private conversations
 */
public class ConversationInfo implements Comparable<ConversationInfo> {
    private String participantOne;
    private String participantTwo;
    private String receiver;
    private String latestMsg;
    private String id;
    private String otherNickname;
    private String otherImgPath;
    private String latestPoster;
    private String latestPostDate;
    private boolean isViewed;

    /**
     * Default empty constructor
     *
     * @constructor
     */
    public ConversationInfo() {

    }

    /**
     * Create a new ConversationInfo with both participants
     * @param participantOne participant one
     * @param participantTwo participant two
     */
    public ConversationInfo(String participantOne, String participantTwo) {
        this.participantOne = participantOne;
        this.participantTwo = participantTwo;
    }

    /**
     * Create new ConversationInfo with receiver
     * @param receiver the receiver
     */
    public ConversationInfo(String receiver) {
        this.receiver = receiver;
    }

    /**
     * Set if viewed or not
     * @param isViewed boolean is viewed
     */
    public void setIsViewed(boolean isViewed) {
        this.isViewed = isViewed;
    }

    /**
     * Get viewed boolean
     * @return boolean viewed
     */
    public boolean getIsViewed() {
        return this.isViewed;
    }

    /**
     * Set latest post datetime
     * @param latestPostDate latest post datetime
     */
    public void setLatestPostDate(String latestPostDate) {
        this.latestPostDate = latestPostDate;
    }

    /**
     * Get latest post datetime
     * @return String latest post datetime
     */
    public String getLatestPostDate() {
        return this.latestPostDate;
    }

    /**
     * Set latest poster
     * @param latestPoster the latest poster
     */
    public void setLatestPoster(String latestPoster) {
        this.latestPoster = latestPoster;
    }

    /**
     * Get latest poster
     * @return String the latest poster
     */
    public String getLatestPoster() {
        return this.latestPoster;
    }

    /**
     * Get other nickname
     * @return return other nickname
     */
    public String getOtherNickname() {
        return this.otherNickname;
    }

    /**
     * Set other nickname
     * @param otherNickname the other nickname
     */
    public void setOtherNickname(String otherNickname) {
        this.otherNickname = otherNickname;
    }

    /**
     * Get other imgpath
     * @return String other imgpath
     */
    public String getOtherImgPath() {
        return this.otherImgPath;
    }

    /**
     * Set other imgpath
     * @param otherImgPath the other imgpath
     */
    public void setOtherImgPath(String otherImgPath) {
        this.otherImgPath = otherImgPath;
    }

    /**
     * Get id
     * @return String id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Set id
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get latest message
     * @return String the latest message
     */
    public String getLatestMsg() {
        return this.latestMsg;
    }

    /**
     * Set the latest message
     * @param latestMsg the latest message
     */
    public void setLatestMsg(String latestMsg) {
        this.latestMsg = latestMsg;
    }

    /**
     * Set receiver
     * @param receiver the receiver
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * Get the receiver
     * @return String the receiver
     */
    public String getReceiver() {
        return this.receiver;
    }

    /**
     * Set participant one
     * @param participantOne participant one
     */
    public void setParticipantOne(String participantOne) {
        this.participantOne = participantOne;
    }

    /**
     * Set participant two
     * @param participantTwo participant two
     */
    public void setParticipantTwo(String participantTwo) {
        this.participantTwo = participantTwo;
    }

    /**
     * Get participant one
     * @return String participant one
     */
    public String getParticipantOne() {
        return this.participantOne;
    }

    /**
     * Get participant two
     * @return String participant two
     */
    public String getParticipantTwo() {
        return this.participantTwo;
    }

    /**
     * Overridden equals method based on conversation id
     *
     * @param obj the other obj
     * @return boolean true if equals, false if not
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ConversationInfo)) {
            return false;
        }

        ConversationInfo convoInfo = (ConversationInfo) obj;

        return this.id.equals(convoInfo.id);


    }

    /**
     * Overridden hashCode method based on conversation id
     *
     * @return int the hashcode
     */
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    /**
     * Overridden compareTo method based on latest post datetime
     *
     * @param o the other object
     * @return int 0 if equal, negative value if other object is greater, positive value if this object is greater
     */
    @Override
    public int compareTo(@NonNull ConversationInfo o) {
        return this.latestPostDate.compareTo(o.latestPostDate);
    }


}
