package ru.vsu.rtree;

import ru.vsu.rtree.geometry.Geometry;
import ru.vsu.rtree.geometry.HasGeometry;
import ru.vsu.rtree.geometry.Rectangle;
import ru.vsu.rtree.geometry.utils.Optional;
import ru.vsu.rtree.internal.NodeAndEntries;
import ru.vsu.rtree.util.Lists;
import rx.Observable;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static ru.vsu.rtree.geometry.utils.Optional.absent;
import static ru.vsu.rtree.geometry.utils.Optional.of;

public final class RTree<T, S extends Geometry> {
    private final Optional<? extends Node<T, S>> root;

    private final Context<T, S> context;

    /**
     * Benchmarks show that this is a good choice for up to O(10,000) entries when
     * using Quadratic splitter (Guttman).
     */
    public static final int MAX_CHILDREN_DEFAULT_GUTTMAN = 4;

    /**
     * Benchmarks show that this is the sweet spot for up to O(10,000) entries when
     * using R*-tree heuristics.
     */
    public static final int MAX_CHILDREN_DEFAULT_STAR = 4;

    private final int size;

    private RTree(Optional<? extends Node<T, S>> root, int size, Context<T, S> context) {
        this.root = root;
        this.size = size;
        this.context = context;
    }

    private RTree() {
        this(Optional.<Node<T, S>>absent(), 0, null);
    }

    /**
     * Constructor.
     *
     * @param root    the root node of the R-tree
     * @param context options for the R-tree
     */
    private RTree(Node<T, S> root, int size, Context<T, S> context) {
        this(of(root), size, context);
    }

    static <T, S extends Geometry> RTree<T, S> create(Optional<? extends Node<T, S>> root, int size,
                                                      Context<T, S> context) {
        return new RTree<>(root, size, context);
    }

    /**
     * Returns a new Builder instance for {@link RTree}. Defaults to
     * maxChildren=128, minChildren=64, splitter=QuadraticSplitter.
     *
     * @param <T> the value type of the entries in the tree
     * @param <S> the geometry type of the entries in the tree
     * @return a new RTree instance
     */
    public static <T, S extends Geometry> RTree<T, S> create() {
        return new Builder().create();
    }

    /**
     * Construct an Rtree through STR bulk loading. Default to maxChildren=128,
     * minChildren=64 and fill nodes by a factor of 0.7
     *
     * @param entries entries to add to the R-tree
     * @param <T>     the value type of the entries in the tree
     * @param <S>     the geometry type of the entries in the tree
     * @return a new RTree instance
     */
    public static <T, S extends Geometry> RTree<T, S> create(List<Entry<T, S>> entries) {
        return new Builder().create(entries);
    }

    /**
     * The tree is scanned for depth and the depth returned. This involves recursing
     * down to the leaf level of the tree to get the current depth. Should be
     * <code>log(n)</code> in complexity.
     *
     * @return depth of the R-tree
     */
    public int calculateDepth() {
        return calculateDepth(root);
    }

    private static <T, S extends Geometry> int calculateDepth(Optional<? extends Node<T, S>> root) {
        if (!root.isPresent())
            return 0;
        else
            return calculateDepth(root.get(), 0);
    }

    private static <T, S extends Geometry> int calculateDepth(Node<T, S> node, int depth) {
        if (node instanceof Leaf)
            return depth + 1;
        else
            return calculateDepth(((NonLeaf<T, S>) node).child(0), depth + 1);
    }

    /**
     * When the number of children in an R-tree node drops below this number the
     * node is deleted and the children are added on to the R-tree again.
     *
     * @param minChildren less than this number of children in a node triggers a node
     *                    deletion and redistribution of its members
     * @return builder
     */
    public static Builder minChildren(int minChildren) {
        return new Builder().minChildren(minChildren);
    }

    /**
     * Sets the max number of children in an R-tree node.
     *
     * @param maxChildren max number of children in an R-tree node
     * @return builder
     */
    public static Builder maxChildren(int maxChildren) {
        return new Builder().maxChildren(maxChildren);
    }

    /**
     * Sets the {@link Splitter} to use when maxChildren is reached.
     *
     * @param splitter the splitter algorithm to use
     * @return builder
     */
    public static Builder splitter(Splitter splitter) {
        return new Builder().splitter(splitter);
    }

    /**
     * Sets the node {@link Selector} which decides which branches to follow when
     * inserting or searching.
     *
     * @param selector determines which branches to follow when inserting or searching
     * @return builder
     */
    public static Builder selector(Selector selector) {
        return new Builder().selector(selector);
    }

    /**
     * Sets the splitter to {@link SplitterRStar} and selector to
     * {@link SelectorRStar} and defaults to minChildren=10.
     *
     * @return builder
     */
    public static Builder star() {
        return new Builder().star();
    }


    /**
     * RTree Builder.
     */
    public static class Builder {
        /**
         * According to http://dbs.mathematik.uni-marburg.de/publications/myPapers
         * /1990/BKSS90.pdf (R*-tree paper), best filling ratio is 0.4 for both
         * quadratic split and R*-tree split.
         */
        private static final double DEFAULT_FILLING_FACTOR = 0.4;
        private static final double DEFAULT_LOADING_FACTOR = 0.7;
        private Optional<Integer> maxChildren = absent();
        private Optional<Integer> minChildren = absent();
        private Splitter splitter = new SplitterRStar();
        private Selector selector = new SelectorMinimalAreaIncrease();
        private double loadingFactor;
        private boolean star = false;
        private Factory<Object, Geometry> factory = Factories.defaultFactory();

        private Builder() {
            loadingFactor = DEFAULT_LOADING_FACTOR;
        }

        /**
         * The factor is used as the fill ratio during bulk loading.
         *
         * @param factor loading factor
         * @return this
         */
        public Builder loadingFactor(double factor) {
            this.loadingFactor = factor;
            return this;
        }

        /**
         * When the number of children in an R-tree node drops below this number the
         * node is deleted and the children are added on to the R-tree again.
         *
         * @param minChildren less than this number of children in a node triggers a
         *                    redistribution of its children.
         * @return builder
         */
        public Builder minChildren(int minChildren) {
            this.minChildren = of(minChildren);
            return this;
        }

        /**
         * Sets the max number of children in an R-tree node.
         *
         * @param maxChildren max number of children in R-tree node.
         * @return builder
         */
        public Builder maxChildren(int maxChildren) {
            this.maxChildren = of(maxChildren);
            return this;
        }

        /**
         * Sets the {@link Splitter} to use when maxChildren is reached.
         *
         * @param splitter node splitting method to use
         * @return builder
         */
        public Builder splitter(Splitter splitter) {
            this.splitter = splitter;
            return this;
        }

        /**
         * Sets the node {@link Selector} which decides which branches to follow when
         * inserting or searching.
         *
         * @param selector selects the branch to follow when inserting or searching
         * @return builder
         */
        public Builder selector(Selector selector) {
            this.selector = selector;
            return this;
        }

        /**
         * Sets the splitter to
         *
         * @return builder
         */
        public Builder star() {
            selector = new SelectorRStar();
            splitter = new SplitterRStar();
            star = true;
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder factory(Factory<?, ? extends Geometry> factory) {
            // TODO could change the signature of Builder to have types to
            // support this method but would be breaking change for existing
            // clients
            this.factory = (Factory<Object, Geometry>) factory;
            return this;
        }

        /**
         * Builds the {@link RTree}.
         *
         * @param <T> value type
         * @param <S> geometry type
         * @return RTree
         */
        @SuppressWarnings("unchecked")
        public <T, S extends Geometry> RTree<T, S> create() {
            setDefaultCapacity();

            return new RTree<>(Optional.<Node<T, S>>absent(), 0,
                    new Context<>(minChildren.get(), maxChildren.get(), selector, splitter, (Factory<T, S>) factory));
        }

        /**
         * Create an RTree by bulk loading, using the STR method. STR: a simple and
         * efficient algorithm for R-tree packing
         * http://ieeexplore.ieee.org/abstract/document/582015/
         * <p>
         * Note: this method mutates the input entries, the internal order of the List
         * may be changed.
         * </p>
         *
         * @param entries entries to be added to the r-tree
         * @return a loaded RTree
         */
        @SuppressWarnings("unchecked")
        public <T, S extends Geometry> RTree<T, S> create(List<Entry<T, S>> entries) {
            setDefaultCapacity();

            Context<T, S> context = new Context<T, S>(minChildren.get(), maxChildren.get(),
                    selector, splitter, (Factory<T, S>) factory);
            return packingSTR(entries, true, entries.size(), context);
        }

        private void setDefaultCapacity() {
            if (!maxChildren.isPresent())
                if (star)
                    maxChildren = of(MAX_CHILDREN_DEFAULT_STAR);
                else
                    maxChildren = of(MAX_CHILDREN_DEFAULT_GUTTMAN);
            if (!minChildren.isPresent())
                minChildren = of((int) Math.round(maxChildren.get() * DEFAULT_FILLING_FACTOR));
        }

        @SuppressWarnings("unchecked")
        private <T, S extends Geometry> RTree<T, S> packingSTR(List<? extends HasGeometry> objects,
                                                               boolean isLeaf, int size, Context<T, S> context) {
            int capacity = (int) Math.round(maxChildren.get() * loadingFactor);
            int nodeCount = (int) Math.ceil(1.0 * objects.size() / capacity);

            if (nodeCount == 0) {
                return create();
            } else if (nodeCount == 1) {
                Node<T, S> root;
                if (isLeaf) {
                    root = context.factory().createLeaf((List<Entry<T, S>>) objects, context);
                } else {
                    root = context.factory().createNonLeaf((List<Node<T, S>>) objects, context);
                }
                return new RTree<>(of(root), size, context);
            }

            int nodePerSlice = (int) Math.ceil(Math.sqrt(nodeCount));
            int sliceCapacity = nodePerSlice * capacity;
            int sliceCount = (int) Math.ceil(1.0 * objects.size() / sliceCapacity);
            Collections.sort(objects, new MidComparator((short) 0));

            List<Node<T, S>> nodes = new ArrayList<Node<T, S>>(nodeCount);
            for (int s = 0; s < sliceCount; s++) {
                @SuppressWarnings("rawtypes")
                List slice = objects.subList(s * sliceCapacity,
                        Math.min((s + 1) * sliceCapacity, objects.size()));
                Collections.sort(slice, new MidComparator((short) 1));

                for (int i = 0; i < slice.size(); i += capacity) {
                    if (isLeaf) {
                        List<Entry<T, S>> entries = slice.subList(i,
                                Math.min(slice.size(), i + capacity));
                        Node<T, S> leaf = context.factory().createLeaf(entries, context);
                        nodes.add(leaf);
                    } else {
                        List<Node<T, S>> children = slice.subList(i,
                                Math.min(slice.size(), i + capacity));
                        Node<T, S> nonleaf = context.factory().createNonLeaf(children, context);
                        nodes.add(nonleaf);
                    }
                }
            }
            return packingSTR(nodes, false, size, context);
        }

        private static final class MidComparator implements Comparator<HasGeometry> {
            private final short dimension; // leave space for multiple dimensions, 0 for x, 1 for y,
            // ...

            public MidComparator(short dim) {
                dimension = dim;
            }

            @Override
            public int compare(HasGeometry o1, HasGeometry o2) {
                return Double.compare(mid(o1), mid(o2));
            }

            private double mid(HasGeometry o) {
                Rectangle mbr = o.geometry().mbr();
                if (dimension == 0)
                    return (mbr.x1() + mbr.x2()) / 2;
                else
                    return (mbr.y1() + mbr.y2()) / 2;
            }
        }
    }

    /**
     * Returns all entries in the tree as an {@link Observable} sequence.
     *
     * @return all entries in the R-tree
     */
    public Observable<Entry<T, S>> entries() {
        return search(ALWAYS_TRUE);
    }

    /**
     * Returns an immutable copy of the RTree with the addition of given entry.
     *
     * @param entry item to add to the R-tree.
     * @return a new immutable R-tree including the new entry
     */
    @SuppressWarnings("unchecked")
    public RTree<T, S> add(Entry<? extends T, ? extends S> entry) {
        if (root.isPresent()) {
            List<Node<T, S>> nodes = root.get().add(entry);
            Node<T, S> node;
            if (nodes.size() == 1)
                node = nodes.get(0);
            else {
                node = context.factory().createNonLeaf(nodes, context);
            }
            return new RTree<>(node, size + 1, context);
        } else {
            Leaf<T, S> node = context.factory().createLeaf(Lists.newArrayList((Entry<T, S>) entry),
                    context);
            return new RTree<>(node, size + 1, context);
        }
    }

    /**
     * Returns an immutable copy of the RTree with the addition of an entry
     * comprised of the given value and Geometry.
     *
     * @param value    the value of the {@link Entry} to be added
     * @param geometry the geometry of the {@link Entry} to be added
     * @return a new immutable R-tree including the new entry
     */
    public RTree<T, S> add(T value, S geometry) {
        return add(context.factory().createEntry(value, geometry));
    }

    /**
     * Returns an immutable RTree with the current entries and the additional
     * entries supplied as a parameter.
     *
     * @param entries entries to add
     * @return R-tree with entries added
     */
    public RTree<T, S> add(Iterable<Entry<T, S>> entries) {
        RTree<T, S> tree = this;
        for (Entry<T, S> entry : entries)
            tree = tree.add(entry);
        return tree;
    }

    /**
     * Deletes one or all matching entries depending on the value of
     * <code>all</code>. If multiple copies of the entry are in the R-tree only one
     * will be deleted if all is false otherwise all matching entries will be
     * deleted. The entry must match on both value and geometry to be deleted.
     *
     * @param entry the {@link Entry} to be deleted
     * @param all   if true deletes all matches otherwise deletes first found
     * @return a new immutable R-tree without one instance of the specified entry
     */
    public RTree<T, S> delete(Entry<? extends T, ? extends S> entry, boolean all) {
        if (root.isPresent()) {
            NodeAndEntries<T, S> nodeAndEntries = root.get().delete(entry, all);
            if (nodeAndEntries.node().isPresent() && nodeAndEntries.node().get() == root.get())
                return this;
            else
                return new RTree<>(nodeAndEntries.node(),
                        size - nodeAndEntries.countDeleted() - nodeAndEntries.entriesToAdd().size(),
                        context)
                        .add(nodeAndEntries.entriesToAdd());
        } else
            return this;
    }

    /**
     * Deletes one entry if it exists, returning an immutable copy of the RTree
     * without that entry. If multiple copies of the entry are in the R-tree only
     * one will be deleted. The entry must match on both value and geometry to be
     * deleted.
     *
     * @param entry the {@link Entry} to be deleted
     * @return a new immutable R-tree without one instance of the specified entry
     */
    public RTree<T, S> delete(Entry<? extends T, ? extends S> entry) {
        return delete(entry, false);
    }


    /**
     * Returns a new R-tree with the given entries deleted. If <code>all</code> is
     * false deletes only one if exists. If <code>all</code> is true deletes all
     * matching entries.
     *
     * @param entries entries to delete
     * @param all     if false deletes one if exists else deletes all
     * @return R-tree with entries deleted
     */
    public RTree<T, S> delete(Iterable<Entry<T, S>> entries, boolean all) {
        RTree<T, S> tree = this;
        for (Entry<T, S> entry : entries)
            tree = tree.delete(entry, all);
        return tree;
    }

    /**
     * Returns a new R-tree with the given entries deleted but only one matching
     * occurence of each entry is deleted.
     *
     * @param entries entries to delete
     * @return R-tree with entries deleted up to one matching occurence per entry
     */
    public RTree<T, S> delete(Iterable<Entry<T, S>> entries) {
        RTree<T, S> tree = this;
        for (Entry<T, S> entry : entries)
            tree = tree.delete(entry);
        return tree;
    }

    Observable<Entry<T, S>> search(Func1<? super Geometry, Boolean> condition) {
        if (root.isPresent())
            return Observable.unsafeCreate(new OnSubscribeSearch<>(root.get(), condition));
        else
            return Observable.empty();
    }

    /**
     * Returns the always true predicate. See {@link RTree#entries()} for example
     * use.
     */
    private static final Func1<Geometry, Boolean> ALWAYS_TRUE = new Func1<Geometry, Boolean>() {
        @Override
        public Boolean call(Geometry rectangle) {
            return true;
        }
    };
}
