package prototype.todoapp.query.handler;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.google.common.collect.ImmutableList;

import core.QueryHandler;
import core.Result;
import prototype.todoapp.Todo;
import prototype.todoapp.TodoRepository;
import prototype.todoapp.TodoRepository.Criteria;
import prototype.todoapp.query.ShowTodoChildren;

public class ShowTodoChildrenHandler implements QueryHandler<ShowTodoChildren, ImmutableList<Todo>> {

    private final TodoRepository _repository;

    public ShowTodoChildrenHandler(TodoRepository repository) {
        _repository = requireNonNull(repository);
    }

    @Override
    public Result<ImmutableList<Todo>> handle(ShowTodoChildren command) {
        try {
            Criteria byParentId = _repository
                    .criteria()
                    .parentId(command.parentTodoId());
            List<Todo> todo = _repository
                    .find(byParentId)
                    .fetchAll()
                    .get();
            return Result.success(ImmutableList.copyOf(todo));
        } catch (Exception e) {
            return Result.unknownFailure(e);
        }
    }

    @Override
    public Class<ShowTodoChildren> queryType() {
        return ShowTodoChildren.class;
    }
}
