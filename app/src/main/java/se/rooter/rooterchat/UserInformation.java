package se.rooter.rooterchat;

/**
 * Created by Johan Andersson on 2017-04-09.
 */

public class UserInformation {

    public String nickname;

    public UserInformation() {

    }

    public UserInformation(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nick) {
        this.nickname = nick;
    }

}
