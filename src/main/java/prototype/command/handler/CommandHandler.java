package prototype.command.handler;

import java.util.ServiceLoader;

import io.vertx.core.Future;
import prototype.command.Command;
import prototype.command.Handler;

/**
 * Defines an implementation for a specified command and provides and {@link Future} e.g. to modify a todo.
 *
 * @param <T> command which is implemented
 * @param <U> result of the command handler
 */
public abstract class CommandHandler<T extends Command<U>, U> extends Handler {

    @SuppressWarnings("unchecked")
    private static Iterable<CommandHandler<? extends Command<?>, ?>> implementations() {
        return Iterable.class.cast(ServiceLoader.load(CommandHandler.class));
    }

    /**
     * Resolves the implementation for a given command or returns a failing {@link Future}.
     *
     * @param fileId file identifier of a workbook that should be modified
     * @param command described the modification on a workbook
     * @param performedBy user which performs the given command
     * @param eventBus event bus to publish events
     * @return task that modifies a workbook based on the given command
     */
    @SuppressWarnings("unchecked")
    public static <U> Future<U> resolve(Command<U> command) {
        if (command == null) {
            return Future.failedFuture("No command received");
        }
        for (CommandHandler<? extends Command<?>, ?> implementation : implementations()) {
            if (implementation.matches(command)) {
                return (Future<U>) implementation.apply(command);
            }
        }
        return Future.failedFuture(new UnsupportedOperationException(String.format("Cannot find implementation for '%s'", command.getClass().getName())));
    }

    @SuppressWarnings("unchecked")
    private Future<U> apply(Command<?> command) {
        return handle((T) command);
    }

    /**
     * @return the supported/implemented type of {@link Command}
     */
    public abstract Class<T> commandType();

    /**
     * @param fileId file identifier of a workbook that should be modified
     * @param command described the modification on a workbook
     * @param performedBy user which performs the given command
     * @param eventBus event bus to publish events
     * @return task that modifies a workbook based on the given command
     */
    public abstract Future<U> handle(T command);

    private boolean matches(Command<?> command) {
        return commandType().isAssignableFrom(command.getClass());
    }
}
