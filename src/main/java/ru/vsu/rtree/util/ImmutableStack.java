package ru.vsu.rtree.util;

import ru.vsu.rtree.geometry.utils.Optional;

import java.util.Iterator;

public final class ImmutableStack<T> implements Iterable<T> {
    private final Optional<T> head;
    private final Optional<ImmutableStack<T>> tail;

    private static ImmutableStack<?> EMPTY = new ImmutableStack<Object>();

    public ImmutableStack(final T head, final ImmutableStack<T> tail) {
        this(Optional.of(head), Optional.of(tail));
    }

    private ImmutableStack(Optional<T> head, Optional<ImmutableStack<T>> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <T> ImmutableStack<T> create(T t) {
        return new ImmutableStack<>(Optional.of(t), Optional.of(ImmutableStack.<T>empty()));
    }

    public ImmutableStack() {
        this(Optional.<T>absent(), Optional.<ImmutableStack<T>>absent());
    }

    @SuppressWarnings("unchecked")
    public static <S> ImmutableStack<S> empty() {
        return (ImmutableStack<S>) EMPTY;
    }

    public boolean isEmpty() {
        return !head.isPresent();
    }

    public T peek() {
        return this.head.get();
    }

    public ImmutableStack<T> pop() {
        return this.tail.get();
    }

    public ImmutableStack<T> push(T value) {
        return new ImmutableStack<>(value, this);
    }

    @Override
    public Iterator<T> iterator() {
        return new StackIterator<>(this);
    }

    private static class StackIterator<U> implements Iterator<U> {
        private ImmutableStack<U> stack;

        StackIterator(final ImmutableStack<U> stack) {
            this.stack = stack;
        }

        @Override
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        @Override
        public U next() {
            final U result = this.stack.peek();
            this.stack = this.stack.pop();
            return result;
        }

        @Override
        public void remove() {
            throw new RuntimeException("not supported");
        }
    }
}
