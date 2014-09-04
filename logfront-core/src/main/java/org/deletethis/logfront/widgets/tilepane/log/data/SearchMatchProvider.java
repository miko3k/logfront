package org.deletethis.logfront.widgets.tilepane.log.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deletethis.logfront.SearchNeedle;
import org.deletethis.logfront.SearchResult;

public class SearchMatchProvider<A, B extends CharSequence, C> implements VersionChangeListener {

    private long firstId;
    private List<SearchMatchList> data;
    private SearchNeedle needle;
    private final FilteredText<A, B, C> text;

    private SearchMatchList createMatches(CharSequence ft) {
        SearchMatchList result = new SearchMatchList();
        if(needle == null) {
            return result;
        }

        SearchResult res = null;
        while(true) {
            res = needle.find(
                    ft,
                    (res == null) ? 0 : res.getFirstCharacter() + res.getLength(),
                    false);

            if(res == null) {
                break;
            }

            result.addMatch(res);
        }
        return result;
    }

    @Override
    public void textVersionChanged() {
        this.data = null;
    }

    public SearchMatchProvider(FilteredText<A, B, C> text) {
        this.text = text;
        this.data = null;
    }

    public void setNeedle(SearchNeedle needle) {
        this.needle = needle;
        this.data = null;
    }

    public SearchMatchList getMatches(long id) {
        if(id < firstId || id >= text.getEndId()) {
            return null;
        }

        if(data == null) {
            firstId = text.getBeginId();
            data = new ArrayList<>(32);
        }

        int index = (int) (id - firstId);
        if(index >= data.size()) {
            int missing = index - data.size() + 1;
            data.addAll(Collections.nCopies(missing, (SearchMatchList) null));
        }

        SearchMatchList result = data.get(index);
        if(result == null) {
            CharSequence ft = text.getChunkById(id);
            result = createMatches(ft);
            data.set(index, result);
        }
        return result;
    }

}
