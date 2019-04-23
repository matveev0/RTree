package ru.vsu.rtree.internal;

import ru.vsu.rtree.Entry;
import ru.vsu.rtree.geometry.Geometry;
import ru.vsu.rtree.geometry.utils.Objects;
import ru.vsu.rtree.geometry.utils.Optional;
import ru.vsu.rtree.geometry.utils.Preconditions;
import ru.vsu.rtree.internal.util.ObjectsHelper;

/**
 * An entry in the R-tree which has a spatial representation.
 *
 * @param <T> value type
 * @param <S> geometry type
 */
public final class EntryDefault<T, S extends Geometry> implements Entry<T, S> {
    private final T value;
    private final S geometry;

    /**
     * Constructor.
     *
     * @param value    the value of the entry
     * @param geometry the geometry of the value
     */
    public EntryDefault(T value, S geometry) {
        Preconditions.checkNotNull(geometry);
        this.value = value;
        this.geometry = geometry;
    }

    /**
     * Factory method.
     *
     * @param <T>      type of value
     * @param <S>      type of geometry
     * @param value    object being given a spatial context
     * @param geometry geometry associated with the value
     * @return entry wrapping value and associated geometry
     */
    public static <T, S extends Geometry> Entry<T, S> entry(T value, S geometry) {
        return new EntryDefault<>(value, geometry);
    }

    @Override
    public T value() {
        return value;
    }

    @Override
    public S geometry() {
        return geometry;
    }

    @Override
    public String toString() {
        return "Entry [value=" + value + ", geometry=" + geometry + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value, geometry);
    }

    @Override
    public boolean equals(Object obj) {
        Optional<EntryDefault> other = ObjectsHelper.asClass(obj, EntryDefault.class);
        if (other.isPresent()) {
            return Objects.equal(value, other.get().value)
                    && Objects.equal(geometry, other.get().geometry);
        } else {
            return false;
        }
    }
}

