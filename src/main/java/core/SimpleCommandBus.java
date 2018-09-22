package core;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of the {@link CommandBus} that dispatches commands to the handlers subscribed to that specific command type.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class SimpleCommandBus implements CommandBus {

    private final ConcurrentMap<Class<?>, CommandHandler> _cache;
    private final Iterable<CommandHandler> _handlers;

    public SimpleCommandBus(Iterable<CommandHandler> handlers) {
        _cache = new ConcurrentHashMap<>();
        _handlers = requireNonNull(handlers);
    }

    @Override
    public <C extends Command<R>, R> Result<R> command(C command) {
        requireNonNull(command);

        // check cache
        CommandHandler cachedHandler = _cache.get(command.getClass());
        if (cachedHandler != null) {
            return cachedHandler.handle(command);
        }

        // find mapping
        for (CommandHandler handler : _handlers) {
            if (matches(command, handler)) {

                // remember mapping
                _cache.putIfAbsent(command.getClass(), handler);

                return handler.handle(command);
            }
        }

        return Result.unknownFailure(new UnsupportedOperationException(String.format("Cannot find implementation for '%s'", command.getClass().getName())));
    }

    private boolean matches(Command<?> command, CommandHandler handler) {
        return handler.commandType().isAssignableFrom(command.getClass());
    }
}
