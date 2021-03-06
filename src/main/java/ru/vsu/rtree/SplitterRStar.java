package ru.vsu.rtree;

import ru.vsu.rtree.geometry.HasGeometry;
import ru.vsu.rtree.geometry.ListPair;
import ru.vsu.rtree.geometry.utils.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SplitterRStar implements Splitter {
    private enum SortType {
        X_LOWER, X_UPPER, Y_LOWER, Y_UPPER
    }

    private final Comparator<ListPair<?>> comparator;

    SplitterRStar() {
        this.comparator = new Comparator<ListPair<?>>() {

            @Override
            public int compare(ListPair<?> p1, ListPair<?> p2) {
                // check overlap first then areaSum
                int value = Double.compare(overlap(p1), overlap(p2));
                if (value == 0) {
                    return Double.compare(p1.areaSum(), p2.areaSum());
                } else {
                    return value;
                }
            }
        };
    }

    private static Comparator<HasGeometry> comparator(SortType sortType) {
        switch (sortType) {
            case X_LOWER:
                return INCREASING_X_LOWER;
            case X_UPPER:
                return INCREASING_X_UPPER;
            case Y_LOWER:
                return INCREASING_Y_LOWER;
            case Y_UPPER:
                return INCREASING_Y_UPPER;
            default:
                throw new IllegalArgumentException("invalid SortType " + sortType);
        }
    }

    private static final Comparator<HasGeometry> INCREASING_X_LOWER = new Comparator<HasGeometry>() {

        @Override
        public int compare(HasGeometry n1, HasGeometry n2) {
            return Double.compare(n1.geometry().mbr().x1(), n2.geometry().mbr().x1());
        }
    };

    private static final Comparator<HasGeometry> INCREASING_X_UPPER = new Comparator<HasGeometry>() {

        @Override
        public int compare(HasGeometry n1, HasGeometry n2) {
            return Double.compare(n1.geometry().mbr().x2(), n2.geometry().mbr().x2());
        }
    };

    private static final Comparator<HasGeometry> INCREASING_Y_LOWER = new Comparator<HasGeometry>() {

        @Override
        public int compare(HasGeometry n1, HasGeometry n2) {
            return Double.compare(n1.geometry().mbr().y1(), n2.geometry().mbr().y1());
        }
    };

    private static final Comparator<HasGeometry> INCREASING_Y_UPPER = new Comparator<HasGeometry>() {

        @Override
        public int compare(HasGeometry n1, HasGeometry n2) {
            return Double.compare(n1.geometry().mbr().y2(), n2.geometry().mbr().y2());
        }
    };

    private static double overlap(ListPair<? extends HasGeometry> pair) {
        return pair.group1().geometry().mbr().intersectionArea(pair.group2().geometry().mbr());
    }

    @Override
    public <T extends HasGeometry> ListPair<T> split(List<T> items, int minSize) {
        Preconditions.checkArgument(!items.isEmpty());

        // sort nodes into increasing x, calculate min overlap where both groups
        // have more than minChildren

        // compute S the sum of all margin-values of the lists above
        // the list with the least S is then used to find minimum overlap

        List<ListPair<T>> pairs = null;
        float lowerMarginSum = Float.MAX_VALUE;
        List<T> list = null;
        for (SortType sortType : SortType.values()) {
            if (list == null) {
                list = new ArrayList<>(items);
            }
            Collections.sort(list, comparator(sortType));
            List<ListPair<T>> p = getPairs(minSize, list);
            float marginSum = marginValueSum(p);
            if (marginSum < lowerMarginSum) {
                lowerMarginSum = marginSum;
                pairs = p;
                // because p uses subViews of list we need to create a new one
                // for further comparisons
                list = null;
            }
        }
        return Collections.min(pairs, comparator);
    }

    static <T extends HasGeometry> List<ListPair<T>> getPairs(int minSize, List<T> list) {
        List<ListPair<T>> pairs = new ArrayList<>(list.size() - 2 * minSize + 1);
        for (int i = minSize; i < list.size() - minSize + 1; i++) {
            List<T> list1 = list.subList(0, i);
            List<T> list2 = list.subList(i, list.size());
            ListPair<T> pair = new ListPair<>(list1, list2);
            pairs.add(pair);
        }
        return pairs;
    }

    private static <T extends HasGeometry> float marginValueSum(List<ListPair<T>> list) {
        float sum = 0;
        for (ListPair<T> p : list)
            sum += p.marginSum();
        return sum;
    }
}
