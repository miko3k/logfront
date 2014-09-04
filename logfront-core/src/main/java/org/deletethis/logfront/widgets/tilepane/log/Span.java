package org.deletethis.logfront.widgets.tilepane.log;

public class Span {

    private final int firstCharacter;
    private final int length;
    private final Clickable clickable;

    public Span(int firstCharacter, int length, Clickable clickable) {
        this.firstCharacter = firstCharacter;
        this.length = length;
        this.clickable = clickable;
    }

    public int getFirstCharacter() {
        return firstCharacter;
    }

    public int getLength() {
        return length;
    }

    public Clickable getClickable() {
        return clickable;
    }

    public String toString() {
        return super.toString() + "(first: " + firstCharacter
                + ", len: " + length + ", clickable: " + clickable + ")";
    }
}
