package org.deletethis.logfront.widgets.tilepane;

public interface TextProvider {

    public CharData getCharData(int x, int y);

    public int getMaxLineWidth();

    public int getNumberOfLines();

    public void rewrap(int width);

    public Position createPosition(XY xy);
}
