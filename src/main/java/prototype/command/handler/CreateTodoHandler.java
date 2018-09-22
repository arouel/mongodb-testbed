package prototype.command.handler;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

import core.CommandHandler;
import core.Result;
import prototype.command.CreateTodo;
import prototype.command.Todo;
import prototype.command.TodoId;
import prototype.command.TodoRepository;

public class CreateTodoHandler implements CommandHandler<CreateTodo, TodoId> {

    private final Supplier<TodoId> _nextTodoId;
    private final TodoRepository _repository;

    public CreateTodoHandler(Supplier<TodoId> nextTodoId, TodoRepository repository) {
        _nextTodoId = requireNonNull(nextTodoId);
        _repository = requireNonNull(repository);
    }

    @Override
    public Class<CreateTodo> commandType() {
        return CreateTodo.class;
    }

    @Override
    public Result<TodoId> handle(CreateTodo command) {
        try {
            TodoId id = _nextTodoId.get();
            Todo todo = Todo
                    .builder()
                    .description(command.description())
                    .id(id)
                    .parentId(command.parentId())
                    .title(command.title())
                    .version(1)
                    .build();
            _repository.insert(todo).get();
            return Result.success(id);
        } catch (Exception e) {
            return Result.unknownFailure(e);
        }
    }
}
