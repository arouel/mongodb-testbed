package prototype.command;

import org.immutables.value.Value.Immutable;

import core.Command;
import core.Unit;

@Immutable
public abstract class DeleteTodo implements Command<Unit> {

    public abstract TodoId todoId();

}
