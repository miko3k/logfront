/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.deletethis.logfront.logback;

import java.awt.Component;
import org.deletethis.logfront.standalone.plugin.ConnectUI;
import org.deletethis.logfront.standalone.plugin.ConnectUIEnvironment;

/**
 *
 * @author miko
 */
public class SocketReceiverUI implements ConnectUI<Config> {

    private final SocketReceiverPluginPanel panel;

    public SocketReceiverUI(ConnectUIEnvironment env, Config connectParameters) {
        panel = new SocketReceiverPluginPanel(connectParameters, env);
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public String getConnectTitle() {
        return "Connect to Logback ServerSocketAppender";
    }

    @Override
    public String getConnectButtonTitle() {
        return "Connect";
    }

    @Override
    public Config getConfiguration() {
        return panel.getConnectionParameters();
    }

}
