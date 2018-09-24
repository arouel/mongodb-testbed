package prototype.todoapp.event;

import org.immutables.gson.Gson.TypeAdapters;
import org.immutables.mongo.Mongo.Id;
import org.immutables.value.Value.Immutable;

import core.EventId;
import prototype.todoapp.TodoId;

@Immutable
@TypeAdapters
public abstract class DescriptionEdited implements TodoEvent {

    public abstract String description();

    @Id
    @Override
    public abstract EventId eventId();

    public abstract TodoId id();

}
