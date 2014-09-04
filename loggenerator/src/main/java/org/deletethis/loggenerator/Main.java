package org.deletethis.loggenerator;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.deletethis.loggenerator.backends.Slf4jBackend;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                List<Backend> backends = new ArrayList<>();
                backends.add(new Slf4jBackend());
                backends.add(new DummyBackend());

                new Frame(backends);
            }
        });
    }
}
