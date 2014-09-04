/*
 * xxx
 */
package org.deletethis.logfront.message;

import org.deletethis.logfront.extras.ThrowableInfoBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.zip.DataFormatException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author miko
 */
public class TrowableInfoTest {
    private static void testException(Throwable t) {
        StringPrintStream ps1 = StringPrintStream.create();
        t.printStackTrace(ps1);
        String trace1 = ps1.toString();
        
        ThrowableInfoBuilder tf = new ThrowableInfoBuilder();
        ThrowableInfo ti = tf.createThrowableInfo(t);
        StringPrintStream ps2 = StringPrintStream.create();
        SimpleThrowablePrinter stp = new SimpleThrowablePrinter(ps2);
        ti.printStackTrace(stp);
        String trace2 = ps2.toString();
        
        //System.out.println(trace1);
        //System.out.println("----");
        //System.out.println(trace2);
        //System.out.println(trace1.equals(trace2));
        Assert.assertEquals(trace1, trace2);
        
    }

    @Test
    public void test1() {
        try {
            TrowableInfoTest newClass = new TrowableInfoTest();
            newClass.run();
        } catch (RuntimeException e) {
            testException(e);
        }
    }

    private void run() {
        try {
            try (
                    NaughtyResource naughty1 = new NaughtyResource(true);
                    NaughtyResource naughty2 = new NaughtyResource(false)) {

                naughty1.doNothingGood();
                naughty2.doNothingGood();
            }
        } catch (IOException | DataFormatException e) {
            throw new IllegalStateException(e);
        }
    }
}
