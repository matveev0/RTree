package ru.vsu.rtree;

import ru.vsu.rtree.geometry.Geometry;

public interface Factory<T, S extends Geometry>
        extends LeafFactory<T, S>, NonLeafFactory<T, S>, EntryFactory<T,S> {
}
