/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.deletethis.logfront.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.LifeCycle;

public class ListenerWrapper implements LoggerContextListener {

    private final LifeCycle wrapped;

    ListenerWrapper(LifeCycle wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean isResetResistant() {
        return false;
    }

    @Override
    public void onStart(LoggerContext context) {
        wrapped.start();
    }

    @Override
    public void onReset(LoggerContext context) {
        wrapped.stop();
    }

    @Override
    public void onStop(LoggerContext context) {
        wrapped.stop();
    }

    @Override
    public void onLevelChange(Logger logger, Level level) {
    }
}
