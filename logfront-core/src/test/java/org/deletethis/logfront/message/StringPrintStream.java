/*
 * xxx
 */

package org.deletethis.logfront.message;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author miko
 */
public class StringPrintStream extends PrintStream {
    private ByteArrayOutputStream os;
    
    private StringPrintStream(OutputStream output) 
    { 
        super(output);
    }
    
    public static StringPrintStream create()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringPrintStream result = new StringPrintStream(baos);
        result.os = baos;
        return result;
    }

    @Override
    public String toString()
    {
        flush();
        return new String(os.toByteArray());
    }
}
