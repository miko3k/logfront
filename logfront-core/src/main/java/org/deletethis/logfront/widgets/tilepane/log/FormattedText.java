package org.deletethis.logfront.widgets.tilepane.log;

import org.deletethis.logfront.widgets.tilepane.CharData;

public class FormattedText implements CharSequence {

    private final CharData[] characters;
    private final Span[] spans;

    public FormattedText(CharData[] characters, Span[] spans) {
        this.characters = characters;
        this.spans = spans;
    }

    public CharData[] getCharacters() {
        return characters;
    }

    public Span[] getSpans() {
        return spans;
    }

    @Override
    public char charAt(int n) {
        return characters[n].getCharacter();
    }

    @Override
    public int length() {
        return characters.length;
    }

    @Override
    public String toString() {
        char[] chars = new char[ characters.length ];
        for(int i = 0; i < characters.length; ++i) {
            chars[i] = characters[i].getCharacter();
        }

        return new String(chars);
    }

    @Override
    public CharSequence subSequence(int arg0, int arg1) {
        throw new UnsupportedOperationException();
    }

    public Span getSpan(int index) {
        for(Span s : spans) {
            int i = index - s.getFirstCharacter();
            if(i >= 0 && i < s.getLength()) {
                return s;
            }
        }
        return null;
    }

    public boolean hasSpan(Span span) {
        for(Span s : spans) {
            if(s == span) {
                return true;
            }
        }
        return false;
    }

    public boolean isInSpan(int index, Span span) {
        for(Span s : spans) {
            int i = index - s.getFirstCharacter();
            if(i >= 0 && i < s.getLength() && s == span) {
                return true;
            }
        }
        return false;
    }
}
