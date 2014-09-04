package org.deletethis.logfront.widgets.tilepane.log.data;

public interface ChunkFilter<KEY> {

    public boolean isVisible(KEY key);
}
