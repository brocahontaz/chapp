package se.rooter.chapp;

import java.util.Comparator;

/**
 * Comparator for conversations
 */
public class ConversationComparator implements Comparator<ConversationInfo> {
    @Override
    public int compare(ConversationInfo o1, ConversationInfo o2) {
        return o2.compareTo(o1);
    }
}
