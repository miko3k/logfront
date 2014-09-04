package org.deletethis.loggenerator.backends;

import org.deletethis.loggenerator.Backend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jBackend implements Backend {

    enum Severity {

        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    @Override
    public String getShortName() {
        return "slf4j";
    }

    @Override
    public String getLongName() {
        return "SLF4J";
    }

    @Override
    public int getMinSeverity() {
        return 0;
    }

    @Override
    public int getMaxSeverity() {
        return Severity.values().length - 1;
    }

    @Override
    public String getSeverityName(int severity) {
        return Severity.values()[severity].name();
    }

    @Override
    public void createLog(String name, int severity, String message, Throwable t) {
        Logger logger = LoggerFactory.getLogger(name);
        switch(Severity.values()[severity]) {
            case TRACE:
                logger.trace(message, t);
                break;
            case DEBUG:
                logger.debug(message, t);
                break;
            case INFO:
                logger.info(message, t);
                break;
            case WARN:
                logger.warn(message, t);
                break;
            case ERROR:
                logger.error(message, t);
                break;
            default:
                throw new IllegalArgumentException("weird severity: " + severity);
        }
    }
}
