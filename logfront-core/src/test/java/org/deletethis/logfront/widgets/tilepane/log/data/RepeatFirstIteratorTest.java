package org.deletethis.logfront.widgets.tilepane.log.data;

import java.util.Arrays;
import java.util.List;

import org.deletethis.logfront.junit.TestUtil;
import org.deletethis.logfront.widgets.tilepane.log.data.RepeatFirstIterator;
import org.junit.Test;

import static org.deletethis.logfront.junit.Assert.*;

public class RepeatFirstIteratorTest {
	private void testIterator(Iterable<? extends Object> iterable, Object ... expected)
	{
		RepeatFirstIterator<Object> it = new RepeatFirstIterator<Object>(iterable.iterator());
		List<Object> lst = TestUtil.createList(it);
		assertIterable(lst, expected);
	}

	@Test public void test1() { testIterator(Arrays.asList()); }
	@Test public void test2() { testIterator(Arrays.asList("A"), "A", "A"); }
	@Test public void test3() { testIterator(Arrays.asList("A", "B"), "A", "B", "A"); }
	@Test public void test4() { testIterator(Arrays.asList("A", "B", "C"), "A", "B", "C", "A"); }
}
