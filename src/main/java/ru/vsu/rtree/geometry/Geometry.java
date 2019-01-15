package ru.vsu.rtree.geometry;

public interface Geometry {

    boolean intersects(Rectangle r);

    double distance(Rectangle r);

    /**
     * Returns the minimum bounding rectangle of this geometry.
     *
     * @return minimum bounding rectangle
     */
    Rectangle mbr();

    boolean isDoublePrecision();
}
