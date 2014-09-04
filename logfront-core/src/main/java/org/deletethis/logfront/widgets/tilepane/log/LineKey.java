package org.deletethis.logfront.widgets.tilepane.log;

import org.deletethis.logfront.message.LogMessage;

public class LineKey {

    final private LogMessage logMessage;
    final private boolean expand;

    public LineKey(LogMessage logMessage, boolean expand) {
        this.logMessage = logMessage;
        this.expand = expand;
    }

    public LogMessage getLogMessage() {
        return logMessage;
    }

    public boolean isExpand() {
        return expand;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (expand ? 1231 : 1237);
        result = prime * result + logMessage.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        LineKey other = (LineKey) obj;
        return expand == other.expand && logMessage == other.logMessage;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + logMessage + ", " + expand + ")";
    }
}
