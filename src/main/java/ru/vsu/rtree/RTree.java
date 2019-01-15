package ru.vsu.rtree;

import ru.vsu.rtree.geometry.Geometry;
import ru.vsu.rtree.geometry.utils.Optional;

public class RTree<T, S extends Geometry> {
//    private RTree(Optional<? extends Node<T, S>> root, int size, Context<T, S> context) {
//        this.root = root;
//        this.size = size;
//        this.context = context;
//    }
//
//    private RTree() {
//        this(Optional.<Node<T, S>>absent(), 0, null);
//    }
//
//    /**
//     * Constructor.
//     *
//     * @param root
//     *            the root node of the R-tree
//     * @param context
//     *            options for the R-tree
//     */
//    private RTree(Node<T, S> root, int size, Context<T, S> context) {
//        this(of(root), size, context);
//    }
//
//    static <T, S extends Geometry> RTree<T, S> create(Optional<? extends Node<T, S>> root, int size,
//                                                      Context<T, S> context) {
//        return new RTree<T, S>(root, size, context);
//    }
}
