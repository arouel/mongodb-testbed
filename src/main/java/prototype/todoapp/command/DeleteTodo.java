package prototype.todoapp.command;

import org.immutables.value.Value.Immutable;

import core.Command;
import core.Unit;
import prototype.todoapp.TodoId;

@Immutable
public abstract class DeleteTodo implements Command<Unit> {

    public abstract TodoId todoId();

}
