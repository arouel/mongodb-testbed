package prototype.command.handler;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

import core.CommandHandler;
import core.Result;
import prototype.command.CreateTodo;
import prototype.command.Todo;
import prototype.command.TodoEventStore;
import prototype.command.TodoId;
import prototype.command.TodoRepository;
import prototype.command.event.Events;

public class CreateTodoHandler implements CommandHandler<CreateTodo, TodoId>, Events {

    private final Supplier<TodoId> _nextTodoId;
    private final TodoRepository _repository;
    private final TodoEventStore _eventStore;

    public CreateTodoHandler(Supplier<TodoId> nextTodoId, TodoRepository repository, TodoEventStore eventStore) {
        _nextTodoId = requireNonNull(nextTodoId);
        _repository = requireNonNull(repository);
        _eventStore = requireNonNull(eventStore);
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
            _eventStore.saveTodoCreated(id, command.parentId(), command.title(), command.description());
            return Result.success(id);
        } catch (Exception e) {
            return Result.unknownFailure(e);
        }
    }
}
