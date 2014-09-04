package org.deletethis.logfront;

public interface SearchNeedle {

    public SearchResult find(CharSequence data, int start, boolean backwards);
}
