package ru.vsu.rtree;

import ru.vsu.rtree.geometry.Geometry;
import ru.vsu.rtree.internal.util.Comparators;

import java.util.Collections;
import java.util.List;

/**
 * Uses minimal area increase to select a node from a list.
 */
public class SelectorMinimalAreaIncrease implements Selector {
    @Override
    public <T, S extends Geometry> Node<T, S> select(Geometry g, List<? extends Node<T, S>> nodes) {
        return Collections.min(nodes, Comparators.areaIncreaseThenAreaComparator(g.mbr()));
    }
}
