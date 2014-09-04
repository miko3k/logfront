package org.deletethis.logfront.util;

import java.util.ArrayList;
import java.util.List;

import static org.deletethis.logfront.junit.Assert.assertIterableV;
import org.junit.Test;

public class SimpleTextWrapperTest {

    private static List<String> wrap(String text, int l1, int l2) {
        SimpleTextWrapper stw = new SimpleTextWrapper(text, l1, l2);
        List<String> result = new ArrayList<>();
        while (stw.hasNext()) {
            WrappedLine wl = stw.next();
            result.add(text.substring(wl.getBegin(), wl.getBegin() + wl.getLength()));
        }
        return result;
    }

    public static void printVector(List<String> list) {
        for (String s : list) {
            System.out.println(s + "|");
        }
    }

    @Test
    public void doText() {
		//printVector(wrap("hello, everyone, how are you?", 10, 5));

        assertIterableV(wrap("a b c ", 2, 2), "a ", "b ", "c ");
        assertIterableV(wrap("aaa bb cc ", 5, 3), "aaa ", "bb ", "cc ");
        assertIterableV(wrap("hello, everyone, how are you?", 10, 5), "hello, ", "every", "one, ", "how ", "are ", "you?");
    }

}
