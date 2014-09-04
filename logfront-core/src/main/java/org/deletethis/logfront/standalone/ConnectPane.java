/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.deletethis.logfront.standalone;

import org.deletethis.logfront.standalone.plugin.ConnectUI;
import org.deletethis.logfront.standalone.plugin.Plugin;
import org.deletethis.logfront.standalone.plugin.ConnectUIEnvironment;
import org.deletethis.logfront.standalone.plugin.ConnectUIEnvironmentImpl;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.deletethis.logfront.colors.GlobalStyle;

/**
 *
 * @author miko
 */
public class ConnectPane extends JPanel {
    final private ConnectHandler connectHandler;

    private <T> JPanel createOne(GlobalStyle style, final Plugin<T> p) {
        int gap = style.getBasicGap();

        JButton button = new JButton();

        ConnectUIEnvironment env = new ConnectUIEnvironmentImpl(button, gap);

        final ConnectUI<T> connectUI = p.createConnectUI(env, p.defaultConfig());

        button.setText(connectUI.getConnectButtonTitle());

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(connectUI.getConnectTitle()));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(gap, gap, gap, gap);
        panel.add(connectUI.getComponent(), c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(gap, 0, gap, gap);

            //panel.setLayout(new BorderLayout());
        panel.add(button, c);
        
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                connectHandler.doConnect(p, connectUI.getConfiguration());
                /*System.out.println(connectUI.getConnectUIParameters().getConnectionParameters());
                System.out.println(p.paramsAsLongDescription(connectUI.getConnectUIParameters().getConnectionParameters()));*/
            }
        });
        
        return panel;
    }

    public ConnectPane(GlobalStyle style, Collection<Plugin<?>> plugins, ConnectHandler connectHandler) {
        this.connectHandler = connectHandler;
        setLayout(new GridBagLayout());

        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 0;
        c2.gridy = GridBagConstraints.RELATIVE;
        c2.weightx = 1;
        c2.weighty = 0;
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.anchor = GridBagConstraints.PAGE_START;

        for (Plugin<?> p : plugins) {
            JPanel panel = createOne(style, p);
            add(panel, c2);
        }
        
        c2.weighty = 1;
        c2.fill = GridBagConstraints.BOTH;
        add(Box.createVerticalGlue(), c2);
    }
}
