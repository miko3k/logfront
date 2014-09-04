package org.deletethis.logfront.message;

public class SimpleLevel extends AbstractLevel {

    private final String name;
    private final int severity;
    private final float hue;

    public SimpleLevel(String name, int severity, float hue) {
        this.name = name;
        this.severity = severity;
        this.hue = hue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getHue() {
        return hue;
    }

    @Override
    public int getNumericSeverity() {
        return severity;
    }
}
