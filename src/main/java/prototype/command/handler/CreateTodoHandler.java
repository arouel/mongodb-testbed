package prototype.command.handler;

import static java.util.Objects.*;

import java.util.function.Supplier;

import core.CommandHandler;
import core.Result;
import prototype.command.CreateTodo;
import prototype.command.Todo;
import prototype.command.TodoRepository;

public class CreateTodoHandler implements CommandHandler<CreateTodo, Long> {

    private final Supplier<Long> _nextTodoId;
    private final TodoRepository _repository;

    public CreateTodoHandler(Supplier<Long> nextTodoId, TodoRepository repository) {
        _nextTodoId = requireNonNull(nextTodoId);
        _repository = requireNonNull(repository);
    }

    @Override
    public Class<CreateTodo> commandType() {
        return CreateTodo.class;
    }

    @Override
    public Result<Long> handle(CreateTodo command) {
        try {
            Long id = _nextTodoId.get();
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
