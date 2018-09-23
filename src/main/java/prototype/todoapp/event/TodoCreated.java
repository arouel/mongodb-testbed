package prototype.todoapp.event;

import org.immutables.gson.Gson.TypeAdapters;
import org.immutables.value.Value.Immutable;

import com.google.common.base.Optional;

import core.Event;
import prototype.todoapp.TodoId;

@Immutable
@TypeAdapters
public abstract class TodoCreated implements Event {

    public abstract String description();

    public abstract TodoId id();

    public abstract Optional<TodoId> parentId();

    public abstract String title();

}
