package vip.creatio.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Array Utilities, for faster array processing.
 *
 * For the speed, there's no security check.
 */
public class ArrayUtil {

    //No default constructor
    private ArrayUtil() {}

    public static char[] arrayCopy(char[] src, int srcPos, char[] dest, int destPos, int length) {
        for (int i = 0; i < length; i++) {
            dest[destPos + i] = src[srcPos + i];
        }
        return dest;
    }

    public static int[] arrayCopy(int[] src, int srcPos, int[] dest, int destPos, int length) {
        for (int i = 0; i < length; i++) {
            dest[destPos + i] = src[srcPos + i];
        }
        return dest;
    }

    public static <T> T[] arrayCopy(T[] src, int srcPos, T[] dest, int destPos, int length) {
        if (length >= 0) System.arraycopy(src, srcPos, dest, destPos, length);
        return dest;
    }

    public static char[] subArray(char[] src, int start, int end) {
        return arrayCopy(src, start, new char[end - start], 0, end - start);
    }

    public static int[] subArray(int[] src, int start, int end) {
        return arrayCopy(src, start, new int[end - start], 0, end - start);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] subArray(T[] src, int start, int end) {
        return arrayCopy(src, start, (T[]) Array.newInstance(src.getClass().getComponentType(), end - start), 0, end - start);
    }

    public static <T> T[] subArray(T[] src, int start) {
        return subArray(src, start, src.length);
    }

    public static char[] resize(char[] old, int size) {
        return arrayCopy(old, 0, new char[size], 0, Math.min(size, old.length));
    }

    public static int[] resize(int[] old, int size) {
        return arrayCopy(old, 0, new int[size], 0, Math.min(size, old.length));
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] resize(T[] old, int size) {
        return arrayCopy(old, 0, (T[]) Array.newInstance(old.getClass().getComponentType(), size), 0, Math.min(size, old.length));
    }

    public static <T> T get(List<T> list, int index) {
        return (list.size() - 1 <= index) ? list.get(index) : null;
    }

    public static <T> T get(T[] array, int index) {
        return (array.length > index) ? array[index] : null;
    }

    public static <T> T[] append(T[] arr1, T[] arr2) {
        return arrayCopy(arr2,0, resize(arr1, arr1.length + arr2.length), arr1.length, arr2.length);
    }

    public static int[] toIntArray(Collection<? extends Integer> list) {
        int[] array = new int[list.size()];
        int i = 0;
        for (int num : list) {
            array[i++] = num;
        }
        return array;
    }

    public static byte[] toByteArray(Collection<? extends Byte> list) {
        byte[] array = new byte[list.size()];
        int i = 0;
        for (byte num : list) {
            array[i++] = num;
        }
        return array;
    }

    public static short[] toShortArray(Collection<? extends Short> list) {
        short[] array = new short[list.size()];
        int i = 0;
        for (short num : list) {
            array[i++] = num;
        }
        return array;
    }

    public static char[] toCharArray(Collection<? extends Character> list) {
        char[] array = new char[list.size()];
        char i = 0;
        for (char num : list) {
            array[i++] = num;
        }
        return array;
    }

    public static long[] toLongArray(Collection<? extends Long> list) {
        long[] array = new long[list.size()];
        int i = 0;
        for (long num : list) {
            array[i++] = num;
        }
        return array;
    }

    public static float[] toFloatArray(Collection<? extends Float> list) {
        float[] array = new float[list.size()];
        int i = 0;
        for (float num : list) {
            array[i++] = num;
        }
        return array;
    }

    public static double[] toDoubleArray(Collection<? extends Double> list) {
        double[] array = new double[list.size()];
        int i = 0;
        for (double num : list) {
            array[i++] = num;
        }
        return array;
    }

    public static List<Integer> toList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int j : array) {
            list.add(j);
        }
        return list;
    }

    public static List<Long> toList(long[] array) {
        List<Long> list = new ArrayList<>();
        for (long j : array) {
            list.add(j);
        }
        return list;
    }

    public static List<Float> toList(float[] array) {
        List<Float> list = new ArrayList<>();
        for (float j : array) {
            list.add(j);
        }
        return list;
    }

    public static List<Double> toList(double[] array) {
        List<Double> list = new ArrayList<>();
        for (double j : array) {
            list.add(j);
        }
        return list;
    }
}
