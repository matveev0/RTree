package ru.vsu.rtree.geometry.impl;

import ru.vsu.rtree.geometry.Geometry;
import ru.vsu.rtree.geometry.Rectangle;
import ru.vsu.rtree.geometry.utils.Objects;
import ru.vsu.rtree.geometry.utils.Optional;
import ru.vsu.rtree.geometry.utils.Preconditions;
import ru.vsu.rtree.internal.util.GeometryUtil;
import ru.vsu.rtree.internal.util.ObjectsHelper;

public final class RectangleFloat implements Rectangle {
    public final float x1, y1, x2, y2;

    private RectangleFloat(float x1, float y1, float x2, float y2) {
        Preconditions.checkArgument(x2 >= x1);
        Preconditions.checkArgument(y2 >= y1);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public static Rectangle create(float x1, float y1, float x2, float y2) {
        return new RectangleFloat(x1, y1, x2, y2);
    }

    @Override
    public double x1() {
        return x1;
    }

    @Override
    public double y1() {
        return y1;
    }

    @Override
    public double x2() {
        return x2;
    }

    @Override
    public double y2() {
        return y2;
    }

    @Override
    public double area() {
        return (x2 - x1) * (y2 - y1);
    }

    @Override
    public Rectangle add(Rectangle r) {
        if (r.isDoublePrecision())
            return RectangleDouble.create(
                    GeometryUtil.min(x1, r.x1()), GeometryUtil.min(y1, r.y1()),
                    GeometryUtil.max(x2, r.x2()), GeometryUtil.max(y2, r.y2()));
        else if (r instanceof RectangleFloat) {
            RectangleFloat rf = (RectangleFloat) r;
            return RectangleFloat.create(
                    GeometryUtil.min(x1, rf.x1), GeometryUtil.min(y1, rf.y1),
                    GeometryUtil.max(x2, rf.x2), GeometryUtil.max(y2, rf.y2));
        } else {
            PointFloat rf = (PointFloat) r;
            return RectangleFloat.create(
                    GeometryUtil.min(x1, rf.xFloat()), GeometryUtil.min(y1, rf.yFloat()),
                    GeometryUtil.max(x2, rf.xFloat()), GeometryUtil.max(y2, rf.yFloat()));
        }
    }

    @Override
    public boolean contains(double x, double y) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    @Override
    public boolean intersects(Rectangle r) {
        return GeometryUtil.intersects(x1, y1, x2, y2, r.x1(), r.y1(), r.x2(), r.y2());
    }

    @Override
    public double distance(Rectangle r) {
        return GeometryUtil.distance(x1, y1, x2, y2, r.x1(), r.y1(), r.x2(), r.y2());
    }

    @Override
    public Rectangle mbr() {
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x1, y1, x2, y2);
    }

    @Override
    public boolean equals(Object obj) {
        Optional<RectangleFloat> other = ObjectsHelper.asClass(obj, RectangleFloat.class);
        if (other.isPresent()) {
            return Objects.equal(x1, other.get().x1)
                    && Objects.equal(x2, other.get().x2)
                    && Objects.equal(y1, other.get().y1)
                    && Objects.equal(y2, other.get().y2);
        } else
            return false;
    }

    @Override
    public double intersectionArea(Rectangle r) {
        if (!intersects(r))
            return 0;
        else
            return RectangleDouble
                    .create(GeometryUtil.max(x1, r.x1()), GeometryUtil.max(y1, r.y1()),
                            GeometryUtil.min(x2, r.x2()), GeometryUtil.min(y2, r.y2()))
                    .area();
    }

    @Override
    public double perimeter() {
        return 2 * (x2 - x1) + 2 * (y2 - y1);
    }

    @Override
    public Geometry geometry() {
        return this;
    }

    @Override
    public boolean isDoublePrecision() {
        return false;
    }

    @Override
    public String toString() {
        return "Rectangle [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + "]";
    }
}