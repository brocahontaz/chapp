package se.rooter.rooterchat;


public class ConversationInfo {
    private String participantOne;
    private String participantTwo;
    private String receiver;

    public ConversationInfo() {

    }

    public ConversationInfo(String participantOne, String participantTwo) {
        this.participantOne = participantOne;
        this.participantTwo = participantTwo;
    }

    public ConversationInfo(String receiver) {
        this.receiver = receiver;
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


}
