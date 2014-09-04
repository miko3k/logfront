/*
 * xxx
 */

package org.deletethis.logfront.cfg.serialize;

import org.deletethis.logfront.cfg.CfgValue;

/**
 *
 * @author miko
 */
public interface ConfigMarshallable {
    public CfgValue marshall(ConfigMarshallerContext s);
}
