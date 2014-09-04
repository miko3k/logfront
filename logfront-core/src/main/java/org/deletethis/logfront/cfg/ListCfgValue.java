package org.deletethis.logfront.cfg;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author miko
 */
public class ListCfgValue extends BaseValue {

    private final List<CfgValue> lst = new ArrayList<>();

    @Override
    public CfgValue getListItem(int n) {
        CfgValue result = null;
        if (n < lst.size()) {
            result = lst.get(n);
        }
        if (result == null) {
            result = DummyCfgValue.INSTANCE;
        }

        return result;
    }

    @Override
    public boolean isList()
    {
        return true;
    }
    
    @Override
    public int getListSize() {
        return lst.size();
    }
    
    private CfgValue norm(CfgValue value)
    {
        if(value == null)
            return null;
        if(value.isDummy())
            return null;
        return value;
    }
    
    public void setListItem(int n, CfgValue val)
    {
        lst.set(n, norm(val));
    }

    public void addListItem(CfgValue val)
    {
        lst.add(norm(val));
    }
    
    @Override
    public String toString() { return lst.toString(); }
    
    public boolean isEmpty() { return lst.isEmpty(); }
    
}
