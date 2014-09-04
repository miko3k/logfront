/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.logback;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.deletethis.logfront.standalone.plugin.ConnectUIEnvironment;


/**
 *
 * @author miko
 */
public class SocketReceiverPluginPanel extends JPanel {
    private static final long serialVersionUID = 1345892390423202221L;
    private final JTextField hostname;
    private final JTextField port;
    private final JTextField interval;
    private final ConnectUIEnvironment env;

    private class Validator implements DocumentListener {
        private boolean onlyDigits(String str)
        {
            int len = str.length();
            for(int i=0;i<len;++i) {
                char c = str.charAt(i);
                if(c < '0' || c > '9')
                    return false;
            }
            return true;
        }
        
        private boolean doValidate()
        {
            String h = hostname.getText().trim();
            String p = port.getText().trim();
            String i = interval.getText().trim();
            
            return !h.isEmpty() && !p.isEmpty() && !i.isEmpty() && 
                    onlyDigits(p) && onlyDigits(i);
        }
        
        public void validate() {
            if(doValidate()) {
                env.setConnectable(true);
            } else {
                env.setConnectable(false);
            }
        }
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            validate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validate();
        }
    }
    
    private final Validator validator = new Validator();
    
    public SocketReceiverPluginPanel(Config connectParameters, ConnectUIEnvironment env)
    {
        this.env = env;
        
        int gap = env.getGapSize();
        
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(0, 0, gap, gap);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = gbc.weighty = 0;
        add(new JLabel("Hostname"), gbc);
        add(new JLabel("Port"), gbc);
        
        gbc.insets = new Insets(0, 0, 0, gap);
        add(new JLabel("Reconnect interval"), gbc);
        
        hostname = new JTextField();
        port = new JTextField();
        interval = new JTextField();
        hostname.getDocument().addDocumentListener(validator);
        port.getDocument().addDocumentListener(validator);
        interval.getDocument().addDocumentListener(validator);
        port.setColumns(8);
        interval.setColumns(8);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, gap, 0);
        gbc.weightx = 1;
        add(hostname, gbc);
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(port, gbc);
        
        gbc.insets = new Insets(0, 0, 0, 0);
        add(interval, gbc);

        setConnectionParameters(connectParameters);
        validator.validate();
        
    }

    public Config getConnectionParameters() {
        Config cfg = new Config();
        cfg.setHostname(hostname.getText().trim());
        cfg.setPort(Integer.valueOf(port.getText().trim()));
        cfg.setInterval(Integer.valueOf(interval.getText().trim()));
        return cfg;
    }

    final public void setConnectionParameters(Config cfg) {
        hostname.setText(cfg.getHostname());
        port.setText(String.valueOf(cfg.getPort()));
        interval.setText(String.valueOf(cfg.getInterval()));
    }
    
}
