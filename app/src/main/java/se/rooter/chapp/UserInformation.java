package se.rooter.chapp;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class representing users and their information
 */

public class UserInformation implements Comparable<UserInformation> {

    private String id;
    public String nickname;
    public String imgPath;
    public String email;
    public ArrayList<String> contacts;
    public HashMap<String, Object> contactsMap;
    public HashMap<String, Object> conversationsMap;

    /**
     * Default empty constructor
     *
     * @constructor
     */
    public UserInformation() {

    }

    /**
     * Create new UserInformation with nickname
     *
     * @param nickname the nickname
     */
    public UserInformation(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Create a new UserInformation with nickname and imgpath
     *
     * @param nickname the nickname
     * @param imgPath  the imgpath
     */
    public UserInformation(String nickname, String imgPath) {
        this.nickname = nickname;
        this.imgPath = imgPath;
    }

    /**
     * Set the ID
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the ID
     *
     * @return String the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get the conversations
     *
     * @return HashMap the conversations
     */
    public HashMap<String, Object> getConversations() {
        return this.conversationsMap;
    }

    /**
     * Set the conversations
     *
     * @param conversationsMap the conversations
     */
    public void setConversations(HashMap<String, Object> conversationsMap) {
        this.conversationsMap = conversationsMap;
    }

    /**
     * Get the contacts
     *
     * @return HashMap the contacts
     */
    public HashMap<String, Object> getContacts() {
        return this.contactsMap;
    }

    /**
     * Set the contacts
     *
     * @param contactsMap the contacts
     */
    public void setContacts(HashMap<String, Object> contactsMap) {
        this.contactsMap = contactsMap;
    }

    /**
     * Get the email
     *
     * @return String the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Set the email
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the nickname
     *
     * @return String the nickname
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Set the nickname
     *
     * @param nick the nickname
     */
    public void setNickname(String nick) {
        this.nickname = nick;
    }

    /**
     * Get the imgpath
     *
     * @return String imgpath
     */
    public String getImgPath() {
        return this.imgPath;
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
     * Overridden equals method based on email
     *
     * @param obj the other obj
     * @return boolean true if equals, false if not
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UserInformation)) {
            return false;
        }

        UserInformation ui = (UserInformation) obj;

        return this.email.equals(ui.email);

    }

    /**
     * Overridden hashCode method based on email
     *
     * @return int the hashcode
     */
    @Override
    public int hashCode() {
        return this.email.hashCode();
    }

    /**
     * Overridden compareTo method based on nickname (lower case)
     *
     * @param o the other object
     * @return int 0 if equal, negative value if other object is greater, positive value if this object is greater
     */
    @Override
    public int compareTo(@NonNull UserInformation o) {
        return this.nickname.toLowerCase().compareTo(o.nickname.toLowerCase());
    }
}
