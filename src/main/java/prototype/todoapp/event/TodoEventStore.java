package prototype.todoapp.event;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.base.Optional;

import core.Event;
import core.EventId;
import prototype.todoapp.TodoId;

public final class TodoEventStore implements core.EventStore, Events {

    private final Supplier<EventId> _nextEventId;
    private final EventRepository _repository;

    public TodoEventStore(Supplier<EventId> nextEventId, EventRepository repository) {
        _nextEventId = requireNonNull(nextEventId);
        _repository = requireNonNull(repository);
    }

    public List<Event> events() {
        return _repository
                .findAll()
                .fetchAll()
                .getUnchecked();
    }

    @Override
    public void save(Event event) {
        _repository.insert(event).getUnchecked();
    }

    public void saveDescriptionEdited(TodoId id, String description) {
        EventId eventId = _nextEventId.get();
        save(descriptionEdited(eventId, id, description));
    }

    public void saveTodoCreated(TodoId id, Optional<TodoId> parentId, String title, String description) {
        EventId eventId = _nextEventId.get();
        save(todoCreated(eventId, id, parentId, title, description));
    }

    public void saveTodoDeleted(TodoId id) {
        EventId eventId = _nextEventId.get();
        save(todoDeleted(eventId, id));
    }
}
