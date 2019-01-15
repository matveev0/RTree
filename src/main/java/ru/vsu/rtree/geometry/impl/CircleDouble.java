package ru.vsu.rtree.geometry.impl;

import ru.vsu.rtree.geometry.Circle;
import ru.vsu.rtree.geometry.Line;
import ru.vsu.rtree.geometry.Point;
import ru.vsu.rtree.geometry.Rectangle;
import ru.vsu.rtree.geometry.utils.Objects;
import ru.vsu.rtree.geometry.utils.Optional;
import ru.vsu.rtree.internal.util.GeometryUtil;
import ru.vsu.rtree.internal.util.ObjectsHelper;

public final class CircleDouble implements Circle {
    private final double x, y, radius;
    private final Rectangle mbr;

    private CircleDouble(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mbr = RectangleDouble.create(x - radius, y - radius, x + radius, y + radius);
    }

    public static CircleDouble create(double x, double y, double radius) {
        return new CircleDouble(x, y, radius);
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
    public double radius() {
        return radius;
    }

    @Override
    public boolean intersects(Circle c) {
        double total = radius + c.radius();
        return GeometryUtil.distanceSquared(x, y, c.x(), c.y()) <= total * total;
    }

    @Override
    public boolean intersects(Point point) {
        return Math.sqrt(sqr(x - point.x()) + sqr(y - point.y())) <= radius;
    }

    private double sqr(double x) {
        return x * x;
    }
    @Override
    public boolean intersects(Line line) {
        return line.intersects(this);
    }

    @Override
    public boolean intersects(Rectangle r) {
        return distance(r) == 0;
    }

    @Override
    public double distance(Rectangle r) {
        return Math.max(0, GeometryUtil.distance(x, y, r) - radius);
    }

    @Override
    public Rectangle mbr() {
        return mbr;
    }

    @Override
    public boolean isDoublePrecision() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x, y, radius);
    }

    @Override
    public boolean equals(Object obj) {
        Optional<CircleDouble> other = ObjectsHelper.asClass(obj, CircleDouble.class);
        if (other.isPresent()) {
            return Objects.equal(x, other.get().x) && Objects.equal(y, other.get().y)
                    && Objects.equal(radius, other.get().radius);
        } else
            return false;
    }
}
