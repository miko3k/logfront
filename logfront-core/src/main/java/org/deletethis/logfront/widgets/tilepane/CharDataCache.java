package org.deletethis.logfront.widgets.tilepane;

import java.awt.Color;
import java.util.HashMap;

import org.deletethis.logfront.widgets.tilepane.log.CharDataFactory;

public class CharDataCache implements CharDataFactory {

    private HashMap<CharData, CharData> cache = new HashMap<>();

    private CharData getCached(CharData x) {
        CharData r = cache.get(x);
        if(r != null) {
            return r;
        } else {
            cache.put(x, x);
            return x;
        }

    }

    public CharData getCharData(char c, Color f, Color b, Color bc, int bd) {
        return getCached(new CharData(c, f, b, bc, bd));
    }

    @Override
    public CharData getUnderlined(CharData cd) {
        return getCached(cd.underline());
    }

    @Override
    public CharData getWithForegroundColor(CharData cd, Color foreground) {
        return getCached(cd.setForeground(foreground));
    }

    @Override
    public CharData getWithBackgroundColor(CharData cd, Color background) {
        return getCached(cd.setBackground(background));
    }

}
