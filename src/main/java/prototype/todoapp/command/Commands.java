package prototype.todoapp.command;

import prototype.todoapp.TodoId;

/**
 * Commands of this domain
 */
public interface Commands {

    default CreateTodo createTodo(String title, String description) {
        return ImmutableCreateTodo.builder().title(title).description(description).build();
    }

    default CreateTodo createTodo(String title, String description, TodoId parentTodoId) {
        return ImmutableCreateTodo.builder().title(title).description(description).parentId(parentTodoId).build();
    }

    default DeleteTodo deleteTodo(TodoId todoId) {
        return ImmutableDeleteTodo.builder().todoId(todoId).build();
    }

    default EditDescription editDescription(TodoId todoId, String description) {
        return ImmutableEditDescription.builder().todoId(todoId).description(description).build();
    }

}
