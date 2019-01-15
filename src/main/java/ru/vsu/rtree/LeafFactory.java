package ru.vsu.rtree;

import ru.vsu.rtree.geometry.Geometry;

import java.util.List;

public interface LeafFactory<T, S extends Geometry> {
    Leaf<T, S> createLeaf(List<Entry<T, S>> entries, Context<T, S> context);
}
