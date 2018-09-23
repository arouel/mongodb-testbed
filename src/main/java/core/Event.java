package core;

import java.time.ZonedDateTime;

import org.immutables.value.Value.Default;

import prototype.command.event.EventId;

/**
 * Defines something that <b>happened</b> that domain experts care about.
 */
public interface Event {

    EventId eventId();

    @Default
    default ZonedDateTime eventTime() {
        return ZonedDateTime.now();
    }

    @Default
    default String eventType() {
        return getClass().getName();
    }

    Event withEventId(EventId value);
}
