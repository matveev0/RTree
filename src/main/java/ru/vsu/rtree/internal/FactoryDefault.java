package ru.vsu.rtree.internal;

import ru.vsu.rtree.*;
import ru.vsu.rtree.geometry.Geometry;

import java.util.List;

public final class FactoryDefault<T, S extends Geometry> implements Factory<T, S> {

    private static class Holder {
        private static final Factory<Object, Geometry> INSTANCE = new FactoryDefault<>();
    }

    @SuppressWarnings("unchecked")
    public static <T, S extends Geometry> Factory<T, S> instance() {
        return (Factory<T, S>) Holder.INSTANCE;
    }

    @Override
    public Leaf<T, S> createLeaf(List<Entry<T, S>> entries, Context<T, S> context) {
        return new LeafDefault<>(entries, context);
    }

    @Override
    public NonLeaf<T, S> createNonLeaf(List<? extends Node<T, S>> children, Context<T, S> context) {
        return new NonLeafDefault<>(children, context);
    }

    @Override
    public Entry<T, S> createEntry(T value, S geometry) {
        return Entries.entry(value, geometry);
    }
}
