package org.deletethis.logfront.colors;

import java.awt.Color;
import java.awt.Font;

import org.deletethis.logfront.message.Level;
import org.deletethis.logfront.message.Name;

public interface LogViewStyle extends TilePaneStyle {

    Font getFont();

    Color getSelectionBackgroundColor();

    Color getSelectionTextColor();

    Color getExpandButtonBorderColor();

    Color getExpandButtonHoverBorderColor();

    Color getExpandButtonIconColor();

    Color getExpandButtonHoverIconColor();

    Color getExpandButtonBackgroundColor();

    Color getExpandButtonHoverBackgroundColor();

    Color getLeftPaneBackgroundColor();

    Color getThreadNameColor(String name);

    Color getLoggerColor(Name name);

    Color getNormalTextColor();

    Color getExceptionLineColor();

    Color getLevelColor(Level level);

    Color getHoverForegroundColor(Color normal);

    Color getSearchMatchColor();

    Color getCurrentSearchMatchColor();

}
