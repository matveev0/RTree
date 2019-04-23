package ru.vsu.rtree.internal.util;

import ru.vsu.rtree.Entry;
import ru.vsu.rtree.geometry.Geometry;
import ru.vsu.rtree.geometry.HasGeometry;
import ru.vsu.rtree.geometry.Rectangle;

import java.util.Comparator;
import java.util.List;

public class Comparators {

    private Comparators() {
        // prevent instantiation
    }

    public static <T extends HasGeometry> Comparator<HasGeometry> overlapAreaThenAreaIncreaseThenAreaComparator(
            final Rectangle r, final List<T> list) {
        return new Comparator<HasGeometry>() {

            @Override
            public int compare(HasGeometry g1, HasGeometry g2) {
                int value = Double.compare(overlapArea(r, list, g1), overlapArea(r, list, g2));
                if (value == 0) {
                    value = Double.compare(areaIncrease(r, g1), areaIncrease(r, g2));
                    if (value == 0) {
                        value = Double.compare(area(r, g1), area(r, g2));
                    }
                }
                return value;
            }
        };
    }

    public static <T extends HasGeometry> Comparator<HasGeometry> areaIncreaseThenAreaComparator(
            final Rectangle r) {
        return new Comparator<HasGeometry>() {
            @Override
            public int compare(HasGeometry g1, HasGeometry g2) {
                int value = Double.compare(areaIncrease(r, g1), areaIncrease(r, g2));
                if (value == 0) {
                    value = Double.compare(area(r, g1), area(r, g2));
                }
                return value;
            }
        };
    }

    /**
     * <p>
     * Returns a comparator that can be used to sort entries returned by search
     * methods. For example:
     * </p>
     * <p>
     * <code>search(100).toSortedList(ascendingDistance(r))</code>
     * </p>
     *
     * @param <T> the value type
     * @param <S> the entry type
     * @param r   rectangle to measure distance to
     * @return a comparator to sort by ascending distance from the rectangle
     */
    public static <T, S extends Geometry> Comparator<Entry<T, S>> ascendingDistance(final Rectangle r) {
        return new Comparator<Entry<T, S>>() {
            @Override
            public int compare(Entry<T, S> e1, Entry<T, S> e2) {
                return Double.compare(e1.geometry().distance(r), e2.geometry().distance(r));
            }
        };
    }

    private static double area(final Rectangle r, HasGeometry g1) {
        return g1.geometry().mbr().add(r).area();
    }

    private static float overlapArea(Rectangle r,
                                     List<? extends HasGeometry> list,
                                     HasGeometry g) {
        Rectangle gPlusR = g.geometry().mbr().add(r);
        float m = 0;
        for (HasGeometry other : list) {
            if (other != g) {
                m += gPlusR.intersectionArea(other.geometry().mbr());
            }
        }
        return m;
    }

    private static double areaIncrease(Rectangle r, HasGeometry g) {
        Rectangle gPlusR = g.geometry().mbr().add(r);
        return gPlusR.area() - g.geometry().mbr().area();
    }
}
