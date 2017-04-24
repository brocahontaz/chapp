package se.rooter.rooterchat;

import java.util.Random;

public class ChatMessage {

    public String body, sender, receiver, senderName;
    public String Date, Time;
    public String msgid;
    public boolean isMine;

    public ChatMessage(String sender, String receiver, String messageString, String id, boolean isMINE) {
        body = messageString;
        isMine = isMINE;
        this.sender = sender;
        msgid = id;
        this.receiver = receiver;
        senderName = sender;
    }

    public void setMsgID() {
        msgid += "-" + String.format("%02d", new Random().nextInt(100));
    }

}
