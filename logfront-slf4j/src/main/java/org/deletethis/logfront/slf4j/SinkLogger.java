package org.deletethis.logfront.slf4j;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

public class SinkLogger<LEVEL> extends MarkerIgnoringBase {

    private static final long serialVersionUID = 7613061329840713273L;
    private final Sink sink;

    public SinkLogger(String name, Sink sink) {
        this.sink = sink;
        this.name = name;
    }

    private boolean isLevelEnabled(int level) {
        return sink.isLevelEnabled(name, level);
    }

    private void log(int level, String message, Throwable t) {
        sink.log(name, level, message, t);
    }

    /**
     * For formatted messages, first substitute arguments and then log.
     *
     * @param level
     * @param format
     * @param arg1
     * @param arg2
     */
    private void formatAndLog(int level, String format, Object arg1,
            Object arg2) {
        if(!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    /**
     * For formatted messages, first substitute arguments and then log.
     *
     * @param level
     * @param format
     * @param arguments a list of 3 ore more arguments
     */
    private void formatAndLog(int level, String format, Object... arguments) {
        if(!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    @Override
    public boolean isTraceEnabled() {
        return isLevelEnabled(Sink.TRACE);
    }

    @Override
    public void trace(String msg) {
        log(Sink.TRACE, msg, null);
    }

    @Override
    public void trace(String format, Object param1) {
        formatAndLog(Sink.TRACE, format, param1, null);
    }

    @Override
    public void trace(String format, Object param1, Object param2) {
        formatAndLog(Sink.TRACE, format, param1, param2);
    }

    @Override
    public void trace(String format, Object... argArray) {
        formatAndLog(Sink.TRACE, format, argArray);
    }

    @Override
    public void trace(String msg, Throwable t) {
        log(Sink.TRACE, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return isLevelEnabled(Sink.DEBUG);
    }

    @Override
    public void debug(String msg) {
        log(Sink.DEBUG, msg, null);
    }

    @Override
    public void debug(String format, Object param1) {
        formatAndLog(Sink.DEBUG, format, param1, null);
    }

    @Override
    public void debug(String format, Object param1, Object param2) {
        formatAndLog(Sink.DEBUG, format, param1, param2);
    }

    @Override
    public void debug(String format, Object... argArray) {
        formatAndLog(Sink.DEBUG, format, argArray);
    }

    @Override
    public void debug(String msg, Throwable t) {
        log(Sink.DEBUG, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return isLevelEnabled(Sink.INFO);
    }

    @Override
    public void info(String msg) {
        log(Sink.INFO, msg, null);
    }

    @Override
    public void info(String format, Object arg) {
        formatAndLog(Sink.INFO, format, arg, null);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        formatAndLog(Sink.INFO, format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... argArray) {
        formatAndLog(Sink.INFO, format, argArray);
    }

    @Override
    public void info(String msg, Throwable t) {
        log(Sink.INFO, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return isLevelEnabled(Sink.WARN);
    }

    @Override
    public void warn(String msg) {
        log(Sink.WARN, msg, null);
    }

    @Override
    public void warn(String format, Object arg) {
        formatAndLog(Sink.WARN, format, arg, null);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        formatAndLog(Sink.WARN, format, arg1, arg2);
    }

    @Override
    public void warn(String format, Object... argArray) {
        formatAndLog(Sink.WARN, format, argArray);
    }

    @Override
    public void warn(String msg, Throwable t) {
        log(Sink.WARN, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return isLevelEnabled(Sink.ERROR);
    }

    @Override
    public void error(String msg) {
        log(Sink.ERROR, msg, null);
    }

    @Override
    public void error(String format, Object arg) {
        formatAndLog(Sink.ERROR, format, arg, null);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        formatAndLog(Sink.ERROR, format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... argArray) {
        formatAndLog(Sink.ERROR, format, argArray);
    }

    @Override
    public void error(String msg, Throwable t) {
        log(Sink.ERROR, msg, t);
    }
}
