package core;

/**
 * Defines an implementation for a specified command
 *
 * @param <C> the command type expected by this handler
 * @param <R> result of this handler
 */
public interface CommandHandler<C extends Command<R>, R> {

    /**
     * @return the command type expected by this handler
     */
    Class<C> commandType();

    /**
     * @param command the input to change the state of our system
     * @return result of the command
     */
    Result<R> handle(C command);
}
