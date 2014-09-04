/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront;

import javax.swing.SwingUtilities;
import org.deletethis.logfront.message.LogMessage;

/**
 * Accepts LogMessages and passes them forward on swing thread.
 * 
 * Optionally in can do something special on first message.
 * 
 * We should consider some kind of message queue or something
 * for performance boost.
 * 
 * @author miko
 */
abstract public class SwingLogConsumerBase implements LogConsumer {
    private boolean first = true;
    
    protected void beforeFirstMessage() { }
    
    abstract protected void onMessage(LogMessage message);
    
    @Override
    final synchronized public void addMessage(final LogMessage logMessage) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(first) {
                    beforeFirstMessage();
                    first = false;
                    
                }
                onMessage(logMessage);
            }
        });
    }
}
