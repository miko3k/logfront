package org.deletethis.logfront.slf4j;

public interface Sink {
    public static final int TRACE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARN = 3;
    public static final int ERROR = 4;
    
    public void log(String name, int level, String message, Throwable t);
    public boolean isLevelEnabled(String name, int level);
}
