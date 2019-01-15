package ru.vsu.rtree.geometry;

import ru.vsu.rtree.geometry.impl.CircleDouble;
import ru.vsu.rtree.geometry.impl.LineDouble;
import ru.vsu.rtree.geometry.impl.PointDouble;
import ru.vsu.rtree.geometry.impl.RectangleDouble;
import ru.vsu.rtree.geometry.utils.annotations.VisibleForTesting;

public final class Geometries {
    public static Point point(double x, double y) {
        return PointDouble.create(x, y);
    }

    public static Point pointGeographic(double lon, double lat) {
        return point(normalizeLongitudeDouble(lon), lat);
    }

    public static Rectangle rectangle(double x1, double y1, double x2, double y2) {
        return RectangleDouble.create(x1, y1, x2, y2);
    }

    public static Rectangle rectangleGeographic(double lon1, double lat1, double lon2, double lat2) {
        return rectangleGeographic((float) lon1, (float) lat1, (float) lon2, (float) lat2);
    }

    public static Rectangle rectangleGeographic(float lon1, float lat1, float lon2, float lat2) {
        float x1 = normalizeLongitude(lon1);
        float x2 = normalizeLongitude(lon2);
        if (x2 < x1) {
            x2 += 360;
        }
        return rectangle(x1, lat1, x2, lat2);
    }

    public static Circle circle(double x, double y, double radius) {
        return CircleDouble.create(x, y, radius);
    }

    public static Line line(double x1, double y1, double x2, double y2) {
        return LineDouble.create(x1, y1, x2, y2);
    }

    @VisibleForTesting
    static double normalizeLongitude(double d) {
        return normalizeLongitude((float) d);
    }

    private static double normalizeLongitudeDouble(double d) {
        if (d == -180.0f)
            return -180.0d;
        else {
            double sign = Math.signum(d);
            double x = Math.abs(d) / 360;
            double x2 = (x - (float) Math.floor(x)) * 360;
            if (x2 >= 180)
                x2 -= 360;
            return x2 * sign;
        }
    }

    private static float normalizeLongitude(float d) {
        if (d == -180.0f)
            return -180.0f;
        else {
            float sign = Math.signum(d);
            float x = Math.abs(d) / 360;
            float x2 = (x - (float) Math.floor(x)) * 360;
            if (x2 >= 180)
                x2 -= 360;
            return x2 * sign;
        }
    }
}
