package org.deletethis.logfront.widgets.tilepane.log.data;

import java.util.Iterator;

import org.deletethis.logfront.SearchNeedle;
import org.deletethis.logfront.SearchResult;
import org.deletethis.logfront.widgets.tilepane.log.LogPosition;

public class Searcher<A, B extends CharSequence, C> implements VersionChangeListener {

    public static enum MatchType {

        MATCHED,
        CURRENT_MATCH
    }

    private SearchMatchProvider<A, B, C> searchMatchProvider;
    private FilteredText<A, B, C> text;

    public static final class Match {

        final private long chunkId;
        final private int matchId;

        public Match(long chunkId, int matchId) {
            this.chunkId = chunkId;
            this.matchId = matchId;
        }

        public long getChunkId() {
            return chunkId;
        }

        public int getMatchId() {
            return matchId;
        }
    }

    public Searcher(FilteredText<A, B, C> text) {
        this.searchMatchProvider = new SearchMatchProvider<A, B, C>(text);
        this.text = text;
    }

    private Iterator<Long> chunkIdIterator(long startChunkId, boolean forward) {
        return new RepeatFirstIterator<>(new NumberIterator(forward,
                startChunkId, startChunkId,
                text.getBeginId(), text.getEndId() - 1));
    }

    public Match findNextMatch(Match currentMatch, boolean forward) {
        boolean first = true;
        Iterator<Long> it = chunkIdIterator(currentMatch.getChunkId(), forward);

        while(it.hasNext()) {
            long chunkId = it.next();

            SearchMatchList matchList = searchMatchProvider.getMatches(chunkId);
            int matchCount = matchList.getMatchCount();

            if(forward) {
                if(first) {
                    if(currentMatch.getMatchId() < matchCount - 1) {
                        return new Match(chunkId, currentMatch.getMatchId() + 1);
                    }
                } else {
                    if(matchCount > 0) {
                        return new Match(chunkId, 0);
                    }
                }
            } else {
                if(first) {
                    if(currentMatch.getMatchId() > 0) {
                        return new Match(chunkId, currentMatch.getMatchId() - 1);
                    }
                } else {
                    if(matchCount > 0) {
                        return new Match(chunkId, matchCount - 1);
                    }
                }
            }

            first = false;
        }
        return null;
    }

    private SearchResult getSearchResultForMatch(Match m) {
        SearchMatchList matchList = searchMatchProvider.getMatches(m.getChunkId());
        if(matchList == null) {
            return null;
        }

        int cnt = matchList.getMatchCount();
        if(m.getMatchId() >= cnt) {
            return null;
        }

        return matchList.getMatchById(m.getMatchId());
    }

    public int getMatchLength(Match match) {
        SearchResult r = getSearchResultForMatch(match);
        if(r != null) {
            return r.getLength();
        } else {
            return -1;
        }
    }

    public Integer getMatchOffset(Match m) {
        SearchResult r = getSearchResultForMatch(m);
        if(r != null) {
            return r.getFirstCharacter();
        } else {
            return null;
        }
    }

    public Match findMatchAfter(LogPosition pos) {
        int offset = pos.getOffset();
        Iterator<Long> it = chunkIdIterator(pos.getId(), true);
        while(it.hasNext()) {
            long chunkId = it.next();

            SearchMatchList matchList = searchMatchProvider.getMatches(chunkId);
            if(matchList == null) {
                continue;
            }

            int cnt = matchList.getCharacterCount();

            for(int i = offset; i < cnt; ++i) {
                Integer m = matchList.getMatchIdAtOffset(i);
                if(m != null) {
                    return new Match(chunkId, m);
                }
            }
            offset = 0;
        }
        return null;
    }

    public void setNeedle(SearchNeedle needle) {
        searchMatchProvider.setNeedle(needle);
    }

    @Override
    public void textVersionChanged() {
        searchMatchProvider.textVersionChanged();
    }

    public MatchType getMatchType(long chunkId, int offset, Match currentMatch) {
        SearchMatchList matches = searchMatchProvider.getMatches(chunkId);

        if(matches == null) {
            return null;
        }

        Integer matchId = matches.getMatchIdAtOffset(offset);

        if(matchId == null) {
            return null;
        }

        if(currentMatch == null) {
            return MatchType.MATCHED;
        }

        if(chunkId == currentMatch.getChunkId() && matchId == currentMatch.getMatchId()) {
            return MatchType.CURRENT_MATCH;
        } else {
            return MatchType.MATCHED;
        }
    }
}
