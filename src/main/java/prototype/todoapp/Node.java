package prototype.todoapp;

import java.util.function.Function;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Lazy;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.graph.Traverser;

/**
 * Node of a tree
 *
 * @param <T> type of the node value
 */
@Immutable
public abstract class Node<T> {

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static <T> Node<T> of(T value, Iterable<? extends Node<T>> children) {
        return Node.<T>builder().value(value).children(children).build();
    }

    @Lazy
    public ImmutableList<T> allChildValues() {
        return FluentIterable
                .from(traverser().depthFirstPreOrder(children()))
                .transform(Node::value)
                .toList();
    }

    @Lazy
    public ImmutableList<T> allValues() {
        return FluentIterable
                .from(traverser().depthFirstPreOrder(this))
                .transform(Node::value)
                .toList();
    }

    public abstract ImmutableList<Node<T>> children();

    @Lazy
    public ImmutableList<T> childValues() {
        return FluentIterable.from(children()).transform(Node::value).toList();
    }

    public <R> Node<R> map(Function<? super T, ? extends R> mapper) {
        R value = mapper.apply(value());
        FluentIterable<Node<R>> children = FluentIterable.from(children()).transform(n -> n.map(mapper));
        return Node.of(value, children);
    }

    public Traverser<Node<T>> traverser() {
        return Traverser.<Node<T>>forTree(node -> node.children());
    }

    public abstract T value();

    public static final class Builder<T> extends ImmutableNode.Builder<T> {
    }
}
