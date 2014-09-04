package org.deletethis.logfront.impl;

import java.awt.Color;

import org.deletethis.logfront.Matcher;
import org.deletethis.logfront.cfg.CfgValue;
import org.deletethis.logfront.cfg.MapCfgValue;
import org.deletethis.logfront.cfg.serialize.ConfigMapMarhallable;
import org.deletethis.logfront.cfg.serialize.ConfigMarshallerContext;
import org.deletethis.logfront.colors.MatcherStyle;
import org.deletethis.logfront.message.Level;
import org.deletethis.logfront.message.LogMessage;

public class LevelMatcher implements Matcher {

    public enum Operation {

        SAME, SAME_OR_MORE_SEVERE, SAME_OR_LESS_SEVERE
    };

    final private Operation operation;
    final private Level level;

    public LevelMatcher(Level level, Operation operation) {
        this.operation = operation;
        this.level = level;
    }

    public LevelMatcher(ConfigMarshallerContext context, CfgValue value) {
        this.operation = context.unmarshall(Operation.class, value.getMember("op"));
        this.level = context.unmarshall(Level.class, value.getMember("level"));
    }
    
    @Override
    public void marshallInto(MapCfgValue out, ConfigMarshallerContext s) {
        out.setMember("level", s.marshall(level));
        out.setMember("op", s.marshall(operation));
    }
    
    public Operation getOperation() {
        return operation;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public String getLongDescription() {
        return "Level is " + getShortDescription();
    }

    @Override
    public String getShortDescription() {
        switch(operation) {
            case SAME:
                return level.toString();
            case SAME_OR_LESS_SEVERE:
                return level.toString() + " or less severe";
            case SAME_OR_MORE_SEVERE:
                return level.toString() + " or more severe";
            default:
                throw new IllegalStateException("weird data here!");
        }
    }

    @Override
    public boolean matches(LogMessage lm) {
        Level mylevel = lm.getLevel();
        if(mylevel.equals(level)) {
            return true;
        }

        switch(operation) {
            case SAME_OR_LESS_SEVERE:
                return mylevel.isLessSevereThan(level);
            case SAME_OR_MORE_SEVERE:
                return mylevel.isMoreSevereThan(level);
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return getLongDescription();
    }

    @Override
    public Color getButtonBackgroundColor(MatcherStyle style) {
        return style.getLevelBackground(level);
    }
}
