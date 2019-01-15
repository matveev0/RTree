package ru.vsu.rtree.internal;

import ru.vsu.rtree.Context;
import ru.vsu.rtree.Entry;
import ru.vsu.rtree.Leaf;
import ru.vsu.rtree.Node;
import ru.vsu.rtree.geometry.Geometry;
import ru.vsu.rtree.geometry.Rectangle;
import ru.vsu.rtree.internal.NodeAndEntries;
import ru.vsu.rtree.internal.Util;
import rx.Subscriber;
import rx.functions.Func1;

import java.util.List;


public class  LeafDefault<T, S extends Geometry> implements Leaf<T, S> {
    private final List<Entry<T, S>> entries;
    private final Rectangle mbr;
    private final Context<T, S> context;

    public LeafDefault(List<Entry<T, S>> entries, Context<T, S> context) {
        this.entries = entries;
        this.context = context;
        this.mbr = Util.mbr(entries);
    }

    @Override
    public Geometry geometry() {
        return mbr;
    }

    @Override
    public List<Entry<T, S>> entries() {
        return entries;
    }

    // TODO: 23.12.2018
    @Override
    public void searchWithoutBackpressure(Func1<? super Geometry, Boolean> condition,
                                          Subscriber<? super Entry<T, S>> subscriber) {
        throw new RuntimeException();
        //LeafHelper.search(condition, subscriber, this);
    }

    @Override
    public int count() {
        return entries.size();
    }

    @Override
    public List<Node<T, S>> add(Entry<? extends T, ? extends S> entry) {
        throw new RuntimeException();

//        return LeafHelper.add(entry, this);
    }

    // TODO: 23.12.2018
    @Override
    public NodeAndEntries<T, S> delete(Entry<? extends T, ? extends S> entry, boolean all) {
        throw new RuntimeException();

//        return LeafHelper.delete(entry, all, this);
    }

    @Override
    public Context<T, S> context() {
        return context;
    }

    @Override
    public Entry<T, S> entry(int i) {
        return entries.get(i);
    }

}
