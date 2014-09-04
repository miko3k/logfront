package org.deletethis.logfront.cfg;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author miko
 */
public class MapCfgValue extends BaseValue {

    private final Map<String, CfgValue> members = new HashMap<>();

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public CfgValue getMember(String name) {
        CfgValue val = members.get(name);
        if (val == null) {
            return DummyCfgValue.INSTANCE;
        } else {
            return val;
        }

    }
    @Override
    public Iterable<String> getMemberNames()
    {
        return members.keySet();
    }
    
    public void setMember(String name, CfgValue value)
    {
        if(value == null || value.isDummy()) {
            members.remove(name);
        } else {
            members.put(name, value);
        }
    }
    
    @Override
    public String toString() { return members.toString(); }
}
