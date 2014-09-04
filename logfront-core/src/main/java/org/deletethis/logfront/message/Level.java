package org.deletethis.logfront.message;

public interface Level extends Comparable<Level> {

    public String getName();

    public float getHue();

    public int getNumericSeverity();

    public boolean isMoreSevereThan(Level level);

    public boolean isLessSevereThan(Level level);

    @Override
    public boolean equals(Object object);

    @Override
    public int hashCode();
}
