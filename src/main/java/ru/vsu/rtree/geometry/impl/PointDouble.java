package ru.vsu.rtree.geometry.impl;

import ru.vsu.rtree.geometry.Geometry;
import ru.vsu.rtree.geometry.Point;
import ru.vsu.rtree.geometry.Rectangle;
import ru.vsu.rtree.internal.util.GeometryUtil;
import ru.vsu.rtree.geometry.utils.Objects;

public final class PointDouble implements Point {
    private final double x;
    private final double y;

    private PointDouble(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static PointDouble create(double x, double y) {
        return new PointDouble(x, y);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double x1() {
        return x;
    }

    @Override
    public double y1() {
        return y;
    }

    @Override
    public double x2() {
        return x;
    }

    @Override
    public double y2() {
        return y;
    }

    @Override
    public boolean contains(double x, double y) {
        return this.x == x && this.y == y;
    }

    @Override
    public double perimeter() {
        return 0;
    }

    @Override
    public double area() {
        return 0;
    }

    @Override
    public double distance(Rectangle r) {
        return GeometryUtil.distance(x, y, r);
    }

    @Override
    public Rectangle mbr() {
        return this;
    }

    @Override
    public boolean intersects(Rectangle r) {
        return r.x1() <= x && x <= r.x2() && r.y1() <= y && y <= r.y2();
    }

    @Override
    public Geometry geometry() {
        return this;
    }

    // TODO: 23.12.2018
    @Override
    public Rectangle add(Rectangle r) {
        throw new RuntimeException();
       /* return Geometries.rectangle(Math.min(x, r.x1()), Math.min(y, r.y1()), Math.max(x, r.x2()),
                Math.max(y, r.y2()));
    */

    }

    @Override
    public double intersectionArea(Rectangle r) {
        return 0;
    }

    @Override
    public String toString() {
        return "Point [x=" + x() + ", y=" + y() + "]";
    }

    @Override
    public boolean isDoublePrecision() {
        return true;
    }

    @Override
    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        long temp;
//        temp = Double.doubleToLongBits(x);
//        result = prime * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(y);
//        result = prime * result + (int) (temp ^ (temp >>> 32));
//        return result;
        return Objects.hashCode(x,y);

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PointDouble other = (PointDouble) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
            return false;
        return true;
    }
}
