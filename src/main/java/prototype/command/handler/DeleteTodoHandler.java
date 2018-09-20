package prototype.command.handler;

import static java.util.Objects.*;

import com.google.common.base.Optional;
import core.CommandHandler;
import core.Result;
import core.Unit;
import prototype.command.DeleteTodo;
import prototype.command.Todo;
import prototype.command.TodoRepository;

public class DeleteTodoHandler implements CommandHandler<DeleteTodo, Unit> {

    private final TodoRepository _repository;

    public DeleteTodoHandler(TodoRepository repository) {
        _repository = requireNonNull(repository);
    }

    @Override
    public Class<DeleteTodo> commandType() {
        return DeleteTodo.class;
    }

    @Override
    public Result<Unit> handle(DeleteTodo command) {
        try {
            Optional<Todo> delete = _repository
                    .findById(command.todoId())
                    .deleteFirst()
                    .get();
            if (!delete.isPresent()) {
                return Result.notFoundOrNotAccessible("Todo %s doesn’t exist", command.todoId());
            }
            return Result.success(Unit.VALUE);
        } catch (Exception e) {
            return Result.unknownFailure(e);
        }
    }
}
