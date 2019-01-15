package ru.vsu.rtree.geometry;

import org.junit.Test;
import ru.vsu.rtree.geometry.impl.RectangleDouble;

import static org.junit.Assert.assertEquals;

public class GeometriesTest {
    @Test
    public void testNormalizeLongitude() {
       // ReflectionTestUtils.invokeMethod(myClass, "getWorkArray", values, length);
       assertEquals(0, Geometries.normalizeLongitude(0), 0.01);

        RectangleDouble rectangleDouble = RectangleDouble.create(1,2,3,1009680689);
    }
}
