package org.deletethis.loggenerator;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class Frame extends JFrame {

    private static final long serialVersionUID = -2666249204666087128L;
    private ButtonGroup backendGroup = new ButtonGroup();
    private Backend currentBackend;
    private Random random = new Random();

    // currentBackend should be set before calling this
    private JRadioButton createBackendRationButton(final Backend backend) {
        JRadioButton result = new JRadioButton(backend.getLongName());
        if(backend == currentBackend) {
            result.setSelected(true);
        }
        result.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                currentBackend = backend;
            }
        });
        backendGroup.add(result);
        return result;
    }

    private String[] names = {
        "org.deletethis.loggenerator",
        "org.deletethis.loggenerator.subpackage.Class",
        "org.deletethis.loggenerator.subpackage.AnotherClass",
        "org.deletethis.loggenerator.another_subpackage.Class"
    };

    private String[] messages = {
        "The Simple Logging Facade for Java (SLF4J) serves as a simple facade or abstraction for various logging frameworks (e.g. java.util.logging, logback, log4j) allowing the end user to plug in the desired logging framework at deployment time.",
        "Before you start using SLF4J, we highly recommend that you read the two-page SLF4J user manual.",
        "Hello world"
    };

    private void generateMessage() {
        int mins = currentBackend.getMinSeverity();
        int maxs = currentBackend.getMaxSeverity();

        int severity = random.nextInt(maxs - mins + 1) + mins;
        String name = names[random.nextInt(names.length)];
        String message = messages[random.nextInt(messages.length)];
        Throwable t = null;
        try {
            switch(random.nextInt(8)) {
                case 0:
                    throw new InternalError("TEST EXCEPTION");
                case 1:
                    throw new IllegalStateException("test");
                default:
                    break;
            }
        } catch(Throwable thr) {
            t = thr;
        }
        currentBackend.createLog(name, severity, message, t);
    }

    private void generateMessages(int number) {
        for(int i = 0; i < number; ++i) {
            generateMessage();
        }
    }

    public Frame(List<Backend> backends) {
        super("Log Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        currentBackend = backends.get(0);

        for(Backend b : backends) {
            contentPane.add(createBackendRationButton(b));
        }

        final SpinnerNumberModel spinnerModel = new SpinnerNumberModel(10, 0, 100000, 1);
        contentPane.add(new JSpinner(spinnerModel));
        JButton goButton = new JButton("Go!");
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                generateMessages(spinnerModel.getNumber().intValue());
            }
        });
        contentPane.add(goButton);

        pack();
        setVisible(true);
    }

}
