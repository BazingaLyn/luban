package org.luban.utils;

import com.google.common.collect.Maps;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;

/**
 * @author liguolin
 * 2019-03-27 12:58:15
 */
public class ReflectUtils {

    private static final Objenesis objenesis = new ObjenesisStd(true);

    /**
     * Maps primitive {@link Class}es to their corresponding wrapper {@link Class}.
     */
    private static final Map<Class<?>, Class<?>> primitiveWrapperMap = Maps.newConcurrentMap();

    static {
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
    }

    /**
     * Maps wrapper {@link Class}es to their corresponding primitive types.
     */
    private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = Maps.newIdentityHashMap();

    static {
        for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperMap.entrySet()) {
            final Class<?> wrapperClass = entry.getValue();
            final Class<?> primitiveClass = entry.getKey();
            if (!primitiveClass.equals(wrapperClass)) {
                wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
            }
        }
    }

    /**
     * Array of primitive number types ordered by "promotability".
     */
    private static final Class<?>[] ORDERED_PRIMITIVE_TYPES = {
            Byte.TYPE,
            Short.TYPE,
            Character.TYPE,
            Integer.TYPE,
            Long.TYPE,
            Float.TYPE,
            Double.TYPE
    };

    public static <T> T newInstance(Class<T> clazz) {
        return newInstance(clazz, true);
    }

    /**
     * Creates a new object.
     *
     * @param clazz             the class to instantiate
     * @param constructorCalled whether or not any constructor being called
     * @return new instance of clazz
     */
    public static <T> T newInstance(Class<T> clazz, boolean constructorCalled) {
        if (constructorCalled) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
            }
        } else {
            // without any constructor being called
            return objenesis.newInstance(clazz);
        }
        return null; // should never get here
    }
}
