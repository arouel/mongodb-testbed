package prototype.command.handler;

import static java.util.Objects.*;

import java.util.List;

import com.google.common.collect.ImmutableList;
import core.QueryHandler;
import core.Result;
import prototype.command.ShowTodoChildren;
import prototype.command.Todo;
import prototype.command.TodoRepository;
import prototype.command.TodoRepository.Criteria;

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
