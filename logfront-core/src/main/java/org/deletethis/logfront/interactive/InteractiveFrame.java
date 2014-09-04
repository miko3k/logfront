package org.deletethis.logfront.interactive;

import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import org.deletethis.logfront.FilterStorage;
import org.deletethis.logfront.FilterStorage.FilterStorageListener;
import org.deletethis.logfront.FilterStorage.FilterStorageListener.FilterStorageEvent;
import org.deletethis.logfront.FilterStorage.MatcherItem;
import org.deletethis.logfront.FilterStorage.Slot;
import org.deletethis.logfront.LogConsumer;
import org.deletethis.logfront.MainPanel;
import org.deletethis.logfront.Matcher;
import org.deletethis.logfront.cfg.CfgValue;
import org.deletethis.logfront.cfg.ConfigBackend;
import org.deletethis.logfront.cfg.ListCfgValue;
import org.deletethis.logfront.cfg.MapCfgValue;
import org.deletethis.logfront.cfg.ScalarCfgValue;
import org.deletethis.logfront.cfg.serialize.ChainMarshaller;
import org.deletethis.logfront.cfg.serialize.ConfigMarshallerContext;
import org.deletethis.logfront.cfg.serialize.EnumMarshaller;
import org.deletethis.logfront.cfg.serialize.PolymorphicMarshaller;
import org.deletethis.logfront.cfg.serialize.SimpleContext;
import org.deletethis.logfront.colors.GlobalStyle;
import org.deletethis.logfront.colors.GlobalStyleImpl;
import org.deletethis.logfront.message.LevelFactory;
import org.deletethis.logfront.message.LogMessage;
import org.deletethis.logfront.selflog.SelfLogger;
import org.deletethis.logfront.selflog.SelfLoggerFactory;


/**
 *
 * @author miko
 */
public class InteractiveFrame extends JFrame implements LogConsumer {
    final private ConfigBackend configBackend;
    final private ChainMarshaller cfgMarshaller;
    final private ConfigMarshallerContext configMarshallerContext;
    final private SelfLogger logger = SelfLoggerFactory.getLogger(InteractiveFrame.class);
    /**
     * We'll use timer to save configuration updates after short delay
     * (necessary for window position (moving generates LOTS of events), 
     * less necessary for filter changes but we use it anyway)
     */
    final private Timer configTimer;
    final private MainPanel mainPanel;
    
    public InteractiveFrame(ConfigBackend cfgBackend, String title, LevelFactory levelFactory) {
        GlobalStyle style = new GlobalStyleImpl();
        int gap = style.getBasicGap();

        mainPanel = new MainPanel(new GlobalStyleImpl());
        
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(1, 2, gap, gap));
        actionPanel.add(new JToggleButton(mainPanel.getSearchAction()));
        actionPanel.add(new JButton(mainPanel.getClearAction()));
        
        mainPanel.setActionPanel(actionPanel);
        
        //setAutoRequestFocus(false); -- this seems to break minimze button (at least) on KDE4
        setFocusableWindowState(false); // SEEMS TO WORK, but need some tweaks to restore state after showing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent ew) {
                setFocusableWindowState(true);
            }
        });              
        
        //setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        
        this.configBackend = cfgBackend;
        this.configTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                InteractiveFrame.this.configBackend.storeConfiguration(createConfiguration());
            }
        });
        configTimer.setRepeats(false);
        cfgMarshaller = new ChainMarshaller();
        cfgMarshaller.addMarshaller(new LevelMarshaller(levelFactory));
        cfgMarshaller.addMarshaller(new RectangleMarshaller());
        cfgMarshaller.addMarshaller(new EnumMarshaller());
        cfgMarshaller.addMarshaller(new PolymorphicMarshaller<>());
        configMarshallerContext = new SimpleContext(cfgMarshaller, configBackend);
        
        CfgValue config = null;
        if(configBackend != null)
            config = configBackend.loadConfiguration();

        Rectangle rc = null;
        if(config != null) {
            rc = configMarshallerContext.unmarshall(Rectangle.class, config.getMember("window"));
            FilterStorage st = mainPanel.getFilterStorage();
            for (Slot s : Slot.values()) {
                CfgValue val = config.getMember(configBackend.normalizeString(s.toString()));
                int count = val.getListSize();
                for (int i = 0; i < count; ++i) {
                    CfgValue item = val.getListItem(i);
                    boolean enabled = item.getMember("enabled").getBoolean(true);
                    Matcher m = configMarshallerContext.unmarshall(Matcher.class, item.getMember("value"));

                    st.add(s, m, enabled);
                }
            }
        }
        
        if(rc == null)
            rc = getDefaultWindowLocation();
        
        setLocation(rc.x, rc.y);
        setSize(rc.width, rc.height);
        
        setTitle(title);
        
        setContentPane(mainPanel);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                String[] buttons = {"Yes", "No"};

                int PromptResult = JOptionPane.showOptionDialog(null,
                        "Are you sure you want to close this window?\nYou won't be able to open it again.",
                        "Confirm close",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        buttons, buttons[1]);

                if(PromptResult == JOptionPane.YES_OPTION) {
                    setVisible(false);
                    dispose();
                }
            }
        });
        
        if(config != null) {
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentMoved(ComponentEvent e) {
                    scheduleConfigSave();
                }

                @Override
                public void componentResized(ComponentEvent e) {
                    scheduleConfigSave();
                }
            });
            
            mainPanel.getFilterStorage().addFilterStorageListener(new FilterStorageListener() {

                @Override
                public void onFilterStorageEvent(FilterStorageEvent e, FilterStorage filterStorage, MatcherItem item) {
                    scheduleConfigSave();
                }
            });
        }
    }

    private Rectangle getDefaultWindowLocation() {
        Rectangle rc = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int x1 = (int) rc.getX();
        int y1 = (int) rc.getY();
        int x2 = (int) rc.getMaxX();
        int y2 = (int) rc.getMaxY() - 200;
        int w = x2 - x1 + 1;
        int h = y2 - y1 + 1;

        int myw = w / 2;
        if(myw < 200) {
            myw = 200;
            if(myw > w) {
                myw = w;
            }
        }

        return new Rectangle(x2 - myw, y1, myw, h);
    }    
    
    
    private void scheduleConfigSave()
    {
        configTimer.restart();
    }
    
    private CfgValue createConfiguration()
    {
        MapCfgValue config = new MapCfgValue();
        config.setMember("window", configMarshallerContext.marshall(getBounds()));
        
        FilterStorage st = mainPanel.getFilterStorage();
        
        for(Slot s: Slot.values()) {
            Iterable<MatcherItem> matchers = st.getSlot(s);
            if(matchers == null)
                continue;

            ListCfgValue list = new ListCfgValue();
            for(MatcherItem m: matchers) {
                MapCfgValue val = new MapCfgValue();
                
                val.setMember("enabled", new ScalarCfgValue(m.isEnabled()));
                val.setMember("value", configMarshallerContext.marshall(m.getMatcher()));
                
                list.addListItem(val);
            }
            
            if(!list.isEmpty())
                config.setMember(configBackend.normalizeString(s.toString()), list);
        }
        logger.info(config);
        return config;
    }

    @Override
    public void addMessage(LogMessage logMessage) {
        mainPanel.addMessage(logMessage);
    }
}
