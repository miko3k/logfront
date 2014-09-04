package org.deletethis.logfront.colors;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JButton;
import javax.swing.UIManager;

public class GlobalStyleImpl implements GlobalStyle {

    final private int basicGap;

    final private LogViewStyle logViewStyle;
    final private MatcherStyle matcherStyle;
    final private FilterEntryStyle filterEntryStyle;

    public GlobalStyleImpl() {
        Color selBack = UIManager.getColor("TextArea.selectionBackground");
        Color selFront = UIManager.getColor("TextArea.selectionForeground");
        Color textBackground = UIManager.getColor("TextArea.background");
        Color textForeground = UIManager.getColor("TextArea.foreground");
        Color panelBackground = UIManager.getColor("Panel.background");

        if(selBack == null) {
            selBack = Color.BLUE;
        }
        if(selFront == null) {
            selFront = Color.BLACK;
        }
        if(textBackground == null) {
            textBackground = Color.WHITE;
        }

        Color selectionBackground = selBack;
        Color selectionColor = selFront;

        JButton btn = new JButton("test button");
        Font defaultButtonFont = btn.getFont();
        FontMetrics fm = btn.getFontMetrics(defaultButtonFont);

        int height = fm.getHeight();
        Color color = btn.getForeground();

        basicGap = height / 2;

        logViewStyle = new LogViewStyleImpl(
                new Font("monospaced", 0, defaultButtonFont.getSize()),
                textBackground,
                textForeground,
                selectionBackground,
                selectionColor);

        filterEntryStyle = new FilterEntryStyleImpl(defaultButtonFont.deriveFont(0), color, panelBackground, height, basicGap / 2);
        matcherStyle = new MatcherStyleImpl(filterEntryStyle);
    }

    @Override
    public LogViewStyle getLogViewStyle() {
        return logViewStyle;
    }

    @Override
    public MatcherStyle getMatcherStyle() {
        return matcherStyle;
    }

    @Override
    public FilterEntryStyle getFilterEntryStyle() {
        return filterEntryStyle;
    }

    @Override
    public int getBasicGap() {
        return basicGap;
    }
}
