package prototype.todoapp.query;

import org.immutables.value.Value.Immutable;

import com.google.common.collect.ImmutableList;

import core.Query;
import prototype.todoapp.Todo;
import prototype.todoapp.TodoId;

@Immutable
public abstract class ShowTodoChildren implements Query<ImmutableList<Todo>> {

    public abstract TodoId parentTodoId();

}
