package org.deletethis.logfront.widgets.tilepane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static org.deletethis.logfront.util.UnicodeChars.*;

public class TextTileProvider implements TileProvider {

    private int tileWidth, tileHeight, fontBaseLine;
    private Font font;
    private int underlineHeight;
    private TextProvider textProvider;
	// cache eviction is probably not necessary, number of colors and
    // characters is quite limited
    private Map<CharData, Image> tileCache = new HashMap<CharData, Image>();
    private int minWidth;

    public TextTileProvider(Font font, TextProvider data, int minWidth) {
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        FontMetrics m = g.getFontMetrics(font);
        this.tileWidth = m.getMaxAdvance();
        this.tileHeight = m.getHeight();
        this.fontBaseLine = m.getAscent();
        this.font = font;
        this.textProvider = data;
        this.minWidth = minWidth;
        this.underlineHeight = tileHeight / 32 + 1;
        g.dispose();
    }

    @Override
    public int getTileWidth() {
        return tileWidth;
    }

    @Override
    public int getTileHeight() {
        return tileHeight;
    }

    private Image createTile(CharData charData) {
        BufferedImage result = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = result.getGraphics();

        g.setColor(charData.getBackground() != null ? charData.getBackground() : Color.RED);
        g.fillRect(0, 0, tileWidth, tileHeight);

        if(charData.getBorder() != 0 && charData.getBorderColor() != null) {
            g.setColor(charData.getBorderColor());
            int b = charData.getBorder();
            if((b & CharData.TOP_BORDER) != 0) {
                g.drawLine(0, 0, tileWidth - 1, 0);
            }
            if((b & CharData.RIGHT_BORDER) != 0) {
                g.drawLine(tileWidth - 1, 0, tileWidth - 1, tileHeight - 1);
            }
            if((b & CharData.BOTTOM_BORDER) != 0) {
                g.drawLine(0, tileHeight - 1, tileWidth - 1, tileHeight - 1);
            }
            if((b & CharData.LEFT_BORDER) != 0) {
                g.drawLine(0, 0, 0, tileHeight - 1);
            }
        }
        char c = charData.getCharacter();
        char[] chars = {c};
        // we'll render certain graphical shapes by hand
        g.setColor(charData.getForeground());

        if(c == BLACK_LOWER_RIGHT_TRIANGLE || c == BLACK_LOWER_LEFT_TRIANGLE
                || c == BLACK_UPPER_LEFT_TRIANGLE || c == BLACK_UPPER_RIGHT_TRIANGLE) {

            int size = Math.min(tileWidth, tileHeight) - 1;
            int top = (tileHeight - size) / 2;
            int bottom = top + size - 1;
            int left, right;

            if(c == BLACK_LOWER_RIGHT_TRIANGLE || c == BLACK_UPPER_RIGHT_TRIANGLE) {
                left = tileWidth - size;
                right = tileWidth - 1;
            } else {
                left = 0;
                right = size - 1;
            }
            int[] x, y;
            switch(c) {
                case BLACK_LOWER_RIGHT_TRIANGLE:
                    x = new int[] {left, right, right};
                    y = new int[] {bottom, bottom, top};
                    break;
                case BLACK_UPPER_RIGHT_TRIANGLE:
                    x = new int[] {left, right, right};
                    y = new int[] {top, top, bottom};
                    break;
                case BLACK_LOWER_LEFT_TRIANGLE:
                    x = new int[] {left, left, right};
                    y = new int[] {top, bottom, bottom};
                    break;
                case BLACK_UPPER_LEFT_TRIANGLE:
                    x = new int[] {left, left, right};
                    y = new int[] {bottom, top, top};
                    break;
                default:
                    throw new IllegalStateException();
            }
            g.fillPolygon(x, y, 3);
            g.drawPolygon(x, y, 3);
        } else {
            g.setFont(font);
            g.drawChars(chars, 0, 1, 0, fontBaseLine);
        }
        if(charData.isUnderline()) {
			// underline by hand, I don't really wanna use AttributedString
            // and friends for such a simple task.
            g.fillRect(0, fontBaseLine + 1, tileWidth, underlineHeight);
        }
        g.dispose();
        return result;
    }

    @Override
    public Image getTile(int x, int y) {
        CharData charData = textProvider.getCharData(x, y);

        if(charData == null) {
            return null;
        }

        Image img = tileCache.get(charData);
        if(img == null) {
            img = createTile(charData);
            tileCache.put(charData, img);
        }

        return img;
    }

    @Override
    public int getWidth() {
        return textProvider.getMaxLineWidth();
    }

    @Override
    public int getHeight() {
        return textProvider.getNumberOfLines();
    }

    @Override
    public void setPreferredWidth(int width) {
        textProvider.rewrap((width < minWidth) ? minWidth : width);
    }

    @Override
    public Position createPosition(XY xy) {
        return textProvider.createPosition(xy);
    }
}
