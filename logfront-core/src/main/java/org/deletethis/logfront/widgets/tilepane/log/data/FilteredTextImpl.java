package org.deletethis.logfront.widgets.tilepane.log.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deletethis.logfront.util.MetaListener;

public class FilteredTextImpl<KEY, CHUNK extends CharSequence, LINE> implements FilteredText<KEY, CHUNK, LINE> {

    private static class ChunkInfo<KEY, CHUNK, LINE> {

        final KEY key;
        CHUNK chunk;
        List<LINE> lines;
        List<Integer> lineIndexes;
        boolean visible;

        ChunkInfo(KEY key, CHUNK chunk, List<LINE> lines, List<Integer> lineIndexes, boolean visible) {
            this.key = key;
            this.chunk = chunk;
            this.lines = lines;
            this.lineIndexes = lineIndexes;
            this.visible = visible;
        }
    }

    private static class LineInfo<LINE> {

        LINE line;
        long chunkId;

        public LineInfo(LINE line, long chunkId) {
            this.line = line;
            this.chunkId = chunkId;
        }
    }

    private final ChunkWrapper<KEY, CHUNK, LINE> wrapper;
    private final ChunkFilter<KEY> filter;
    private final ChunkFactory<KEY, CHUNK> factory;

    private Map<KEY, Long> keyMap = new HashMap<>();
    private List<ChunkInfo<KEY, CHUNK, LINE>> chunks = new ArrayList<>();
    private List<LineInfo<LINE>> lines = new ArrayList<>();

    private long firstId = 0;
    private final MetaListener<VersionChangeListener> listeners = new MetaListener<>(VersionChangeListener.class);

    private void updateVersion() {
        listeners.getMetaListener().textVersionChanged();
    }

    private List<Integer> seq(int start, int count) {
        List<Integer> result = new ArrayList<>(count);
        for(int i = 0; i < count; ++i) {
            result.add(start + i);
        }
        return result;
    }

    private List<LineInfo<LINE>> lineInfos(Iterable<LINE> lines, long chunkId) {
        List<LineInfo<LINE>> lst = new ArrayList<>();
        for(LINE line : lines) {
            lst.add(new LineInfo<LINE>(line, chunkId));
        }
        return lst;
    }

    public FilteredTextImpl(
            ChunkWrapper<KEY, CHUNK, LINE> wrapper,
            ChunkFilter<KEY> filter,
            ChunkFactory<KEY, CHUNK> factory) {
        this.wrapper = wrapper;
        this.filter = filter;
        this.factory = factory;
    }

    @Override
    public long add(KEY key) {
        if(keyMap.containsKey(key)) {
            throw new IllegalArgumentException("duplicate key: " + key);
        }

        updateVersion();

        CHUNK chunk = factory.createChunk(key);

        long id = firstId + chunks.size();
        keyMap.put(key, id);

        List<LINE> lst = new ArrayList<>();
        for(LINE l : wrapper.wrap(key, chunk)) {
            lst.add(l);
        }

        boolean visible = filter.isVisible(key);
        List<Integer> lineIndexes = null;

        if(visible) {
            lineIndexes = seq(lines.size(), lst.size());
            visible = true;

            lines.addAll(lineInfos(lst, id));
        }

        chunks.add(new ChunkInfo<KEY, CHUNK, LINE>(key, chunk, lst, lineIndexes, visible));
        return id;
    }

    @Override
    public void clear() {
        updateVersion();
        firstId += chunks.size();
        chunks.clear();
        lines.clear();
        keyMap.clear();
    }

    private void verifyChunkId(long chunkId) {
        if(chunkId < firstId) {
            throw new IllegalArgumentException("chunkid too low: " + chunkId + " < " + firstId);
        }

        if(chunkId >= firstId + chunks.size()) {
            throw new IllegalArgumentException(
                    "chunkid too high: " + chunkId + " >= " + firstId + " + " + chunks.size());
        }
    }

    private boolean isLineIndex(int lineIndex) {
        return lineIndex >= 0 && lineIndex < firstId + lines.size();
    }

    private void verifyLineIndex(int idx) {
        if(!isLineIndex(idx)) {
            throw new IllegalArgumentException("bad line index: " + idx + ", size: " + lines.size());
        }
    }

    private ChunkInfo<KEY, CHUNK, LINE> getChunkInfo(long chunkid) {
        verifyChunkId(chunkid);
        return chunks.get((int) (chunkid - firstId));
    }

    @Override
    public KEY getKeyById(long chunkid) {
        return getChunkInfo(chunkid).key;
    }

    @Override
    public long getIdByKey(KEY key) {
        Long res = keyMap.get(key);
        if(res == null) {
            throw new IllegalArgumentException("unknown key: " + key);
        }

        return res;
    }

    @Override
    public LINE getLineByIndex(int lineIndex) {
        verifyLineIndex(lineIndex);

        return lines.get(lineIndex).line;
    }

    @Override
    public Iterable<Integer> getLineIndexesById(long chunkId) {
        List<Integer> idx = getChunkInfo(chunkId).lineIndexes;
        if(idx == null) {
            throw new IllegalArgumentException("chunk " + chunkId + " has no line indexes");
        }

        return idx;
    }

    @Override
    public long getIdByLineIndex(int lineIndex) {
        verifyLineIndex(lineIndex);
        return lines.get(lineIndex).chunkId;
    }

    @Override
    public long getBeginId() {
        return firstId;
    }

    @Override
    public long getEndId() {
        return firstId + chunks.size();
    }

    @Override
    public boolean isVisible(long id) {
        return getChunkInfo(id).visible;
    }

    @Override
    public CHUNK getChunkById(long chunkid) {
        return getChunkInfo(chunkid).chunk;
    }

    @Override
    public boolean anythingVisible() {
        return !lines.isEmpty();
    }

    @Override
    public void addVersionChangeListener(VersionChangeListener listener) {
        listeners.addListener(listener);

    }

    private void resomething(boolean updateVisibility, boolean updateChunk) {
        lines.clear();

        int sz = chunks.size();
        for(int i = 0; i < sz; ++i) {
            long id = firstId + i;

            ChunkInfo<KEY, CHUNK, LINE> ci = chunks.get(i);

            if(updateChunk) {
                ci.chunk = factory.createChunk(ci.key);
                ci.lines = new ArrayList<>();
                for(LINE l : wrapper.wrap(ci.key, ci.chunk)) {
                    ci.lines.add(l);
                }
            }

            if(updateVisibility) {
                ci.visible = filter.isVisible(ci.key);
            }

            if(ci.visible) {
                ci.lineIndexes = seq(lines.size(), ci.lines.size());

                lines.addAll(lineInfos(ci.lines, id));
            } else {
                ci.lineIndexes = null;
            }
        }
    }

    @Override
    public void recreate() {
        updateVersion();
        resomething(true, true);
    }

    @Override
    public void refilter() {
        updateVersion();
        resomething(true, false);
    }

    @Override
    public void rewrap() {
        updateVersion();
        resomething(false, true);
    }

    @Override
    public int getLineCount() {
        return lines.size();
    }

    @Override
    public Long getPrevVisibleId(long id) {
        verifyChunkId(id);
        int idx = (int) (id - firstId);
        for(; idx < chunks.size(); ++idx) {
            if(chunks.get(idx).visible) {
                return idx + firstId;
            }
        }
        return null;
    }

    @Override
    public Long getNextVisibleId(long id) {
        verifyChunkId(id);
        int idx = (int) (id - firstId);
        for(; idx >= 0; --idx) {
            if(chunks.get(idx).visible) {
                return idx + firstId;
            }
        }
        return null;

    }
}
