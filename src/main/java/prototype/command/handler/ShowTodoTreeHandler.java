package prototype.command.handler;

import static java.util.Objects.requireNonNull;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;

import core.QueryHandler;
import core.Result;
import prototype.command.Node;
import prototype.command.ShowTodoTree;
import prototype.command.Todo;
import prototype.command.TodoId;
import prototype.command.TodoRepository;
import prototype.command.TodoRepository.Criteria;

public class ShowTodoTreeHandler implements QueryHandler<ShowTodoTree, Node<Todo>> {

    private final TodoRepository _repository;

    public ShowTodoTreeHandler(TodoRepository repository) {
        _repository = requireNonNull(repository);
    }

    private Iterable<Node<Todo>> children(TodoId parentId) {
        Criteria byParentId = _repository
                .criteria()
                .parentId(parentId);
        Iterable<Todo> childs = _repository
                .find(byParentId)
                .fetchAll()
                .getUnchecked();
        return FluentIterable
                .from(childs)
                .transform(new Function<Todo, Node<Todo>>() {
                    @Override
                    public Node<Todo> apply(Todo todo) {
                        return node(todo);
                    }

                });
    }

    @Override
    public Result<Node<Todo>> handle(ShowTodoTree command) {
        try {
            Optional<Node<Todo>> node = _repository
                    .findById(command.todoId())
                    .fetchFirst()
                    .get()
                    .transform(this::node);
            return node.isPresent()
                    ? Result.success(node.get())
                    : Result.absent();
        } catch (Exception e) {
            return Result.unknownFailure(e);
        }
    }

    private Node<Todo> node(Todo todo) {
        return Node.of(todo, children(todo.id()));
    }

    @Override
    public Class<ShowTodoTree> queryType() {
        return ShowTodoTree.class;
    }
}
