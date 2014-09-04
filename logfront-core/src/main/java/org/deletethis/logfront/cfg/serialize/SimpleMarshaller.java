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
 * @param <T>
 */
public class SimpleMarshaller<T extends ConfigMarshallable> implements ConfigMarshaller<T> {

    @Override
    public boolean supports(Class<?> clz) {
        return ConfigMarshallable.class.isAssignableFrom(clz);
    }

    @Override
    public CfgValue marshall(ConfigMarshallerContext context, T object) {
        ConfigMarshallable cs = (ConfigMarshallable)object;
        return cs.marshall(context);
    }

    @Override
    public T unmarshall(ConfigMarshallerContext context, Class<T> clz, CfgValue val) {
        return ReflectUtil.construct(clz, context, val);
    }
}
