/*
 * xxx
 */

package org.deletethis.logfront.interactive;

import java.awt.Rectangle;
import org.deletethis.logfront.cfg.CfgValue;
import org.deletethis.logfront.cfg.MapCfgValue;
import org.deletethis.logfront.cfg.ScalarCfgValue;
import org.deletethis.logfront.cfg.serialize.ConfigMarshaller;
import org.deletethis.logfront.cfg.serialize.ConfigMarshallerContext;

/**
 *
 * @author miko
 */
public class RectangleMarshaller implements ConfigMarshaller<Rectangle> {

    @Override
    public boolean supports(Class<?> clz) {
        return Rectangle.class.isAssignableFrom(clz);
    }

    @Override
    public CfgValue marshall(ConfigMarshallerContext context, Rectangle rc) {
        MapCfgValue result = new MapCfgValue();
        result.setMember("x", new ScalarCfgValue(rc.x));
        result.setMember("y", new ScalarCfgValue(rc.y));
        result.setMember("w", new ScalarCfgValue(rc.width));
        result.setMember("h", new ScalarCfgValue(rc.height)); 
        return result;
    }

    @Override
    public Rectangle unmarshall(ConfigMarshallerContext context, Class<Rectangle> clz, CfgValue data) {
        Integer cfgX = data.getMember("x").getInteger(null);
        Integer cfgY = data.getMember("y").getInteger(null);
        Integer cfgW = data.getMember("w").getInteger(null);
        Integer cfgH = data.getMember("h").getInteger(null);        
        
       if(cfgX != null && cfgY != null && cfgW != null && cfgH != null)
            return new Rectangle(cfgX, cfgY, cfgW, cfgH);
        else
            return null;        
    }
    
}
