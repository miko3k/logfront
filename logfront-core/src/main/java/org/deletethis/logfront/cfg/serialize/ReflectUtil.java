/*
 * xxx
 */
package org.deletethis.logfront.cfg.serialize;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.deletethis.logfront.cfg.CfgValue;

/**
 *
 * @author miko
 */
public class ReflectUtil {

    public static <T> T construct(Class<T> clz, ConfigMarshallerContext context, CfgValue value) {
        Constructor<T> constructor;
        try {
            constructor = clz.getConstructor(ConfigMarshallerContext.class, CfgValue.class);
        } catch(NoSuchMethodException ex) {
            throw new IllegalArgumentException(clz.getCanonicalName() + " is missing proper constructor", ex);
        }
        T res;
        try {
            res = constructor.newInstance(context, value);
        } catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException("error invoking constructor on " + clz.getCanonicalName(), ex);
        }
        return res;

    }

}
