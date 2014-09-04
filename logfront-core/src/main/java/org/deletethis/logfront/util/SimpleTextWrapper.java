package org.deletethis.logfront.util;

import java.util.Iterator;

public class SimpleTextWrapper implements Iterator<WrappedLine> {

    private final CharSequence text;
    private int maxWidth;
    private final int maxWidthNext;
    private int nextBegin = 0;

    private boolean isPossibleBreakBefore(int idx) {
        if(idx == 0) {
            return false;
        }

        char c = text.charAt(idx);
        if(c == '(') {
            return true;
        }

        char prev = ((idx == 0) ? 0 : text.charAt(idx - 1));

        if(prev != 0 && Character.isWhitespace(prev)
                && !Character.isWhitespace(c)) {
            // keep spaces at first line
            return true;
        }

        if(prev == ')') {
            return true;
        }

        if(prev == '.') {
            return true;
        }

        if(prev == ',') {
            return true;
        }

        return false;
    }

    public SimpleTextWrapper(CharSequence cs, int firstLineLength, int otherLineLength) {
        this.text = cs;
        this.maxWidth = firstLineLength;
        this.maxWidthNext = otherLineLength;
    }

    @Override
    public boolean hasNext() {
        return nextBegin < text.length();
    }

    @Override
    public WrappedLine next() {
        if(nextBegin >= text.length()) {
            throw new IllegalStateException("already past end!");
        }

        int begin = nextBegin;
        int len;
        int possibleLen = -1;
        for(len = 0; len <= maxWidth; ++len) {
            int idx = len + begin;
            if(idx >= text.length()) {
                nextBegin = idx;
                break;
            }

            char c = text.charAt(idx);
            if(c == '\n') {
                nextBegin = idx + 1;
                break;
            }

            if(isPossibleBreakBefore(idx)) {
                possibleLen = len;
            }
        }

        if(nextBegin == begin) {
            // no forced line break
            if(len > maxWidth) {
                if(possibleLen > 0) {
                    len = possibleLen;
                } else {
                    len = maxWidth;
                }
            } else {
                len = maxWidth;
            }

            nextBegin = begin + len;
        }
        maxWidth = maxWidthNext;

        return new WrappedLine(begin, len);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
