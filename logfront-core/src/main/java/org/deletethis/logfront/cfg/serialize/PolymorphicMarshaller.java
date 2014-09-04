/*
 * xxx
 */

package org.deletethis.logfront.cfg.serialize;

import org.deletethis.logfront.cfg.CfgValue;
import org.deletethis.logfront.cfg.MapCfgValue;
import org.deletethis.logfront.cfg.ScalarCfgValue;

/**
 *
 * @author miko
 * @param <T>
 */
public class PolymorphicMarshaller<T extends ConfigMapMarhallable> implements ConfigMarshaller<T> {

    @Override
    public boolean supports(Class<?> clz) {
        return ConfigMapMarhallable.class.isAssignableFrom(clz);
    }

    @Override
    public CfgValue marshall(ConfigMarshallerContext context, T object) {
        ConfigMapMarhallable cs = (ConfigMapMarhallable)object;
        MapCfgValue result = new MapCfgValue();
        cs.marshallInto(result, context);
        result.setMember("class", new ScalarCfgValue(object.getClass().getCanonicalName()));
        return result;
    }

    @Override
    public T unmarshall(ConfigMarshallerContext context, Class<T> clzIgnored, CfgValue val) {
        String className = val.getMember("class").getString(null);
        if(className == null)
            throw new IllegalArgumentException("no class name found in " + val);
        
        Class<?> clz;
        try {
            clz = Class.forName(className);
        } catch(ClassNotFoundException ex) {
            throw new IllegalArgumentException("class not found: ", ex);
        }
        Object res = ReflectUtil.construct(clz, context, val);
        @SuppressWarnings("unchecked")
        T t = (T)res;
        return t;
    }
}
