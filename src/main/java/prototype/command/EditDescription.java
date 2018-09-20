package prototype.command;

import org.immutables.value.Value.Immutable;

@Immutable
public abstract class EditDescription extends Command<Todo> {

    public abstract String description();

    public abstract long todoId();

}
