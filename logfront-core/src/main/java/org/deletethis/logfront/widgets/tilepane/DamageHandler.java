package org.deletethis.logfront.widgets.tilepane;

public interface DamageHandler {

    void damageRows(int y1, int y2);

    void damageRowPart(int y, int x1, int x2);

    void damageRowAndBelow(int y);

    void damageSize();

    void damageView();
}
