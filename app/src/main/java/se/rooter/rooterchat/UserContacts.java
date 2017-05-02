package se.rooter.rooterchat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rooter on 2017-05-02.
 */

public class UserContacts {

    private HashMap<String, Object> contacts;

    public UserContacts() {
        this.contacts = new HashMap<String, Object>();
    }

    public void setContact(String id) {
        this.contacts.put(id, true);
    }

    public HashMap<String, Object> getContacts() {
        return this.contacts;
    }

    public void setContacts(HashMap<String, Object> contacts) {
        this.contacts = contacts;
    }
}
