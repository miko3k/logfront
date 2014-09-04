package org.deletethis.logfront.interactive.appname;

import java.util.Map.Entry;
import org.deletethis.logfront.interactive.ApplicationNameResolver;

/**
 *
 * @author miko
 */
public class StackTrace implements ApplicationNameResolver {

    private String examineStackTraceElement(StackTraceElement stackTraceElement) {
        if (stackTraceElement.getMethodName().equals("main")) {

            try {
                Class<?> c = Class.forName(stackTraceElement.getClassName());
                Class[] argTypes = new Class[]{String[].class};
                //This will throw NoSuchMethodException in case of fake main methods
                c.getDeclaredMethod("main", argTypes);

                return stackTraceElement.getClassName();
            } catch (NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace(System.err);
            }
        }
        return null;
    }

    @Override
    public String getApplicationName() {
        for (Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            Thread thread = entry.getKey();

            if (thread.getThreadGroup() != null && thread.getThreadGroup().getName().equals("main")) {

                for (StackTraceElement stackTraceElement : entry.getValue()) {

                    String res = examineStackTraceElement(stackTraceElement);
                    if (res != null) {
                        return res;
                    }
                }
            }
        }
        return null;    
    }
}
