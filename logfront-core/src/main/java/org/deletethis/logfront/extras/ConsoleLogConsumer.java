/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.extras;

import org.deletethis.logfront.LogConsumer;
import org.deletethis.logfront.message.LogMessage;

/**
 *
 * @author miko
 */
public class ConsoleLogConsumer implements LogConsumer {

    @Override
    public void addMessage(LogMessage logMessage) {
        System.out.println(logMessage);
    }
    
}
