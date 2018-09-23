package core;

/**
 * Persists events
 * <p>
 * Events that are stored are not allowed to be changed. Once stored, also erroneous events are not changed anymore.
 */
public interface EventStore {

    /**
     * Stores the given event
     *
     * @param event the event to store
     */
    void save(Event event);

}
