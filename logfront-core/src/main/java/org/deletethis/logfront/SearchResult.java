package org.deletethis.logfront;

public class SearchResult {

    private final int firstCharacter;
    private final int length;

    public SearchResult(int firstCharacter, int length) {
        this.firstCharacter = firstCharacter;
        this.length = length;
    }

    public int getFirstCharacter() {
        return firstCharacter;
    }

    public int getLength() {
        return length;
    }
}
