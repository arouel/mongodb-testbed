package prototype.cqrs;

import org.immutables.value.Value.Immutable;

@Immutable
public abstract class RedeemedEvent {

    public static RedeemedEvent of(String id, int amount) {
        return ImmutableRedeemedEvent.builder().id(id).amount(amount).build();
    }

    public abstract int amount();

    public abstract String id();

}
