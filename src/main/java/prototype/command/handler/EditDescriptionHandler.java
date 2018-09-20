package prototype.command.handler;

import static java.util.Objects.*;

import com.google.common.base.Optional;
import core.CommandHandler;
import core.Result;
import prototype.command.EditDescription;
import prototype.command.Todo;
import prototype.command.TodoRepository;

public class EditDescriptionHandler implements CommandHandler<EditDescription, Long> {

    private final TodoRepository _repository;

    public EditDescriptionHandler(TodoRepository repository) {
        _repository = requireNonNull(repository);
    }

    @Override
    public Class<EditDescription> commandType() {
        return EditDescription.class;
    }

    @Override
    public Result<Long> handle(EditDescription command) {
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
