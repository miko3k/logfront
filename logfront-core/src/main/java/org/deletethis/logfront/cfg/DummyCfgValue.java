package org.deletethis.logfront.cfg;

/**
 *
 * @author miko
 */
public class DummyCfgValue extends BaseValue {
    static final public DummyCfgValue INSTANCE = new DummyCfgValue();
    private DummyCfgValue() { }
    @Override
    public boolean isDummy() {
        return true;
    }    
    @Override
    public String toString()
    {
        return "DUMMY VALUE";
    }
}
