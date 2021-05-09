package vip.creatio.common.util;

import java.lang.reflect.Array;

@SuppressWarnings("unchecked")
public class Inheritable<T> {

    private final T def;

    private Object[] item;

    private boolean isInherit;

    public Inheritable(T def) {
        this.def = def;
        initItemArray();
        this.item[0] = def;
    }

    public Inheritable(T item, T def) {
        initItemArray();
        this.item[0] = item;
        this.def = def;
    }

    /** Inherit from a parent, returns false if we had a custom value */
    public boolean inherit(Inheritable<T> parent) {
        if (item[0] == def) {
            this.item = parent.item;
            this.isInherit = true;
            return true;
        }
        return false;
    }

    public void set(T item) {
        if (isInherit) {
            initItemArray();
            this.item[0] = item;
            isInherit = false;
        } else {
            if (this.item == null) {
                initItemArray();
            }
            this.item[0] = item;
        }
    }

    public T get() {
        return (T) item[0];
    }

    private void initItemArray() {
        this.item = new Object[1];
    }

    @Override
    public int hashCode() {
        return item[0].hashCode() * 31 * (isInherit ? 1 : -1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Inheritable) {
            return item[0] == ((Inheritable<?>) obj).item[0];
        }
        return false;
    }

    @Override
    public String toString() {
        return "Inheritable{isInherited=" + isInherit + ", item=" + (item == null ? null : item[0]) + '}';
    }
}
