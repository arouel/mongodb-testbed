package core;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of the {@link CommandBus} that dispatches commands to the handlers subscribed to that specific command type.
 */
public final class SimpleCommandBus implements CommandBus {

    private final ConcurrentMap<Class<?>, CommandHandler<Command<?>, ?>> _cache;
    private final Iterable<CommandHandler<Command<?>, ?>> _handlers;

    public SimpleCommandBus(Iterable<CommandHandler<Command<?>, ?>> handlers) {
        _cache = new ConcurrentHashMap<>();
        _handlers = requireNonNull(handlers);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Command<R>, R> Result<R> command(C command) {
        requireNonNull(command);

        // check cache
        CommandHandler<Command<?>, ?> cachedHandler = _cache.get(command.getClass());
        if (cachedHandler != null) {
            return (Result<R>) cachedHandler.handle(command);
        }

        // find mapping
        for (CommandHandler<Command<?>, ?> handler : _handlers) {
            if (matches(command, handler)) {

                // remember mapping
                _cache.putIfAbsent(command.getClass(), handler);

                return (Result<R>) handler.handle(command);
            }
        }

        return Result.unknownFailure(new UnsupportedOperationException(String.format("Cannot find implementation for '%s'", command.getClass().getName())));
    }

    private boolean matches(Command<?> command, CommandHandler<Command<?>, ?> handler) {
        return handler.commandType().isAssignableFrom(command.getClass());
    }
}
