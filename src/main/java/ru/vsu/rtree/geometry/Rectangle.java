package ru.vsu.rtree.geometry;

public interface Rectangle extends Geometry, HasGeometry {
    double x1();

    double y1();

    double x2();

    double y2();

    boolean contains(double x, double y);

    double perimeter();

    double area();

    double intersectionArea(Rectangle r);

    Rectangle add(Rectangle r);
}