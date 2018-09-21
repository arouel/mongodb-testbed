package prototype.command;

/**
 * Queries of this domain
 */
public interface Queries {

    default ShowTodo showTodo(long todoId) {
        return ImmutableShowTodo.builder().todoId(todoId).build();
    }

    default ShowTodoChildren showTodoChildren(long parentTodoId) {
        return ImmutableShowTodoChildren.builder().parentTodoId(parentTodoId).build();
    }

}
