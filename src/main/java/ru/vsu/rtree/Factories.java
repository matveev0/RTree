package ru.vsu.rtree;

import ru.vsu.rtree.geometry.Geometry;
import ru.vsu.rtree.internal.FactoryDefault;

public final class Factories {

    private Factories() {
        // prevent instantiation
    }

    public static <T, S extends Geometry> Factory<T, S> defaultFactory() {
        return FactoryDefault.instance();
    }
}