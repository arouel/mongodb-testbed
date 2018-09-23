package prototype.todoapp;

import org.immutables.value.Value.Immutable;

import core.Query;

@Immutable
public abstract class ShowTodoTree implements Query<Node<Todo>> {

    public abstract TodoId todoId();

}
