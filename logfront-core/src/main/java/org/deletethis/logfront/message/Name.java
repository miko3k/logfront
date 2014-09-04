package org.deletethis.logfront.message;

import java.util.Objects;

public final class Name {

    private final String name;
    private final Name parent;

    public Name(String name) {
        int idx = name.lastIndexOf('.');
        if(idx < 0) {
            parent = null;
            this.name = name;
        } else {
            parent = new Name(name.substring(0, idx));
            this.name = name.substring(idx + 1);
        }
    }

    @Override
    public String toString() {
        if(parent == null) {
            return name;
        } else {
            return parent.toString() + "." + name;
        }
    }

    public Name getParent() {
        return parent;
    }

    public int length() {
        Name p = parent;
        int len = 1;
        while(p != null) {
            ++len;
            p = p.parent;
        }
        return len;
    }

    public boolean beginsWith(Name name) {
        int mylen = length();
        int prefixlen = name.length();

        int diff = mylen - prefixlen;
        if(diff < 0) {
            return false;
        }

        Name me = this;
        for(int i = 0; i < diff; ++i) {
            me = me.parent;
        }

        return me.equals(name);
    }

    public boolean equals(Name other) {
        if(other.parent == null && this.parent != null) {
            return false;
        }

        if(other.parent != null && this.parent == null) {
            return false;
        }

        if(other.parent != null && this.parent != null) {
            if(!other.parent.equals(this.parent)) {
                return false;
            }
        }

        return name.equals(other.name);
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Name)) {
            return false;
        }

        return equals((Name) other);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.parent);
        return hash;
    }

    public String getName() {
        return name;
    }
}
