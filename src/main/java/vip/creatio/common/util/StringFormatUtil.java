package vip.creatio.common.util;

import java.text.NumberFormat;
import java.util.function.Function;

public class StringFormatUtil {

    private static final NumberFormat DIGIT_1 = NumberFormat.getInstance();
    private static final NumberFormat DIGIT_2 = NumberFormat.getInstance();
    static {
        DIGIT_2.setMaximumFractionDigits(2);
        DIGIT_2.setMinimumFractionDigits(2);
        DIGIT_1.setMaximumFractionDigits(1);
        DIGIT_1.setMinimumFractionDigits(1);
    }

    public static NumberFormat getDigit1Format() {
        return (NumberFormat) DIGIT_1.clone();
    }

    public static NumberFormat getDigit2Format() {
        return (NumberFormat) DIGIT_2.clone();
    }


    public static String intToString(int num) {
        if (num == 1) return "1st";
        if (num == 2) return "2nd";
        if (num == 3) return "3rd";
        return num + "th";
    }

    public static String toAlignedString(int num, int maxLen) {
        String str = String.valueOf(num);
        return " ".repeat(Math.max(0, maxLen - str.length())) + str;
    }

    public static String toRomanNumber(int num) {
        StringBuilder sb = new StringBuilder();
        if (num == 0) return "0";
        while (num != 0) {
            if (num >= 1000) {
                sb.append("M");
                num -= 1000;
            } else if (num >= 900) {
                sb.append("CM");
                num -= 900;
            } else if (num >= 500) {
                sb.append("D");
                num -= 500;
            } else if (num >= 400) {
                sb.append("CD");
                num -= 400;
            } else if (num >= 100) {
                sb.append("C");
                num -= 100;
            } else if (num >= 90) {
                sb.append("XC");
                num -= 90;
            } else if (num >= 50) {
                sb.append("L");
                num -= 50;
            } else if (num >= 40) {
                sb.append("XL");
                num -= 40;
            } else if (num >= 10) {
                sb.append("X");
                num -= 10;
            } else if (num >= 9) {
                sb.append("IX");
                num -= 9;
            } else if (num >= 5) {
                sb.append("V");
                num -= 5;
            } else if       (num >= 4) {
                sb.append("IV");
                num -= 4;
            } else if (num >= 1) {
                sb.append("I");
                num -= 1;
            }
        }
        return sb.toString();
    }

    /** Format multiple string segments to something like 'A, B & C' or 'A, B, C & 55+ items' */
    public static String formatItems(int count, String suffix, String... items) {
        StringBuilder builder = new StringBuilder();
        if (items.length > count) {
            boolean flag = false;
            for (int i = 0; i < count; i++) {
                if (flag) builder.append(", ");
                else flag = true;
                builder.append(items[i]);
            }
            builder.append(" & ").append(items.length - count).append("+").append(suffix);
        } else {
            if (items.length == 1) {
                builder.append(items[0]).append(suffix);
            } else if (items.length == 0) {
                builder.append('0').append(suffix);
            } else {
                boolean flag = false;
                for (int i = 0; i < count - 1; i++) {
                    if (flag) builder.append(", ");
                    else flag = true;
                    builder.append(items[i]);
                }
                builder.append(" & ").append(items[items.length - 1]).append(suffix);
            }
        }
        return builder.toString();
    }

    public static String formatItems(String suffix, String... items) {
        return formatItems(3, suffix, items);
    }

    @SafeVarargs
    public static <T> String formatItems(String suffix, Function<T, String> toString, T... items) {
        return formatItems(3, suffix, eachToString(items, toString));
    }

    @SafeVarargs
    public static <T> String formatItems(String suffix, T... items) {
        return formatItems(suffix, Object::toString, items);
    }

    public static <T> String[] eachToString(T[] array, Function<T, String> toString) {
        String[] str = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            str[i] = toString.apply(array[i]);
        }
        return str;
    }
}
