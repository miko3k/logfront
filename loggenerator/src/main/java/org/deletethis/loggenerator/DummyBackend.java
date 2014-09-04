package org.deletethis.loggenerator;

public class DummyBackend implements Backend {

    @Override
    public String getShortName() {
        return "dummy";
    }

    @Override
    public String getLongName() {
        return "Dummy Backend";
    }

    @Override
    public int getMinSeverity() {
        return 0;
    }

    @Override
    public int getMaxSeverity() {
        return 1;
    }

    @Override
    public String getSeverityName(int severity) {
        if(severity == 0) {
            return "INFO";
        } else {
            return "ERROR";
        }
    }

    @Override
    public void createLog(String name, int severity, String message, Throwable t) {
        System.out.println(name + " (" + severity + "): " + message);
        if(t != null) {
            t.printStackTrace(System.out);
        }
    }
}
