package org.deletethis.logfront.widgets.tilepane.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.deletethis.logfront.colors.LogViewStyle;
import org.deletethis.logfront.message.LogMessage;
import org.deletethis.logfront.message.Name;
import org.deletethis.logfront.message.SimpleThrowablePrinter;
import org.deletethis.logfront.widgets.tilepane.log.data.ChunkFactory;

public class FormattedMessageFactory implements ChunkFactory<LineKey, FormattedText> {

    private final LogViewStyle style;
    private final CharDataFactory factory;

    public FormattedMessageFactory(LogViewStyle style, CharDataFactory factory) {
        this.style = style;
        this.factory = factory;
    }

    private void appendName(LogMessage lm, Formatter f, Name n) {
        f.startSpan(Clickable.loggerName(lm, n));
        if(n.getParent() != null) {
            appendName(lm, f, n.getParent());
            f.append(".");
        }
        f.append(n.getName());
        f.endSpan();
    }

    @Override
    public FormattedText createChunk(LineKey key) {
        LogMessage msg = key.getLogMessage();
        Formatter f = new Formatter(factory);
        f.setBackground(style.getBackgroundColor());
        if(!key.isExpand()) {
            f.setColor(style.getNormalTextColor());
            f.append(msg.getDate().toString());
            f.append(" [");
            f.setColor(style.getThreadNameColor(msg.getThreadName()));
            f.startSpan(Clickable.threadName(msg));
            f.append(msg.getThreadName());
            f.endSpan();
            f.setColor(style.getNormalTextColor());
            f.append("] ");
            f.setColor(style.getLevelColor(msg.getLevel()));
            f.startSpan(Clickable.level(msg));
            f.append(msg.getLevel().toString());
            f.endSpan();
            f.append(" ");
            f.setColor(style.getLoggerColor(msg.getName()));
            appendName(msg, f, msg.getName());
            f.setColor(style.getNormalTextColor());
            f.append(" ");
            f.append(msg.getMessage());

            return f.getFormattedText();
        } else {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(byteOut);
            SimpleThrowablePrinter stp = new SimpleThrowablePrinter(ps, true);
            msg.getThrowable().printStackTrace(stp);
            ps.flush();
            
            f.startSpan(Clickable.stackTrace(msg));
            f.setColor(style.getExceptionLineColor());
            f.append(byteOut.toString().trim());
            f.endSpan();

            return f.getFormattedText();
        }
    }
}
