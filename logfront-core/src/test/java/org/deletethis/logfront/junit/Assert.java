package org.deletethis.logfront.junit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Assert {
    public static <T> void assertIterable(Iterable<T> iterable, long [] values)
    {
        List<Long> list = new ArrayList<>(values.length);
        for(long n : values)
            list.add(n);
        
        assertIterable(iterable, list.toArray());
    }

    
    public static <T> void assertIterableV(Iterable<T> iterable, Object ... values)
    {
        assertIterable(iterable, values);
    }
    
    public static <T> void assertIterable(Iterable<T> iterable, Object [] values)
    {
        if(iterable == null)
            throw new AssertionError("iterable is null");

        Iterator<T> iter = iterable.iterator();

        assertIterator(iter, values);
    }

    public static <T> void assertIteratorV(Iterator<T> iter, Object ... values) {
        assertIterator(iter, values);
    }
    
    public static <T> void assertIterator(Iterator<T> iter, Object [] values)
    {
        if(iter == null)
            throw new AssertionError("iterator is null");
        
        int n;
        for(n = 0; n < values.length; ++n) {
            if(!iter.hasNext()) {
                throw new AssertionError("iterable too short after " + n + " elements");
            }
            T next = iter.next();
            if((values[n] != null && !values[n].equals(next)) || (values[n] == null && next != null)) {
                throw new AssertionError("element " + n + ": " + values[n] + " != " + next);
            }
        }
        if(iter.hasNext())
           throw new AssertionError("iterable too short after " + n + " elements, next element is " + iter.next());
    	
    }
}
