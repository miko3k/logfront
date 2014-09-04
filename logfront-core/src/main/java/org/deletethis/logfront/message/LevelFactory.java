package org.deletethis.logfront.message;

/**
 *
 * @author miko
  */
public interface LevelFactory {
    public Level getByName(String name);
    public Level getDefaultLevel();
}
