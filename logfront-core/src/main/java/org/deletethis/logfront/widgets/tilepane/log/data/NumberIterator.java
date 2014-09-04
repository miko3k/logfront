package org.deletethis.logfront.widgets.tilepane.log.data;

import java.util.Iterator;

public class NumberIterator implements Iterator<Long> {

    private long next;
    private boolean hasNext;

    final private long min, max, end;
    final private boolean forward;

    private long after(long n) {
        if(forward) {
            ++n;
        } else {
            --n;
        }
        if(n > max) {
            n = min;
        }
        if(n < min) {
            n = max;
        }
        return n;
    }

    private long normalize(long n) {
        if(n < min || n > max) {
            return after(n);
        } else {
            return n;
        }
    }

    public NumberIterator(boolean forward, long begin, long end, long min, long max) {
        this.min = min;
        this.max = max;
        this.forward = forward;

        if(max < min) {
            hasNext = false;
            this.next = this.end = 0;
        } else {
            this.next = normalize(begin);
            this.end = normalize(end);
            hasNext = true;
        }
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public Long next() {
		//System.out.println(next + ", " + hasNext + ", " + end);

        if(!hasNext) {
            throw new IllegalStateException();
        }

        long result = next;

        next = after(next);
        if(next == end) {
            hasNext = false;
        }

        return result;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
