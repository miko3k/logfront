package org.deletethis.logfront.widgets.tilepane.log;

import org.deletethis.logfront.colors.LogViewStyle;
import org.deletethis.logfront.util.UnicodeChars;
import org.deletethis.logfront.widgets.tilepane.CharData;

public class ExpandButtons {

    public static final int BUTTON_WIDTH = 4;
    private final CharData[] expandButton;
    private final CharData[] collapseButton;
    private final CharData[] expandButtonMouseOver;
    private final CharData[] collapseButtonMouseOver;

    private CharData[] createButton(LogViewStyle style,
            CharDataFactory factory, boolean collapse, boolean hover) {
        Formatter f = new Formatter(factory);
        StringBuilder bld = new StringBuilder();
        if(collapse) {
            bld.append(UnicodeChars.BLACK_LOWER_RIGHT_TRIANGLE).append(
                    UnicodeChars.BLACK_LOWER_LEFT_TRIANGLE);
        } else {
            bld.append(UnicodeChars.BLACK_UPPER_RIGHT_TRIANGLE).append(
                    UnicodeChars.BLACK_UPPER_LEFT_TRIANGLE);
        }
        String icon = bld.toString();

        if(hover) {
            f.setBackground(style.getExpandButtonHoverBackgroundColor());
            f.setColor(style.getExpandButtonHoverIconColor());
            f.append(' ' + icon + ' ', style.getExpandButtonHoverBorderColor());
        } else {
            f.setBackground(style.getExpandButtonBackgroundColor());
            f.setColor(style.getExpandButtonIconColor());
            f.append(' ' + icon + ' ', style.getExpandButtonBorderColor());
        }

        CharData[] result = f.getCharData();
        assert result.length == BUTTON_WIDTH;
        return result;
    }

    public ExpandButtons(LogViewStyle style, CharDataFactory factory) {
        this.expandButton = createButton(style, factory, false, false);
        this.collapseButton = createButton(style, factory, true, false);
        this.expandButtonMouseOver = createButton(style, factory, false, true);
        this.collapseButtonMouseOver = createButton(style, factory, true, true);

    }

    public CharData getButtonChar(int x, boolean expand, boolean hover) {
        if(hover) {
            if(expand) {
                return expandButtonMouseOver[x];
            } else {
                return collapseButtonMouseOver[x];
            }
        } else {
            if(expand) {
                return expandButton[x];
            } else {
                return collapseButton[x];
            }
        }
    }
}
