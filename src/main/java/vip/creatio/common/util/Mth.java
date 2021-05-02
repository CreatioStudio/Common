package vip.creatio.common.util;

import java.util.Random;

import static java.lang.Math.PI;

public final class Mth {

    private static final float[] SIN = new float[65536];
    public static final float TO_RADIANS = (float) Math.toRadians(1);
    public static final float TO_DEGREES = (float) Math.toDegrees(1);
    public static final float SQRT_OF_TWO = sqrt(2.0f);
    private static final Random RANDOM = new Random();
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{
            0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
    };
    private static final double FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
    private static final double[] ASIN_TAB = new double[257];
    private static final double[] COS_TAB = new double[257];

    static {
        for (int i = 0; i < SIN.length; ++i) {
            SIN[i] = (float)Math.sin(i * PI * 2.0D / 65536.0D);
        }

        for (int i = 0; i < 257; ++i) {
            double d2 = Math.asin(i / 256D);
            COS_TAB[i] = Math.cos(d2);
            ASIN_TAB[i] = d2;
        }
    }

    //No default constructor
    private Mth() {}


    public static float sin(float value) {
        return SIN[(int)(value * 10430.378F) & 0xFFFF];
    }

    public static float cos(float value) {
        return SIN[(int) (value * 10430.378F + 16384.0F) & 0xFFFF];
    }


    public static float sqrt(float value) {
        return sqrt((double) value);
    }

    public static float sqrt(double value) {
        return (float) Math.sqrt(value);
    }


    //Make sure the value is inside a range.
    public static double clamp(double ori, double min, double max) {
        if (ori < min) {
            return min;
        } else {
            return ori > max ? max : ori;
        }
    }

    public static float clamp(float ori, float min, float max) {
        if (ori < min) {
            return min;
        } else {
            return ori > max ? max : ori;
        }
    }

    public static int clamp(int ori, int min, int max) {
        if (ori < min) {
            return min;
        } else {
            return ori > max ? max : ori;
        }
    }

    public static long clamp(long ori, long min, long max) {
        if (ori < min) {
            return min;
        } else {
            return ori > max ? max : ori;
        }
    }


    public static int floor(float val) {
        int var = (int)val;
        return val < (float)var ? var - 1 : var;
    }

    public static int floor(double var0) {
        int var2 = (int)var0;
        return var0 < (double)var2 ? var2 - 1 : var2;
    }

    public static long lfloor(double var0) {
        long var2 = (long)var0;
        return var0 < (double)var2 ? var2 - 1L : var2;
    }


    public static float abs(float value) {
        return Math.abs(value);
    }

    public static int abs(int value) {
        return Math.abs(value);
    }


    public static int ceil(final float value) {
        int v = (int) value;
        return (value > v) ? (v + 1) : v;
    }

    public static int ceil(final double value) {
        int v = (int) value;
        return (value > v) ? (v + 1) : v;
    }


    public static int smallestEncompassingPowerOfTwo(int value) {
        int v = value - 1;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        return v + 1;
    }

    public static boolean isPowerOfTwo(int v) {
        return v != 0 && (v & v - 1) == 0x0;
    }

    public static int ceillog2(int v) {
        v = (isPowerOfTwo(v) ? v : smallestEncompassingPowerOfTwo(v));
        return Mth.MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)(v * 125613361L >> 27) & 0x1F];
    }

    public static int log2(int v) {
        return ceillog2(v) - (isPowerOfTwo(v) ? 0 : 1);
    }


    public static double dist(double dx, double dz) {
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static double dist(double dx, double dy, double dz) {
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static float wrapDegrees(float value) {
        float v = value % 360.0f;
        if (v >= 180.0f) {
            v -= 360.0f;
        }
        if (v < -180.0f) {
            v += 360.0f;
        }
        return v;
    }

    public static double wrapDegrees(double value) {
        double v = value % 360.0;
        if (v >= 180.0) {
            v -= 360.0;
        }
        if (v < -180.0) {
            v += 360.0;
        }
        return v;
    }

    public static float byteToAngle(byte b) {
        return b * 360f / 256f;
    }

    public static byte angleToByte(float a) {
        return (byte) (wrapDegrees(a) * 256f / 360f);
    }

    public static float lerp(float var0, float var1, float var2) {
        return var1 + var0 * (var2 - var1);
    }

    public static double lerp(double var0, double var1, double var2) {
        return var1 + var0 * (var2 - var1);
    }

    public static double fastInvSqrt(double val) {
        double var2 = 0.5D * val;
        long bits = Double.doubleToRawLongBits(val);
        bits = 6910469410427058090L - (bits >> 1);
        val = Double.longBitsToDouble(bits);
        val *= 1.5D - var2 * val * val;
        return val;
    }

    public static double atan2(double var0, double var2) {
        double var4 = var2 * var2 + var0 * var0;
        if (Double.isNaN(var4)) {
            return Double.NaN;
        } else {
            boolean var6 = var0 < 0.0D;
            if (var6) {
                var0 = -var0;
            }

            boolean var7 = var2 < 0.0D;
            if (var7) {
                var2 = -var2;
            }

            boolean var8 = var0 > var2;
            double var9;
            if (var8) {
                var9 = var2;
                var2 = var0;
                var0 = var9;
            }

            var9 = fastInvSqrt(var4);
            var2 *= var9;
            var0 *= var9;
            double var11 = FRAC_BIAS + var0;
            int var13 = (int)Double.doubleToRawLongBits(var11);
            double var14 = ASIN_TAB[var13];
            double var16 = COS_TAB[var13];
            double var18 = var11 - FRAC_BIAS;
            double var20 = var0 * var16 - var2 * var18;
            double var22 = (6.0D + var20 * var20) * var20 * 0.16666666666666666D;
            double result = var14 + var22;
            if (var8) {
                result = PI / 2D - result;
            }

            if (var7) {
                result = PI - result;
            }

            if (var6) {
                result = -result;
            }

            return result;
        }
    }

    public static int nextInt(int low, int high) {
        if (low > high) throw new ArithmeticException("Lower is greater than Higher");
        return RANDOM.nextInt(high - low) + low;
    }

    public static float nextFloat(float low, float high) {
        if (low > high) throw new ArithmeticException("Lower is greater than Higher");
        return (high - low) * RANDOM.nextFloat() + low;
    }

    public static double nextDouble(double low, double high) {
        if (low > high) throw new ArithmeticException("Lower is greater than Higher");
        return (high - low) * RANDOM.nextDouble() + low;
    }

    /** [lowest, highest] */
    public static boolean within(int ori, int lowest, int highest) {
        return ori >= lowest && ori <= highest;
    }

    public static boolean within(long ori, long lowest, long highest) {
        return ori >= lowest && ori <= highest;
    }

    public static boolean within(float ori, float lowest, float highest) {
        return ori >= lowest && ori <= highest;
    }

    public static boolean within(double ori, double lowest, double highest) {
        return ori >= lowest && ori <= highest;
    }

}
