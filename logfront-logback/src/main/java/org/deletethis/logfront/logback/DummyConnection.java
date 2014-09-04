/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.logback;

import org.deletethis.logfront.LogConsumer;
import org.deletethis.logfront.extras.LogMessageBuilder;
import org.deletethis.logfront.extras.Slf4jLevelFactory;
import org.deletethis.logfront.message.LogMessage;
import org.deletethis.logfront.standalone.plugin.PluginConnection;

/**
 *
 * @author miko
 */
public class DummyConnection implements PluginConnection {
    private class MyThread extends Thread{
        boolean shouldStop = false;
        
        synchronized public void stopThread() { shouldStop = true; }
        synchronized private boolean shouldStop() { return shouldStop; }

        private void createMessage() {
            LogMessage lm
                    = LogMessageBuilder.createLogMessage("dummy", Slf4jLevelFactory.INFO, "hello, dummy message", null);
            logConsumer.addMessage(lm);

        }

        @Override
        public void run() {
            int i = 0;
            
            while(true) {
                if(shouldStop())
                    break;
                ++i;
                try {
                    if(i > 10) {
                        i = 0;
                        createMessage();
                    }
                    Thread.sleep(100);
                } catch (InterruptedException|RuntimeException ex) {
                    // continue;
                }
            }
        }
    };
    
    private final LogConsumer logConsumer;
    private final MyThread thread;
    
    
    public DummyConnection(LogConsumer consumer)
    {
        this.logConsumer = consumer;
        this.thread = new MyThread();
    }

    public void start()
    {
        this.thread.start();
    }
    
    @Override
    public void close() {
        this.thread.stopThread();
        try {
            this.thread.join();
        } catch (InterruptedException ex) {
        }
    }
    
}
