package core;

/**
 * Defines a light-weight distributed messaging system which allows different parts of your application, or different applications and services to communicate with each in a
 * loosely coupled way.
 * <p>
 * An event-bus supports publish-subscribe messaging, point-to-point messaging and request-response messaging.
 */
public interface EventBus {

    /**
     * Sends a message.
     * <p>
     * The message will be delivered to at most one of the handlers registered to the address.
     *
     * @param event the message
     * @return a reference to this, so the API can be used fluently
     */
    EventBus send(Event event);

}
