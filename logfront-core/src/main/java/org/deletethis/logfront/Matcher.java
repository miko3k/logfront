package org.deletethis.logfront;

import java.awt.Color;
import org.deletethis.logfront.cfg.serialize.ConfigMapMarhallable;
import org.deletethis.logfront.colors.MatcherStyle;

import org.deletethis.logfront.message.LogMessage;

public interface Matcher extends ConfigMapMarhallable {

    public String getLongDescription();

    public String getShortDescription();

    public boolean matches(LogMessage lm);

    public Color getButtonBackgroundColor(MatcherStyle style);
}
