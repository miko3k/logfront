/*
 * xxx
 */

package org.deletethis.logfront.message;

import java.util.List;

/**
 *
 * @author miko
 */
public interface ThrowablePrinter {
    public void printMain(String brief, List<StackTraceElementInfo> stackTrace);
    public void printCausedBy(String brief, List<StackTraceElementInfo> stackTrace, int framesToPrint, int suppressedLevel);
    public void printSuppressed(String brief, List<StackTraceElementInfo> stackTrace, int framesToPrint, int suppressedLevel);
    public void printError(String error, int suppressedLevel);
}
