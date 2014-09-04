/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.logback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.deletethis.logfront.standalone.plugin.Plugin;
import org.deletethis.logfront.standalone.plugin.PluginProvider;

/**
 *
 * @author miko
 */
public class PluginList implements PluginProvider {
    final private List<Plugin<?>> plugins;

    public PluginList() {
        plugins = new ArrayList<>();
        plugins.add(new SocketReceiverPlugin());
    }
    
    @Override
    public Iterator<Plugin<?>> iterator() {
        return plugins.iterator();
    }
    
}
