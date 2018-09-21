package prototype.command;

import core.Query;
import org.immutables.value.Value.Immutable;

@Immutable
public abstract class ShowTodoTree implements Query<Node<Todo>> {

    public abstract long todoId();

}
