package prototype.command;

import core.Query;
import org.immutables.value.Value.Immutable;

@Immutable
public abstract class ShowTodo implements Query<Todo> {

    public abstract long todoId();

}
