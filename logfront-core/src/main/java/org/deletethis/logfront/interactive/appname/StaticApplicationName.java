package org.deletethis.logfront.interactive.appname;

import org.deletethis.logfront.interactive.ApplicationNameResolver;

/**
 *
 * @author miko
 */
public class StaticApplicationName implements ApplicationNameResolver {
    private static String name = null;
    
    public static void setName(String name) { StaticApplicationName.name = name; }

    @Override
    public String getApplicationName() {
        return name;
    }
}
