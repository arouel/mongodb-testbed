package prototype.todoapp.command.handler;

import static java.util.Objects.requireNonNull;

import com.google.common.base.Optional;

import core.CommandHandler;
import core.Result;
import core.Unit;
import prototype.todoapp.Todo;
import prototype.todoapp.TodoRepository;
import prototype.todoapp.command.DeleteTodo;
import prototype.todoapp.event.TodoEventStore;

public class DeleteTodoHandler implements CommandHandler<DeleteTodo, Unit> {

    private final TodoEventStore _eventStore;
    private final TodoRepository _repository;

    public DeleteTodoHandler(TodoRepository repository, TodoEventStore eventStore) {
        _repository = requireNonNull(repository);
        _eventStore = requireNonNull(eventStore);
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
            _eventStore.saveTodoDeleted(command.todoId());
            return Result.success(Unit.VALUE);
        } catch (Exception e) {
            return Result.unknownFailure(e);
        }
    }
}
