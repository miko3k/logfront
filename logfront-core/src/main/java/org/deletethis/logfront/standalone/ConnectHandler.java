/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.standalone;

import org.deletethis.logfront.standalone.plugin.Plugin;

/**
 *
 * @author miko
 */
public interface ConnectHandler {
    <T> void doConnect(Plugin<T> plugin, T params);
}
