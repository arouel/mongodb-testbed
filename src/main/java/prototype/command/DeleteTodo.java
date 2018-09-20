package prototype.command;

import core.Command;
import core.Unit;
import org.immutables.value.Value.Immutable;

@Immutable
public abstract class DeleteTodo implements Command<Unit> {

    public abstract long todoId();

}
