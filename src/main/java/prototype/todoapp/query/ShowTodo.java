package prototype.todoapp.query;

import org.immutables.value.Value.Immutable;

import core.Query;
import prototype.todoapp.Todo;
import prototype.todoapp.TodoId;

@Immutable
public abstract class ShowTodo implements Query<Todo> {

    public abstract TodoId todoId();

}
