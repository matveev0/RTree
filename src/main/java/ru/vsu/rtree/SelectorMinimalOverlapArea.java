package ru.vsu.rtree;

import ru.vsu.rtree.geometry.Geometry;
import ru.vsu.rtree.internal.util.Comparators;

import java.util.Collections;
import java.util.List;

public class SelectorMinimalOverlapArea implements Selector {
    @Override
    public <T, S extends Geometry> Node<T, S> select(Geometry g, List<? extends Node<T, S>> nodes) {
        return Collections.min(nodes,
                Comparators.overlapAreaThenAreaIncreaseThenAreaComparator(g.mbr(), nodes));    }
}
