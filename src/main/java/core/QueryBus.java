package core;

/**
 * The mechanism that dispatches Query objects to their appropriate QueryHandlers.
 */
public interface QueryBus {

    /**
     * Dispatch the given {@code query} to a single QueryHandler subscribed to the given {@code query} type.
     *
     * @param <Q> the type of the query
     * @param <R> the result type of the query
     * @param query the query
     * @return the {@link Result} of the query execution
     */
    <Q extends Query<R>, R> Result<R> query(Q query);
}
