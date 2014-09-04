package org.deletethis.logfront.widgets.tilepane;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import org.deletethis.logfront.colors.TilePaneStyle;

/**
 * Something that is able to display grid of Images.
 *
 * Only vertical scrolling is implemented.
 *
 * I decided to switch away from JTextPane for number of reasons. - performance,
 * too heavy weight, log viewing is quite simple, we don't need editing and we
 * use monospace font only - uncontrollable order of highlights - weird line
 * wrapping (most importantly subtle magical changes to it) - too many hacks to
 * make it work, amount of code to make it work was comparable to this custom
 * component - implementing a model is close to impossible, so we have to work
 * primarily with (annotated) text instead of actual objects which makes things
 * much harder - (probably) no (precise) control over line wrapping
 *
 * @author miko
 */
public class TilePane extends JComponent implements Scrollable, DamageHandler {

    private static final long serialVersionUID = 2758015495908108126L;
    private static final boolean PRINT_REPAINT_STATS = false;

    private TileProvider tileProvider;
    private int lastPreferredWidth = -1;

    private boolean scrolledToBottom = true;

    public XY getTileAt(int x, int y) {
        int tilew = tileProvider.getTileWidth();
        int tileh = tileProvider.getTileHeight();
        return new XY(x / tilew, y / tileh);
    }

    final public XY getTileAt(Point p) {
        return getTileAt(p.x, p.y);
    }

    public Rectangle getRectForTile(int x, int y) {
        int w = tileProvider.getTileWidth();
        int h = tileProvider.getTileHeight();
        return new Rectangle(x * w, y * h, w - 1, h - 1);
    }

    private MouseAdapter mouse = new MouseAdapter() {
        @Override
        public void mouseDragged(MouseEvent ev) {
            rememberScrolledToBottom();
        }

        @Override
        public void mousePressed(MouseEvent ev) {
            rememberScrolledToBottom();
        }

        @Override
        public void mouseMoved(MouseEvent ev) {
            rememberScrolledToBottom();
        }

        @Override
        public void mouseClicked(MouseEvent ev) {
            rememberScrolledToBottom();

        }

        @Override
        public void mouseReleased(MouseEvent ev) {
            rememberScrolledToBottom();
        }

        @Override
        public void mouseExited(MouseEvent ev) {
            rememberScrolledToBottom();
        }
    };

    private void setWidthInTiles(int w) {
        if(lastPreferredWidth == w) {
            return;
        }
        //System.out.println("setting pref width to " + w);
        lastPreferredWidth = w;

        int lastHeight = tileProvider.getHeight();
        Rectangle visible = getVisibleRect();
        XY t = getTileAt(visible.getLocation());
        Position p = tileProvider.createPosition(t);

        tileProvider.setPreferredWidth(w);
        if(lastHeight != tileProvider.getHeight()) {
            int tilew = tileProvider.getTileWidth();
            int tileh = tileProvider.getTileHeight();
            XY xy = p.resolve();
            Rectangle rc2 = new Rectangle(xy.getX() * tilew, xy.getY() * tileh, 1, visible.height);
            //System.out.println(t + "|" + visible + " -> " + xy + "|" + rc2);
            scrollRectToVisible(rc2);
        }
    }

    public TilePane(TileProvider tp, TilePaneStyle style) {
        tileProvider = tp;
        tileProvider.setPreferredWidth(10);

        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        setAutoscrolls(true);
        setBackground(style.getBackgroundColor());

        this.addComponentListener(new ComponentAdapter() {
            private void setPrefWidth() {
                int w = getWidth() / tileProvider.getTileWidth();
                setWidthInTiles(w);
            }

            @Override
            public void componentShown(ComponentEvent ev) {
                setPrefWidth();
            }

            @Override
            public void componentResized(ComponentEvent ev) {
                setPrefWidth();
                repaint();
                scrollToBottomIfNeeded();
            }
        });
    }

    private void printStatus(Rectangle rc, int tiles, int renderedTiles) {
        if(PRINT_REPAINT_STATS) {
            System.out.println("paintComponent: " + rc + ", tiles: " + tiles + ", drawImages: " + renderedTiles);
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        if(lastPreferredWidth < 0) {
            return;
        }

        Rectangle rc = g.getClipBounds();
        g.setColor(getBackground());
        g.fillRect(rc.x, rc.y, rc.width, rc.height);
        int tileW = tileProvider.getTileWidth();
        int tileH = tileProvider.getTileHeight();
        int x1 = rc.x / tileW;
        int y1 = rc.y / tileH;
        int x2 = (rc.width + rc.x - 1) / tileW + 1;
        int y2 = (rc.height + rc.y - 1) / tileH + 1;

        int tiles = 0, renderedTiles = 0;

        for(int y = y1; y < y2; ++y) {
            for(int x = x1; x < x2; ++x) {
                Image img = tileProvider.getTile(x, y);
                ++tiles;
                if(img != null) {
                    g.drawImage(img, x * tileW, y * tileH, null);
                    ++renderedTiles;
                }
            }
        }
        printStatus(rc, tiles, renderedTiles);
    }

    private Dimension getAnySize() {
        Dimension d = new Dimension(
                tileProvider.getWidth() * tileProvider.getTileWidth(),
                tileProvider.getHeight() * tileProvider.getTileHeight());
        //System.out.println("get any size: " + d);
        return d;
    }

    @Override
    public Dimension getPreferredSize() {
        return getAnySize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getAnySize();
    }

    @Override
    public Dimension getMinimumSize() {
        return getAnySize();
    }

    public int getTileHeight() {
        return tileProvider.getTileHeight();
    }

    public int getTileWidth() {
        return tileProvider.getTileWidth();
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getAnySize();
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle rc, int o, int d) {
        int unit, maxPixels;

        if(o == SwingConstants.VERTICAL) {
            unit = tileProvider.getTileHeight();
            maxPixels = rc.height;
        } else {
            unit = tileProvider.getTileWidth();
            maxPixels = rc.width;
        }

        int a = maxPixels / 2;
        if(a < unit) {
            a = unit;
        }
        return a;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle rc, int o, int d) {
        int unit, maxPixels;

        if(o == SwingConstants.VERTICAL) {
            unit = tileProvider.getTileHeight();
            maxPixels = rc.height;
        } else {
            unit = tileProvider.getTileWidth();
            maxPixels = rc.width;
        }

        return ((maxPixels / 32) / unit + 1) * unit;
    }

    @Override
    public void damageRows(int y1, int y2) {
        int th = tileProvider.getTileHeight();
        int h = y2 - y1 + 1;
        //System.out.println("Damage " + y1 + " (" + h + ")");
        repaint(0, y1 * th, getWidth(), h * th);

    }

    @Override
    public void damageRowPart(int y, int x1, int x2) {
        int tw = tileProvider.getTileWidth();
        int th = tileProvider.getTileHeight();

        int w = x2 - x1 + 1;

		//System.out.println("Damage " + x1 + ", " + y1 + "(" + w + " x " + h + ")");
        repaint(x1 * tw, y * th, w * tw, th);
    }

    @Override
    public void damageRowAndBelow(int y) {
        int th = tileProvider.getTileHeight();
        int y1 = y * th;
        int h = getHeight() - y1;
        //System.out.println("Damage " + y1 + " (" + h + ")");
        repaint(0, y1, getWidth(), h);
    }

    @Override
    public void damageView() {
        repaint(0, 0, getWidth(), getHeight());
    }

    @Override
    public void damageSize() {
        revalidate();
        scrollToBottomIfNeeded();
    }

    public void rememberScrolledToBottom() {
        Rectangle rc = getVisibleRect();
        scrolledToBottom = (rc.height + rc.y >= getPreferredSize().height);
    }

    public void scrollToBottomIfNeeded() {
        if(scrolledToBottom) {
            scrollRectToVisible(new Rectangle(0, getHeight(), 1, 1));
        }
    }

    public void scrollToBottom() {
        scrolledToBottom = true;
        scrollToBottomIfNeeded();
    }

    public void scrollToTop() {
        scrollRectToVisible(new Rectangle(0, 0, 1, 1));
        rememberScrolledToBottom();
    }

    private void scrollPixels(int amount) {
        Rectangle rc = getVisibleRect();
        rc.y += amount;
        scrollRectToVisible(rc);
        rememberScrolledToBottom();
    }

    public void scrollLines(float amount) {
        float a = getTileHeight() * amount;
        scrollPixels((int) a);
    }

    public void scrollScreens(float amount) {
        float a = Math.max(getVisibleRect().height, getTileHeight());
        System.out.println(a);
        a *= amount;
        System.out.println(a);
        scrollPixels((int) a);
    }
}
