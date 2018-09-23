package prototype.todoapp.handler;

import static java.util.Objects.requireNonNull;

import com.google.common.base.Optional;

import core.QueryHandler;
import core.Result;
import prototype.todoapp.ShowTodo;
import prototype.todoapp.Todo;
import prototype.todoapp.TodoRepository;

public class ShowTodoHandler implements QueryHandler<ShowTodo, Todo> {

    private final TodoRepository _repository;

    public ShowTodoHandler(TodoRepository repository) {
        _repository = requireNonNull(repository);
    }

    @Override
    public Result<Todo> handle(ShowTodo command) {
        try {
            Optional<Todo> todo = _repository
                    .findById(command.todoId())
                    .fetchFirst()
                    .get();
            return todo.isPresent()
                    ? Result.success(todo.get())
                    : Result.absent();
        } catch (Exception e) {
            return Result.unknownFailure(e);
        }
    }

    @Override
    public Class<ShowTodo> queryType() {
        return ShowTodo.class;
    }
}
