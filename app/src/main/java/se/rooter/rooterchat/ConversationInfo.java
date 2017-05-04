package se.rooter.rooterchat;


public class ConversationInfo {
    private String participantOne;
    private String participantTwo;
    private String receiver;
    private String latestMsg;
    private String id;
    private String otherNickname;
    private String otherImgPath;
    private String latestPoster;

    public ConversationInfo() {

    }

    public ConversationInfo(String participantOne, String participantTwo) {
        this.participantOne = participantOne;
        this.participantTwo = participantTwo;
    }

    public void setLatestPoster(String latestPoster) {
        this.latestPoster = latestPoster;
    }

    public String getLatestPoster() {
        return this.latestPoster;
    }

    public String getOtherNickname() {
        return this.otherNickname;
    }

    public void setOtherNickname(String otherNickname) {
        this.otherNickname = otherNickname;
    }

    public String getOtherImgPath() {
        return this.otherImgPath;
    }

    public void setOtherImgPath(String otherImgPath) {
        this.otherImgPath = otherImgPath;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConversationInfo(String receiver) {
        this.receiver = receiver;
    }

    public String getLatestMsg() {
        return this.latestMsg;
    }

    public void setLatestMsg(String latestMsg) {
        this.latestMsg = latestMsg;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setParticipantOne(String participantOne) {
        this.participantOne = participantOne;
    }

    public void setParticipantTwo(String participantTwo) {
        this.participantTwo = participantTwo;
    }

    public String getParticipantOne() {
        return this.participantOne;
    }

    public String getParticipantTwo() {
        return this.participantTwo;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ChannelMessage)) {
            return false;
        }

        ConversationInfo convoInfo = (ConversationInfo) obj;

        return this.id.equals(convoInfo.id);


    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }


}
