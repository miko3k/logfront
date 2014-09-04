/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront;

import org.deletethis.logfront.message.LogMessage;

/**
 *
 * @author miko
 */
public class SwingLogConsumer extends SwingLogConsumerBase {
    public LogConsumer logConsumer;

    public SwingLogConsumer(LogConsumer logConsumer) {
        this.logConsumer = logConsumer;
    }

    @Override
    protected void onMessage(LogMessage message) {
        logConsumer.addMessage(message);
    }
}
