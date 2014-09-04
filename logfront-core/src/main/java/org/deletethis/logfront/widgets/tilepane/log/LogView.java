package org.deletethis.logfront.widgets.tilepane.log;

import org.deletethis.logfront.SearchNeedle;
import org.deletethis.logfront.message.LogMessage;
import org.deletethis.logfront.widgets.tilepane.DamageHandler;
import org.deletethis.logfront.widgets.tilepane.Position;
import org.deletethis.logfront.widgets.tilepane.XY;

public interface LogView {

    public void setDamageHandler(DamageHandler damage);

    public void addMessage(LogMessage logMessage);

    public void clearMessages();

    public void refilter();

    public Clickable getCurrentClickable();

    public Position createPosition(XY xy);

    public void setSearchNeedle(SearchNeedle needle);

    public Position getCurrentMatch();

    public Position getFollowingMatch(boolean forward);

    public boolean setCurrentMatch(Position p);

    public void unhighlighSearchMatch();

    public void mouseOverTile(XY xy);

    public void mouseReleasedOnTile(XY xy, int button);

    public void mouseClickOnTile(XY xy, int button);

    public void mouseDraggedLeftButton(XY xy);

    public void mousePressedOnTile(XY xy, int button);

    public void mouseExited();

    public String getSelectedText();
}
