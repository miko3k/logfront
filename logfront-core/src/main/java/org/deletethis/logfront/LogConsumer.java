package org.deletethis.logfront;

import org.deletethis.logfront.message.LogMessage;

public interface LogConsumer {

    public void addMessage(LogMessage logMessage);
}
