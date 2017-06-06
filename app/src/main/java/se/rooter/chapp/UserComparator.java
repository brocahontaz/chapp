package se.rooter.chapp;

import java.util.Comparator;

/**
 * Comparator class for users
 */

public class UserComparator implements Comparator<UserInformation> {
    @Override
    public int compare(UserInformation o1, UserInformation o2) {
        return o1.compareTo(o2);
    }
}
