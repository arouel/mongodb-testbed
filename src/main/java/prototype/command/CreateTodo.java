package prototype.command;

import com.google.common.base.Optional;
import core.Command;
import org.immutables.value.Value.Immutable;

@Immutable
public abstract class CreateTodo implements Command<Long> {

    public abstract String description();

    public abstract Optional<Long> parentId();

    public abstract String title();

}
