package vip.creatio.common.util;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class ReflectUtil {

    public static final String CLASSNAME_ILLEGAL_CHARS = " '[]{}()-+=;,`~!@#%^&*";

    public static Class<?> toWrapper(Class<?> primitive) {
        if (primitive.isPrimitive()) {
            switch (primitive.getName()) {
                case "int":
                    return Integer.class;
                case "byte":
                    return Byte.class;
                case "char":
                    return Character.class;
                case "long":
                    return Long.class;
                case "short":
                    return Short.class;
                case "float":
                    return Float.class;
                case "double":
                    return Double.class;
                case "boolean":
                    return Boolean.class;
            }
        }
        return primitive;
    }

    public static Class<?> toPrimitive(Class<?> wrapper) {
        if (!wrapper.isPrimitive()) {
            switch (wrapper.getName()) {
                case "java.lang.Integer":
                    return int.class;
                case "java.lang.Byte":
                    return byte.class;
                case "java.lang.Character":
                    return char.class;
                case "java.lang.Long":
                    return long.class;
                case "java.lang.Short":
                    return short.class;
                case "java.lang.Float":
                    return float.class;
                case "java.lang.Double":
                    return double.class;
                case "java.lang.Boolean":
                    return boolean.class;
            }
        }
        return wrapper;
    }

    public static Class<?> forName(String name) throws ReflectionException {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new ReflectionException(e);
        }
    }

    public static Class<?> forName(String name, boolean initialize, ClassLoader loader) throws ReflectionException {
        try {
            return Class.forName(name, initialize, loader);
        } catch (ClassNotFoundException e) {
            throw new ReflectionException(e);
        }
    }

    public static Method method(Class<?> cls, String name, Class<?>... paramsType) throws ReflectionException  {
        try {
            Method mth = cls.getDeclaredMethod(name, paramsType);
            mth.setAccessible(true);
            return mth;
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e);
        }
    }

    public static Field field(Class<?> cls, String name) throws ReflectionException {
        try {
            Field f = cls.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            throw new ReflectionException(e);
        }
    }

    /** Get field through index, a name-independent way */
    public static Field field(Class<?> cls, int index) throws ReflectionException {
        Field[] fields = cls.getDeclaredFields();
        if (fields.length <= index) throw new ReflectionException(new NoSuchFieldException("Index too large!"));

        for (int i = 0, c = 0; i < fields.length; i++) {
            Field f = fields[i];
            if (!Modifier.isStatic(f.getModifiers())) {
                if (c == index) {
                    f.setAccessible(true);
                    return f;
                }
                c++;
            }
        }
        throw new ReflectionException(new NoSuchFieldException("Index too large!"));
    }

    public static Field staticField(Class<?> cls, int index) throws ReflectionException {
        Field[] fields = cls.getDeclaredFields();
        if (fields.length <= index) throw new ReflectionException(new NoSuchFieldException("Index too large!"));

        for (int i = 0, c = 0; i < fields.length; i++) {
            Field f = fields[i];
            if (Modifier.isStatic(f.getModifiers())) {
                if (c == index) {
                    f.setAccessible(true);
                    return f;
                }
                c++;
            }
        }
        throw new ReflectionException(new NoSuchFieldException("Index too large!"));
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<?> cls, String name, Object member) throws ReflectionException, ClassCastException {
        try {
            return (T) field(cls, name).get(member);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> T get(Class<?> cls, String name) throws ReflectionException, ClassCastException {
        return get(cls, name, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Field f, Object member) throws ReflectionException, ClassCastException {
        try {
            f.setAccessible(true);
            return (T) f.get(member);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> T get(Field f) throws ReflectionException, ClassCastException {
        return get(f, null);
    }


    public static void set(Class<?> cls, String name, Object member, Object value) throws ReflectionException, ClassCastException {
        try {
            field(cls, name).set(member, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static void set(Class<?> cls, String name, Object value) throws ReflectionException, ClassCastException {
        set(cls, name, null ,value);
    }

    public static void set(Field f, Object member, Object value) throws ReflectionException, ClassCastException {
        try {
            f.setAccessible(true);
            f.set(member, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static void set(Field f, Object value) throws ReflectionException, ClassCastException {
        set(f, null, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T invoke(Class<?> cls, String name, Object member, Object... args) throws ReflectionException, ClassCastException {
        try {
            // Find method
            OUTER:
            for (Method m : cls.getDeclaredMethods()) {
                if (m.getParameterCount() == args.length) {
                    if (m.getName().equals(name)) {
                        Class<?>[] paramsType = m.getParameterTypes();

                        // Check parameter type
                        for (int i = 0; i < paramsType.length; i++) {
                            if (!args[i].getClass().isAssignableFrom(toWrapper(paramsType[i])))
                                continue OUTER;
                        }

                        m.setAccessible(true);
                        return (T) m.invoke(member, args);
                    }
                }
            }
            throw new ReflectionException("No method found in class " + cls.getName() + " with name " + name + " and parameter type "
                    + Arrays.stream(args).map(Object::getClass).collect(Collectors.toList()));
        } catch (InvocationTargetException e) {
            throw new ReflectionException(e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> T invoke(Object member, String name, Object... args) throws ReflectionException, ClassCastException {
        return invoke(member.getClass(), name, member, args);
    }

    @SuppressWarnings("unchecked")
    public static <T> T invoke(Method mth, Object member, Object... args) throws ReflectionException, ClassCastException {
        try {
            mth.setAccessible(true);
            return (T) mth.invoke(member, args);
        } catch (InvocationTargetException e) {
            throw new ReflectionException(e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> Constructor<T> constructor(Class<T> cls, Class<?>... args) throws ReflectionException {
        try {
            Constructor<T> c = cls.getDeclaredConstructor(args);
            c.setAccessible(true);
            return c;
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> cls, Object... args) throws ReflectionException {
        try {
            // Find constructor
            OUTER:
            for (Constructor<?> c : cls.getDeclaredConstructors()) {
                if (c.getParameterCount() == args.length) {
                    Class<?>[] paramsType = c.getParameterTypes();

                    // Check parameter type
                    for (int i = 0; i < paramsType.length; i++) {
                        if (!args[i].getClass().isAssignableFrom(toWrapper(paramsType[i])))
                            continue OUTER;
                    }

                    c.setAccessible(true);
                    return (T) c.newInstance(args);
                }
            }
            throw new ReflectionException("No constructor found in class " + cls.getName() + " with parameter type "
                    + Arrays.stream(args).map(Object::getClass).collect(Collectors.toList()));
        } catch (InvocationTargetException e) {
            throw new ReflectionException(e.getTargetException());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> T newInstance(Constructor<T> constructor, Object... args) throws ReflectionException {
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (InvocationTargetException e) {
            throw new ReflectionException(e.getTargetException());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static MethodHandle methodHandle(Class<?> cls, String name, Class<?>... paramsType) throws ReflectionException {
        try {
            return MethodHandles.lookup().unreflect(method(cls, name, paramsType));
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static VarHandle varHandle(Class<?> cls, String name) throws ReflectionException {
        try {
            return MethodHandles.lookup().unreflectVarHandle(field(cls, name));
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static MethodHandles.Lookup lookupIn(Class<?> cls) {
        try {
            return MethodHandles.privateLookupIn(cls, MethodHandles.lookup());
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static MethodHandles.Lookup lookup() {
        return MethodHandles.lookup();
    }

    @SuppressWarnings("unchecked")
    public static <T> T createLambda(Class<T> interfaceClass, MethodHandle wrappedMethod, Class<?> caller) throws ReflectionException {
        Method[] mths = interfaceClass.getMethods();
        Method selected = null;
        for (Method m : mths) {
            if (Modifier.isStatic(m.getModifiers()) || m.isDefault() || m.isBridge() || m.isSynthetic()) continue;
            if (selected == null) selected = m;
            else throw new ReflectionException("Class " + interfaceClass.getName() + " is not a functional interface!");
        }

        if (selected == null) throw new ReflectionException("Class " + interfaceClass.getName() + " is not a functional interface!");

        MethodHandles.Lookup lookup = lookupIn(caller);

        try {
            return (T) LambdaMetafactory.metafactory(
                    lookup,
                    selected.getName(),
                    MethodType.methodType(interfaceClass),
                    MethodType.methodType(selected.getReturnType(), selected.getParameterTypes()),
                    wrappedMethod,
                    wrappedMethod.type()).getTarget().invoke();
        } catch (Throwable throwable) {
            throw new ReflectionException(throwable);
        }
    }

    public static <T> T createLambda(Class<T> interfaceClass, Method mth) throws ReflectionException {
        MethodHandles.Lookup lookup = lookupIn(mth.getDeclaringClass());
        try {
            return createLambda(interfaceClass, lookup.unreflect(mth), mth.getDeclaringClass());
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static Class<?> getCallerClass() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String callerClassName = null;
        for (int i = 1; i < stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().startsWith("java.lang.Thread")) {
                if (callerClassName == null) {
                    callerClassName = ste.getClassName();
                } else if (!callerClassName.equals(ste.getClassName())) {
                    if (i + 1 == stElements.length) return null;
                    ste = stElements[i + 1];
                    if (ste.getClassName().startsWith("java") || ste.getClassName().startsWith("jdk"))
                        continue;
                    try {
                        return Class.forName(ste.getClassName());
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }


}
