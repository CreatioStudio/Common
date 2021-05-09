package vip.creatio.common.test;

public class FastMth {

    public static void main(String[] args) {
        long t0 = System.currentTimeMillis();

        double r = 0D;
        for (int i = 0; i < 1_000_00000; i++) {
            r = sqrt(5D);
        }

        System.out.println("Time used: " + (System.currentTimeMillis() - t0) + "ms");
        System.out.println("Result: " + r);

        t0 = System.currentTimeMillis();

        r = 0D;
        for (int i = 0; i < 1_000_00000; i++) {
            r = Math.sqrt(5D);
        }

        System.out.println("Time used: " + (System.currentTimeMillis() - t0) + "ms");
        System.out.println("Result: " + r);
    }

    private static double sqrt(double v) {

        final double v0 = v;
        double invexp =  1D / (((int) (Double.doubleToRawLongBits(v) >>> 52) & 0b0000_0111_1111_1111) - 1023);
        v *= invexp * invexp * invexp;
        for (int i = 0; i < 8; i++) {
            v = 0.5F * (v + v0 / v);
        }

        return v;
    }

    private static float invSqrt(float v) {
        int i;
        final float d = v * 0.5F;

        i  = Float.floatToRawIntBits(v);
        i  = 0x5f3759df - (i >>> 1);
        v  = Float.intBitsToFloat(i);
        v  = v * (1.5F - (d * v * v));
        v  = v * (1.5F - (d * v * v));
        return v;
    }

    private static double invSqrt(double v) {
        long i;
        final double d = v * 0.5D;

        i  = Double.doubleToRawLongBits(v);
        i  = 0x5fe6eb50c7b537a9L - (i >>> 1);
        v  = Double.longBitsToDouble(i);
        v  = v * (1.5D - (d * v * v));
        v  = v * (1.5D - (d * v * v));
        return v;
    }

}
