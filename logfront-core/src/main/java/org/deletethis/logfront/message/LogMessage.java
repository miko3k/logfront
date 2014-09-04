package org.deletethis.logfront.message;

import java.util.Date;

public class LogMessage {

    private final Name name;
    private final Level level;
    private final String message;
    private final ThrowableInfo throwable;
    private final String threadName;
    private final Date date;

    public LogMessage(Date date, Name name, Level level, String threadName, String message, ThrowableInfo t) {
        this.name = name;
        this.level = level;
        this.message = message;
        this.threadName = threadName;
        this.throwable = t;
        this.date = date;
    }

    public Name getName() {
        return name;
    }

    public Level getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public ThrowableInfo getThrowable() {
        return throwable;
    }

    public String getThreadName() {
        return threadName;
    }

    public Date getDate() {
        return date;
    }
    
    
}
