package org.deletethis.logfront.logback;

import org.deletethis.logfront.LogConsumer;
import org.deletethis.logfront.cfg.CfgValue;
import org.deletethis.logfront.standalone.plugin.ConnectUIEnvironment;
import org.deletethis.logfront.standalone.plugin.Plugin;
import org.deletethis.logfront.standalone.plugin.PluginConnectException;
import org.deletethis.logfront.standalone.plugin.PluginConnection;

/**
 *
 * @author miko
 */
public class SocketReceiverPlugin implements Plugin<Config> {
    @Override
    public SocketReceiverUI createConnectUI(ConnectUIEnvironment env, Config connectParameters) {
        return new SocketReceiverUI(env, connectParameters);
    }

    @Override
    public String paramsAsLongDescription(Config c) {
        return "Logback ServerSocketAppender at " + 
                c.getHostname() + ":" + c.getPort() + " (" + c.getInterval() + " ms)";
    }

    @Override
    public String paramsAsKey(Config c) {
        return c.getHostname() + ":" + c.getPort();
    }

    @Override
    public Config defaultConfig() {
        return new Config();
    }
    
    @Override
    public CfgValue serializeConfig(Config conf) {
        return conf.getCfgValue();
    }

    @Override
    public Config unserializeConfig(CfgValue cfg) {
        return new Config(cfg);
    }

    @Override
    public PluginConnection connect(Config connectParameters, LogConsumer logConsumer) throws PluginConnectException {
        DummyConnection dc = new DummyConnection(logConsumer);
        dc.start();
        return dc;
    }

}
