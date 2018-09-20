package core;

import static java.util.Objects.*;

/**
 * Implementation of the {@link CommandBus} that dispatches commands to the handlers subscribed to that specific command type.
 */
public final class SimpleCommandBus implements CommandBus {

    private final Iterable<CommandHandler<Command<?>, ?>> _handlers;

    public SimpleCommandBus(Iterable<CommandHandler<Command<?>, ?>> handlers) {
        _handlers = requireNonNull(handlers);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Command<R>, R> Result<R> command(C command) {
        if (command == null) {
            return Result.requirementNotMet("command required");
        }
        // use a cache in future to remember the mapping
        for (CommandHandler<Command<?>, ?> handler : _handlers) {
            if (matches(command, handler)) {
                return (Result<R>) handler.handle(command);
            }
        }
        return Result.unknownFailure(new UnsupportedOperationException(String.format("Cannot find implementation for '%s'", command.getClass().getName())));
    }

    private boolean matches(Command<?> command, CommandHandler<Command<?>, ?> handler) {
        return handler.commandType().isAssignableFrom(command.getClass());
    }
}
