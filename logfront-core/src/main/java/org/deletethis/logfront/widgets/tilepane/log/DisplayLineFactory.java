package org.deletethis.logfront.widgets.tilepane.log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deletethis.logfront.util.SimpleTextWrapper;
import org.deletethis.logfront.util.WrappedLine;
import org.deletethis.logfront.widgets.tilepane.log.data.ChunkWrapper;

abstract public class DisplayLineFactory
        implements ChunkWrapper<LineKey, FormattedText, DisplayLine> {

    abstract protected int getMaxWidth();

    final private int defaultLeftMargin, wrappedLeftMargin, wrappedLeftMarginExpand;

    public DisplayLineFactory(int defaultLeftMargin, int wrappedLeftMargin,
            int wrappedLeftMarginExpand) {
        this.defaultLeftMargin = defaultLeftMargin;
        this.wrappedLeftMargin = wrappedLeftMargin;
        this.wrappedLeftMarginExpand = wrappedLeftMarginExpand;
    }

    @Override
    public Iterable<DisplayLine> wrap(LineKey key, FormattedText chunk) {
        int maxWidth = getMaxWidth();
        int firstLineMargin = defaultLeftMargin;
        int wrappedMargin = (key.isExpand()) ? wrappedLeftMarginExpand : wrappedLeftMargin;

        List<DisplayLine> result = new ArrayList<>();

        Iterator<WrappedLine> iterator = new SimpleTextWrapper(chunk, maxWidth - defaultLeftMargin, maxWidth - wrappedMargin);
        boolean first = true;
        while(iterator.hasNext()) {
            WrappedLine line = iterator.next();
            boolean last = !iterator.hasNext();

            int margin = (first) ? firstLineMargin : wrappedMargin;

            result.add(new DisplayLine(key, chunk, line.getBegin(), margin, line.getLength(),
                    first, last));

            first = false;
        }

        return result;
    }
}
