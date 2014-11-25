package org.deletethis.logfront.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.deletethis.logfront.colors.FilterEntryStyle;
import org.deletethis.logfront.util.MetaListener;

public class FilterEntry extends JComponent {

    private static final long serialVersionUID = -2803467228275923198L;

    private final FilterEntryStyle style;

    private final String text;
    private final int vgap;
    private final Dimension size;
    private final Icon closeIcon;
    private final int roundRadius;
    private final Font font;
    private final int textY;

    private final int height;

    private final int textX;
    private int separatorX;
    private final int iconX;
    private final int hgap;

    private boolean active;

    private enum State {

        NORMAL,
        PRESSED_LEFT,
        PRESSED_RIGHT,
        HOVER_LEFT,
        HOVER_RIGHT,
    };
    private State state;
    private MetaListener<FilterEntryListener> listeners
            = new MetaListener<>(FilterEntryListener.class);

    private void fireAction(boolean left) {
        if(left) {
            listeners.getMetaListener().onMainClick();
        } else {
            listeners.getMetaListener().onCloseClick();
        }
    }

    private final MouseAdapter mouse = new MouseAdapter() {
        private void setState(State st) {
            if(st != state) {
                state = st;
                repaint();
            }
        }

        private boolean isInside(MouseEvent e) {
            return e.getX() >= 0 && e.getY() >= 0 && e.getX() < getWidth() && e.getY() < getHeight();
        }

        private boolean isLeft(MouseEvent e) {
            return (e.getX() < separatorX);
        }

        private boolean isInsideLeft(MouseEvent e) {
            return isInside(e) && isLeft(e);
        }

        private boolean isInsideRight(MouseEvent e) {
            return isInside(e) && !isLeft(e);
        }

        private State getStateByLocation(MouseEvent e) {
            if(isInsideLeft(e)) {
                return State.HOVER_LEFT;
            }
            if(isInsideRight(e)) {
                return State.HOVER_RIGHT;
            }
            return State.NORMAL;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            State newState;
            switch(state) {
                case PRESSED_LEFT:
                    newState = State.HOVER_LEFT;
                    break;
                case PRESSED_RIGHT:
                    newState = State.HOVER_RIGHT;
                    break;
                case HOVER_LEFT:
                    newState = isLeft(e) ? State.NORMAL : State.HOVER_LEFT;
                    break;
                case HOVER_RIGHT:
                    newState = !isLeft(e) ? State.NORMAL : State.HOVER_RIGHT;
                    break;
                default:
                    newState = State.NORMAL;
                    break;
            }
            setState(newState);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // we'll ignore this one. It could be handled but it's not really useful
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            setState(getStateByLocation(e));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            State newState = state;
            if((e.getModifiers() & MouseEvent.BUTTON1_MASK) == 0) {
                newState = getStateByLocation(e);
            } else {
                if(state == State.PRESSED_LEFT || state == State.HOVER_LEFT) {
                    newState = isInsideLeft(e) ? State.PRESSED_LEFT : State.HOVER_LEFT;
                }
                if(state == State.PRESSED_RIGHT || state == State.HOVER_RIGHT) {
                    newState = isInsideRight(e) ? State.PRESSED_RIGHT : State.HOVER_RIGHT;
                }
            }

            setState(newState);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() != MouseEvent.BUTTON1) {
                return;
            }

            setState(isLeft(e) ? State.PRESSED_LEFT : State.PRESSED_RIGHT);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.getButton() != MouseEvent.BUTTON1) {
                return;
            }

            if(state == State.PRESSED_LEFT && isInsideLeft(e)) {
                fireAction(true);
            } else if(state == State.PRESSED_RIGHT && isInsideRight(e)) {
                fireAction(false);
            }

            setState(getStateByLocation(e));
        }
    };

    public FilterEntry(String text, FilterEntryStyle style) {
        this.active = true;
        this.style = style;
        this.state = State.NORMAL;
        this.vgap = style.getGap();
        this.closeIcon = style.getCloseIcon();
        this.text = text;
        this.font = style.getFont();

        FontMetrics fm = getFontMetrics(font);

        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int iconHeight = closeIcon.getIconHeight();
        int maxHeight = Math.max(textHeight, iconHeight);
        height = maxHeight + vgap * 2;
        this.roundRadius = height * 2 / 3;

        if(roundRadius / 2 > vgap) {
            hgap = roundRadius / 2;
        } else {
            hgap = vgap;
        }

        this.textY = (height - textHeight) / 2 + fm.getAscent();

        this.textX = hgap;
        this.separatorX = textX + textWidth + hgap;
        this.iconX = separatorX + hgap;
        int width = iconX + closeIcon.getIconWidth() + hgap;

        this.size = new Dimension(width, height);

        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);
    }

    @Override
    public Dimension getPreferredSize() {
        return size;
    }

    @Override
    public Dimension getMinimumSize() {
        return size;
    }

    @Override
    public Dimension getMaximumSize() {
        return size;
    }

    private void drawBackground(Graphics g) {
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, roundRadius, roundRadius);
    }

    private void drawBackground(Graphics g, Color left, Color right) {
        if(left == right) {
            g.setColor(left);
            drawBackground(g);
        } else {
            g.setClip(0, 0, separatorX, height);
            g.setColor(left);
            drawBackground(g);

            g.setClip(separatorX, 0, getWidth() - separatorX, height);
            g.setColor(right);
            drawBackground(g);

            g.setClip(null);
        }
    }

    private void drawForeground(Graphics g, Color textColor) {
        g.setColor(textColor);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, roundRadius, roundRadius);
        g.drawLine(separatorX, 0, separatorX, height - 1);

        closeIcon.paintIcon(this, g, iconX, (height - closeIcon.getIconHeight()) / 2);
        g.setFont(font);
        g.drawString(text, textX, textY);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color background;
        Color foreground;
        if(active) {
            background = getBackground();
            foreground = style.getActiveTextColor();
        } else {
            background = style.getInactiveBackgroundColor();
            foreground = style.getInaciveTextColor();
        }
        Color backgroundLeft = background, backgroundRight = background;
        switch(state) {
            case HOVER_LEFT:
                backgroundLeft = style.getHoverBackgroundColor(background);
                break;
            case HOVER_RIGHT:
                backgroundRight = style.getHoverBackgroundColor(background);
                break;
            case PRESSED_LEFT:
                backgroundLeft = style.getPressedBackgroundColor(background);
                break;
            case PRESSED_RIGHT:
                backgroundRight = style.getPressedBackgroundColor(background);
                break;
            default:
                break;
        }

        drawBackground(g, backgroundLeft, backgroundRight);
        drawForeground(g, foreground);
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        boolean changed = this.active != active;
        this.active = active;
        if(changed) {
            repaint();
        }
    }

    public void addFilterEntryListener(FilterEntryListener l) {
        listeners.addListener(l);
    }
}
