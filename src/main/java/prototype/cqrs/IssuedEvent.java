package prototype.cqrs;

import org.immutables.value.Value.Immutable;

@Immutable
public abstract class IssuedEvent {

    public static IssuedEvent of(String id, int amount) {
        return ImmutableIssuedEvent.builder().id(id).amount(amount).build();
    }

    public abstract int amount();

    public abstract String id();

}
