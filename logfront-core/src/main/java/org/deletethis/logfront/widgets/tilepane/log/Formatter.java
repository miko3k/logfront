package org.deletethis.logfront.widgets.tilepane.log;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.deletethis.logfront.widgets.tilepane.CharData;

public class Formatter {

    private static class IncompleteSpan {

        int start;
        Clickable clickable;
    }

    private final List<CharData> characters = new ArrayList<>();
    private final List<IncompleteSpan> openSpans = new ArrayList<>();
    private final List<Span> closedSpans = new ArrayList<>();
    private final CharDataFactory factory;
    private Color currentColor = Color.BLACK;
    private Color currentBackground = null;

    public Formatter(CharDataFactory factory) {
        this.factory = factory;
    }

    public void append(String str) {
        append(str, null);
    }

    public void append(String str, Color borderColor) {
        int len = str.length();
        for(int i = 0; i < len; ++i) {

            int border = 0;
            if(borderColor != null) {
                border = CharData.TOP_BORDER | CharData.BOTTOM_BORDER;
                if(i == 0) {
                    border |= CharData.LEFT_BORDER;
                } else if(i == len - 1) {
                    border |= CharData.RIGHT_BORDER;
                }

            }
            char c = str.charAt(i);

            characters.add(factory.getCharData(c, currentColor,
                    currentBackground, borderColor, border));
        }
    }

    public void startSpan(Clickable s) {
        IncompleteSpan sp = new IncompleteSpan();
        sp.start = characters.size();
        sp.clickable = s;
        openSpans.add(sp);
    }

    public void endSpan() {
        assert !openSpans.isEmpty();
        int length = openSpans.size();
        IncompleteSpan s = openSpans.remove(length - 1);

        closedSpans.add(new Span(s.start, characters.size() - s.start, s.clickable));
    }

    public void setColor(Color color) {
        currentColor = color;
    }

    public void setBackground(Color background) {
        currentBackground = background;
    }

    private CharData[] chararray() {
        return characters.toArray(new CharData[ characters.size() ]);
    }

    private Span[] spanarray() {
        return closedSpans.toArray(new Span[ closedSpans.size() ]);
    }

    public FormattedText getFormattedText() {
        assert openSpans.isEmpty();
        return new FormattedText(chararray(), spanarray());
    }

    public CharData[] getCharData() {
        return chararray();
    }
}
