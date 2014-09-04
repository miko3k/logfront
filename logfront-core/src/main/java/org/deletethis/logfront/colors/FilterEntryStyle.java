package org.deletethis.logfront.colors;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;

public interface FilterEntryStyle {

    Font getFont();

    Color getActiveTextColor();

    Color getInaciveTextColor();

    Color getInactiveBackgroundColor();

    Color getHoverBackgroundColor(Color normalBackground);

    Color getPressedBackgroundColor(Color normalBackground);

    Icon getCloseIcon();

    int getGap();
}
