package org.deletethis.logfront.widgets.tilepane;

import java.awt.Image;

public interface TileProvider {

    int getTileWidth();

    int getTileHeight();

    Image getTile(int x, int y);

    int getWidth();

    int getHeight();

    void setPreferredWidth(int width);

    Position createPosition(XY xy);
}
