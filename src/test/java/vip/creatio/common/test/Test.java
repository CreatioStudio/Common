package vip.creatio.common.test;

import vip.creatio.common.util.StringUtil;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        double sum = 0d;
        int i = 1;
        while (sum < 2d) {
            sum += Math.log10((i + 1) / (double)i);
            i++;
        }
        System.out.println(i);
        System.out.println(sum);
    }

}
