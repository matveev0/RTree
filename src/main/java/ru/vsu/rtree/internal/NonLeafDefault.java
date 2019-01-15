package ru.vsu.rtree.internal;

import ru.vsu.rtree.Context;
import ru.vsu.rtree.Entry;
import ru.vsu.rtree.Node;
import ru.vsu.rtree.NonLeaf;
import ru.vsu.rtree.geometry.Geometry;
import ru.vsu.rtree.geometry.Rectangle;
import ru.vsu.rtree.geometry.utils.Preconditions;
import rx.Subscriber;
import rx.functions.Func1;

import java.util.List;

public final class NonLeafDefault<T, S extends Geometry> implements NonLeaf<T, S> {

    private final List<? extends Node<T, S>> children;
    private final Rectangle mbr;
    private final Context<T, S> context;

    public NonLeafDefault(List<? extends Node<T, S>> children, Context<T, S> context) {
        Preconditions.checkArgument(!children.isEmpty());
        this.context = context;
        this.children = children;
        this.mbr = Util.mbr(children);
    }

    @Override
    public Geometry geometry() {
        return mbr;
    }

    // TODO: 23.12.2018
    @Override
    public void searchWithoutBackpressure(Func1<? super Geometry, Boolean> criterion,
                                          Subscriber<? super Entry<T, S>> subscriber) {
        throw new RuntimeException();
        //NonLeafHelper.search(criterion, subscriber, this);
    }

    @Override
    public int count() {
        return children.size();
    }

    // TODO: 23.12.2018
    @Override
    public List<Node<T, S>> add(Entry<? extends T, ? extends S> entry) {
        throw new RuntimeException();

        //return NonLeafHelper.add(entry, this);
    }

    // TODO: 23.12.2018
    @Override
    public NodeAndEntries<T, S> delete(Entry<? extends T, ? extends S> entry, boolean all) {
        throw new RuntimeException();
        //return NonLeafHelper.delete(entry, all, this);
    }

    @Override
    public Context<T, S> context() {
        return context;
    }

    @Override
    public Node<T, S> child(int i) {
        return children.get(i);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Node<T, S>> children() {
        return (List<Node<T, S>>) children;
    }
}