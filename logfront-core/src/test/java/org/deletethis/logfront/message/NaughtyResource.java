package org.deletethis.logfront.message;

import java.io.IOException;
import java.util.zip.DataFormatException;

/**
 * stolen from
 * http://marxsoftware.blogspot.sk/2012/04/java-7s-support-for-suppressed.html
 *
 * @author miko
 */
public class NaughtyResource implements AutoCloseable {

    private final boolean bigfail;

    public NaughtyResource(boolean bigfail) {
        this.bigfail = bigfail;
    }

    public void doNothingGood() throws DataFormatException {
        throw new DataFormatException("Nothing good can come of this.");
    }

    private void closeImpl() throws IOException {
        if (bigfail) {
            try {
                try {
                    try (
                            NaughtyResource naughty1 = new NaughtyResource(false);
                            NaughtyResource naughty2 = new NaughtyResource(false)) {
                        
                        naughty1.doNothingGood();
                        naughty2.doNothingGood();
                    }
                } catch (IOException | DataFormatException e) {
                    throw new IOException(e);
                }
            } catch (IOException e) {
                throw new IOException(e);
            }

        }
        throw new IOException("Close failed.");
    }
    
    @Override
    public void close() throws IOException {
        closeImpl();
    }
}
