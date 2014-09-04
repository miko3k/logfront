package org.deletethis.logfront.impl;

import java.awt.Color;

import org.deletethis.logfront.Matcher;
import org.deletethis.logfront.cfg.CfgValue;
import org.deletethis.logfront.cfg.MapCfgValue;
import org.deletethis.logfront.cfg.ScalarCfgValue;
import org.deletethis.logfront.cfg.serialize.ConfigMarshallerContext;
import org.deletethis.logfront.colors.MatcherStyle;
import org.deletethis.logfront.message.LogMessage;

public class ThreadNameMatcher implements Matcher {

    final private String threadName;

    public ThreadNameMatcher(String threadName) {
        this.threadName = threadName;
    }
    
    public ThreadNameMatcher(ConfigMarshallerContext context, CfgValue value) {
        this.threadName = value.getMember("name").getString("unknown_thread");
    }    
    
    @Override
    public void marshallInto(MapCfgValue out, ConfigMarshallerContext s) {
        out.setMember("name", new ScalarCfgValue(threadName));
    }    

    @Override
    public String getLongDescription() {
        return "Thread name is \"" + threadName + "\"";
    }

    @Override
    public String getShortDescription() {
        return "\"" + threadName + "\"";
    }

    @Override
    public boolean matches(LogMessage lm) {
        return threadName.equals(lm.getThreadName());
    }

    @Override
    public String toString() {
        return getLongDescription();
    }

    @Override
    public Color getButtonBackgroundColor(MatcherStyle style) {
        return style.getThreadNameBackground(threadName);
    }
}
