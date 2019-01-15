package ru.vsu.rtree.internal.util;

import ru.vsu.rtree.geometry.utils.Optional;
import ru.vsu.rtree.geometry.utils.annotations.VisibleForTesting;

public final class ObjectsHelper {

    private ObjectsHelper() {
        // prevent instantiation
    }

    @VisibleForTesting
    static void instantiateForTestCoveragePurposesOnly() {
        new ObjectsHelper();
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> asClass(Object object, Class<T> cls) {
        if (object == null)
            return Optional.absent();
        else if (object.getClass() != cls)
            return Optional.absent();
        else
            return Optional.of((T) object);
    }

}