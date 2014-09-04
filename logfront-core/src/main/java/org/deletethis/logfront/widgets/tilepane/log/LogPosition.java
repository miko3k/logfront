package org.deletethis.logfront.widgets.tilepane.log;

import org.deletethis.logfront.widgets.tilepane.log.data.FilteredText;
import org.deletethis.logfront.widgets.tilepane.Position;
import org.deletethis.logfront.widgets.tilepane.XY;

public class LogPosition implements Position {

    private final FilteredText<LineKey, FormattedText, DisplayLine> text;
    private final long chunkid;
    private final int offset;

    private final static long END_ID = Long.MAX_VALUE;

    public static LogPosition end(FilteredText<LineKey, FormattedText, DisplayLine> text) {
        return new LogPosition(text, END_ID, 0);
    }

    public boolean after(LogPosition x) {
        assert (text == x.text);

        if(chunkid < x.chunkid) {
            return false;
        }
        if(chunkid > x.chunkid) {
            return true;
        }
        return offset > x.offset;
    }

	// not using equals because it's harder to implement (uses Object) and we
    // don't really wanna override hashcode too
    public boolean same(LogPosition x) {
        assert (text == x.text);
        return chunkid == x.chunkid && offset == x.offset;
    }

    public LogPosition(FilteredText<LineKey, FormattedText, DisplayLine> text,
            long chunkid, int offset) {
        this.text = text;
        this.chunkid = chunkid;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "(" + chunkid + ", " + offset + ")";
    }

    public long getId() {
        return chunkid;
    }

    public int getOffset() {
        return offset;
    }

    private XY getXY() {
        if(chunkid == END_ID) {
            return new XY(offset, text.getLineCount());
        }

        long id = chunkid;

        if(!text.isVisible(id)) {
            Long d = text.getPrevVisibleId(id);

            if(d == null) {
                return new XY(0, 0);
            }
            id = 0;
        }

        Iterable<Integer> ys = text.getLineIndexesById(id);
        for(int y : ys) {
            DisplayLine dl = text.getLineByIndex(y);
            int x = dl.getColumn(offset);
            if(x >= 0) {
                return new XY(x, y);
            }
        }
        throw new IllegalStateException("wtf! id: " + id + ", offset: " + offset);
    }

    @Override
    public XY resolve() {
        return getXY();
    }
}
