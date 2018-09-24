package prototype.todoapp.command;

import org.immutables.value.Value.Immutable;

import com.google.common.base.Optional;

import core.Command;
import prototype.todoapp.TodoId;

@Immutable
public abstract class CreateTodo implements Command<TodoId> {

    public abstract String description();

    public abstract Optional<TodoId> parentId();

    public abstract String title();

}
