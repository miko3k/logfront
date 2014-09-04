/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.extras;

import java.util.Date;
import org.deletethis.logfront.message.Level;
import org.deletethis.logfront.message.LogMessage;
import org.deletethis.logfront.message.Name;
import org.deletethis.logfront.message.ThrowableInfo;

/**
 *
 * @author miko
 */
public class LogMessageBuilder {
    public static LogMessage createLogMessage(String name, Level level, String message, Throwable t)
    {
        ThrowableInfo ti = null;
        if(t != null) {
            ti = ThrowableInfoBuilder.createThrowableInfo(t);
        }
        
        return new LogMessage(new Date(), new Name(name), level, Thread.currentThread().getName(), message, ti);
        
    }
}
