package core;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of the {@link QueryBus} that dispatches queries to the handlers subscribed to that specific query type.
 */
public final class SimpleQueryBus implements QueryBus {

    private final ConcurrentMap<Class<?>, QueryHandler<Query<?>, ?>> _cache;
    private final Iterable<QueryHandler<Query<?>, ?>> _handlers;

    public SimpleQueryBus(Iterable<QueryHandler<Query<?>, ?>> handlers) {
        _cache = new ConcurrentHashMap<>();
        _handlers = requireNonNull(handlers);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Query<R>, R> Result<R> query(C query) {
        requireNonNull(query);

        // check cache
        QueryHandler<Query<?>, ?> cachedHandler = _cache.get(query.getClass());
        if (cachedHandler != null) {
            return (Result<R>) cachedHandler.handle(query);
        }

        // find mapping
        for (QueryHandler<Query<?>, ?> handler : _handlers) {
            if (matches(query, handler)) {

                // remember mapping
                _cache.putIfAbsent(query.getClass(), handler);

                return (Result<R>) handler.handle(query);
            }
        }

        return Result.unknownFailure(new UnsupportedOperationException(String.format("Cannot find implementation for '%s'", query.getClass().getName())));
    }

    private boolean matches(Query<?> query, QueryHandler<Query<?>, ?> handler) {
        return handler.queryType().isAssignableFrom(query.getClass());
    }
}
