package org.deletethis.logfront;

import org.deletethis.logfront.FilterStorage.MatcherItem;
import org.deletethis.logfront.FilterStorage.Slot;
import org.deletethis.logfront.message.LogMessage;

public class FilterStorageFilter implements Filter {

    private final FilterStorage filterStorage;

    public FilterStorageFilter(FilterStorage filterStorage) {
        this.filterStorage = filterStorage;
    }

    @Override
    public boolean passes(LogMessage f) {
        boolean shown = false;
        boolean hasShowOnlyMatcher = false;

        for(MatcherItem it : filterStorage.getSlot(Slot.SHOW_ONLY)) {
            if(!it.isEnabled()) {
                continue;
            }

            hasShowOnlyMatcher = true;
            if(it.getMatcher().matches(f)) {
                shown = true;
                break;
            }
        }
        if(hasShowOnlyMatcher && !shown) {
            return false;
        }

        for(MatcherItem it : filterStorage.getSlot(Slot.DO_NOT_SHOW)) {
            if(!it.isEnabled()) {
                continue;
            }

            if(it.getMatcher().matches(f)) {
                return false;
            }
        }
        return true;
    }

}
