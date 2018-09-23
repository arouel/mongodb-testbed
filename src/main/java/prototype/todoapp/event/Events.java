package prototype.todoapp.event;

import com.google.common.base.Optional;

import core.Event;
import core.EventId;
import prototype.RuntimeTypeAdapterFactory;
import prototype.todoapp.TodoId;

public interface Events {

    static final RuntimeTypeAdapterFactory<Event> RUNTIME_TYPE_ADAPTER_FACTORY = RuntimeTypeAdapterFactory

            .of(Event.class)

            .registerSubtype(ImmutableTodoCreated.class)

    ;

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

}
