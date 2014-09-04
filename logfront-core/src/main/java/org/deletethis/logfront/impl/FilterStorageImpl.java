package org.deletethis.logfront.impl;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.deletethis.logfront.FilterStorage;
import org.deletethis.logfront.Matcher;
import org.deletethis.logfront.FilterStorage.FilterStorageListener.FilterStorageEvent;
import org.deletethis.logfront.util.MetaListener;

public class FilterStorageImpl implements FilterStorage {

    private class Item implements MatcherItem {

        private final Matcher matcher;
        private final Slot slot;
        private boolean enabled;

        private Item(Matcher matcher, Slot slot, boolean enabled) {
            this.matcher = matcher;
            this.enabled = enabled;
            this.slot = slot;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;

            metaListener.getMetaListener().onFilterStorageEvent(
                    FilterStorageEvent.MATCHER_TOGGLED, FilterStorageImpl.this, this);
        }

        @Override
        public void remove() {
            map.get(slot).remove(this);
            metaListener.getMetaListener().onFilterStorageEvent(
                    FilterStorageEvent.MATCHER_REMOVED, FilterStorageImpl.this, this);
        }

        @Override
        public Matcher getMatcher() {
            return matcher;
        }

        @Override
        public Slot getSlot() {
            return slot;
        }
    }

    private final Map<Slot, List<MatcherItem>> map = new EnumMap<>(Slot.class);
    private final MetaListener<FilterStorageListener> metaListener = new MetaListener<>(FilterStorageListener.class);

    public FilterStorageImpl() {
        for(Slot s : Slot.values()) {
            map.put(s, new ArrayList<MatcherItem>());
        }
    }

    @Override
    public void add(Slot slot, Matcher matcher, boolean enabled) {
        if(matcher == null)
            throw new IllegalArgumentException("cannot add null matcher");
        
        Item item = new Item(matcher, slot, enabled);
        map.get(slot).add(item);
        metaListener.getMetaListener().onFilterStorageEvent(
                FilterStorageEvent.MATCHER_ADDED, this, item);
    }

    @Override
    public Iterable<MatcherItem> getSlot(Slot slot) {
        return map.get(slot);
    }

    @Override
    public void addFilterStorageListener(FilterStorageListener listener) {
        metaListener.addListener(listener);
    }
}
