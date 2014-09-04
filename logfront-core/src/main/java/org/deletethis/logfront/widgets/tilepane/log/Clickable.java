package org.deletethis.logfront.widgets.tilepane.log;

import org.deletethis.logfront.message.LogMessage;
import org.deletethis.logfront.message.Name;

public class Clickable {

    private final LogMessage logMessage;
    private final Name loggerName;
    private final boolean stackTrace;
    private final boolean level;
    private final boolean threadName;

    private Clickable(LogMessage lm, Name loggerName, boolean stackTrace, boolean level, boolean threadName) {
        if(lm == null) {
            throw new IllegalArgumentException();
        }

        this.logMessage = lm;
        this.loggerName = loggerName;
        this.stackTrace = stackTrace;
        this.level = level;
        this.threadName = threadName;
    }

    public static Clickable loggerName(LogMessage lm, Name loggerName) {
        if(loggerName == null) {
            throw new IllegalArgumentException();
        }
        return new Clickable(lm, loggerName, false, false, false);
    }

    public static Clickable stackTrace(LogMessage lm) {
        return new Clickable(lm, null, true, false, false);
    }

    public static Clickable level(LogMessage lm) {
        return new Clickable(lm, null, false, true, false);
    }

    public static Clickable threadName(LogMessage lm) {
        return new Clickable(lm, null, false, false, true);
    }

    public Name getLoggerName() {
        return loggerName;
    }

    public boolean isStackTrace() {
        return stackTrace;
    }

    public boolean isLevel() {
        return level;
    }

    public boolean isThreadName() {
        return threadName;
    }

    public LogMessage getLogMessage() {
        return logMessage;
    }
}
