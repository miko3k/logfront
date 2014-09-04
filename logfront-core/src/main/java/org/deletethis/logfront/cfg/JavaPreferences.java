package org.deletethis.logfront.cfg;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class JavaPreferences implements ConfigBackend {
    private final Preferences root;
    private final ListRemover lr;

    public static CfgValue loadRawValue(Preferences pref) throws BackingStoreException {
        String[] children = pref.childrenNames();
        String[] keys = pref.keys();

        MapCfgValue result = new MapCfgValue();

        for(String key : keys) {
            String value = pref.get(key, null);
            // check for null is probably not necessary
            if(value != null) {
                result.setMember(key, new ScalarCfgValue(value));
            }
        }

        for(String child : children) {
            CfgValue childValue = loadRawValue(pref.node(child));
            result.setMember(child, childValue);
        }

        return result;
    }

    public static void storeRawValue(Preferences pref, CfgValue value)
            throws BackingStoreException {
        
        pref.clear();
        String[] children = pref.childrenNames();
        for(String child : children) {
            pref.node(child).removeNode();
        }
        
        if(!value.isMap()) {
            throw new IllegalStateException("only map is supported!");
        }

        for(String key : value.getMemberNames()) {
            CfgValue val = value.getMember(key);
            
            if(val.isScalar()) {
                pref.put(key, val.getString(""));
            } else {
                storeRawValue(pref.node(key), val);
            }
        }
        pref.flush();
    }
    
    @Override
    public String normalizeString(String str)
    {
        return str.replace('.', '-').replace('_', '-');
    }

    private Preferences getPreferences(String [] path)
    {
        Preferences p = Preferences.userRoot();
        for(String s: path) {
            p = p.node(normalizeString(s));
        }
        return p;
    }
        
    public JavaPreferences(String... path)
    {
        root = getPreferences(path);
        lr = new ListRemover();
    }
    

    @Override
    public CfgValue loadConfiguration() {
        try {
            return lr.createLists(JavaPreferences.loadRawValue(root));
        } catch(BackingStoreException ex) {
            ex.printStackTrace(System.err);
            return DummyCfgValue.INSTANCE;
        }
    }

    @Override
    public void storeConfiguration(CfgValue v) {

        try {
            JavaPreferences.storeRawValue(root, lr.removeLists(v));
        } catch(BackingStoreException ex) {
            ex.printStackTrace(System.err);
        }    
    }       
}
