package vip.creatio.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class StringUtil {

    private static final Pattern brackets = Pattern.compile("[\\[{(](.*?)[]})]");

    private static final Pattern hexCode = Pattern.compile("§x(§[0-9a-fA-F]){6}");

    private static final Pattern qoutes = Pattern.compile("\"(.*?)\"");

    private static final String SPACE = "(^\\s+|\\s+$)";

    public static int checkDepth(String sample) {
        int max = 0, loop = 0;
        boolean inString = false;
        for (byte c : sample.getBytes()) {
            if (!inString) {
                switch (c) {
                    case '{':
                    case '[':
                        loop++;
                        max = Math.max(max, loop);
                        break;
                    case '}':
                    case ']':
                        loop--;
                        break;
                    case '\'':
                    case '"':
                        inString = true;
                        break;
                }
            } else {
                if (c == '"' || c == '\'') {
                    inString = false;
                }
            }
        }
        if (max > 512) {
            throw new RuntimeException("Map string depth out of range: 512 < " + max);
        }
        return max;
    }

    public static Map<String, Object> parseMap(String raw) {
        checkDepth(raw);
        if (raw.charAt(0) == '{' && raw.charAt(raw.length() - 1) == '}') {
            List<String> s = splitJson(raw.substring(1, raw.length() - 1));
            Map<String, Object> map = new HashMap<>();
            for (String d : s) {
                List<String> set = splitJson(d, ':');
                if (set.size() != 2) throw new RuntimeException("Invalid Json Element: " + d + ' ' + '(' + d.getClass().getSimpleName() + ')');
                String key = set.get(0).replaceAll("\"", "");

                map.put(key, mapping(set.get(1)));
            }
            return map;
        }
        throw new RuntimeException("Invalid Json signature.");
    }

    public static List<Object> parseList(String raw) {
        checkDepth(raw);
        List<String> d = splitJson(raw.substring(1, raw.length() - 1));
        List<Object> l = new ArrayList<>();
        for (String s : d) {
            l.add(mapping(s));
        }
        return l;
    }

    private static Object mapping(String ori) {
        switch (ori.charAt(0)) {
            case '"':
                return ori.substring(1, ori.length() - 1);
            case '{':
                return parseMap(ori);
            case '[':
                return parseList(ori);
            default:
                return ori;
        }
    }

    public static List<String> splitJson(String raw) {
        return splitJson(raw, ',');
    }
    public static List<String> splitJson(String raw, char splitter) {
        int inList =0;
        int last = 0;
        List<String> s = new ArrayList<>();
        for (int i = 0, lth = raw.length(); i < lth; i++) {
            if (raw.charAt(i) == '[' || raw.charAt(i) == '{') inList++;
            else if (raw.charAt(i) == ']' || raw.charAt(i) == '}') inList--;
            else if (inList == 0 && raw.charAt(i) == splitter) {
                s.add(raw.substring(last, i).trim());
                last = i + 1;
            }
            if (i == raw.length() - 1) s.add(raw.substring(last, i + 1).trim());
        }
        return s;
    }

    /**
     * Trim only at the end of text.
     */
    public static String backTrim(String src) {
        int len = src.length();
        char[] val = src.toCharArray();

        while (src.charAt(len - 1) <= ' ') {
            len--;
        }
        return (len < src.length()) ? src.substring(0, len) : src;
    }

    /**
     * Part of the replaceAll() method, used to count key string to be replaced.
     */
    public static int[] count(String src, String target) {
        /*
         * Creating corresponding char array for src and target
         * The decisive factor for the code speed.
         */
        char[] tArray = target.toCharArray();
        char[] repArray = src.toCharArray();

        int cursor;
        /* Int array to store where the targets are */
        int[] l = new int[8];
        int valid = 0;

        for (int i = 0, tt = repArray.length - tArray.length; i <= tt; i++) {
            cursor = i;
            for (char sf : tArray) {
                if (repArray[cursor++] != sf) break;
                if ((cursor - i) == tArray.length) {
                    /*
                     * Array resize, a slow step.
                     */
                    if (valid >= l.length) {
                        l = ArrayUtil.resize(l, 8 * (valid >> 3) + 8);
                    }
                    l[valid++] = i;
                }
            }
        }
        /* Remove all empty rooms of the list, costs 50% more time to process with a short string. */
        return ArrayUtil.subArray(l, 0, valid);
    }

    /**
     * Simplified replaceAll() method with default 8-bit replacement map.
     */
    public static String replaceAll(String src, String target, String Replacement) {
        return replaceAll(src, target, Replacement, 8);
    }

    /**
     * The complex version of replace() method, process faster in some cases.
     */
    public static String replaceAll(String src, String target, String replacement, int size) {

        final int difference = replacement.length() - target.length();

        if (difference <= 0) return replace(src, target, replacement);

        char[] array = src.toCharArray();

        /* Init var declaration */
        int cursor;

        int valid = 0;
        int[] l = new int[size];
        boolean cc = false;

        /* Loop all chars in repArray */
        for (int i = 0; i <= array.length - target.length(); i++) {
            cursor = i;
            for (int c = 0; c < target.length(); c++) {
                if (array[cursor++] != target.charAt(c)) break;
                if ((cursor - i) == target.length()) {
                    /*
                     * Array resize, a slow step.
                     */
                    if (valid >= l.length) {
                        cc = true;
                        break;
                    }
                    l[valid++] = i;
                }
            }
            if (cc) break;
        }

        if (cc) {
            return replace(src, target, replacement);
        }

        /* repArray resize if valid != 0 and length of rArray != tArray */
        if (valid != 0) {
            array = ArrayUtil.resize(array, array.length + difference * valid);
        }

        /* Item replacing */
        if (valid > 0) {
            final char[] rArray = replacement.toCharArray();
            for (int i = 0; i < l.length; i++) {

                /* Break if is empty room */
                if (i != 0 && l[i] == 0) break;

                cursor = l[i] + difference * i;

                System.arraycopy(array, cursor + target.length(), array, cursor + replacement.length(), array.length - replacement.length() - cursor);
                System.arraycopy(rArray, 0, array, cursor, replacement.length());
            }
        }
        return String.valueOf(array);
    }

    /**
     * Highly optimized string replacing method.
     *
     * This method uses char array to match the target substring, all implements
     * were based on primitive array operation, which could run 3-4 times faster
     * than classic replaceAll() method in String.class, which uses regex that
     * lags horribly.
     *
     * Since the implements based on char array, if array needs to resize(etc.
     * replacement.length() > target.length()) the speed would draw down quite a
     * lot, but still 2 times faster than replaceAll().
     *
     * @param src The string to be processed.
     *
     * @param target The key string to be replaced
     *
     * @param replacement The replacement string for target string.
     *
     * @return java.lang.String
     */
    public static String replace(String src, String target, String replacement) {
        /*
         * Creating corresponding char array for src, target and replacement
         * The decisive factor for the code speed.
         */
        char[] tArray = target.toCharArray();
        char[] rArray = replacement.toCharArray();
        char[] repArray = src.toCharArray();

        /* Init var declaration */
        int cursor;
        int i = 0;
        int size = repArray.length;

        /* Loop all chars in repArray */
        do {
            cursor = i;
            for (char sf : tArray) {
                /*
                 * If a series of repArray doesn't match tArray template,
                 * then continue to next char loop.
                 */
                if (repArray[cursor++] != sf) break;

                /*
                 * If the cursor successfully pointed to the end of target
                 * char array, then it will start the replacement work.
                 */
                if ((cursor - i) == tArray.length) {
                    cursor -= tArray.length;
                    size += rArray.length - tArray.length;
                    /*
                     * If the length of replacement is greater than target's,
                     * then it needs to be resized, which is another decisive.
                     */
                    if (rArray.length > tArray.length) {
                        char[] repto = new char[size];
                        System.arraycopy(repArray, 0, repto, 0, cursor);
                        System.arraycopy(repArray, cursor + tArray.length, repto, cursor + rArray.length, size - rArray.length - cursor);
                        repArray = repto;
                    } else {
                        System.arraycopy(repArray, cursor + tArray.length, repArray, cursor + rArray.length, size - rArray.length - cursor);
                    }
                    System.arraycopy(rArray, 0, repArray, cursor, rArray.length);
                    i += rArray.length - 1;
                }
            }
            i++;
        } while (i <= size - tArray.length);
        /*
         * If the length of replacement is smaller than target's
         * then the extra chars need to be wiped.
         */
        if (size < repArray.length) {
            for (int c = size; c < repArray.length; c++) {
                repArray[c] = 0;
            }
        }
        return String.valueOf(repArray);
    }

    /**
     * Returns the index of key string if it's present after several spaces
     * in src string.
     *
     * Use "*any*" to match anything.
     *
     * @param src Source string
     *
     * @param key The string wanna to match
     *
     * @return int Index of key in src
     */
    public static int matchAfter(String src, String key) {
        char[] set = key.toCharArray();
        for (int i = 0, success = 0; i <= src.length() - key.length(); i++) {
            if (src.charAt(i) == ' ') continue;
            if (key.equals("*any*")) return i;
            else {
                for (char c : set) {
                    if (src.charAt(i + success++) != c) break;
                    if (success == key.length()) return i;
                }
            }
            break;
        }
        return -1;
    }

    /** Escape text */
    public static String escape(String src) {
        src = replaceAll(src, "\\", "\\\\");
        src = replaceAll(src, "\n", "\\n");
        src = replaceAll(src, "\"", "\\\"");
        return src;
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
}
