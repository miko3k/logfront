package org.deletethis.logfront.colors;

import java.awt.Color;

import org.deletethis.logfront.message.Level;
import org.deletethis.logfront.message.Name;

public interface MatcherStyle {

    public Color getLevelBackground(Level level);

    public Color getLoggerBackground(Name name);

    public Color getThreadNameBackground(String name);
}
