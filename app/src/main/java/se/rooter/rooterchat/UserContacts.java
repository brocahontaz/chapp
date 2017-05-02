package se.rooter.rooterchat;

import java.util.ArrayList;

/**
 * Created by Rooter on 2017-05-02.
 */

public class UserContacts {

    private ArrayList<String> contacts;

    public UserContacts() {
        this.contacts = new ArrayList<String>();
    }

    public void addContact(String id) {
        this.contacts.add(id);
    }

    public ArrayList<String> getContacts() {
        return this.contacts;
    }
}
