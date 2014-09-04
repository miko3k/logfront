package org.deletethis.logfront.widgets.tilepane.log.data;

import java.util.List;

import org.deletethis.logfront.junit.TestUtil;
import org.junit.Test;

import static org.deletethis.logfront.junit.Assert.*;

public class NumberIteratorTest {

    private void testIterator(boolean forward, long begin, long end, long min, long max, long... expected) {
        NumberIterator it = new NumberIterator(forward, begin, end, min, max);
        List<Long> lst = TestUtil.createList(it);
        assertIterable(lst, expected);
    }

    @Test
    public void test1() {
        testIterator(true, 0, 5, 0, 5, 0, 1, 2, 3, 4);
    }

    @Test
    public void test2() {
        testIterator(false, 5, 0, 0, 5, 5, 4, 3, 2, 1);
    }

    @Test
    public void test3() {
        testIterator(true, 1, 1, 0, 5, 1, 2, 3, 4, 5, 0);
    }

    @Test
    public void test4() {
        testIterator(false, 1, 1, 0, 5, 1, 0, 5, 4, 3, 2);
    }

    @Test
    public void test5() {
        testIterator(true, 0, 6, 0, 5, 0, 1, 2, 3, 4, 5);
    }

    @Test
    public void test6() {
        testIterator(true, 0, 7, 0, 5, 0, 1, 2, 3, 4, 5);
    }

    @Test
    public void testOneElement() {
        testIterator(false, -1, 5, -6, -6, -6);
        testIterator(false, -5, -3, -6, -6, -6);
        testIterator(false, 4, 5, -6, -6, -6);
        testIterator(true, -1, 5, -6, -6, -6);
        testIterator(true, -5, -3, -6, -6, -6);
        testIterator(true, 4, 5, -6, -6, -6);
    }

    @Test
    public void testNoElement() {
        testIterator(false, -1, 5, -6, -7);
        testIterator(false, -5, -3, -6, -7);
        testIterator(false, 4, 5, -6, -7);
        testIterator(true, -1, 5, -6, -7);
        testIterator(true, -5, -3, -6, -7);
        testIterator(true, 4, 5, -6, -7);
    }
}
