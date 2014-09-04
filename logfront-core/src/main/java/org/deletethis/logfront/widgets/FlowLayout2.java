package org.deletethis.logfront.widgets;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Something like FlowLayout but always left aligned, should work inside
 * GridBagLayout and gaps are only used as gaps (unlike normal FlowLayout always
 * seems to add gap to the bottom of the layout).
 *
 * @author miko
 */
public class FlowLayout2 implements LayoutManager {

    final private int xgap, ygap;
	// we'll use these values in layoutContainer, instead
    // actual size because we want to use same values as
    // we computed minimal size for. Otherwise it
    // behaved weird during resizing, because
    // methods were called with slightly different values.
    int layoutWidth;
    Insets layoutInsets;
    boolean hasLayoutStuff = false;
    private final static int MIN_LAYOUT_WIDTH = 2;

    public FlowLayout2(int xgap, int ygap) {
        this.xgap = xgap;
        this.ygap = ygap;
    }

    private class LayoutMaker {

        final protected int beginX, endX;
        final protected int bottomYInset;

        protected int currentX, currentY;
        protected int maxY;
        protected int lastX;
        protected int lastY;
        protected int lastWidth;
        protected int lastHeight;
        protected boolean lastVisible;

        public LayoutMaker(Insets containerInsets, int containerWidth) {
            Insets insets = containerInsets;
            beginX = insets.left;
            endX = containerWidth - insets.right;
            bottomYInset = insets.bottom;

            currentX = beginX;
            currentY = insets.top;
            lastX = lastY = lastWidth = lastHeight = -1;
        }

        public int getLastX() {
            return lastX;
        }

        public int getLastY() {
            return lastY;
        }

        public int getLastWidth() {
            return lastWidth;
        }

        public int getLastHeight() {
            return lastHeight;
        }

        public int getHeight() {
            return maxY + bottomYInset;
        }

        public boolean getLastVisible() {
            return lastVisible;
        }

        public void layoutCompontent(Component comp) {
            if(!comp.isVisible()) {
                lastVisible = false;
                return;
            }

            lastVisible = true;
            Dimension d = comp.getPreferredSize();
            int w = d.width;
            int h = d.height;

            if(w > endX - beginX) {
                w = endX - beginX;
            }

            boolean firstInRow = currentX == beginX;

            lastWidth = w;
            lastHeight = h;

            lastX = currentX;
            lastY = currentY;

            currentX += w;
            if(!firstInRow && currentX > endX) {
                currentX = beginX;
                currentY = maxY + ygap;
                lastX = currentX;
                lastY = currentY;
                currentX += w;
            }
            currentX += xgap;
            if(currentY + lastHeight > maxY) {
                maxY = currentY + lastHeight;
            }
        }
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public void layoutContainer(Container parent) {
        if(!hasLayoutStuff || layoutWidth < MIN_LAYOUT_WIDTH) {
            return;
        }

        LayoutMaker layoutMaker = new LayoutMaker(layoutInsets, layoutWidth);
        int count = parent.getComponentCount();

        for(int i = 0; i < count; ++i) {
            Component comp = parent.getComponent(i);
            layoutMaker.layoutCompontent(comp);
            if(layoutMaker.getLastVisible()) {
                comp.setBounds(
                        layoutMaker.getLastX(),
                        layoutMaker.getLastY(),
                        layoutMaker.getLastWidth(),
                        layoutMaker.getLastHeight());
            }
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        layoutInsets = parent.getInsets();
        layoutWidth = parent.getWidth();
        hasLayoutStuff = true;

        if(layoutWidth == 0) {
			// from <http://tips4java.wordpress.com/2008/11/06/wrap-layout/>
            //  When the container width = 0, the preferred width of the container
            //  has not yet been calculated so lets ask for the maximum.
            layoutWidth = Integer.MAX_VALUE;
        }

        LayoutMaker layoutMaker = new LayoutMaker(layoutInsets, layoutWidth);
        int count = parent.getComponentCount();

        for(int i = 0; i < count; ++i) {
            Component comp = parent.getComponent(i);
            layoutMaker.layoutCompontent(comp);
        }

        return new Dimension(parent.getWidth(), layoutMaker.getHeight());
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension d = minimumLayoutSize(parent);
        return d;
    }
}
