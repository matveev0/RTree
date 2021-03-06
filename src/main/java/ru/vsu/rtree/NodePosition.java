package ru.vsu.rtree;

import ru.vsu.rtree.geometry.Geometry;
import ru.vsu.rtree.geometry.utils.Preconditions;

public class NodePosition<T,S extends Geometry> {

    private final Node<T, S> node;
    private final int position;

    NodePosition(Node<T, S> node, int position) {
        Preconditions.checkNotNull(node);
        this.node = node;
        this.position = position;
    }

    Node<T, S> node() {
        return node;
    }

    int position() {
        return position;
    }

    NodePosition<T, S> nextPosition() {
        return new NodePosition<>(node, position + 1);
    }

    @Override
    public String toString() {
        return "NodePosition [node=" +
                node +
                ", position=" +
                position +
                "]";
    }
}
