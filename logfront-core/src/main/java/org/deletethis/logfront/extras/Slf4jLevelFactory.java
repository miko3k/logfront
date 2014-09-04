/*
 * xxx
 */

package org.deletethis.logfront.extras;

import java.util.HashMap;
import java.util.Map;
import org.deletethis.logfront.message.Level;
import org.deletethis.logfront.message.LevelFactory;
import org.deletethis.logfront.message.SimpleLevel;

/**
 *
 * @author miko
 */
public class Slf4jLevelFactory implements LevelFactory {
    private static final Map<String, Level> levelByName = new HashMap<>();

    private static Level createLevel(String name, int severity, float hue) {
        SimpleLevel simpleLevel = new SimpleLevel(name, severity, hue);
        levelByName.put(name, simpleLevel);
        return simpleLevel;
    }
    
    public static final Level TRACE = createLevel("TRACE", 1, 208f / 360f);
    public static final Level DEBUG = createLevel("DEBUG", 2, 188f / 360f);
    public static final Level INFO = createLevel("INFO", 3, 128f / 360f);
    public static final Level WARN = createLevel("WARN", 4, 30f / 360f);
    public static final Level ERROR = createLevel("ERROR", 5, 0f / 360f);

    @Override
    public Level getByName(String name) {
        return levelByName.get(name);
    }

    @Override
    public Level getDefaultLevel() {
        return INFO;
    }
}
