package org.deletethis.logfront.selflog;

/**
 *
 * @author miko
 */
public class SelfLoggerFactory {
    public static SelfLogger getLogger(Class clz)
    {
        return new SelfLogger(clz.getName());
    }
}
