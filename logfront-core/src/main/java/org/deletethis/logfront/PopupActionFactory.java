package org.deletethis.logfront;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.deletethis.logfront.FilterStorage.Slot;
import org.deletethis.logfront.colors.MatcherStyle;
import org.deletethis.logfront.impl.LevelMatcher;
import org.deletethis.logfront.impl.LoggerMatcher;
import org.deletethis.logfront.impl.ThreadNameMatcher;
import org.deletethis.logfront.message.Level;
import org.deletethis.logfront.message.Name;
import org.deletethis.logfront.message.SimpleThrowablePrinter;
import org.deletethis.logfront.message.ThrowablePrinter;
import org.deletethis.logfront.widgets.tilepane.log.Clickable;

public class PopupActionFactory {

    private void addAll(List<PopupAction> popupActions,
            final FilterStorage filterStorage, final String desc, final Slot slot, Matcher[] matchers) {
        for(final Matcher m : matchers) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    filterStorage.add(slot, m, true);
                }
            };

            popupActions.add(new PopupAction(desc + " " + m.getShortDescription(), runnable));
        }
    }

    private List<PopupAction> createList(FilterStorage filterStorage, Matcher... matchers) {
        List<PopupAction> result = new ArrayList<>();

        addAll(result, filterStorage, "Do not show", Slot.DO_NOT_SHOW, matchers);
        addAll(result, filterStorage, "Show only", Slot.SHOW_ONLY, matchers);

        return result;
    }

    public List<PopupAction> getPopupActions(FilterStorage filterStorage, final Clickable clickable) {
        if(clickable.isLevel()) {
            Level lev = clickable.getLogMessage().getLevel();
            return createList(filterStorage,
                    new LevelMatcher(lev, LevelMatcher.Operation.SAME),
                    new LevelMatcher(lev, LevelMatcher.Operation.SAME_OR_LESS_SEVERE),
                    new LevelMatcher(lev, LevelMatcher.Operation.SAME_OR_MORE_SEVERE)
            );
        }
        if(clickable.isThreadName()) {
            return createList(filterStorage,
                    new ThreadNameMatcher(clickable.getLogMessage().getThreadName())
            );
        }
        if(clickable.getLoggerName() != null) {
            Name n = clickable.getLoggerName();

            return createList(filterStorage,
                    new LoggerMatcher(n, false),
                    new LoggerMatcher(n, true)
            );
        }

        if(clickable.isStackTrace()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ThrowablePrinter tp = new SimpleThrowablePrinter(System.err);
                    clickable.getLogMessage().getThrowable().printStackTrace(tp);
                }
            };
            List<PopupAction> result = new ArrayList<>();
            result.add(new PopupAction("Dump stack trace to stderr", runnable));
            return result;
        }

        throw new IllegalArgumentException("don't know how to handle: " + clickable);
    }

    public JPopupMenu createPopupMenu(List<PopupAction> popupActions) {
        JPopupMenu result = new JPopupMenu();

        for(final PopupAction a : popupActions) {
            JMenuItem item = new JMenuItem(a.getDescription());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    a.getRunnable().run();
                }
            });
            result.add(item);
        }
        return result;
    }
}
