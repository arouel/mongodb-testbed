package prototype.todoapp.event;

import com.google.common.base.Optional;

import core.Event;
import core.EventId;
import prototype.RuntimeTypeAdapterFactory;
import prototype.todoapp.TodoId;

public interface Events {

    static final RuntimeTypeAdapterFactory<Event> RUNTIME_TYPE_ADAPTER_FACTORY = RuntimeTypeAdapterFactory
            .of(Event.class)
            .registerSubtype(ImmutableDescriptionEdited.class, DescriptionEdited.class.getName())
            .registerSubtype(ImmutableTodoCreated.class, TodoCreated.class.getName())
            .registerSubtype(ImmutableTodoDeleted.class, TodoDeleted.class.getName())

    ;

    default DescriptionEdited descriptionEdited(
            EventId eventId,
            TodoId todoId,
            String description) {
        return ImmutableDescriptionEdited
                .builder()
                .description(description)
                .eventId(eventId)
                .id(todoId)
                .build();
    };

    default TodoCreated todoCreated(
            EventId eventId,
            TodoId todoId,
            Optional<TodoId> parentId,
            String title,
            String description) {
        return ImmutableTodoCreated
                .builder()
                .description(description)
                .eventId(eventId)
                .id(todoId)
                .parentId(parentId)
                .title(title)
                .build();
    };

    default TodoDeleted todoDeleted(
            EventId eventId,
            TodoId todoId) {
        return ImmutableTodoDeleted
                .builder()
                .eventId(eventId)
                .id(todoId)
                .build();
    };

}
