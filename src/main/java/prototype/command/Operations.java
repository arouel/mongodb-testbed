package prototype.command;

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

    default Result<Unit> commandDeleteTodo(long todoId) {
        return command(deleteTodo(todoId));
    }

    default Result<Long> commandEditDescription(long todoId, String description) {
        return command(editDescription(todoId, description));
    }

    default Result<Todo> queryShowTodo(long todoId) {
        return query(showTodo(todoId));
    }

}
