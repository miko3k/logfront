package org.deletethis.logfront.cfg;

public class ScalarCfgValue extends BaseValue {

    private String value;

    @Override
    public String toString() {
        return value;
    }

    public ScalarCfgValue(String str) {
        if (str == null) {
            throw new IllegalArgumentException();
        }
        this.value = str;
    }

    public ScalarCfgValue(int v) {
        this(String.valueOf(v));
    }

    public ScalarCfgValue(float v) {
        this(String.valueOf(v));
    }

    public ScalarCfgValue(boolean v) {
        this(String.valueOf(v));
    }

    @Override
    public String getString(String def) {
        return value;
    }

    @Override
    public Boolean getBoolean(Boolean def) {
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        return def;
    }

    @Override
    public Float getFloat(Float def) {
        try {
            return Float.valueOf(value);
        } catch (NumberFormatException ex) {
            return def;
        }
    }

    @Override
    public Integer getInteger(Integer def) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            return def;
        }
    }

    @Override
    public boolean isScalar() {
        return true;
    }
}
