package org.deletethis.logfront;

public class SearchCriteria implements SearchNeedle {

    final private String text;
    final private boolean caseSensitive;

    public SearchCriteria(String text, boolean caseSensitive) {
        this.text = text;
        this.caseSensitive = caseSensitive;
    }

    public String getText() {
        return text;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    private boolean match(CharSequence data, int pos) {
        int len = text.length();
        boolean cs = caseSensitive;

        for(int i = 0; i < len; ++i) {
            char c1 = data.charAt(pos + i);
            char c2 = text.charAt(i);

            if(cs) {
                if(c1 != c2) {
                    return false;
                }
            } else {
                if(Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public SearchResult find(CharSequence data, int start, boolean backwards) {
        if(backwards) {
            for(; start >= 0; --start) {
                if(match(data, start)) {
                    return new SearchResult(start, text.length());
                }
            }
        } else {
            int len = data.length() - text.length();
            for(; start <= len; ++start) {
                if(match(data, start)) {
                    return new SearchResult(start, text.length());
                }
            }
        }
        return null;
    }

}
