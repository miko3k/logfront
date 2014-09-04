/*
 * xxx
 */

package org.deletethis.logfront.extras;

import java.util.ArrayList;
import java.util.List;
import org.deletethis.logfront.message.Name;
import org.deletethis.logfront.message.StackTraceElementInfo;
import org.deletethis.logfront.message.ThrowableInfo;

/**
 *
 * @author miko
 */
public class ThrowableInfoBuilder {
    public static ThrowableInfo createThrowableInfo(Throwable thr) {
        assert thr != null;
        
        List<ThrowableInfo> suppressed = new ArrayList<>();
        ThrowableInfo cause = null;
        
        List<StackTraceElementInfo> trace = new ArrayList<>();
        for(StackTraceElement e: thr.getStackTrace()) {
            //ame declaringClass, String methodName,
            //String fileName, Integer lineNumber, boolean nativeMethod
            StackTraceElementInfo ei
                    = new StackTraceElementInfo(
                            new Name(e.getClassName()),
                            e.getMethodName(),
                            e.getFileName(),
                            (e.getLineNumber() >= 0) ? e.getLineNumber() : null,
                            e.isNativeMethod());

            trace.add(ei);
        }
        
        Throwable thrCause = thr.getCause();
        if(thrCause != null) {
            cause = createThrowableInfo(thrCause);
        }
        
        for(Throwable s: thr.getSuppressed()) {
            suppressed.add(createThrowableInfo(s));
        }
        
        return new ThrowableInfo(
                thr.toString(),
                trace,
                cause,
                suppressed);
    }
}
