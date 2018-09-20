package core;

import static java.util.Objects.*;

/**
 * Implementation of the {@link QueryBus} that dispatches queries to the handlers subscribed to that specific query type.
 */
public final class SimpleQueryBus implements QueryBus {

    private final Iterable<QueryHandler<Query<?>, ?>> _handlers;

    public SimpleQueryBus(Iterable<QueryHandler<Query<?>, ?>> handlers) {
        _handlers = requireNonNull(handlers);
    }

    private boolean matches(Query<?> query, QueryHandler<Query<?>, ?> handler) {
        return handler.queryType().isAssignableFrom(query.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Q extends Query<R>, R> Result<R> query(Q query) {
        if (query == null) {
            return Result.requirementNotMet("query required");
        }
        // use a cache in future to remember the mapping
        for (QueryHandler<Query<?>, ?> handler : _handlers) {
            if (matches(query, handler)) {
                return (Result<R>) handler.handle(query);
            }
        }
        return Result.unknownFailure(new UnsupportedOperationException(String.format("Cannot find implementation for '%s'", query.getClass().getName())));
    }
}
