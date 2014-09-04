package org.deletethis.logfront.interactive.appname;

import org.deletethis.logfront.interactive.ApplicationNameResolver;

/**
 *
 * @author miko
 */
public class ApplicationNameResolverDefault {
    public static ApplicationNameResolver getDefault()
    {
        ChainApplicationNameResolver result = new ChainApplicationNameResolver();
        result.add(new StaticApplicationName());
        result.add(new SystemProperty());
        result.add(new StackTrace());
        result.add(new SunJavaCommand());
        return result;
    }
}
