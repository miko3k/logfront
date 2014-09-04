/*
 * xxx
 */

package org.deletethis.logfront.cfg.serialize;

import org.deletethis.logfront.cfg.CfgValue;
import org.deletethis.logfront.cfg.MapCfgValue;

/**
 *
 * @author miko
 */
public interface ConfigMapMarhallable {
    public void marshallInto(MapCfgValue out, ConfigMarshallerContext s);
}
