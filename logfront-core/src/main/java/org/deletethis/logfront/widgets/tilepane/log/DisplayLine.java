package org.deletethis.logfront.widgets.tilepane.log;

import org.deletethis.logfront.message.LogMessage;
import org.deletethis.logfront.widgets.tilepane.CharData;

public class DisplayLine {

    private final LineKey lineKey;
    private final FormattedText formattedText;
    private final int firstCharacter;
    private final int length;
    private final int firstColumn;
    private final boolean paragraphStart, paragraphEnd;

    public DisplayLine(LineKey key, FormattedText ft, int firstCharacter,
            int firstColumn, int length, boolean paragraphStart, boolean paragraphEnd) {

        assert (length >= 0);

        this.lineKey = key;
        this.formattedText = ft;
        this.firstCharacter = firstCharacter;
        this.firstColumn = firstColumn;
        this.length = length;
        this.paragraphStart = paragraphStart;
        this.paragraphEnd = paragraphEnd;
    }

    public FormattedText getText() {
        return formattedText;
    }

    public int getLeftMargin() {
        return firstColumn;
    }

    public int getCharacterIndex(int column) {
        int relChar = column - firstColumn;
        if(relChar < 0 || relChar >= length) {
            return -1;
        } else {
            int absChar = relChar + firstCharacter;
            return absChar;
        }
    }

    public int getCharacterIndexRounded(int column) {
        int relChar = column - firstColumn;
        if(relChar < 0) {
            relChar = 0;
        } else {
            assert (length >= 0);
            if(relChar >= length) {
                relChar = length - 1;
            }
        }

        int absChar = relChar + firstCharacter;
        return absChar;
    }

    public int getColumn(int charIndex) {
        int relChar = charIndex - firstCharacter;
        if(relChar < 0 || relChar >= length) {
            return -1;
        } else {
            int column = relChar + firstColumn;
            return column;
        }
    }

    public int getColumnRounded(int charIndex) {
        int relChar = charIndex - firstCharacter;
        if(relChar < 0) {
            relChar = 0;
        } else if(relChar >= length) {
            assert (length >= 0);
            if(relChar >= length) {
                relChar = length - 1;
            }
        }
        int column = relChar + firstColumn;
        return column;
    }

    public int getLength() {
        return length;
    }

    public CharData getCharData(int x) {
        int idx = getCharacterIndex(x);
        if(idx < 0) {
            return null;
        } else {
            return getText().getCharacters()[idx];
        }
    }

    public Span getSpan(int x) {
        return getText().getSpan(getCharacterIndex(x));
    }

    public boolean isInSpan(int x, Span s) {
        int idx = getCharacterIndex(x);
        if(idx < 0) {
            return false;
        } else {
            return getText().isInSpan(idx, s);
        }
    }

    private int getColumnForSpan(int index, Span span) {
        if(getText().hasSpan(span)) {
            int col = getColumn(index);
            if(col >= 0) {
                return col;
            }
        }
        return -1;
    }

    public int getSpanBegin(Span span) {
        return getColumnForSpan(span.getFirstCharacter(), span);
    }

    public int getSpanEnd(Span span) {
        return getColumnForSpan(span.getFirstCharacter() + span.getLength() - 1, span);
    }

    public boolean isExpand() {
        return lineKey.isExpand();
    }

    public LogMessage getLogMessage() {
        return lineKey.getLogMessage();
    }

    public boolean isParagraphEnd() {
        return paragraphEnd;
    }

    public boolean isParagraphStart() {
        return paragraphStart;
    }

    public boolean hasExpand() {
        return !lineKey.isExpand() && getLogMessage().getThrowable() != null;
    }

    public LineKey getKey() {
        return lineKey;
    }
}
