package prototype.command;

import com.google.common.collect.ImmutableList;
import core.Query;
import org.immutables.value.Value.Immutable;

@Immutable
public abstract class ShowTodoChildren implements Query<ImmutableList<Todo>> {

    public abstract long parentTodoId();

}
