/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.slf4j;

import org.deletethis.logfront.LogConsumer;
import org.deletethis.logfront.extras.LogMessageBuilder;
import org.deletethis.logfront.extras.Slf4jLevelFactory;
import org.deletethis.logfront.message.Level;
import org.deletethis.logfront.message.LogMessage;

/**
 *
 * @author miko
 */
public class LogConsumerSink implements Sink {
    final private LogConsumer logConsumer;

    public LogConsumerSink(LogConsumer logConsumer) {
        this.logConsumer = logConsumer;
    }
    
    private Level mapLevel(int level) {
        switch(level) {
            case Sink.TRACE: return Slf4jLevelFactory.TRACE;
            case Sink.DEBUG: return Slf4jLevelFactory.DEBUG;
            case Sink.INFO: return Slf4jLevelFactory.INFO;
            case Sink.WARN: return Slf4jLevelFactory.WARN;
            case Sink.ERROR: return Slf4jLevelFactory.ERROR;
            default:
                throw new IllegalArgumentException("unable to map level: " + level);
        }
    }    
    
    @Override
    public void log(String name, int level, String message, Throwable t) {
        LogMessage lm = LogMessageBuilder.createLogMessage(name, mapLevel(level), message, t);
        logConsumer.addMessage(lm);
    }

    @Override
    public boolean isLevelEnabled(String name, int level) {
        return true;
    }
}
