package org.deletethis.logfront.widgets.tilepane.log.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.deletethis.logfront.junit.Assert.assertIterableV;

import org.junit.Before;
import org.junit.Test;

public class FilteredTextImplTest {

    private static class Factory implements ChunkFactory<String, String> {

        @Override
        public String createChunk(String key) {
            switch (key) {
                case "a":
                    return "apple";
                case "b":
                    return "Bob";
                case "c":
                    return "centipede is long";
                case "e":
                    return "elephant";
                case "f":
                    return "FFF";
                case "g":
                    return "GO!";
                case "h":
                    return "hoooray";
                case "i":
                    return "Indiana.";
            }
            throw new IllegalArgumentException("key = " + key);
        }
    }

    private class Filter implements ChunkFilter<String> {

        @Override
        public boolean isVisible(String key) {
            return !invisible.contains(key);
        }
    }

    private class Wrapper implements ChunkWrapper<String, String, String> {

        @Override
        public List<String> wrap(String key, String chunk) {
            int w = maxw;
            if (w <= 0) {
                w = 1;
            }
            List<String> result = new ArrayList<>();
            for (int i = 0;; i += w) {
                if (i + w >= chunk.length()) {
                    result.add(chunk.substring(i));
                    break;
                } else {
                    result.add(chunk.substring(i, i + w));
                }
            }
            return result;
        }
    }

    private FilteredText<String, String, String> ft;
    private Set<String> invisible;
    private int maxw;
    private Factory factory;
    private Wrapper wrapper;
    private Filter filter;

    @Before
    public void init() {
        invisible = new HashSet<>();
        maxw = 5;
        factory = new Factory();
        wrapper = new Wrapper();
        filter = new Filter();
        ft = new FilteredTextImpl<>(wrapper, filter, factory);

        ft.add("a");
        ft.add("c");
        ft.add("f");
    }

    @Test
    public void testWrapper() {
        Wrapper wrapper = new Wrapper();
        assertIterableV(wrapper.wrap(null, "1234567"), "12345", "67");
        assertIterableV(wrapper.wrap(null, "1234567890"), "12345", "67890");
        assertIterableV(wrapper.wrap(null, "12345678901"), "12345", "67890", "1");
        assertIterableV(wrapper.wrap(null, "1234"), "1234");
        assertIterableV(wrapper.wrap(null, ""), "");
    }

    /*
     @Test
     public void testSimple()
     {
     assertTrue(ft.isVisible("c"));
     assertFalse(ft.isVisible("d"));
     ////        assertIterable(ft.getLines("a"), "apple");
     //       assertIterable(ft.getLines("c"), "centi", "pede ", "is lo", "ng");

     assertEquals(ft.getLine(0), "apple");
     assertEquals(ft.getLine(3), "is lo");
     assertEquals(ft.getLine(5), "FFF");
     assertEquals(ft.getLine(6), null);
     }

     @Test
     public void test1()
     {
     assertEquals(ft.getLineCount(), 6);
     assertIterable(ft.getLineOffsets("c"), 1, 2, 3, 4);
     invisible.add("a");
     assertEquals(ft.getLineCount(), 6);
     assertEquals(ft.getChunkIndex(1), 1);
     ft.refilter();
     assertEquals(ft.getLineCount(), 5);
     assertEquals(ft.getChunkIndex(1), 1);
     assertIterable(ft.getLineOffsets("c"), 0, 1, 2, 3);
     assertEquals(ft.getLine(5), null);
     assertEquals(ft.getLine(2), "is lo");
     ft.recreate();
     assertIterable(ft.getLineOffsets("c"), 0, 1, 2, 3);
     assertEquals(ft.getLine(2), "is lo");
     ft.rewrap();
     assertIterable(ft.getLineOffsets("c"), 0, 1, 2, 3);
     assertEquals(ft.getLine(2), "is lo");
     ft.recreate();
     assertEquals(ft.getLine(2), "is lo");
     //       assertIterable(ft.getLines("c"), "centi", "pede ", "is lo", "ng");
     assertEquals(ft.getChunkIndex(1), 1);

     assertEquals(ft.getNextKey("a"), "c");
     assertEquals(ft.getNextKey("c"), "f");
     assertEquals(ft.getNextKey("f"), null);

     assertEquals(ft.getPrevKey("a"), null);
     assertEquals(ft.getPrevKey("c"), "a");
     assertEquals(ft.getPrevKey("f"), "c");

     assertEquals(ft.getNextVisibleKey("a"), "c");
     assertEquals(ft.getNextVisibleKey("c"), "f");
     assertEquals(ft.getNextVisibleKey("f"), null);

     assertEquals(ft.getPrevVisibleKey("a"), null);
     assertEquals(ft.getPrevVisibleKey("c"), null);
     assertEquals(ft.getPrevVisibleKey("f"), "c");
     }

     @Test
     public void test2()
     {
     invisible.add("a");
     invisible.add("f");
     ft.refilter();
     assertEquals(ft.getLineOffsets("a"), null);
     assertEquals(ft.getLineCount(), 4);
     assertIterable(ft.getLineOffsets("c"), 0, 1, 2, 3);

     assertEquals(ft.getNextVisibleKey("a"), "c");
     assertEquals(ft.getNextVisibleKey("c"), null);
     assertEquals(ft.getNextVisibleKey("f"), null);

     assertEquals(ft.getPrevVisibleKey("a"), null);
     assertEquals(ft.getPrevVisibleKey("c"), null);
     assertEquals(ft.getPrevVisibleKey("f"), "c");

     ft.rewrap();
     assertEquals(ft.getLine(2), "is lo");
     assertEquals(ft.getLine(5), null);

     //      assertEquals(ft.getChunk("a"), "apple");
     //     assertEquals(ft.getChunk("f"), "FFF");
     }*/
}
