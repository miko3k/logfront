package org.deletethis.logfront.widgets.tilepane.log;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.deletethis.logfront.Filter;
import org.deletethis.logfront.SearchNeedle;
import org.deletethis.logfront.colors.LogViewStyle;
import org.deletethis.logfront.message.LogMessage;
import org.deletethis.logfront.widgets.tilepane.log.data.ChunkFilter;
import org.deletethis.logfront.widgets.tilepane.log.data.FilteredText;
import org.deletethis.logfront.widgets.tilepane.log.data.FilteredTextImpl;
import org.deletethis.logfront.widgets.tilepane.log.data.Searcher;
import org.deletethis.logfront.widgets.tilepane.CharData;
import org.deletethis.logfront.widgets.tilepane.CharDataCache;
import org.deletethis.logfront.widgets.tilepane.DamageHandler;
import org.deletethis.logfront.widgets.tilepane.Position;
import org.deletethis.logfront.widgets.tilepane.TextProvider;
import org.deletethis.logfront.widgets.tilepane.XY;

public class LogMessageTextProvider implements TextProvider, LogView {

    private final static int DEFAULT_LEFT_MARGIN = ExpandButtons.BUTTON_WIDTH;
    private final static int WRAPPED_LEFT_MARGIN = ExpandButtons.BUTTON_WIDTH * 2;
    private final static int WRAPPED_EXPAND_LEFT_MARGIN = DEFAULT_LEFT_MARGIN;

    private int windowWidth = 50;
    private DamageHandler damage = null;

    private final Filter filter;
    private final CharDataFactory charDataCache = new CharDataCache();
    private final Set<LogMessage> collapsedStackTraces = new HashSet<>();
    private final CharData filler;
    private final FilteredText<LineKey, FormattedText, DisplayLine> text;
    private final Searcher<LineKey, FormattedText, DisplayLine> searcher;
    private final LogViewStyle style;
    private final ExpandButtons expandButtons;

    private Span currentSpan = null;
    private Long currentSpanId = null;
    private Integer currentExpandButton = null;
    private Searcher.Match currentMatch = null;
    private LogPosition selectionStart = null, selectionEnd = null;

    public void setDamageHandler(DamageHandler damage) {
        this.damage = damage;
    }

    public LogMessageTextProvider(LogViewStyle style, Filter filter) {
        this.filter = filter;

        DisplayLineFactory dlf = new DisplayLineFactory(DEFAULT_LEFT_MARGIN,
                WRAPPED_LEFT_MARGIN, WRAPPED_EXPAND_LEFT_MARGIN) {

                    @Override
                    protected int getMaxWidth() {
                        return windowWidth;
                    }
                };

        ChunkFilter<LineKey> cf = new ChunkFilter<LineKey>() {
            @Override
            public boolean isVisible(LineKey key) {
                if(key.isExpand() && collapsedStackTraces.contains(key.getLogMessage())) {
                    return false;
                }

                return LogMessageTextProvider.this.filter.passes(key.getLogMessage());
            }
        };

        FormattedMessageFactory fmf = new FormattedMessageFactory(style,
                charDataCache);

        this.text = new FilteredTextImpl<LineKey, FormattedText, DisplayLine>(
                dlf, cf, fmf);
        //this.searchMatches = null;
        this.searcher = new Searcher<>(text);
        this.text.addVersionChangeListener(this.searcher);

        this.filler = charDataCache.getCharData(' ', null, style.getLeftPaneBackgroundColor(), null, 0);
        this.style = style;
        this.expandButtons = new ExpandButtons(style, charDataCache);
    }

    private boolean isLineExpandable(int y) {
        if(y >= text.getLineCount()) {
            return false;
        }

        DisplayLine dl = text.getLineByIndex(y);

        if(!dl.isParagraphEnd()) {
            return false;
        }

        return dl.hasExpand();
    }

    private boolean isSelected(long chunkId, int offset) {
        if(selectionEnd == null) {
            return false;
        }

        long id1 = selectionStart.getId();
        int of1 = selectionStart.getOffset();
        long id2 = selectionEnd.getId();
        int of2 = selectionEnd.getOffset();

        if(id2 < id1) {
            int x = of1;
            of1 = of2;
            of2 = x;
            long y = id1;
            id1 = id2;
            id2 = y;
        }

        if(chunkId < id1 || chunkId > id2) {
            return false;
        }

        if(id1 == id2) {
            if(of1 < of2) {
                return offset >= of1 && offset <= of2;
            } else {
                return offset >= of2 && offset <= of1;
            }
        }

        if(id1 == chunkId) {
            return offset >= of1;
        }

        if(id2 == chunkId) {
            return offset <= of2;
        }

        return true;
    }

    @Override
    public CharData getCharData(int x, int y) {
        if(y >= text.getLineCount()) {
            return null;
        }

        DisplayLine dl = text.getLineByIndex(y);

        if(x < DEFAULT_LEFT_MARGIN) {
            if(isLineExpandable(y)) {
                boolean expand = collapsedStackTraces.contains(dl.getLogMessage());
                boolean hover = currentExpandButton != null && currentExpandButton == y;

                return expandButtons.getButtonChar(x, expand, hover);
            } else {
                return filler;
            }
        }

        int idx = dl.getCharacterIndex(x);
        if(idx < 0) {
            return null;
        }

        long chunkId = text.getIdByLineIndex(y);
        FormattedText ft = text.getChunkById(chunkId);

        CharData cd = ft.getCharacters()[idx];
        boolean inCurrentSpan = ft.isInSpan(idx, currentSpan);
        if(inCurrentSpan) {
            cd = charDataCache.getUnderlined(cd);
            cd = charDataCache.getWithForegroundColor(cd, style.getHoverForegroundColor(cd.getForeground()));
        }

        if(isSelected(chunkId, idx)) {
            cd = charDataCache.getWithBackgroundColor(cd, style.getSelectionBackgroundColor());
            cd = charDataCache.getWithForegroundColor(cd, style.getSelectionTextColor());
        }

        Searcher.MatchType type = searcher.getMatchType(chunkId, idx, currentMatch);

        if(type != null) {
            Color background = (type == Searcher.MatchType.CURRENT_MATCH)
                    ? style.getCurrentSearchMatchColor()
                    : style.getSearchMatchColor();

            cd = charDataCache.getWithBackgroundColor(cd, background);
        }

        return cd;
    }

    @Override
    public int getMaxLineWidth() {
        return windowWidth;
    }

    @Override
    public int getNumberOfLines() {
        return text.getLineCount();
    }

    @Override
    public void rewrap(int width) {
        if(width <= 0) {
            return;
        }

        windowWidth = width;
        text.rewrap();
    }

    @Override
    public void refilter() {
        text.refilter();
        if(damage != null) {
            damage.damageView();
            damage.damageSize();
        }
    }

    @Override
    public LogPosition createPosition(XY xy) {
        int x = xy.x, y = xy.y;
        if(y < 0) {
            y = 0;
        }

        if(y >= text.getLineCount()) {
            return LogPosition.end(text);
        } else {
            DisplayLine dl = text.getLineByIndex(y);
            long id = text.getIdByLineIndex(y);
            int offset = dl.getCharacterIndexRounded(x);

            return new LogPosition(text, id, offset);
        }
    }

    private void damageArea(LogPosition begin, LogPosition end) {
        damageArea(begin.resolve(), end.resolve());
    }

    private void damageArea(XY begin, XY end) {
        int y1 = begin.y;
        int y2 = end.y;

        if(y1 == y2) {
            if(begin.x < end.x) {
                damage.damageRowPart(y1, begin.x, end.x);
            } else {
                damage.damageRowPart(y1, end.x, begin.x);
            }
        } else {
            if(y1 < y2) {
                damage.damageRows(y1, y2);
            } else {
                damage.damageRows(y2, y1);
            }
        }
    }

    private void clearCurrentSpan() {
        currentSpan = null;
        currentSpanId = null;
        currentExpandButton = null;
    }

    private void damageText(long id, int firstCharacter, int length) {
        if(length == 0) {
            return;
        }

        if(!text.isVisible(id)) {
            return;
        }

        Iterable<Integer> ys = text.getLineIndexesById(id);

        XY begin = null, end = null;
        for(int y : ys) {
            DisplayLine dl = text.getLineByIndex(y);
            int beg = dl.getColumn(firstCharacter);
            if(beg >= 0) {
                begin = new XY(beg, y);
            }
            int e = dl.getColumn(firstCharacter + length - 1);
            if(e >= 0) {
                end = new XY(e, y);
            }
        }
        assert begin != null && end != null;
        damageArea(begin, end);
    }

    private void damageCurrentSpanIfNotNull() {
        if(currentExpandButton != null) {
            damage.damageRowPart(currentExpandButton, 0,
                    DEFAULT_LEFT_MARGIN - 1);
        }

        if(currentSpan != null) {
            damageText(currentSpanId, currentSpan.getFirstCharacter(), currentSpan.getLength());
        }
    }

    private void updateCurrentSpan(XY xy) {
        int y = xy.getY(), x = xy.getX();
        if(y >= text.getLineCount()) {
            return;
        }

        DisplayLine dl = text.getLineByIndex(y);

        Integer expandButton = null;
        Span span = null;
        if(x < DEFAULT_LEFT_MARGIN) {
            if(isLineExpandable(y)) {
                expandButton = y;
            }
        } else {
            span = dl.getSpan(x);
        }
        if(currentSpan == span && expandButton == currentExpandButton) {
            return;
        }

        damageCurrentSpanIfNotNull();
        clearCurrentSpan();

        if(expandButton != null) {
            currentExpandButton = expandButton;
        } else if(span != null) {
            currentSpan = span;
            currentSpanId = text.getIdByLineIndex(y);
        }

        damageCurrentSpanIfNotNull();
    }

    public void mouseOverTile(XY xy) {
        updateCurrentSpan(xy);
    }

    public void mousePressedOnTile(XY xy, int button) {
        if(button == 1) {
            if(selectionEnd != null) {
                damageArea(selectionStart, selectionEnd);
            }
            selectionStart = createPosition(xy);
            selectionEnd = null;
        }

        updateCurrentSpan(xy);
    }

    public void mouseReleasedOnTile(XY xy, int button) {
        updateCurrentSpan(xy);
    }

    public void mouseExited() {
        if(currentSpan != null || currentExpandButton != null) {
            damageCurrentSpanIfNotNull();
        }

        clearCurrentSpan();
    }

    public void mouseClickOnTile(XY xy, int button) {
        updateCurrentSpan(xy);

        if(button != 1) {
            return;
        }

        if(currentExpandButton != null) {
            DisplayLine dl = text.getLineByIndex(currentExpandButton);

            LogMessage lm = dl.getLogMessage();
            if(collapsedStackTraces.contains(lm)) {
                collapsedStackTraces.remove(lm);
            } else {
                collapsedStackTraces.add(lm);
            }
            refilter();
        }
    }

    private void setSelectionEnd(LogPosition pos) {
        if(selectionEnd == null) {
            selectionEnd = pos;
            damageArea(selectionStart, selectionEnd);
            return;
        } else {
            if(selectionEnd.same(pos)) {
                return;
            }

            damageArea(selectionEnd, pos);
            selectionEnd = pos;
        }
    }

    public void mouseDraggedLeftButton(XY xy) {
        if(selectionStart != null) {
            setSelectionEnd(createPosition(xy));
        }
    }

    public Clickable getCurrentClickable() {
        if(currentSpanId == null || currentSpan == null) {
            return null;
        }

        return currentSpan.getClickable();
    }

    public void setSearchNeedle(SearchNeedle needle) {
        searcher.setNeedle(needle);
        currentMatch = null;
        damage.damageView();
    }

    public Position getCurrentMatch() {
        if(currentMatch == null) {
            return null;
        }

        return createMatchPosition(currentMatch);
    }

    private void damageCurrentMatch() {
        if(currentMatch == null) {
            return;
        }

        LogPosition p = createMatchPosition(currentMatch);
        int length = searcher.getMatchLength(currentMatch);

        if(p != null && length > 0) {
            damageText(p.getId(), p.getOffset(), length);
        }
    }

    public boolean setCurrentMatch(Position p) {
        damageCurrentMatch();
        LogPosition lp = (LogPosition) p;

        currentMatch = searcher.findMatchAfter(lp);
        if(currentMatch != null) {
            damageCurrentMatch();
            return true;
        } else {
            return false;
        }
    }

    private LogPosition createMatchPosition(Searcher.Match m) {
        Integer offset = searcher.getMatchOffset(m);
        if(offset != null) {
            return new LogPosition(text, m.getChunkId(), offset);
        } else {
            return null;
        }
    }

    public Position getFollowingMatch(boolean forward) {
        if(currentMatch == null) {
            throw new IllegalStateException("no current match");
        }

        Searcher.Match m = searcher.findNextMatch(currentMatch, forward);
        if(m != null) {
            return createMatchPosition(m);
        } else {
            return null;
        }
    }

    public void unhighlighSearchMatch() {
        searcher.setNeedle(null);
        currentMatch = null;
        damage.damageView();
    }

    public void addMessage(LogMessage message) {
        text.add(new LineKey(message, false));
        if(message.getThrowable() != null) {
            text.add(new LineKey(message, true));
        }
        if(damage != null) {
            damage.damageSize();
        }
    }

    public void clearMessages() {
        text.clear();
        if(damage != null) {
            damage.damageSize();
        }
    }

    @Override
    public String getSelectedText() {
        if(selectionEnd == null) {
            return null;
        }

        LogPosition start, end;
        if(selectionEnd.after(selectionStart)) {
            start = selectionStart;
            end = selectionEnd;
        } else {
            start = selectionEnd;
            end = selectionStart;
        }

        StringBuilder result = new StringBuilder(256);
        if(start.getId() == end.getId()) {
            if(!text.isVisible(start.getId())) {
                return null;
            } else {
                CharSequence seq = text.getChunkById(start.getId());
                result.append(seq, start.getOffset(), end.getOffset() + 1);
            }
        } else {
            boolean first = true;
            // because last id has value of Long.MAX_VALUE
            for(long chunkId = start.getId(); chunkId <= end.getId() && chunkId < text.getEndId(); ++chunkId) {
                if(!text.isVisible(chunkId)) {
                    continue;
                }

                if(first) {
                    first = false;
                } else {
                    result.append('\n');
                }

                CharSequence seq = text.getChunkById(chunkId);
                if(chunkId == start.getId()) {
                    result.append(seq, start.getOffset(), seq.length());
                } else if(chunkId == end.getId()) {
                    result.append(seq, 0, end.getOffset() + 1);
                } else {
                    result.append(seq);
                }
            }
        }
        return result.toString();
    }
}
