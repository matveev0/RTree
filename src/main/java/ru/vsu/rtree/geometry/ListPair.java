package ru.vsu.rtree.geometry;

import java.util.List;

public final class ListPair<T extends HasGeometry> {
    private final Group<T> group1;
    private final Group<T> group2;
    private double areaSum = -1;
    private final double marginSum;

    public ListPair(List<T> list1, List<T> list2) {
        this.group1 = new Group<>(list1);
        this.group2 = new Group<>(list2);
        this.marginSum = group1.geometry().mbr().perimeter() + group2.geometry().mbr().perimeter();
    }

    public Group<T> group1() {
        return group1;
    }

    public Group<T> group2() {
        return group2;
    }

    public double areaSum() {
        if (areaSum == -1)
            areaSum = group1.geometry().mbr().area() + group2.geometry().mbr().area();
        return areaSum;
    }

    public double marginSum() {
        return marginSum;
    }
}
