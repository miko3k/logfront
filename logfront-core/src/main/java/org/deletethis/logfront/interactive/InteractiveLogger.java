package org.deletethis.logfront.interactive;

import org.deletethis.logfront.SwingLogConsumerBase;
import org.deletethis.logfront.cfg.ConfigBackend;
import org.deletethis.logfront.cfg.JavaPreferences;
import org.deletethis.logfront.message.LevelFactory;
import org.deletethis.logfront.message.LogMessage;

public class InteractiveLogger extends SwingLogConsumerBase {

    private final String framework;
    private final ApplicationNameResolver applicationNameResolver;
    private final LevelFactory levelFactory;
    private InteractiveFrame frame;
    
    public InteractiveLogger(String framework, ApplicationNameResolver applicationNameResolver, LevelFactory levelFactory) {
        this.framework = framework;
        this.applicationNameResolver = applicationNameResolver;
        this.levelFactory = levelFactory;
    }

    private InteractiveFrame createFrame(String appname) {
        InteractiveFrame result;
        ConfigBackend cfgBackend = null;
        if(framework != null && appname != null) {
            cfgBackend = new JavaPreferences("logfront", "interactive", framework, appname);
        } 
        String titleapp = appname;
        if(titleapp == null)
            titleapp = "Unknown";
        
        result = new InteractiveFrame(cfgBackend, titleapp + " - " + framework, levelFactory);
        return result;
    }

    @Override
    protected void beforeFirstMessage() {
        String appname = applicationNameResolver.getApplicationName();
        frame = createFrame(appname);
        frame.setVisible(true);
    }
    
    @Override
    protected void onMessage(LogMessage message) {
        frame.addMessage(message);
    }
}
