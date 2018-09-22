package core;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Implementation of the {@link QueryBus} that dispatches queries to the handlers subscribed to that specific query type.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@ThreadSafe
public final class SimpleQueryBus implements QueryBus {

    private final ConcurrentMap<Class<?>, QueryHandler> _cache;
    private final Iterable<QueryHandler> _handlers;

    public SimpleQueryBus(Iterable<QueryHandler> handlers) {
        _cache = new ConcurrentHashMap<>();
        _handlers = requireNonNull(handlers);
    }

    private static boolean matches(Query<?> query, QueryHandler handler) {
        return handler.queryType().isAssignableFrom(query.getClass());
    }

    @Override
    public <C extends Query<R>, R> Result<R> query(C query) {
        requireNonNull(query);

        // check cache
        QueryHandler cachedHandler = _cache.get(query.getClass());
        if (cachedHandler != null) {
            return cachedHandler.handle(query);
        }

        // find mapping
        for (QueryHandler handler : _handlers) {
            if (matches(query, handler)) {

                // remember mapping
                _cache.putIfAbsent(query.getClass(), handler);

                return handler.handle(query);
            }
        }

        return Result.unknownFailure(new UnsupportedOperationException(String.format("Cannot find implementation for '%s'", query.getClass().getName())));
    }
}
