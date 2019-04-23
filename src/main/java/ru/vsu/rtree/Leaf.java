package ru.vsu.rtree;

import ru.vsu.rtree.geometry.Geometry;

import java.util.List;

public interface Leaf<T, S extends Geometry> extends Node<T, S> {

    List<Entry<T, S>> entries();

    /**
     * Returns the ith entry (0-based).
     *
     * @param i 0-based index
     * @return ith entry
     */
    Entry<T, S> entry(int i);
}