package ru.vsu.rtree.geometry.utils;

public final class Preconditions {

    private Preconditions() {
    }

    public static <T> T checkNotNull(T t) {
        return checkNotNull(t, null);
    }

    public static <T> T checkNotNull(T t, String message) {
        if (t == null)
            throw new NullPointerException(message);
        return t;
    }

    public static void checkArgument(boolean b, String message) {
        if (!b)
            throw new IllegalArgumentException(message);
    }

    public static void checkArgument(boolean b) {
        if (!b)
            throw new IllegalArgumentException();
    }
}
