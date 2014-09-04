package org.deletethis.logfront.selflog;

/**
 *
 * @author miko
 */
public class SelfLogger {
    private final String name;

    SelfLogger(String name) {
        this.name = name;
    }
    
    public void info(Object str)
    {
        System.out.println(name + " (INFO): " + str);
    }

    public void debug(Object str)
    {
        System.out.println(name + " (DEBUG): " + str);
    }
    
}
