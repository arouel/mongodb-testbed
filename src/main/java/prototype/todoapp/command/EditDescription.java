package prototype.todoapp.command;

import org.immutables.value.Value.Immutable;

import core.Command;
import prototype.todoapp.TodoId;

@Immutable
public abstract class EditDescription implements Command<TodoId> {

    public abstract String description();

    public abstract TodoId todoId();

}
