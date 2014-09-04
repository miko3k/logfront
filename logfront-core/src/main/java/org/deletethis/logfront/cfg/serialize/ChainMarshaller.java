/*
 * xxx
 */

package org.deletethis.logfront.cfg.serialize;

import java.util.ArrayList;
import java.util.List;
import org.deletethis.logfront.cfg.CfgValue;

/**
 *
 * @author miko
 */
public class ChainMarshaller implements ConfigMarshaller<Object> {
    private List<ConfigMarshaller<Object>> list = new ArrayList<>();
    
    @SuppressWarnings("unchecked")
    public void addMarshaller(ConfigMarshaller<?> s) { list.add((ConfigMarshaller<Object>)s); }
    
    @Override
    public boolean supports(Class<?> clz) {
        for(ConfigMarshaller<Object> s: list) {
            if(s.supports(clz))
                return true;
        }
        return false;
    }

    @Override
    public CfgValue marshall(ConfigMarshallerContext context, Object object) {
        if(object == null)
            return null;
        
        Class clz = object.getClass();
        for(ConfigMarshaller<Object> s: list) {
            if(s.supports(clz))
                return s.marshall(context, object);
        }
        throw new IllegalArgumentException("unable to find serializer for " + clz);
    }

    @Override
    public Object unmarshall(ConfigMarshallerContext context, Class<Object> clz, CfgValue val) {
        if(val == null || val.isDummy())
            return null;
        
        for(ConfigMarshaller<Object> s: list) {
            if(s.supports(clz))
                return s.unmarshall(context, clz, val);
        }
        throw new IllegalArgumentException("unable to find deserializer for " + clz);
    }
}
