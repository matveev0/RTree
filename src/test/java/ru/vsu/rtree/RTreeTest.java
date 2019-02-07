package ru.vsu.rtree;

import org.junit.Test;
import ru.vsu.rtree.geometry.Geometry;

import static org.junit.Assert.assertTrue;

public class RTreeTest {
    @Test
    public void testInstantiation(){
        RTree<Object, Geometry> tree = RTree.create();
        assertTrue(tree.entries().isEmpty().toBlocking().single());
    }
}
