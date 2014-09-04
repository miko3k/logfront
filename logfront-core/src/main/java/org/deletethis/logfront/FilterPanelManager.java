package org.deletethis.logfront;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import org.deletethis.logfront.FilterStorage.FilterStorageListener;
import org.deletethis.logfront.FilterStorage.MatcherItem;
import org.deletethis.logfront.FilterStorage.Slot;
import org.deletethis.logfront.colors.FilterEntryStyle;
import org.deletethis.logfront.colors.MatcherStyle;
import org.deletethis.logfront.widgets.FilterEntry;
import org.deletethis.logfront.widgets.FilterEntryListener;
import org.deletethis.logfront.widgets.FilterPanel;

public class FilterPanelManager implements FilterStorageListener {

    private final FilterPanel filterPanel;
    private final FilterEntryStyle filterEntryStyle;
    private final Map<MatcherItem, FilterEntry> map;
    private final MatcherStyle matcherStyle;

    public FilterPanelManager(FilterPanel filterPanel,
            FilterEntryStyle filterEntryStyle, MatcherStyle matcherStyle) {
        this.filterPanel = filterPanel;
        this.filterEntryStyle = filterEntryStyle;
        this.matcherStyle = matcherStyle;
        this.map = new HashMap<>();
    }

    public void updateFilterPanel(FilterStorage filterStorage, final Slot slot) {
        JComponent comp;
        switch(slot) {
            case DO_NOT_SHOW:
                comp = filterPanel.getRow(FilterPanel.RowId.DO_NOT_SHOW);
                break;
            case SHOW_ONLY:
                comp = filterPanel.getRow(FilterPanel.RowId.SHOW_ONLY);
                break;
            default:
                throw new IllegalArgumentException("bad slot: " + slot);
        }

        comp.removeAll();
        for(final MatcherItem item : filterStorage.getSlot(slot)) {
            FilterEntry entry = map.get(item);
            if(entry == null) {
                entry = new FilterEntry(item.getMatcher().getLongDescription(), filterEntryStyle);
                entry.addFilterEntryListener(new FilterEntryListener() {

                    @Override
                    public void onMainClick() {
                        item.setEnabled(!item.isEnabled());
                    }

                    @Override
                    public void onCloseClick() {
                        item.remove();
                    }
                });

                entry.setBackground(item.getMatcher().getButtonBackgroundColor(matcherStyle));
                //entry.setActive(false);
                map.put(item, entry);
            }
            entry.setActive(item.isEnabled());
            comp.add(entry);
        }
    }

    @Override
    public void onFilterStorageEvent(FilterStorageEvent e,
            FilterStorage filterStorage, MatcherItem item) {
        updateFilterPanel(filterStorage, item.getSlot());
    }
}
