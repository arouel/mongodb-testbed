package prototype.command;

/**
 * Queries of this domain
 */
public interface Queries {

    default ShowTodo showTodo(TodoId todoId) {
        return ImmutableShowTodo.builder().todoId(todoId).build();
    }

    default ShowTodoChildren showTodoChildren(TodoId parentTodoId) {
        return ImmutableShowTodoChildren.builder().parentTodoId(parentTodoId).build();
    }

    default ShowTodoTree showTodoTree(TodoId todoId) {
        return ImmutableShowTodoTree.builder().todoId(todoId).build();
    }

}
