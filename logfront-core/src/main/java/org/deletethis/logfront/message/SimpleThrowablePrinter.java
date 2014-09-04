/*
 * xxx
 */

package org.deletethis.logfront.message;

import java.io.PrintStream;
import java.util.List;

/**
 *
 * @author miko
 */
public class SimpleThrowablePrinter implements ThrowablePrinter {
    final private PrintStream out;
    final private boolean useSpaces;

    private String ntabs(int n)
    {
        if(useSpaces) {
            StringBuilder res = new StringBuilder(n*8);
            for(int i=0;i<n*8;++i)
                res.append(' ');
            return res.toString();
        } else {
            switch(n) {
                case 0: return "";
                case 1: return "\t";
                case 2: return "\t\t";
                case 3: return "\t\t\t";
            }
            StringBuilder res = new StringBuilder(n);
            for(int i=0;i<n;++i)
                res.append('\t');
            return res.toString();
        }
    }

    public SimpleThrowablePrinter(PrintStream out) {
        this(out, false);
    }

    public SimpleThrowablePrinter(PrintStream out, boolean useSpaces) {
        this.out = out;
        this.useSpaces = useSpaces;
    }
    
    @Override
    public void printMain(String brief, List<StackTraceElementInfo> stackTrace) {
        out.println(brief);
        printStackTrace(stackTrace, stackTrace.size(), 1);
    }
    
    private void printStackTrace(List<StackTraceElementInfo> stackTrace, int framesToPrint, int tabs) {
        int cnt = 0;
        int omitted = 0;
        
        String t = ntabs(tabs);
        
        for(StackTraceElementInfo e: stackTrace) {
            if(cnt < framesToPrint) {
                out.print(t);
                out.print("at ");
                out.println(e);
            } else {
                ++omitted;
            }
            ++cnt;
        }        
        if(omitted > 0) {
            out.print(t);
            out.print("... ");
            out.print(omitted);
            out.println(" more");
        }
    }
    
    @Override
    public void printCausedBy(String brief, List<StackTraceElementInfo> stackTrace, int framesToPrint, int suppressedLevel) {
        out.print(ntabs(suppressedLevel));
        out.print("Caused by: ");
        out.println(brief);
        printStackTrace(stackTrace, framesToPrint, suppressedLevel+1);
        
    }

    @Override
    public void printSuppressed(String brief, List<StackTraceElementInfo> stackTrace, int framesToPrint, int suppressedLevel) {
        out.print(ntabs(suppressedLevel));
        out.print("Suppressed: ");
        out.println(brief);
        printStackTrace(stackTrace, framesToPrint, suppressedLevel+1);
    }

    @Override
    public void printError(String error, int suppressedLevel) {
        out.print(ntabs(suppressedLevel+1));
        out.println(error);
    }
    
    
}
