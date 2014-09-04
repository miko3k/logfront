/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.net.SocketReceiver;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.status.StatusManager;
import org.deletethis.logfront.LogConsumer;
import org.deletethis.logfront.standalone.plugin.PluginConnection;

/**
 *
 * @author miko
 */
public class SocketReceiverConnection implements PluginConnection {
    static void register(LoggerContext lc, LifeCycle c)
    {
        if(c instanceof ContextAware) {
            ((ContextAware)c).setContext(lc);
        }
        lc.addListener(new ListenerWrapper(c));
    }
    
    private LoggerContext loggerContext;
    
    public SocketReceiverConnection(Config cfg, LogConsumer consumer) {
        loggerContext = new LoggerContext();
        StatusManager statusManager = loggerContext.getStatusManager();
        OnConsoleStatusListener onConsoleListener = new OnConsoleStatusListener();
        statusManager.add(onConsoleListener);
        register(loggerContext, onConsoleListener);

        Appender<ILoggingEvent> a = new ConsumerAppender(consumer);
        register(loggerContext, a);
        
        Logger root = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.ALL);
        root.addAppender(a);
        
        SocketReceiver sr = new SocketReceiver();
        sr.setRemoteHost(cfg.getHostname());
        sr.setPort(cfg.getPort());
        sr.setReconnectionDelay(cfg.getInterval());
        
        register(loggerContext, sr);

        loggerContext.start();        
    }
    
    @Override
    public void close() {
        loggerContext.stop();
    }
    
}
