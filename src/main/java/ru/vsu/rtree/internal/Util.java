package ru.vsu.rtree.internal;

import ru.vsu.rtree.geometry.Geometries;
import ru.vsu.rtree.geometry.HasGeometry;
import ru.vsu.rtree.geometry.Rectangle;
import ru.vsu.rtree.geometry.utils.Preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Util {
    private Util() {
        // prevent instantiation
    }

    /**
     * Returns the minimum bounding rectangle of a number of items.
     *
     * @param items items to bound
     * @return the minimum bounding rectangle items
     */
    public static Rectangle mbr(Collection<? extends HasGeometry> items) {
        Preconditions.checkArgument(!items.isEmpty());
        double minX1 = Double.MAX_VALUE;
        double minY1 = Double.MAX_VALUE;
        double maxX2 = -Double.MAX_VALUE;
        double maxY2 = -Double.MAX_VALUE;
        boolean isDoublePrecision = false;

        for (final HasGeometry item : items) {
            Rectangle r = item.geometry().mbr();
            if (r.isDoublePrecision()) {
                isDoublePrecision = true;
            }
            if (r.x1() < minX1)
                minX1 = r.x1();
            if (r.y1() < minY1)
                minY1 = r.y1();
            if (r.x2() > maxX2)
                maxX2 = r.x2();
            if (r.y2() > maxY2)
                maxY2 = r.y2();
        }
        if (isDoublePrecision) {
            return Geometries.rectangle(minX1, minY1, maxX2, maxY2);
        } else {
            return Geometries.rectangle((float) minX1, (float) minY1, (float) maxX2, (float) maxY2);
        }
    }

    public static <T> List<T> add(List<T> list, T element) {
        final ArrayList<T> result = new ArrayList<>(list);
        result.add(element);
        return result;
    }

    public static <T> List<T> remove(List<? extends T> list, List<? extends T> elements) {
        final ArrayList<T> result = new ArrayList<>(list);
        result.removeAll(elements);
        return result;
    }

    public static <T> List<? extends T> replace(List<? extends T> list,
                                                T element,
                                                List<T> replacements) {
        List<T> list2 = new ArrayList<>(list.size() + replacements.size());
        for (T node : list) {
            if (node != element) {
                list2.add(node);
            }
        }
        list2.addAll(replacements);
        return list2;
    }
}
