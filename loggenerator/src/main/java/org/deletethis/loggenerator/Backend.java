package org.deletethis.loggenerator;

public interface Backend {

    public String getLongName();

    public String getShortName();

    public int getMinSeverity();

    public int getMaxSeverity();

    public String getSeverityName(int severity);

    public void createLog(String name, int severity, String message, Throwable t);
}
