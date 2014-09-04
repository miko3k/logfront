package org.deletethis.logfront.util;

import java.util.ArrayList;
import java.util.List;

import static org.deletethis.logfront.junit.Assert.assertIterableV;
import org.junit.Before;
import org.junit.Test;

public class MetaListenerTest {

    private interface Listener {

        public void onA(String what);

        public void onB(String what);
    }

    private List<String> results;

    private void action(String id, String method, String what) {
        results.add(id + "-" + method + "-" + what);
    }

    private class Impl implements Listener {

        String id;

        public Impl(String id) {
            this.id = id;
        }

        @Override
        public void onA(String what) {
            action(id, "A", what);

        }

        @Override
        public void onB(String what) {
            action(id, "B", what);
        }
    }

    @Before
    public void init() {
        results = new ArrayList<>();
    }

    @Test
    public void test1() {
        MetaListener<Listener> meta = new MetaListener<>(Listener.class);
        meta.addListener(new Impl("1"));
        meta.addListener(new Impl("2"));
        meta.getMetaListener().onA("test");

        assertIterableV(results, "1-A-test", "2-A-test");
        meta.getMetaListener().onB("test");
        assertIterableV(results, "1-A-test", "2-A-test", "1-B-test", "2-B-test");
    }

}
