package se.rooter.rooterchat;

import java.util.Comparator;

public class ConversationComparator implements Comparator<ConversationInfo> {
    @Override
    public int compare(ConversationInfo o1, ConversationInfo o2) {
        return o2.compareTo(o1);
    }
}
