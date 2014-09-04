package org.deletethis.logfront.slf4j;

import org.deletethis.logfront.LogConsumer;
import org.deletethis.logfront.extras.Slf4jLevelFactory;
import org.deletethis.logfront.interactive.InteractiveLogger;
import org.deletethis.logfront.interactive.appname.ApplicationNameResolverDefault;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class LogFrontLoggerFactory implements ILoggerFactory {
    final private Slf4jLevelFactory levelFactory = new Slf4jLevelFactory();
    final private Sink sink;

    public LogFrontLoggerFactory() {
        LogConsumer consumer = new InteractiveLogger(
            "SLF4J",
            ApplicationNameResolverDefault.getDefault(), 
            levelFactory);
        
        sink = new LogConsumerSink(consumer);
    }
    
    @Override
    public Logger getLogger(String name) {
        return new SinkLogger<>(name, sink);
    }

    void reset() {

    }
}
