package prototype.command;

/**
 * Commands of this domain
 */
public interface Commands {

    default CreateTodo createTodo(String title, String description) {
        return ImmutableCreateTodo.builder().title(title).description(description).build();
    }

    default CreateTodo createTodo(String title, String description, long parentTodoId) {
        return ImmutableCreateTodo.builder().title(title).description(description).parentId(parentTodoId).build();
    }

    default DeleteTodo deleteTodo(long todoId) {
        return ImmutableDeleteTodo.builder().todoId(todoId).build();
    }

    default EditDescription editDescription(long todoId, String description) {
        return ImmutableEditDescription.builder().todoId(todoId).description(description).build();
    }

}
