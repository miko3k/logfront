package org.deletethis.logfront.colors;

import java.awt.Color;

import org.deletethis.logfront.message.Level;
import org.deletethis.logfront.message.Name;
import org.deletethis.logfront.util.HSL;

public class MatcherStyleImpl implements MatcherStyle {

    private float backgroundLuminance;

    MatcherStyleImpl(FilterEntryStyle entry) {
        if(ColorUtil.isColorBright(entry.getActiveTextColor())) {
            backgroundLuminance = 0.3f;
        } else {
            backgroundLuminance = 0.7f;
        }
    }

    @Override
    public Color getLevelBackground(Level level) {
        return HSL.create(level.getHue(), 1, backgroundLuminance);
    }

    @Override
    public Color getLoggerBackground(Name name) {
        return HSL.create(ColorUtil.getLoggerHue(name), 1, backgroundLuminance);
    }

    @Override
    public Color getThreadNameBackground(String name) {
        return HSL.create(ColorUtil.getThreadNameHue(name), 1, backgroundLuminance);
    }

}
