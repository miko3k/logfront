/*
 * xxx
 */

package org.deletethis.logfront.cfg.serialize;

import org.deletethis.logfront.cfg.CfgValue;
import org.deletethis.logfront.cfg.ScalarCfgValue;

/**
 *
 * @author miko
 */
public class EnumMarshaller implements ConfigMarshaller<Enum> {

    @Override
    public boolean supports(Class<?> clz) {
        return clz.isEnum();
    }

    @Override
    public CfgValue marshall(ConfigMarshallerContext context, Enum object) {
        return new ScalarCfgValue(object.toString());
    }

    @Override
    public Enum unmarshall(ConfigMarshallerContext context, Class<Enum> clz, CfgValue val) {
        Enum[] enumConstants = clz.getEnumConstants();
        for(Enum t: enumConstants) {
            if(t.toString().equals(val.getString(null))) {
                return t;
            }
        }
        if(enumConstants.length > 0) {
            return enumConstants[0];
        } else {
            return null;
        }
    }
    
}
