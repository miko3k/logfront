/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.standalone.plugin;

import org.deletethis.logfront.LogConsumer;
import org.deletethis.logfront.cfg.CfgValue;

/**
 *
 * @author miko
 * @param <CONF> class which holds configuration
 */
public interface Plugin<CONF> {
    ConnectUI<CONF> createConnectUI(ConnectUIEnvironment env, CONF connectParameters);
    public CONF defaultConfig();
    public CfgValue serializeConfig(CONF conf);
    public CONF unserializeConfig(CfgValue cfg);
    public String paramsAsLongDescription(CONF connectParameters);
    public String paramsAsKey(CONF connectParameters);
    public PluginConnection connect(CONF connectParameters, LogConsumer logConsumer) throws PluginConnectException;
}
