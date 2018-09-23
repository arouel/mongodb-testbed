package prototype.todoapp;

import org.immutables.value.Value.Immutable;

import core.Command;

@Immutable
public abstract class EditDescription implements Command<TodoId> {

    public abstract String description();

    public abstract TodoId todoId();

}
