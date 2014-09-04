package org.deletethis.logfront.widgets.tilepane.log.data;

import java.util.Iterator;

public class RepeatFirstIterator<T> implements Iterator<T> {

    private static final int CONSTRUCTED = 0, NORMAL = 1, AFTER_LAST = 3;

    private final Iterator<? extends T> base;
    private T toRepeat;
    private int state;

    public RepeatFirstIterator(Iterator<? extends T> base) {
        this.base = base;
        this.state = CONSTRUCTED;
    }

    @Override
    public boolean hasNext() {
        switch(state) {
            case CONSTRUCTED:
                return base.hasNext();
            case NORMAL:
                return true;
            case AFTER_LAST:
                return false;
            default:
                throw new IllegalStateException("state = " + state);
        }
    }

    @Override
    public T next() {
        T tmp;
        switch(state) {
            case CONSTRUCTED:
                tmp = base.next();
                toRepeat = tmp;
                state = NORMAL;
                return tmp;

            case NORMAL:
                if(base.hasNext()) {
                    return base.next();
                } else {
                    state = AFTER_LAST;
                    return toRepeat;
                }

            default:
                throw new IllegalStateException("state = " + state);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
