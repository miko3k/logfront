/*
 * xxx
 */

package org.deletethis.logfront.cfg.serialize;

import org.deletethis.logfront.cfg.CfgValue;

/**
 *
 * @author miko
 */
public interface ConfigMarshallerContext {
    public String normalizeString(String str);
    public CfgValue marshall(Object object);
    public <T> T unmarshall(Class<T> clz, CfgValue val);    
}
