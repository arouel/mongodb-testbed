package prototype.command;

import org.immutables.value.Value.Immutable;

import core.Query;

@Immutable
public abstract class ShowTodo implements Query<Todo> {

    public abstract TodoId todoId();

}
