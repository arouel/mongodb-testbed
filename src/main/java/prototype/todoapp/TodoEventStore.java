package prototype.todoapp;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

import com.google.common.base.Optional;

import core.Event;
import core.EventId;
import prototype.todoapp.event.EventRepository;
import prototype.todoapp.event.Events;

public final class TodoEventStore implements core.EventStore, Events {

    private final Supplier<EventId> _nextEventId;
    private final EventRepository _repository;

    public TodoEventStore(Supplier<EventId> nextEventId, EventRepository repository) {
        _nextEventId = requireNonNull(nextEventId);
        _repository = requireNonNull(repository);
    }

    @Override
    public void save(Event event) {
        _repository.insert(event).getUnchecked();
    }

    public void saveTodoCreated(TodoId id, Optional<TodoId> parentId, String title, String description) {
        EventId eventId = _nextEventId.get();
        save(todoCreated(eventId, id, parentId, title, description));
    }
}
