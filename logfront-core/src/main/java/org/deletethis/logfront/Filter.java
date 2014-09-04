package org.deletethis.logfront;

import org.deletethis.logfront.message.LogMessage;

public interface Filter {

    public boolean passes(LogMessage f);
}
