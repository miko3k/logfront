/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.standalone.plugin;

/**
 *
 * @author miko
 */
public class PluginConnectException extends Exception {

    public PluginConnectException() {
    }

    public PluginConnectException(String message) {
        super(message);
    }

    public PluginConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginConnectException(Throwable cause) {
        super(cause);
    }
}
