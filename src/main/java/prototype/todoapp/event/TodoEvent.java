package prototype.todoapp.event;

import java.time.ZonedDateTime;

import org.immutables.value.Value.Default;

import core.Event;

public interface TodoEvent extends Event {

    @Default
    @Override
    default ZonedDateTime eventTime() {
        return ZonedDateTime.now();
    }

    @Default
    @Override
    default String eventType() {
        return getClass().getName().replaceAll("Immutable", "");
    }

}
