package prototype.todoapp.command.handler;

import static java.util.Objects.requireNonNull;

import com.google.common.base.Optional;

import core.CommandHandler;
import core.Result;
import prototype.todoapp.Todo;
import prototype.todoapp.TodoId;
import prototype.todoapp.TodoRepository;
import prototype.todoapp.command.EditDescription;
import prototype.todoapp.event.TodoEventStore;

public class EditDescriptionHandler implements CommandHandler<EditDescription, TodoId> {

    private final TodoEventStore _eventStore;
    private final TodoRepository _repository;

    public EditDescriptionHandler(TodoRepository repository, TodoEventStore eventStore) {
        _repository = requireNonNull(repository);
        _eventStore = requireNonNull(eventStore);
    }

    @Override
    public Class<EditDescription> commandType() {
        return EditDescription.class;
    }

    @Override
    public Result<TodoId> handle(EditDescription command) {
        try {
            Optional<Todo> update = _repository
                    .findById(command.todoId())
                    .andModifyFirst()
                    .setDescription(command.description())
                    .update()
                    .get();
            if (!update.isPresent()) {
                return Result.notFoundOrNotAccessible("Todo %s doesn’t exist", command.todoId());
            }
            _eventStore.saveDescriptionEdited(command.todoId(), command.description());
            return Result.success(update.get().id());
        } catch (Exception e) {
            return Result.unknownFailure(e);
        }
    }
}
