/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.standalone.plugin;

import java.util.Objects;
import javax.swing.JButton;

/**
 *
 * @author miko
 */
public class ConnectUIEnvironmentImpl implements ConnectUIEnvironment {
    private final int gap;
    private final JButton button;
    
    public ConnectUIEnvironmentImpl(JButton button, int gap) { 
        this.gap = gap;
        this.button = Objects.requireNonNull(button); 
    }

    @Override
    public int getGapSize() {
        return gap;
    }

    @Override
    public void setConnectable(boolean connectable) {
        button.setEnabled(connectable);
    }
}
