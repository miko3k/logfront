package org.deletethis.logfront.cfg;

/**
 *
 * @author miko
 */
public interface ConfigBackend {
    public CfgValue loadConfiguration();
    public void storeConfiguration(CfgValue value);
    public String normalizeString(String str);
}
