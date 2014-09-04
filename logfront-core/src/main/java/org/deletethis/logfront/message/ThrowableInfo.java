/*
 * xxx
 */

package org.deletethis.logfront.message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author miko
 */
public class ThrowableInfo {
    // we don't use class: message because exception may override toString
    final private String brief;
    final private List<StackTraceElementInfo> stackTrace;
    final private ThrowableInfo cause;
    final private List<ThrowableInfo> suppressed;
    
    private <T> List<T> copyAndMakeUnmodifiable(Collection<T> lst)
    {
        if(lst == null)
            return null;
        
        List<T> res = new ArrayList<>(lst);
        return Collections.unmodifiableList(res);
    }

    public ThrowableInfo(String brief, Collection<StackTraceElementInfo> stackTrace, ThrowableInfo cause, Collection<ThrowableInfo> suppressed) {
        this.brief = brief;
        this.suppressed = copyAndMakeUnmodifiable(suppressed);
        this.cause = cause;
        this.stackTrace = copyAndMakeUnmodifiable(stackTrace);
    }

    public Iterable<StackTraceElementInfo> getStackTrace() {
        return stackTrace;
    }

    public ThrowableInfo getCause() {
        return cause;
    }

    public Iterable<ThrowableInfo> getSuppressed() {
        return suppressed;
    }
    
    /**
     * mostly stolen form JDK.
     * 
     * @param printer 
     */
    public void printStackTrace(ThrowablePrinter printer) {
        Set<ThrowableInfo> printed
                = Collections.newSetFromMap(new IdentityHashMap<ThrowableInfo, Boolean>());
        printed.add(this);

        printer.printMain(brief, stackTrace);

        for(ThrowableInfo t : getSuppressed()) {
            t.printEnclosedStackTrace(printer, stackTrace, false, printed, 1);
        }

        if(cause != null) {
            cause.printEnclosedStackTrace(printer, stackTrace, true, printed, 0);
        }

    }

    /**
     * Print our stack trace as an enclosed exception for the specified
     * stack trace.
     */
    private void printEnclosedStackTrace(ThrowablePrinter s,
                                         List<StackTraceElementInfo> enclosingTrace,
                                         boolean asCause,
                                         Set<ThrowableInfo> dejaVu,
                                         int suppressedLevel) {
        if (dejaVu.contains(this)) {
            s.printError("[CIRCULAR REFERENCE:" + this + "]", suppressedLevel);
        } else {
            dejaVu.add(this);
            // Compute number of frames in common between this and enclosing trace
            int m = stackTrace.size() - 1;
            int n = enclosingTrace.size() - 1;
            while (m >= 0 && n >=0 && stackTrace.get(m).equals(enclosingTrace.get(n))) {
                m--; n--;
            }
            if(asCause) {
                s.printCausedBy(brief, stackTrace, m+1, suppressedLevel);
            } else {
                s.printSuppressed(brief, stackTrace, m+1, suppressedLevel);
            }
            
            // Print suppressed exceptions, if any
            for (ThrowableInfo se : suppressed)
                se.printEnclosedStackTrace(s, stackTrace, false, dejaVu, suppressedLevel+1);

            // Print cause, if any
            if(cause != null)
                cause.printEnclosedStackTrace(s, stackTrace, true, dejaVu, suppressedLevel);
        }
    }
}
