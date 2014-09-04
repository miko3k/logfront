package org.deletethis.logfront.standalone;

import org.deletethis.logfront.standalone.plugin.PluginProvider;
import org.deletethis.logfront.standalone.plugin.Plugin;
import java.util.HashSet;
import java.util.Set;
import javax.swing.SwingUtilities;
import org.deletethis.logfront.colors.GlobalStyleImpl;
import org.deletethis.logfront.colors.GlobalStyle;
import org.deletethis.logfront.selflog.SelfLogger;
import org.deletethis.logfront.selflog.SelfLoggerFactory;

/**
 *
 * @author miko
 */
public class Main {
    private static final SelfLogger logger = SelfLoggerFactory.getLogger(Main.class);
    
    private static PluginProvider stringPluginProvider(String s) {
        try {
            Class<?> forName = Class.forName(s);
            Object newInstance = forName.newInstance();

            if (newInstance instanceof PluginProvider) {
                return (PluginProvider) newInstance;
            } else {
                logger.info("plugin provider " + s + " is not actually plugin provider");
            }
        } catch (ClassNotFoundException ex) {
            logger.info("plugin provider " + s + " not found in classpath");
        } catch (InstantiationException ex) {
            logger.info("unable to instantiate plugin provider: " + s);
        } catch (IllegalAccessException ex) {
            logger.info("illegal access when instantiating plugin provider: " + s);
        }
        return null;
    }
    
    public static void main(String [] args)
    {
        final Set<Plugin<?>> plugins = new HashSet<>();
        for(String s: args) {
            PluginProvider provider = stringPluginProvider(s);
            if(provider != null) {
                for(Plugin<?> plugin: provider) {
                    plugins.add(plugin);
                }
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GlobalStyle style = new GlobalStyleImpl();
                Frame f = new Frame(style, plugins);
                f.setVisible(true);
            }
        });
    }
}
