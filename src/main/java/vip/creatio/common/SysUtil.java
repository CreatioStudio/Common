package vip.creatio.common;

import java.lang.reflect.Array;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.function.Supplier;

public final class SysUtil {

    private SysUtil() {}

    public static <R> R exec(Supplier<R> sup) {
        return sup.get();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <R, T> List<R> batch(List<T> list, Function<T, R> func) {
        ListIterator iter = list.listIterator();
        while (iter.hasNext()) {
            iter.set(func.apply((T) iter.next()));
        }
        return (List<R>) list;
    }

    @SuppressWarnings({"unchecked"})
    public static <R, T> R[] batch(T[] array, Function<T, R> func) {
        if (array.length == 0) return (R[]) new Object[0];

        R first = func.apply(array[0]);
        R[] arr = (R[]) Array.newInstance(first.getClass(), array.length);
        arr[0] = first;

        for (int i = 1; i < arr.length; i++) {
            arr[i] = func.apply(array[i]);
        }

        return arr;
    }

    public static <T, R> R nullable(T obj, Function<T, R> func) {
        if (obj == null) return null;
        return func.apply(obj);
    }

    public static <T, R> R notnull(T obj, Function<T, R> func, Supplier<? extends RuntimeException> exc) {
        if (obj == null) throw exc.get();
        return func.apply(obj);
    }

    public static <T, R> R notnull(T obj, Function<T, R> func) {
        return notnull(obj, func, () -> new NullPointerException("Object cannot be null!"));
    }

}
