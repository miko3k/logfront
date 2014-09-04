package org.deletethis.logfront;

public interface FilterStorage {

    public enum Slot {

        SHOW_ONLY,
        DO_NOT_SHOW
    }

    public interface MatcherItem {

        public boolean isEnabled();

        public void setEnabled(boolean enabled);
        // remove is here, not on FilterStorage, because
        // we don't want to require Matcher to be unique

        public void remove();

        public Matcher getMatcher();

        public Slot getSlot();
    }

    public interface FilterStorageListener {

        public enum FilterStorageEvent {

            MATCHER_ADDED,
            MATCHER_REMOVED,
            MATCHER_TOGGLED
        }

        public void onFilterStorageEvent(FilterStorageEvent e, FilterStorage filterStorage, MatcherItem item);
    }

    public void add(Slot slot, Matcher matcher, boolean enabled);

    public Iterable<MatcherItem> getSlot(Slot slot);

    public void addFilterStorageListener(FilterStorageListener listener);
}
