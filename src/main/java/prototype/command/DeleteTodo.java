package prototype.command;

import org.immutables.value.Value.Immutable;

@Immutable
public abstract class DeleteTodo extends Command<Unit> {

    public abstract long todoId();

}
