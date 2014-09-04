package org.deletethis.logfront.colors;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class FilterEntryStyleImpl implements FilterEntryStyle {

    private Font font;
    private Color textColorActive;
    private Color textColorInacive;
    private Color backgroundInactive;
    private Icon closeIcon;
    private int gap;
    private boolean darkText;

    public FilterEntryStyleImpl(Font font, Color textColorActive,
            Color backgroundInactive, int fontHeight, int gap) {
        this.font = font;
        this.textColorActive = textColorActive;
        this.textColorInacive = ColorUtil.between(textColorActive, backgroundInactive);
        this.backgroundInactive = backgroundInactive;
        this.gap = gap;
        this.darkText = !ColorUtil.isColorBright(textColorActive);

        int iconSize = fontHeight * 2 / 3;

        this.closeIcon = new ImageIcon(Images.createCloseImage(iconSize, 0, iconSize / 6, textColorActive));

    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public Color getActiveTextColor() {
        return textColorActive;
    }

    @Override
    public Color getInaciveTextColor() {
        return textColorInacive;
    }

    @Override
    public Color getInactiveBackgroundColor() {
        return backgroundInactive;
    }

    @Override
    public Color getHoverBackgroundColor(Color normalBackground) {
        if(darkText) {
            return ColorUtil.colorMult(normalBackground, 0.8f);
        } else {
            return ColorUtil.colorMult(normalBackground, 1.2f);
        }
    }

    @Override
    public Color getPressedBackgroundColor(Color normalBackground) {
        if(darkText) {
            return ColorUtil.colorMult(normalBackground, 0.6f);
        } else {
            return ColorUtil.colorMult(normalBackground, 1.4f);
        }
    }

    @Override
    public Icon getCloseIcon() {
        return closeIcon;
    }

    @Override
    public int getGap() {
        return gap;
    }
}
