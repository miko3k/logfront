/*
 * xxx
 */

package org.deletethis.logfront.cfg.serialize;

import org.deletethis.logfront.cfg.CfgValue;
import org.deletethis.logfront.cfg.ConfigBackend;

/**
 *
 * @author miko
 */
public class SimpleContext implements ConfigMarshallerContext {
    private final ConfigMarshaller<Object> marshaller;
    private final ConfigBackend backend;

    public SimpleContext(ConfigMarshaller<Object> marshaller, ConfigBackend backend) {
        this.marshaller = marshaller;
        this.backend = backend;
    }

    @Override
    public String normalizeString(String str) {
        return backend.normalizeString(str);
    }

    @Override
    public CfgValue marshall(Object object) {
        return marshaller.marshall(this, object);
    }

    @Override
    @SuppressWarnings("unchecked") // TODO: think about this
    public <T> T unmarshall(Class<T> clz, CfgValue val) {
        return (T)marshaller.unmarshall(this, (Class<Object>) clz, val);
    }
    
    
}
