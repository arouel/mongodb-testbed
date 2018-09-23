package prototype.todoapp;

import org.immutables.value.Value.Immutable;

import com.google.common.base.Optional;

import core.Command;

@Immutable
public abstract class CreateTodo implements Command<TodoId> {

    public abstract String description();

    public abstract Optional<TodoId> parentId();

    public abstract String title();

}
