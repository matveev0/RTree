package ru.vsu.rtree;

import ru.vsu.rtree.geometry.Geometry;

import java.util.List;

public class SelectorRStar implements Selector {

    private static Selector overlapAreaSelector = new SelectorMinimalOverlapArea();
    private static Selector areaIncreaseSelector = new SelectorMinimalAreaIncrease();

    @Override
    public <T, S extends Geometry> Node<T, S> select(Geometry g, List<? extends Node<T, S>> nodes) {
        boolean leafNodes = nodes.get(0) instanceof Leaf;
        if (leafNodes)
            return overlapAreaSelector.select(g, nodes);
        else
            return areaIncreaseSelector.select(g, nodes);
    }
}
