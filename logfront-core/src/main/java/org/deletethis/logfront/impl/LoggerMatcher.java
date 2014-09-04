package org.deletethis.logfront.impl;

import java.awt.Color;

import org.deletethis.logfront.Matcher;
import org.deletethis.logfront.cfg.CfgValue;
import org.deletethis.logfront.cfg.MapCfgValue;
import org.deletethis.logfront.cfg.ScalarCfgValue;
import org.deletethis.logfront.cfg.serialize.ConfigMarshallerContext;
import org.deletethis.logfront.colors.MatcherStyle;
import org.deletethis.logfront.message.LogMessage;
import org.deletethis.logfront.message.Name;

public class LoggerMatcher implements Matcher {

    final private Name name;
    final private boolean includeSubpackages;

    public LoggerMatcher(Name name, boolean includeSubpackages) {
        this.name = name;
        this.includeSubpackages = includeSubpackages;
    }

    public LoggerMatcher(ConfigMarshallerContext context, CfgValue value) {
        this.name = new Name(value.getMember("name").getString("unknown.package"));
        this.includeSubpackages = value.getMember("subpackages").getBoolean(true);
    }    
    
    @Override
    public void marshallInto(MapCfgValue out, ConfigMarshallerContext s) {
        out.setMember("name", new ScalarCfgValue(name.getName()));
        out.setMember("subpackages", new ScalarCfgValue(includeSubpackages));
    }
    
    @Override
    public String getLongDescription() {
        return "Logger name is " + getShortDescription();
    }

    @Override
    public String getShortDescription() {
        String res = name.toString();
        if(includeSubpackages) {
            res += ".*";
        }

        return res;
    }

    @Override
    public boolean matches(LogMessage lm) {
        if(includeSubpackages) {
            return lm.getName().beginsWith(name);
        } else {
            return lm.getName().equals(name);
        }
    }

    @Override
    public String toString() {
        return getLongDescription();
    }

    @Override
    public Color getButtonBackgroundColor(MatcherStyle style) {
        return style.getLoggerBackground(name);
    }

}
