package org.deletethis.logfront.widgets.tilepane.log.data;

public interface FilteredText<KEY, CHUNK extends CharSequence, LINE> {

    public long add(KEY key);

    public void clear();

    public long getIdByKey(KEY key);

    public KEY getKeyById(long chunkid);

    public CHUNK getChunkById(long chunkid);

    public LINE getLineByIndex(int lineIndex);

    public Iterable<Integer> getLineIndexesById(long chunkId);

    public long getIdByLineIndex(int lineIndex);

    public long getBeginId();

    public long getEndId(); // end+1

    public boolean isVisible(long id);

    public int getLineCount();

    public Long getPrevVisibleId(long id);

    public Long getNextVisibleId(long id);

    public boolean anythingVisible();

    public void addVersionChangeListener(VersionChangeListener listener);

    public void recreate();

    public void refilter();

    public void rewrap();
}
