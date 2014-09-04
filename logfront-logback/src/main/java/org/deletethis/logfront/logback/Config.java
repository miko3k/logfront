/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.logback;

import org.deletethis.logfront.cfg.CfgValue;
import org.deletethis.logfront.cfg.MapCfgValue;
import org.deletethis.logfront.cfg.ScalarCfgValue;

/**
 *
 * @author miko
 */
public class Config {
    private String hostname;
    private int port;
    private int interval;
    
    public Config() { this(new MapCfgValue()); }
            
    
    public Config(CfgValue cfg)
    {
        hostname = cfg.getMember("hostname").getString("localhost");
        port = cfg.getMember("port").getInteger(9000);
        interval = cfg.getMember("interval").getInteger(1000);
    }
    
    public CfgValue getCfgValue()
    {
        MapCfgValue result = new MapCfgValue();

        result.setMember("hostname", new ScalarCfgValue(hostname));
        result.setMember("port", new ScalarCfgValue(port));
        result.setMember("interval", new ScalarCfgValue(interval));
        
        return result;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
    
    
}
