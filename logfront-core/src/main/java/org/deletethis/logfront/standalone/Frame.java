/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.standalone;

import java.awt.Container;
import java.awt.Dimension;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.deletethis.logfront.MainPanel;
import org.deletethis.logfront.SwingLogConsumer;
import org.deletethis.logfront.colors.GlobalStyle;
import org.deletethis.logfront.colors.GlobalStyleImpl;
import org.deletethis.logfront.extras.ConsoleLogConsumer;
import org.deletethis.logfront.standalone.plugin.Plugin;
import org.deletethis.logfront.standalone.plugin.PluginConnectException;

/**
 *
 * @author miko
 */
public class Frame extends JFrame implements ConnectHandler {
    private final GlobalStyle style;
    
    public Frame(GlobalStyle style, Collection<Plugin<?>> plugins)
    {
        this.style = style;
        
        setMinimumSize(new Dimension(400, 200));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        Container contentPane = new ConnectPane(style, plugins, this);
        setContentPane(contentPane);
    }

    @Override
    public <T> void doConnect(Plugin<T> plugin, T params) {
        System.out.println(plugin + "|" + params);
        try {
            MainPanel mainPanel = new MainPanel(style);

            plugin.connect(params, new SwingLogConsumer(mainPanel));
            
            setContentPane(mainPanel);
            setTitle(plugin.paramsAsLongDescription(params));
            
            revalidate();
        } catch (PluginConnectException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
