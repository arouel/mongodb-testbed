package prototype.todoapp.query;

import org.immutables.value.Value.Immutable;

import core.Query;
import prototype.todoapp.Node;
import prototype.todoapp.Todo;
import prototype.todoapp.TodoId;

@Immutable
public abstract class ShowTodoTree implements Query<Node<Todo>> {

    public abstract TodoId todoId();

}
