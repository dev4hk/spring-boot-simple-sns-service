package com.example.simple_sns_service.util;

import java.util.Optional;

public class ClassUtils {
    public static <T> Optional<T> getSafeCastInstance(Object obj, Class<T> aClass) {
        return aClass != null && aClass.isInstance(obj) ? Optional.of(aClass.cast(obj)) : Optional.empty();
    }
}
