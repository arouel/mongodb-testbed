package prototype.command;

import com.google.gson.GsonBuilder;
import prototype.RuntimeTypeAdapterFactory;

public abstract class Command<T> {

    /**
     * Factory which provides a type adapter for GSON that supports various sub types of {@code WorkbookAction}
     */
    private static final RuntimeTypeAdapterFactory<Command<?>> TYPE_ADAPTER_FACTORY = null;

    public static CreateTodo createTodo(String title, String description) {
        return ImmutableCreateTodo.builder().title(title).description(description).build();
    }

    public static DeleteTodo deleteTodo(long todoId) {
        return ImmutableDeleteTodo.builder().todoId(todoId).build();
    }

    public static EditDescription editDescription(long todoId, String description) {
        return ImmutableEditDescription.builder().todoId(todoId).description(description).build();
    }

    static GsonBuilder registerAll(GsonBuilder builder) {
        return builder.registerTypeAdapterFactory(TYPE_ADAPTER_FACTORY);
    }
}
