package org.deletethis.logfront.junit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestUtil {
	public static <T> List<T> createList(Iterator<T> iter)
	{
		List<T> result =new ArrayList<>();
		while(iter.hasNext()) {
			result.add(iter.next());
		}
		return result;
	}
}
