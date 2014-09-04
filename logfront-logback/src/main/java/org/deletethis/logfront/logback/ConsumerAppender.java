/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.logback;

//import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.Date;
import org.deletethis.logfront.LogConsumer;
import org.deletethis.logfront.extras.Slf4jLevelFactory;
import org.deletethis.logfront.message.Level;
import org.deletethis.logfront.message.LogMessage;
import org.deletethis.logfront.message.Name;

/**
 *
 * @author miko
 */
public class ConsumerAppender extends AppenderBase<ILoggingEvent> {
    final private LogConsumer consumer;

    public ConsumerAppender(LogConsumer consumer) {
        this.consumer = consumer;
    }

    private Level mapLevel(ch.qos.logback.classic.Level lev) {
        switch(lev.toInt()) {
            case ch.qos.logback.classic.Level.ERROR_INT: return Slf4jLevelFactory.ERROR;
            case ch.qos.logback.classic.Level.WARN_INT: return Slf4jLevelFactory.WARN;
            case ch.qos.logback.classic.Level.INFO_INT: return Slf4jLevelFactory.INFO;
            case ch.qos.logback.classic.Level.DEBUG_INT: return Slf4jLevelFactory.DEBUG;
            case ch.qos.logback.classic.Level.TRACE_INT: return Slf4jLevelFactory.TRACE;            
            default: return Slf4jLevelFactory.TRACE;            
        }
    }
    
    @Override
    protected void append(ILoggingEvent ev) {
        Name nm = new Name(ev.getLoggerName());
        Level level = mapLevel(ev.getLevel());
        
        LogMessage msg = new LogMessage(
                new Date(ev.getTimeStamp()),
                nm, 
                level, 
                ev.getThreadName(), 
                ev.getFormattedMessage(), 
                null);
        
        consumer.addMessage(msg);
    }
    
}
