package org.deletethis.logfront.interactive.appname;

import org.deletethis.logfront.interactive.ApplicationNameResolver;

/**
 *
 * @author miko
 */
public class SunJavaCommand implements ApplicationNameResolver {

    private final static String PROPERTY = "sun.java.command";
    // This seems to work even with various VM args on command line.
    // Also, code found here
    //   <http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/7-b147/sun/jvmstat/monitor/MonitoredVmUtil.java#MonitoredVmUtil.mainClass%28sun.jvmstat.monitor.MonitoredVm%2Cboolean%29>
    // Uses similar approach. I'm not sure if this property is same as "sun.rt.javaCommand" though.
    
    private String mainClass(String commandLine) {
        if(commandLine == null || commandLine.isEmpty())
            return null;
        
        String arg0 = commandLine;

        int firstSpace = commandLine.indexOf(' ');
        if(firstSpace > 0) {
            arg0 = commandLine.substring(0, firstSpace);
        }
        return arg0;
    }

    @Override
    public String getApplicationName() {
        String cmd = System.getProperty(PROPERTY);
        if(cmd != null) {
            return mainClass(cmd);
        } else {
            return null;
        }
    }
}
