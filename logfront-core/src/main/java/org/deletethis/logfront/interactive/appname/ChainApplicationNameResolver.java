package org.deletethis.logfront.interactive.appname;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.deletethis.logfront.interactive.ApplicationNameResolver;

/**
 *
 * @author miko
 */
public class ChainApplicationNameResolver implements ApplicationNameResolver {
    final private List<ApplicationNameResolver> resolvers = new ArrayList<>();
    
    public ChainApplicationNameResolver() { }
    public ChainApplicationNameResolver(ApplicationNameResolver r) { resolvers.add(r); }
    public ChainApplicationNameResolver(Collection<ApplicationNameResolver> r) { resolvers.addAll(r); }
    public void add(ApplicationNameResolver r) { resolvers.add(r); }
    public void add(Collection<ApplicationNameResolver> r) { resolvers.addAll(r); }

    @Override
    public String getApplicationName() {
        for(ApplicationNameResolver r: resolvers) {
            String name = r.getApplicationName();
            if(name != null)
                return name;
        }
        return null;
    }
}
