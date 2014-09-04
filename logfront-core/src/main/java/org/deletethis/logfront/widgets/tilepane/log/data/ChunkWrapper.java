package org.deletethis.logfront.widgets.tilepane.log.data;

public interface ChunkWrapper<KEY, CHUNK, LINE> {

    public Iterable<LINE> wrap(KEY key, CHUNK chunk);
}
