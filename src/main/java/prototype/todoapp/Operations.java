package prototype.todoapp;

import com.google.common.collect.ImmutableList;

import core.CommandBus;
import core.QueryBus;
import core.Result;
import core.Unit;
import prototype.todoapp.command.Commands;
import prototype.todoapp.query.Queries;

/**
 * Convenient access to possible operations of this domain
 */
public interface Operations extends CommandBus, Commands, QueryBus, Queries {

    default Result<TodoId> commandCreateTodo(String title, String description) {
        return command(createTodo(title, description));
    }

    default Result<TodoId> commandCreateTodo(String title, String description, TodoId parentTodoId) {
        return command(createTodo(title, description, parentTodoId));
    }

    default Result<Unit> commandDeleteTodo(TodoId todoId) {
        return command(deleteTodo(todoId));
    }

    default Result<TodoId> commandEditDescription(TodoId todoId, String description) {
        return command(editDescription(todoId, description));
    }

    default Result<Todo> queryShowTodo(TodoId todoId) {
        return query(showTodo(todoId));
    }

    default Result<ImmutableList<Todo>> queryShowTodoChildren(TodoId parentTodoId) {
        return query(showTodoChildren(parentTodoId));
    }

    default Result<Node<Todo>> queryShowTodoTree(TodoId todoId) {
        return query(showTodoTree(todoId));
    }

}
