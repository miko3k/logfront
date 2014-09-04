package org.deletethis.logfront;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import org.deletethis.logfront.FilterStorage.MatcherItem;
import org.deletethis.logfront.colors.GlobalStyle;
import org.deletethis.logfront.colors.LogViewStyle;
import org.deletethis.logfront.impl.FilterStorageImpl;
import org.deletethis.logfront.message.LogMessage;
import org.deletethis.logfront.widgets.FilterPanel;
import org.deletethis.logfront.widgets.SearchPanel;
import org.deletethis.logfront.widgets.SearchPanelListener;
import org.deletethis.logfront.widgets.tilepane.Position;
import org.deletethis.logfront.widgets.tilepane.TextTileProvider;
import org.deletethis.logfront.widgets.tilepane.TilePane;
import org.deletethis.logfront.widgets.tilepane.XY;
import org.deletethis.logfront.widgets.tilepane.log.Clickable;
import org.deletethis.logfront.widgets.tilepane.log.LogMessageTextProvider;
import org.deletethis.logfront.widgets.tilepane.log.LogView;

public class MainPanel extends JPanel implements LogConsumer {

    private static final long serialVersionUID = 2005837077419508221L;

    final private static String SEARCH_COMMAND = MainPanel.class.getName() + ".search";
    final private static String COPY_COMMAND = MainPanel.class.getName() + ".copy";
    final private static String CANCEL_COMMAND = MainPanel.class.getName() + ".cancel";
    
    final private FilterPanel filterPanel;
    final private JScrollPane scrollPane;
    final private SearchPanel searchPanel;
    final private TilePane tilePane;
    final private LogView logMessageText;
    final private FilterPanelManager filterPanelManager;
    final private FilterStorage filterStorage;
    final private PopupActionFactory popupActionFactory;
    final private JPanel actionPanelWrapper;

    private SearchPanelListener searchListener = new SearchPanelListener() {

        private Position getInitialPosition() {
            Rectangle visible = tilePane.getVisibleRect();
            XY xy = tilePane.getTileAt(visible.getLocation());
            return logMessageText.createPosition(xy);
        }

        @Override
        public void onSearchCriteriaChange(SearchPanel panel) {
            logMessageText.setSearchNeedle(panel.getSearchCriteria());
            panel.showFound();
        }

        @Override
        public void onSerachPanelOpened(SearchPanel panel) {
            logMessageText.setSearchNeedle(panel.getSearchCriteria());
        }

        private void doSearch(SearchPanel panel, boolean forward) {
            Position p = logMessageText.getCurrentMatch();
            if(p == null) {
                p = getInitialPosition();
            } else {
                p = logMessageText.getFollowingMatch(forward);
            }
            boolean found = false;
            if(p != null) {
                found = logMessageText.setCurrentMatch(p);
            }

            if(!found) {
                panel.showNotFound();
            } else {
                panel.showFound();

                Position p2 = logMessageText.getCurrentMatch();
                assert p2 != null;
                XY xy = p2.resolve();
                tilePane.scrollRectToVisible(tilePane.getRectForTile(xy.x, xy.y));
            }
        }

        @Override
        public void onSearchNext(SearchPanel panel) {
            doSearch(panel, true);
        }

        @Override
        public void onSearchPrevious(SearchPanel panel) {
            doSearch(panel, false);
        }

    };

    private class SearchAction extends AbstractAction {

        public SearchAction() {
            super("Search");
            putValue(SELECTED_KEY, false);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println(getValue(SELECTED_KEY));
            
            boolean selected = (boolean)getValue(SELECTED_KEY);

            searchPanel.setVisible(selected);
            if(selected) {
                searchPanel.requestFocus();
            } else {
                logMessageText.unhighlighSearchMatch();
            }
        }
    }
    private SearchAction searchAction = new SearchAction();

    private Action searchToggleAction = new AbstractAction("Toggle search") {
        private static final long serialVersionUID = 2867712833889526915L;

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean old = (boolean)searchAction.getValue(SELECTED_KEY);
            searchAction.putValue(SELECTED_KEY, !(boolean)searchAction.getValue(SELECTED_KEY));
            searchAction.actionPerformed(e);
        }
    };     
    
    private Action clearAction = new AbstractAction("Clear") {
        private static final long serialVersionUID = 2867712833889526912L;

        @Override
        public void actionPerformed(ActionEvent e) {
            logMessageText.clearMessages();
        }
    };    

    private Action cancelAction = new AbstractAction("Cancel") {
        private static final long serialVersionUID = -1727453829401139036L;

        @Override
        public void actionPerformed(ActionEvent e) {
            searchAction.putValue(SELECTED_KEY, false);
        }
    };

    private void setClipboard(String text, boolean system) {
        Clipboard clpbrd;
        if(system) {
            clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        } else {
            clpbrd = Toolkit.getDefaultToolkit().getSystemSelection();
        }
        if(clpbrd != null) {
            StringSelection stringSelection = new StringSelection(text);
            clpbrd.setContents(stringSelection, null);
        }
    }

    private Action copyAction = new AbstractAction("Copy to clipboard") {
        private static final long serialVersionUID = -1727453829401139036L;

        @Override
        public void actionPerformed(ActionEvent paramActionEvent) {
            String text = logMessageText.getSelectedText();
            if(text != null) {
                setClipboard(text, true);
            }
        }
    };

    private class ScrollAction extends AbstractAction {

        private static final long serialVersionUID = -7332983386995366709L;

        public final static int RELATIVE_LINE = 1,
                RELATIVE_HALF_SCREEN = 2,
                BEGINING = 3,
                END = 4;

        private final int how, amount;

        public ScrollAction(int how, int amount) {
            super("SCROLL" + how + "_" + amount);
            this.how = how;
            this.amount = amount;
        }

        @Override
        public void actionPerformed(ActionEvent paramActionEvent) {
            switch(how) {
                case BEGINING:
                    tilePane.scrollToTop();
                    break;
                case END:
                    tilePane.scrollToBottom();
                    break;
                case RELATIVE_HALF_SCREEN:
                    tilePane.scrollScreens((float) amount / 2);
                    break;
                case RELATIVE_LINE:
                    tilePane.scrollLines(amount);
                    break;
            }
        }

        public String getName() {
            return getValue(NAME).toString();
        }
    }

    public MainPanel(GlobalStyle globalStyle) {

        int gap = globalStyle.getBasicGap();

        JComponent content = this;
        //setContentPane(content);

        filterPanel = new FilterPanel(globalStyle);
        filterStorage = new FilterStorageImpl();
        filterPanelManager = new FilterPanelManager(filterPanel, globalStyle.getFilterEntryStyle(), globalStyle.getMatcherStyle());
        filterStorage.addFilterStorageListener(filterPanelManager);

        searchPanel = new SearchPanel(globalStyle, searchListener, Color.RED);
        searchPanel.setVisible(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(gap, 0, 0, 0));

        actionPanelWrapper = new JPanel();
        actionPanelWrapper.setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap));
        top.setLayout(new BorderLayout());
        top.add(filterPanel, BorderLayout.CENTER);
        top.add(searchPanel, BorderLayout.SOUTH);
        top.add(actionPanelWrapper, BorderLayout.EAST);

        content.setLayout(new BorderLayout());
        content.add(top, BorderLayout.NORTH);
        LogMessageTextProvider tmp1 = new LogMessageTextProvider(globalStyle.getLogViewStyle(), new FilterStorageFilter(filterStorage));
        LogViewStyle logViewStyle = globalStyle.getLogViewStyle();

        logMessageText = tmp1;
        tilePane = new TilePane(
                new TextTileProvider(logViewStyle.getFont(), tmp1, 16),
                logViewStyle);

        logMessageText.setDamageHandler(tilePane);
        popupActionFactory = new PopupActionFactory();

        MouseAdapter a = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent ev) {
                logMessageText.mouseOverTile(tilePane.getTileAt(ev.getPoint()));
            }

            private List<PopupAction> getCurrentPopupActions(boolean includeCopy) {
                Clickable c = logMessageText.getCurrentClickable();

                if(!includeCopy && c == null) {
                    return null;
                }

                List<PopupAction> result = new ArrayList<>();
                if(includeCopy) {
                    result.add(new PopupAction(copyAction));
                }
                if(c != null) {
                    result.addAll(popupActionFactory.getPopupActions(filterStorage, c));
                }
                return result;
            }

            void doMenu(MouseEvent e) {
                if(!e.isPopupTrigger()) {
                    return;
                }

                List<PopupAction> popupActions = getCurrentPopupActions(logMessageText.getSelectedText() != null);
                if(popupActions != null) {
                    JPopupMenu popup = popupActionFactory.createPopupMenu(popupActions);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseClicked(MouseEvent ev) {
                logMessageText.mouseClickOnTile(tilePane.getTileAt(ev.getPoint()), ev.getButton());
                if(ev.getButton() == MouseEvent.BUTTON1) {
                    // execute first possible reaction on left click
                    List<PopupAction> popupActions = getCurrentPopupActions(false);
                    if(popupActions != null) {
                        popupActions.get(0).getRunnable().run();
                    }
                } else {
                    doMenu(ev);
                }
            }

            @Override
            public void mousePressed(MouseEvent ev) {
                logMessageText.mousePressedOnTile(tilePane.getTileAt(ev.getPoint()), ev.getButton());
                doMenu(ev);
            }

            @Override
            public void mouseDragged(MouseEvent ev) {
                if((ev.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) {
                    logMessageText.mouseDraggedLeftButton(tilePane.getTileAt(ev.getPoint()));
                    String sel = logMessageText.getSelectedText();
                    if(sel != null) {
                        setClipboard(sel, false);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent ev) {
                logMessageText.mouseReleasedOnTile(tilePane.getTileAt(ev.getPoint()), ev.getButton());
                doMenu(ev);
            }

            @Override
            public void mouseExited(MouseEvent ev) {
                logMessageText.mouseExited();
            }
        };
        tilePane.addMouseMotionListener(a);
        tilePane.addMouseListener(a);
        // scrollPane = new JScrollPane(pane)
        scrollPane = new JScrollPane(tilePane);
        scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        content.add(scrollPane, BorderLayout.CENTER);

        filterStorage.addFilterStorageListener(new FilterStorage.FilterStorageListener() {
            @Override
            public void onFilterStorageEvent(FilterStorageEvent e,
                    FilterStorage filterStorage, MatcherItem item) {
                logMessageText.refilter();
            }
        });

        // I just couldn't get WHEN_IN_FOCUSED window to work...
        ScrollAction scrollLineUp = new ScrollAction(ScrollAction.RELATIVE_LINE, -1);
        ScrollAction scrollLineDown = new ScrollAction(ScrollAction.RELATIVE_LINE, 1);
        ScrollAction scrollScreenUp = new ScrollAction(ScrollAction.RELATIVE_HALF_SCREEN, -1);
        ScrollAction scrollScreenDown = new ScrollAction(ScrollAction.RELATIVE_HALF_SCREEN, 1);
        ScrollAction scrollBegin = new ScrollAction(ScrollAction.BEGINING, 0);
        ScrollAction scrollEnd = new ScrollAction(ScrollAction.END, 0);

        InputMap inmap = content.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap amap = content.getActionMap();

        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), SEARCH_COMMAND);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), COPY_COMMAND);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), CANCEL_COMMAND);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), scrollLineUp.getName());
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), scrollLineDown.getName());
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), scrollScreenUp.getName());
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), scrollScreenDown.getName());
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), scrollBegin.getName());
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), scrollEnd.getName());
        amap.put(SEARCH_COMMAND, searchToggleAction);
        amap.put(COPY_COMMAND, copyAction);
        amap.put(CANCEL_COMMAND, cancelAction);
        amap.put(scrollLineUp.getName(), scrollLineUp);
        amap.put(scrollLineDown.getName(), scrollLineDown);
        amap.put(scrollScreenUp.getName(), scrollScreenUp);
        amap.put(scrollScreenDown.getName(), scrollScreenDown);
        amap.put(scrollBegin.getName(), scrollBegin);
        amap.put(scrollEnd.getName(), scrollEnd);
    }

    @Override
    public void addMessage(LogMessage msg)
    {
        tilePane.rememberScrolledToBottom();
        logMessageText.addMessage(msg);
        tilePane.scrollToBottomIfNeeded();
    }
    
    public FilterStorage getFilterStorage()
    {
        return filterStorage;
    }
    
    public Action getClearAction() { return clearAction; }
    public Action getSearchAction() { return searchAction; }
    public void setActionPanel(JPanel panel) {
        actionPanelWrapper.removeAll();
        actionPanelWrapper.add(panel, BorderLayout.PAGE_START);
        actionPanelWrapper.revalidate();
    }
}
