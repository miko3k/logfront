package org.deletethis.logfront.widgets.tilepane;

import java.awt.Color;

public class CharData {

    final private Color foreground;
    final private Color background;
    final private Color borderColor;
    final private int style;
    final private char character;

    public final static int TOP_BORDER = 0x01;
    public final static int RIGHT_BORDER = 0x02;
    public final static int BOTTOM_BORDER = 0x04;
    public final static int LEFT_BORDER = 0x08;
    public final static int BORDER_MASK = 0x0F;

    public final static int UNDERLINE = 0x10;
    public final static int STYLE_MASK = 0x10;

    public CharData(char character, Color foreground, Color background,
            Color borderColor, int style) {

        if((style & BORDER_MASK) == 0 || borderColor == null) {
            style &= ~BORDER_MASK;
            borderColor = null;
        }

        this.foreground = foreground;
        this.background = background;
        this.style = style;
        this.borderColor = borderColor;
        this.character = character;
    }

    public CharData setBackground(Color color) {
        return new CharData(character, foreground, color, borderColor, style);
    }

    public CharData setForeground(Color color) {
        return new CharData(character, color, background, borderColor, style);
    }

    public CharData underline() {
        return new CharData(character, foreground, background, borderColor, style | UNDERLINE);
    }

    public Color getForeground() {
        return foreground;
    }

    public Color getBackground() {
        return background;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public int getBorder() {
        return style & BORDER_MASK;
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public int hashCode() {
        int result = character ^ (style << 16);
        if(background != null) {
            return result ^= background.getRGB();
        }

        if(foreground != null) {
            return result ^= foreground.getRGB();
        }

		// shift it a bit so it doesn't cancel foreground if 
        // color is same
        if(borderColor != null) {
            return result ^= (borderColor.getRGB() << 1);
        }

        return result;
    }

    private boolean isColorSame(Color c1, Color c2) {
        if(c1 == null && c2 == null) {
            return true;
        }

        if(c1 == null && c2 != null) {
            return false;
        }

        if(c1 != null && c2 == null) {
            return false;
        }

        return c1.equals(c2);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }

        if(getClass() != obj.getClass()) {
            return false;
        }

        CharData other = (CharData) obj;

        if(style != other.style) {
            return false;
        }
        if(character != other.character) {
            return false;
        }
        if(!isColorSame(background, other.background)) {
            return false;
        }
        if(!isColorSame(foreground, other.foreground)) {
            return false;
        }
        if(!isColorSame(borderColor, other.borderColor)) {
            return false;
        }

        return true;
    }

    public boolean isUnderline() {
        return (style & UNDERLINE) != 0;
    }
}
