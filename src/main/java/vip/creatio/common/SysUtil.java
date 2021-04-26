package vip.creatio.common;

import java.util.function.Supplier;

public final class SysUtil {

    private SysUtil() {}

    public static <R> R exec(Supplier<R> sup) {
        return sup.get();
    }

}
