package org.deletethis.logfront.colors;

import java.awt.Color;
import java.awt.Font;

import org.deletethis.logfront.message.Level;
import org.deletethis.logfront.message.Name;
import org.deletethis.logfront.util.HSL;

public class LogViewStyleImpl implements LogViewStyle {

    private Font font;
    private Color normalBackground;
    private Color normalText;
    private Color selectedBackground;
    private Color selectedText;
    private Color leftPaneBackground;
    private Color expandButtonBackground;
    private Color expandButtonBackgroundHover;
    private float textLuminance;
    private boolean brightBackground;
    private Color searchMatchColor;
    private Color currentSearchMatchColor;

    public LogViewStyleImpl(
            Font font,
            Color normalBackground,
            Color normalText,
            Color selectedBackground,
            Color selectedText) {
        this.font = font;
        this.normalBackground = normalBackground;
        this.normalText = normalText;
        this.selectedBackground = selectedBackground;
        this.selectedText = selectedText;

        float[] backgroundHsl = HSL.get(normalBackground);
        brightBackground = ColorUtil.isLuminanceBright(backgroundHsl[2]);
        textLuminance = (brightBackground) ? 0.4f : 0.6f;

        float backgroundL = (brightBackground) ? (backgroundHsl[2] - 0.2f) : (backgroundHsl[2] + 0.2f);

        leftPaneBackground = HSL.create(backgroundHsl[0], backgroundHsl[1], backgroundL);
        expandButtonBackground = HSL.create(0, 1, brightBackground ? 0.7f : 0.3f);
        expandButtonBackgroundHover = getHoverForegroundColor(expandButtonBackground);

        float searchLiminance = (brightBackground) ? 0.7f : 0.3f;
        float currentMatchHue = 135f / 360f;
        float anyMatchHue = 56f / 360f;
        searchMatchColor = HSL.create(anyMatchHue, 1, searchLiminance);
        currentSearchMatchColor = HSL.create(currentMatchHue, 1, searchLiminance);
    }

    private Color colorWithHue(float hue) {
        return HSL.create(hue, 1, textLuminance);
    }

    @Override
    public Color getLeftPaneBackgroundColor() {
        return leftPaneBackground;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public Color getThreadNameColor(String name) {
        return colorWithHue(ColorUtil.getThreadNameHue(name));
    }

    @Override
    public Color getNormalTextColor() {
        return Color.BLACK;
    }

    @Override
    public Color getExceptionLineColor() {
        return Color.RED;
    }

    @Override
    public Color getLevelColor(Level level) {
        return HSL.create(level.getHue(), 1f, textLuminance);
    }

    @Override
    public Color getLoggerColor(Name name) {
        return colorWithHue(ColorUtil.getLoggerHue(name));
    }

    @Override
    public Color getSelectionBackgroundColor() {
        return selectedBackground;
    }

    @Override
    public Color getSelectionTextColor() {
        return selectedText;
    }

    @Override
    public Color getBackgroundColor() {
        return normalBackground;
    }

    @Override
    public Color getExpandButtonBorderColor() {
        return normalText;
    }

    @Override
    public Color getExpandButtonHoverBorderColor() {
        return normalText;
    }

    @Override
    public Color getExpandButtonIconColor() {
        return normalText;
    }

    @Override
    public Color getExpandButtonHoverIconColor() {
        return normalText;
    }

    @Override
    public Color getExpandButtonBackgroundColor() {
        return expandButtonBackground;
    }

    @Override
    public Color getExpandButtonHoverBackgroundColor() {
        return expandButtonBackgroundHover;
    }

    public Color getHoverColor(Color normal) {
        if(brightBackground) {
            return ColorUtil.colorMult(normal, 0.5f);
        } else {
            return ColorUtil.colorMult(normal, 1.8f);
        }
    }

    @Override
    public Color getHoverForegroundColor(Color normal) {
        return getHoverColor(normal);
    }

    @Override
    public Color getSearchMatchColor() {
        return searchMatchColor;
    }

    @Override
    public Color getCurrentSearchMatchColor() {
        return currentSearchMatchColor;
    }
}
