package vip.creatio.common;

import java.lang.reflect.InvocationTargetException;

public class ReflectionException extends RuntimeException {

    public ReflectionException(String reason) {
        super(reason);
    }

    public ReflectionException(Throwable t) {
        super(t.getMessage(), t);
    }

    public ReflectionException(InvocationTargetException t) {
        super(t.getCause().getMessage(), t.getCause());
    }

    @Override
    public String toString() {
        Throwable causes = getCause();
        if (causes == null) {
            return super.toString();
        } else {
            return getClass().getName() + " -> " + causes.toString();
        }
    }
}
