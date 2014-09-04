package org.deletethis.logfront.widgets.tilepane.log.data;

import java.util.ArrayList;
import java.util.List;

import org.deletethis.logfront.SearchResult;

public class SearchMatchList {

    private List<SearchResult> matches = new ArrayList<>();
    private List<Integer> matchIdList = new ArrayList<>();

    public void addMatch(SearchResult result) {
        int length = result.getLength();
        int offset = result.getFirstCharacter();
        int matchId = matches.size();

        matches.add(result);

        if(length <= 0) {
            return;
        }

        int first = offset;
        int last = first + length - 1;

        while(last >= matchIdList.size()) {
            matchIdList.add(null);
        }
        for(int i = first; i <= last; ++i) {
            matchIdList.set(i, matchId);
        }
    }

    public int getCharacterCount() {
        return matchIdList.size();
    }

    public int getMatchCount() {
        return matches.size();
    }

    public SearchResult getMatchById(int id) {
        return matches.get(id);
    }

    public Integer getMatchIdAtOffset(int offset) {
        if(offset < 0 || offset >= matchIdList.size()) {
            return null;
        } else {
            return matchIdList.get(offset);
        }
    }
}
