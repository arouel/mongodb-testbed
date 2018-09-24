package core;

import java.time.ZonedDateTime;

/**
 * Defines something that <b>happened</b> that domain experts care about.
 */
public interface Event {

    EventId eventId();

    ZonedDateTime eventTime();

    String eventType();

    Event withEventId(EventId value);
}
