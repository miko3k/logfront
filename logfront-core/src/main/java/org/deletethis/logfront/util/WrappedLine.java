package org.deletethis.logfront.util;

public class WrappedLine {

    final private int begin, length;

    public WrappedLine(int begin, int length) {
        this.begin = begin;
        this.length = length;
    }

    public int getBegin() {
        return begin;
    }

    public int getLength() {
        return length;
    }
}
