package ru.vsu.rtree.geometry;

import ru.vsu.rtree.internal.Util;

import java.util.List;

public class Group<T extends HasGeometry> implements HasGeometry {
    private final List<T> list;
    private final Rectangle mbr;

    Group(List<T> list) {
        this.list = list;
        this.mbr = Util.mbr(list);
    }

    public List<T> list() {
        return list;
    }

    @Override
    public Geometry geometry() {
        return mbr;
    }
}