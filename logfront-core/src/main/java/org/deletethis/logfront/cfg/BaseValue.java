package org.deletethis.logfront.cfg;

import java.util.Collections;

/**
 *
 * @author miko
 */
public class BaseValue implements CfgValue {

    @Override
    public boolean isMap() {
        return false;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public boolean isScalar() {
        return false;
    }

    @Override
    public boolean isDummy() {
        return false;
    }
    
    @Override
    public Integer getInteger(Integer def) {
        return def;
    }

    @Override
    public String getString(String def) {
        return def;
    }

    @Override
    public Float getFloat(Float def) {
        return def;
    }

    @Override
    public Boolean getBoolean(Boolean def) {
        return def;
    }

    @Override
    public CfgValue getMember(String name) {
        return DummyCfgValue.INSTANCE;
    }

    @Override
    public Iterable<String> getMemberNames() {
        return Collections.emptyList();
    }

    @Override
    public CfgValue getListItem(int n) {
        return DummyCfgValue.INSTANCE;
    }

    @Override
    public int getListSize() {
        return 0;
    }
}
