package prototype.command;

import org.immutables.value.Value.Immutable;

@Immutable
public abstract class CreateTodo extends Command<Todo> {

    public abstract String description();

    public abstract String title();

}
