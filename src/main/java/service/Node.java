package service;

import java.util.Objects;

public class Node<T> {

    public T task;
    public Node<T> next;
    public Node<T> prev;

    public Node(Node<T> prev, T task, Node<T> next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }

    @Override
    public String toString() {
        return "Node{" +
                "task=" + task +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(task, node.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task);
    }
}

