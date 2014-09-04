package org.deletethis.logfront.widgets.tilepane.log;

import java.awt.Color;

import org.deletethis.logfront.widgets.tilepane.CharData;

public interface CharDataFactory {

    public CharData getCharData(char c, Color f, Color b, Color bc, int bd);

    public CharData getUnderlined(CharData cd);

    public CharData getWithForegroundColor(CharData cd, Color foreground);

    public CharData getWithBackgroundColor(CharData cd, Color background);
}
