package prototype.todoapp.handler;

import static java.util.Objects.requireNonNull;

import com.google.common.base.Optional;

import core.CommandHandler;
import core.Result;
import prototype.todoapp.EditDescription;
import prototype.todoapp.Todo;
import prototype.todoapp.TodoId;
import prototype.todoapp.TodoRepository;

public class EditDescriptionHandler implements CommandHandler<EditDescription, TodoId> {

    private final TodoRepository _repository;

    public EditDescriptionHandler(TodoRepository repository) {
        _repository = requireNonNull(repository);
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
            return Result.success(update.get().id());
        } catch (Exception e) {
            return Result.unknownFailure(e);
        }
    }
}
