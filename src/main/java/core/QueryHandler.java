package core;

/**
 * Defines an implementation for a specified query
 *
 * @param <Q> the query type expected by this handler
 * @param <R> result of this handler
 */
public interface QueryHandler<Q extends Query<R>, R> {

    /**
     * @param command the parameters to WRITE new state of the system
     * @return result of the command
     */
    Result<R> handle(Q command);

    /**
     * @return the query type expected by this handler
     */
    Class<Q> queryType();
}
