package org.deletethis.logfront.message;

public abstract class AbstractLevel implements Level {

	// Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
    //   negative integer  ... this object is less than
    @Override
    public int compareTo(Level level) {
        int mysev = getNumericSeverity();
        int othersev = level.getNumericSeverity();

        return mysev - othersev;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean isMoreSevereThan(Level level) {
        return getNumericSeverity() > level.getNumericSeverity();
    }

    @Override
    public boolean isLessSevereThan(Level level) {
        return getNumericSeverity() < level.getNumericSeverity();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Level)) {
            return false;
        }
        return getNumericSeverity() == ((Level) obj).getNumericSeverity();
    }

    @Override
    public int hashCode() {
        return getNumericSeverity();
    }

}
