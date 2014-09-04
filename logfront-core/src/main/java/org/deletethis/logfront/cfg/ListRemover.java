/*
 * xxx
 */

package org.deletethis.logfront.cfg;

/**
 *
 * @author miko
 */
public class ListRemover {
    public CfgValue removeLists(CfgValue value)
    {
        if(value.isMap()) {
            MapCfgValue result = new MapCfgValue();
            
            for(String key: value.getMemberNames()) {
                CfgValue prev = value.getMember(key);
                if(!prev.isDummy())
                    result.setMember(key, removeLists(prev));
            }
            
            return result;
        }
        
        if(value.isList()) {
            MapCfgValue result = new MapCfgValue();
            int size = value.getListSize();
            
            for(int i=0;i<size;++i) {
                CfgValue prev = value.getListItem(i);
                if(!prev.isDummy())
                    result.setMember(String.valueOf(i), removeLists(prev));
            }
            
            return result;
        }
        
        return value;
    }
    
    public CfgValue createLists(CfgValue value)
    {
        if(value.isMap()) {
            Boolean isList = null;
            
            for(String key: value.getMemberNames()) {
                try {
                    int n = Integer.valueOf(key);
                    // let's hope lists will be never larger that this, it's
                    // a safeguard against some weird hash maps or something
                    // where keys look like numbers
                    if(n < 0 || n > 1000000)  {
                        isList = false;
                        break;
                    } else {
                        isList = true;
                    }
                } catch(NumberFormatException ex) {
                    isList = false;
                    break;
                }
            }
            if(isList != null && isList) {
                ListCfgValue result = new ListCfgValue();
                for(String key: value.getMemberNames()) {
                    CfgValue prev = value.getMember(key);
                    if(prev.isDummy())
                        continue;
                    
                    
                    int idx = Integer.valueOf(key);
                    while(result.getListSize() <= idx)
                        result.addListItem(null);
                    
                    result.setListItem(idx, createLists(prev));
                }
                return result;
            } else {
                MapCfgValue result = new MapCfgValue();
                
                for (String key : value.getMemberNames()) {
                    CfgValue prev = value.getMember(key);
                    if (!prev.isDummy()) {
                        result.setMember(key, createLists(prev));
                    }
                }
                
                return result;
            }
        } else {
            return value;
        }
    }
}
