package org.deletethis.logfront.widgets.tilepane.log.data;

public interface ChunkFactory<KEY, CHUNK> {

    public CHUNK createChunk(KEY key);
}
