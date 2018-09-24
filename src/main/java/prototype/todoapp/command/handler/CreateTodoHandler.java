package prototype.todoapp.command.handler;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

import core.CommandHandler;
import core.Result;
import prototype.todoapp.Todo;
import prototype.todoapp.TodoId;
import prototype.todoapp.TodoRepository;
import prototype.todoapp.command.CreateTodo;
import prototype.todoapp.event.Events;
import prototype.todoapp.event.TodoEventStore;

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
