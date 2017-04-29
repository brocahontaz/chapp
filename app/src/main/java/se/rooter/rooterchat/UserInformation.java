package se.rooter.rooterchat;

/**
 * Created by Johan Andersson on 2017-04-09.
 */

public class UserInformation {

    public String nickname;
    public String imgPath;
    public String email;

    public UserInformation() {

    }

    public UserInformation(String nickname) {
        this.nickname = nickname;
    }

    public UserInformation(String nickname, String imgPath) {
        this.nickname = nickname;
        this.imgPath = imgPath;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nick) {
        this.nickname = nick;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

}
