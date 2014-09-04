/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.standalone.plugin;

import java.awt.Component;

/**
 *
 * @author miko
 * @param <CONF> class which holds configuration
 */
public interface ConnectUI<CONF> {
    public Component getComponent();
    public String getConnectTitle();
    public String getConnectButtonTitle();
    public CONF getConfiguration();
}
