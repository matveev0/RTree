package ru.vsu.rtree.geometry.utils;

public final class Optional<T> {

    private final T value;
    private final boolean present;

    private Optional(T value, boolean present) {
        this.value = value;
        this.present = present;
    }

    private Optional() {
        this(null, false);
    }

    public boolean isPresent() {
        return present;
    }

    public T get() {
        if (present)
            return value;
        else
            throw new RuntimeException("Not Present!");
    }

    public static <T> Optional<T> of(T t) {
        return new Optional<>(t, true);
    }

    public static <T> Optional<T> absent() {
        return new Optional<>();
    }

    @Override
    public String toString() {
        return present ? String.format("Optional.of(%s)", value) : "Optional.absent";
    }
}
