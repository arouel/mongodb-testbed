package core;

/**
 * Defines a READ operation
 * <p>
 * It reads the state of the system, filters, aggregates and transforms data to deliver it in the most useful format (based on your use case). It can be executed multiple times and
 * will not affect the state of the system.
 *
 * @param <R> result type
 */
public interface Query<R> {
}
