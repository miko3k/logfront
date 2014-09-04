package org.deletethis.logfront;

import javax.swing.Action;

public class PopupAction {

    private final String description;
    private final Runnable runnable;

    public PopupAction(String description, Runnable runnable) {
        this.description = description;
        this.runnable = runnable;
    }

    public PopupAction(final Action a) {
        this.description = a.getValue(Action.NAME).toString();
        this.runnable = new Runnable() {
            @Override
            public void run() {
                // null??
                a.actionPerformed(null);
            }
        };
    }

    public String getDescription() {
        return description;
    }

    public Runnable getRunnable() {
        return runnable;
    }
}
