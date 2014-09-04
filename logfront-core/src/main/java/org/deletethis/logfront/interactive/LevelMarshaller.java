/*
 * xxx
 */

package org.deletethis.logfront.interactive;

import org.deletethis.logfront.cfg.CfgValue;
import org.deletethis.logfront.cfg.ScalarCfgValue;
import org.deletethis.logfront.cfg.serialize.ConfigMarshaller;
import org.deletethis.logfront.cfg.serialize.ConfigMarshallerContext;
import org.deletethis.logfront.message.Level;
import org.deletethis.logfront.message.LevelFactory;

/**
 *
 * @author miko
 */
public class LevelMarshaller implements ConfigMarshaller<Level> {
    final private LevelFactory levelFactory;

    public LevelMarshaller(LevelFactory levelFactory) {
        this.levelFactory = levelFactory;
    }
    
    @Override
    public boolean supports(Class<?> clz) {
        return Level.class.isAssignableFrom(clz);
    }

    @Override
    public CfgValue marshall(ConfigMarshallerContext context, Level object) {
        if(object == null)
            return null;
        
        return new ScalarCfgValue(object.getName());
    }

    @Override
    public Level unmarshall(ConfigMarshallerContext context, Class<Level> clz, CfgValue val) {
        Level level = levelFactory.getByName(val.getString(""));
        if(level == null)
            level = levelFactory.getDefaultLevel();
        return level;
    }
    
}
