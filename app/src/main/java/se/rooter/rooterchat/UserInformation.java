package se.rooter.rooterchat;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Johan Andersson on 2017-04-09.
 */

public class UserInformation implements Comparable<UserInformation> {

    public String nickname;
    public String imgPath;
    public String email;
    public ArrayList<String> contacts;

    public UserInformation() {

    }

    public UserInformation(String nickname) {
        this.nickname = nickname;
    }

    public UserInformation(String nickname, String imgPath) {
        this.nickname = nickname;
        this.imgPath = imgPath;
    }

    public ArrayList<String> getContacts() {
        return this.contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
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

    @Override
    public boolean equals(Object obj) {

        if(obj == this) {
            return true;
        }
        if(!(obj instanceof UserInformation)) {
            return false;
        }

        UserInformation ui = (UserInformation) obj;

        return this.email.equals(ui.email);


    }

    @Override
    public int hashCode() {
        return this.email.hashCode();
    }

    @Override
    public int compareTo(@NonNull UserInformation o) {
        return this.nickname.toLowerCase().compareTo(o.nickname.toLowerCase());
    }
}
