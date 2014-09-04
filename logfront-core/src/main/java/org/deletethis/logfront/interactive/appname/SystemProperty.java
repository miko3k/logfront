package org.deletethis.logfront.interactive.appname;

import org.deletethis.logfront.interactive.ApplicationNameResolver;

/**
 *
 * @author miko
 */
public class SystemProperty implements ApplicationNameResolver {
    public final static String PROPERTY_NAME = "org.deletehis.logfront.application";
    
    @Override
    public String getApplicationName() {
        return System.getProperty(PROPERTY_NAME);
    }
}
