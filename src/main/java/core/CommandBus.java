package core;

/**
 * The mechanism that dispatches {@link Command} objects to their appropriate CommandHandler.
 */
public interface CommandBus {

    /**
     * Dispatch the given {@code command} to the CommandHandler subscribed to the given {@code command} type.
     *
     * @param <C> The type of command to dispatch
     * @param <R> the result type of the command
     * @param command The Command to dispatch
     * @return result of the command execution
     */
    <C extends Command<R>, R> Result<R> command(C command);
}
