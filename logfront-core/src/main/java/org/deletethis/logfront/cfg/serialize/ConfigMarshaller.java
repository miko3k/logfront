/*
 * xxx
 */

package org.deletethis.logfront.cfg.serialize;

import org.deletethis.logfront.cfg.CfgValue;

/**
 *
 * @author miko
 */
public interface ConfigMarshaller<T> {
    public boolean supports(Class<?> clz);
    public CfgValue marshall(ConfigMarshallerContext context, T object);
    public T unmarshall(ConfigMarshallerContext context, Class<T> clz, CfgValue val);
}
