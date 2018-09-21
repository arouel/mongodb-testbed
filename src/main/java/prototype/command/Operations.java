package prototype.command;

import com.google.common.collect.ImmutableList;
import core.CommandBus;
import core.QueryBus;
import core.Result;
import core.Unit;

/**
 * Convenient access to possible operations of this domain
 */
public interface Operations extends CommandBus, Commands, QueryBus, Queries {

    default Result<Long> commandCreateTodo(String title, String description) {
        return command(createTodo(title, description));
    }

    default Result<Long> commandCreateTodo(String title, String description, long parentTodoId) {
        return command(createTodo(title, description, parentTodoId));
    }

    default Result<Unit> commandDeleteTodo(long todoId) {
        return command(deleteTodo(todoId));
    }

    default Result<Long> commandEditDescription(long todoId, String description) {
        return command(editDescription(todoId, description));
    }

    default Result<Todo> queryShowTodo(long todoId) {
        return query(showTodo(todoId));
    }

    default Result<ImmutableList<Todo>> queryShowTodoChildren(long parentTodoId) {
        return query(showTodoChildren(parentTodoId));
    }

    default Result<Node<Todo>> queryShowTodoTree(long todoId) {
        return query(showTodoTree(todoId));
    }

}
